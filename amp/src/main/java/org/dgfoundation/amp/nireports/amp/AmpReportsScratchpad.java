package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.IReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.*;
import org.dgfoundation.amp.nireports.amp.PercentagesCorrector.Snapshot;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.schema.SqlSourcedColumn;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.apache.commons.collections.CollectionUtils.containsAny;
import static org.apache.commons.collections.CollectionUtils.intersection;

/**
 * the AMP-schema-specific scratchpad <br />
 * In short: each {@link NiReportsEngine} instance running a report has an {@link AmpReportsScratchpad} counterpart instance. 
 * They each hold a reference to each other (for ease of coding) and they are both discarded as soon as a report run ends.
 * please see {@link NiReportsEngine#schemaSpecificScratchpad}
 * @author Dolghier Constantin
 *
 */
public class AmpReportsScratchpad implements SchemaSpecificScratchpad {
    
    /**
     * FOR TESTCASES ONLY. In case it is non-null, for computed measures this value will be used in lieu of LocalDate.now()
     */
    public static LocalDate forcedNowDate;

    /**
     * FOR TESTCASES ONLY. When non-null will override the corresponding global setting.
     */
    public static Boolean displayUnlinkedFundingInPledgesReports;

    /**
     * caching area for i18n fetchers
     */
    public final Map<PropertyDescription, ColumnValuesCacher> columnCachers = new ConcurrentHashMap<>();
    
    /**
     * AMP-22850: fiscal calendars which start in the middle of a month have a maledefined behaviour regarding "what is the month number of month X?".
     * For example, if FY starts on the 16th of August, then the 2008-era workers will consider "September 15th" as being the first month, while "September 17th" as being the 2nd month.
     * This leads to problems (e.g. crashes) later in the engine, as inconsistent ComparableValue<String> entries (one with id=1, an another with id=2) are equal by displayed value but different by ids. <br />
     * What this map does is make sure all the TranslatedDate instances for a calendar map a given month to the same id, during the lifetime of a report. The right place to fix it would be a complete rewrite of {@link ICalendarWorker} 
     * with its awful interface and bugs, but that's nontrivial and bug-prone
     */
    public final ConcurrentHashMap<String, Integer> monthNumbersCache = new ConcurrentHashMap<>();

    //public final Map<PercentagesCorrector, PercentagesCorrector.Snapshot> percsCorrectors = new ConcurrentHashMap<>();
    
    /**
     * the JDBC connection used by the fetchers to run SQL queries
     */
    public final Connection connection;
    
    /**
     * the non-report-spec params influencing a report run, like workspace filter, locale, default currency
     */
    private final IReportEnvironment environment;
    
    /**
     * the counterpart engine
     */
    public final NiReportsEngine engine;
    
    /**
     * the last amp_etl_changelog event id which will be taken in consideration by fetchers while running this report
     */
    public final long lastEventId;
    
    /**
     * a {@link Memoizer} of a block of various variables relevant for computed measures. Relatively expensive and relatively rarely used, thus memoized
     */
    public final Memoizer<SelectedYearBlock> computedMeasuresBlock;
    
    /**
     * a {@link Memoizer} for pledge ids. Quite expensive to calculate and very rarely used, thus memoized
     */
    public final Memoizer<Set<Long>> computedPledgeIds;
    
    /**
     * this will be true IFF {@link GlobalSettingsConstants#SPLIT_BY_MODE_OF_PAYMENT} is true AND the report includes {@link ColumnConstants#MODE_OF_PAYMENT} as a column but not as a hierarchy
     */
    public final boolean verticalSplitByModeOfPayment;
    
    /**
     * this will be true IFF {@link GlobalSettingsConstants#SPLIT_BY_TYPE_OF_ASSISTANCE} is true AND the report includes {@link ColumnConstants#TYPE_OF_ASSISTANCE} as a column but not as a hierarchy
     */
    public final boolean verticalSplitByTypeOfAssistance;
    
    /**
     * the currency used to render the report - do not write anything to it!
     */
    protected final AmpCurrency usedCurrency;
    
    protected final NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();

