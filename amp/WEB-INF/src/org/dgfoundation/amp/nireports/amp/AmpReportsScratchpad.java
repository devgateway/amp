package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.SqlSourcedColumn;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.amp.PercentagesCorrector.Snapshot;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * the AMP-schema-specific scratchpad <br />
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
	
	public final Connection connection;
	
	public final ReportEnvironment environment;
	public final NiReportsEngine engine;
	public final long lastEventId;
	public final Memoizer<SelectedYearBlock> computedMeasuresBlock;
	public final Memoizer<Set<Long>> computedPledgeIds;
	
	/**
	 * the currency used to render the report - do not write anything to it!
	 */
	protected final AmpCurrency usedCurrency;
	
	protected final NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();

	public AmpReportsScratchpad(NiReportsEngine engine) {
		this.engine = engine;
		this.computedMeasuresBlock =  new Memoizer<>(() -> SelectedYearBlock.buildFor(this.engine.spec, forcedNowDate == null ? LocalDate.now() : forcedNowDate));
		this.computedPledgeIds = new Memoizer<>(() -> new HashSet<>(SQLUtils.fetchLongs(AmpReportsScratchpad.get(engine).connection, "SELECT id FROM amp_funding_pledges")));
		
		try {this.connection = PersistenceManager.getJdbcConnection();}
		catch(Exception e) {throw AlgoUtils.translateException(e);}
		this.usedCurrency = engine.spec.getSettings() == null || engine.spec.getSettings().getCurrencyCode() == null ? AmpARFilter.getDefaultCurrency() : 
			CurrencyUtil.getAmpcurrency(engine.spec.getSettings().getCurrencyCode());
		this.environment = ReportEnvironment.buildFor(TLSUtils.getRequest());
		this.lastEventId = SQLUtils.getLong(this.connection, "SELECT COALESCE(max(event_id), -1) FROM amp_etl_changelog");
	}
	
	public AmpCurrency getUsedCurrency() {
		return usedCurrency;
	}
		
//	public PercentagesCorrector.Snapshot buildOrGetSnapshot(PercentagesCorrector corrector, Set<Long> ids) {
//		return percsCorrectors.computeIfAbsent(corrector, z -> buildSnapshot(z, ids));
//	}
		
	public Snapshot buildSnapshot(Snapshot earlyEntries, PercentagesCorrector corrector, Set<Long> ids) {
		final ValueWrapper<Snapshot> snapshot = new ValueWrapper<>(null);
		engine.timer.run("normalize_percentages", () -> {
			snapshot.set(corrector.buildSnapshot(this.connection, ids));
			engine.timer.putMetaInNode("denormal activities", snapshot.value.sumOfPercentages.size());
		});
		return earlyEntries == null ? snapshot.value : earlyEntries.mergeWith(snapshot.value);
	}
	
	public Set<Long> getChangedEntities(String entityType, long firstEventId) {
		Set<Long> changedEntityIds = new HashSet<>(SQLUtils.fetchLongs(this.connection, String.format("SELECT DISTINCT entity_id FROM amp_etl_changelog WHERE (entity_name='%s') AND (event_id > %d) AND (event_id <= %d)", entityType, firstEventId, lastEventId)));
		return changedEntityIds;
	}
	
	public Set<Long> getRequestedEntityIds(String entityType) {
		if (entityType.equals("pledge") && engine.spec.getReportType() == ArConstants.DONOR_TYPE)
			return computedPledgeIds.get();

		return engine.getMainIds();
	}
	
	public String getEntityType(String mainColumn) {
		return mainColumn.equals("amp_activity_id") ? "activity" : "pledge";
	}
	
	@Override
	public Set<Long> getMainIds(NiReportsEngine engine, NiReportColumn<?> col) {
		if (!(col instanceof SqlSourcedColumn))
			return engine.getMainIds();
		SqlSourcedColumn<?> ssc = (SqlSourcedColumn<?>) col;
		return getRequestedEntityIds(getEntityType(ssc.mainColumn));
	}
	
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
	 * postprocesses funding column's transaction dates
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
}