    public AmpReportsScratchpad(NiReportsEngine engine) {
        this.engine = engine;
        this.computedMeasuresBlock =  new Memoizer<>(() -> SelectedYearBlock.buildFor(this.engine.spec, forcedNowDate == null ? LocalDate.now() : forcedNowDate));
        this.computedPledgeIds = new Memoizer<>(() -> new HashSet<>(SQLUtils.fetchLongs(AmpReportsScratchpad.get(engine).connection, getPledgesIdsQuery())));
        
        try {this.connection = PersistenceManager.getJdbcConnection();}
        catch(Exception e) {throw AlgoUtils.translateException(e);}
        this.usedCurrency = engine.spec.getSettings() == null || engine.spec.getSettings().getCurrencyCode() == null ? AmpARFilter.getDefaultCurrency() : 
            CurrencyUtil.getAmpcurrency(engine.spec.getSettings().getCurrencyCode());
        this.environment = engine.getReportEnvironment();
        this.lastEventId = SQLUtils.getLong(this.connection, "SELECT COALESCE(max(event_id), -1) FROM amp_etl_changelog");
        this.verticalSplitByTypeOfAssistance = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_TYPE_OF_ASSISTANCE).equalsIgnoreCase("true") &&
            engine.spec.getColumnNames().contains(ColumnConstants.TYPE_OF_ASSISTANCE) &&
            !engine.spec.getHierarchyNames().contains(ColumnConstants.TYPE_OF_ASSISTANCE);
        this.verticalSplitByModeOfPayment = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_MODE_OF_PAYMENT).equalsIgnoreCase("true") &&
            engine.spec.getColumnNames().contains(ColumnConstants.MODE_OF_PAYMENT) &&
            !engine.spec.getHierarchyNames().contains(ColumnConstants.MODE_OF_PAYMENT);

        checkMeasurelessHierarchies(engine.spec);
    }

    private void checkMeasurelessHierarchies(ReportSpecification spec) {
        if (spec.getReportType() == ArConstants.INDICATOR_TYPE) {
            return;
        }
        List<String> amountColumns = AmpReportsSchema.getInstance().getAmountColumns();
        List<String> onlyMeasurelessHierarchies = AmpReportsSchema.ONLY_MEASURELESS_HIERARCHIES;

        if ((!spec.getMeasures().isEmpty() || containsAny(spec.getColumnNames(), amountColumns))
                && containsAny(spec.getHierarchyNames(), onlyMeasurelessHierarchies)) {

            throw new RuntimeException(
                    String.format("Found hierarchies %s that can be used only in measureless reports!",
                    intersection(spec.getHierarchyNames(), onlyMeasurelessHierarchies)));
        }
    }

    private String getPledgesIdsQuery() {
        String query = "SELECT id FROM amp_funding_pledges";
        if (isDisplayUnlinkedFundingInPledgesReports()) {
            query += " UNION SELECT 999999999";
        }
        return query;
    }

    private boolean isDisplayUnlinkedFundingInPledgesReports() {
        if (displayUnlinkedFundingInPledgesReports != null) {
            return displayUnlinkedFundingInPledgesReports;
        } else {
            return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.UNLINKED_FUNDING_IN_PLEDGES_REPORTS);
        }
    }

    public AmpCurrency getUsedCurrency() {
        return usedCurrency;
    }
        
//  public PercentagesCorrector.Snapshot buildOrGetSnapshot(PercentagesCorrector corrector, Set<Long> ids) {
//      return percsCorrectors.computeIfAbsent(corrector, z -> buildSnapshot(z, ids));
//  }
        
    /**
     * builds a {@link PercentagesCorrector} {@link Snapshot} based on a preexisting {@link Snapshot}, a generating {@link PercentagesCorrector} and a set of ids to fetch.
     * Works based on the same ideas as {@link DifferentialCache}
     * @param earlyEntries
     * @param corrector
     * @param ids
     * @return
     */
    public Snapshot buildSnapshot(Snapshot earlyEntries, PercentagesCorrector corrector, Set<Long> ids) {
        final ValueWrapper<Snapshot> snapshot = new ValueWrapper<>(null);
        engine.timer.run("normalize_percentages", () -> {
            snapshot.set(corrector.buildSnapshot(this.connection, ids));
            engine.timer.putMetaInNode("denormal activities", snapshot.value.sumOfPercentages.size());
        });
        return earlyEntries == null ? snapshot.value : earlyEntries.mergeWith(snapshot.value);
    }
    
    /**
     * returns the set of ids of entities of a given type which have been affected by an ETL-logged change between a given first-Event-Id and this scratchpad's max-event-id
     * @param entityType the type of logged entity to query for. You can find a list of them in the ETL documentation here: https://wiki.dgfoundation.org/display/AMPDOC/AMP+2.10+ETL+process, file <i>etl_incremental_tables.png</i>
     * @param firstEventId
     * @return
     */
    public Set<Long> getChangedEntities(String entityType, long firstEventId) {
        Set<Long> changedEntityIds = new HashSet<>(SQLUtils.fetchLongs(this.connection, String.format("SELECT DISTINCT entity_id FROM amp_etl_changelog WHERE (entity_name='%s') AND (event_id > %d) AND (event_id <= %d)", entityType, firstEventId, lastEventId)));
        return changedEntityIds;
    }
    
    /**
     * returns the set of ownerIds which should be fetched for a given sqlsourced column 
     * @param entityType the main-entity-type of the column we are trying to fetch
     * @return
     */
    public Set<Long> getRequestedEntityIds(String entityType) {
        if (entityType.equals("pledge") && engine.spec.getReportType() == ArConstants.DONOR_TYPE)
            return computedPledgeIds.get(); // fetching pledges in a donor report - so it's a "Also Show Pledges" report -> <strong>for this column only</strong> we will fetch pledges

        return engine.getMainIds();
    }
    
    public String getEntityType(String mainColumn) {
        return mainColumn.equals("amp_activity_id") ? "activity" : "pledge";
    }

    /**
     * given an engine context and a column, returns the set of ownerIds which should be fetched from the column
     */
    @Override
    public Set<Long> getMainIds(NiReportsEngine engine, NiReportColumn<?> col) {
        if (!(col instanceof SqlSourcedColumn))
            return engine.getMainIds();
        SqlSourcedColumn<?> ssc = (SqlSourcedColumn<?>) col;
        return getRequestedEntityIds(getEntityType(ssc.mainColumn));
    }
    
    /**
     * given a {@link DifferentialCache} instance, does the following:
     * <ol>
     * <li>queries the database for the entities which have changed since the last time the cache instance has been populated ({@link #getChangedEntities(String, long)}) - let's call it (a)</li>
     * <li>queries the reports engine for the entities which are requested by the current report run ({@link #getRequestedEntityIds(String)}) - let's call it (b)</li>
     * <li>gets the set of entity ids which are already present in the cache {@link DifferentialCache#getCachedEntityIds()} - let's call it (c)</li>
     * <li>calculates the set of entity ids which have to be refetched as being (a - c + b). Let's all it (d)</li>
     * <li>fetches the (d) cells</li>
     * <li>returns (d)</li>
     * </ol> 
     * @param timer the current-column-fetching timer so that the topmost node can be populated with statistics
     * @param mainColumn the "main" column name of the backing view of the fetched column. Used to decide whether to query the ETL log for activity IDs or pledge IDs
     * @param cache the cache to be used as a data repo
     * @param fetcher the function used for fetching cells with a given set of main IDs
     * @return
     */
    public<K extends Cell> Set<Long> differentiallyImportCells(InclusiveTimer timer, String mainColumn, DifferentialCache<K> cache, Function<Set<Long>, List<K>> fetcher) {
        String entityType = getEntityType(mainColumn);
        Set<Long> changedEntityIds = getChangedEntities(entityType, cache.getLastEventId());
        Set<Long> requestedEntityIds = getRequestedEntityIds(entityType);
        Set<Long> idsToReplace = AmpCollections.minusPlus(requestedEntityIds, cache.getCachedEntityIds(), changedEntityIds);
        
        List<K> fetchedCells = fetcher.apply(idsToReplace);
        cache.importCells(idsToReplace, fetchedCells, lastEventId);

        timer.putMetaInNode("lastEventId", lastEventId);
        timer.putMetaInNode("changedEntities", changedEntityIds.size());
        timer.putMetaInNode("fetchedIds", idsToReplace.size());
        timer.putMetaInNode("fetchedCells", fetchedCells.size());
        
        return idsToReplace;
    }

    /**
     * shorthand function to take repetitive casts out of common code: gets the AMP scratchpad attached to a running engine 
     * @param engine
     * @return
     */
    public static AmpReportsScratchpad get(NiReportsEngine engine) {
        return (AmpReportsScratchpad) engine.schemaSpecificScratchpad;
    }
    
    public static SelectedYearBlock getComputedMeasuresBlock(NiReportsEngine engine) {
        return get(engine).computedMeasuresBlock.get();
    }
    
    @Override
    public void close() throws Exception {
        this.connection.close();
    }

    @Override
    public NiPrecisionSetting getPrecisionSetting() {
        return precisionSetting;
    }

    /**
     * adds a caching layer in front of AMP's slow calendar converters. Also translates the text headers for fiscal years and workarounds calendar bugs (AMP-22850)
     */
    @Override
    public CachingCalendarConverter buildCalendarConverter() {
        CalendarConverter underlyingConverter = buildUnderlyingCalendarConverter(engine.spec);
        return new CachingCalendarConverter(underlyingConverter, TranslatorWorker.translateText(underlyingConverter.getDefaultFiscalYearPrefix()), this::postprocessTranslatedDate);
    }

    @Override
    public CalendarConverter getDefaultCalendar() {
        return AmpARFilter.getDefaultCalendar();
    }
    
    /**
     * postprocesses funding column's transaction dates for the sake of AMP-22850
     * @param in
     * @return
     */
    public TranslatedDate postprocessTranslatedDate(TranslatedDate in) {
        int preexistantMonthNumber = (Integer) in.month.getComparable();
        int monthNumber = monthNumbersCache.computeIfAbsent(in.month.getValue(), z -> preexistantMonthNumber);
        if (preexistantMonthNumber != monthNumber)
            return in.withMonth(new ComparableValue<>(in.month.getValue(), monthNumber));
        return in;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimeRangeSubTotalColumnName(ReportSpecification spec) {
        return spec.isDisplayTimeRangeSubTotals() ? TranslatorWorker.translateText("Total") : null;
    }

    public IReportEnvironment getEnvironment() {
        return environment;
    }
}
