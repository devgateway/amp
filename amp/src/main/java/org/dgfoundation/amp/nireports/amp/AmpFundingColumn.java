package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.digijava.kernel.translator.LocalizableLabel;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.diffcaching.ActivityInvalidationDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.IdValuePair;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.CategAmountCellProto;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

import static java.util.stream.Collectors.toCollection;

/**
 * the {@link NiReportColumn} which fetches funding cells. This class is the common ancestors for all the coordinates-based funding cells in the AMP schema 
 * (that is, all of them except Proposed Project Cost): [Donor / Component / Pledges] Funding, the MTEF columns. <br />
 * This class implements a 2-level caching akin to {@link AmpDifferentialColumn}, except that the first level is degenerating: it is a boolean whose key always equals <i>true</i>. <br />
 * Earlier implementations of this class were a true 2-level cache, the key of the first level being the 3-tuple (calendar, currency, locale); 
 * this was abandoned because it might lead to big amounts of memory being consumed on the Cartesian product of the key: each cell might be duplicated upto (nr_currencies x nr_calendars x nr_locales) times. <br />
 * Thus, the AMP schema uses a different schema for caching funding cells: what is actually cached in the level2-caches in {@link DifferentialCache} is <i>cell prototypes</i>.
 * A <i>cell prototype</i> is an instance of {@link CategAmountCellProto}: a class which closely mirrors {@link CategAmountCell}, but has its fields dependent on locale / calendar / currency unpopulated.
 * Through <i>materialization</i>, a {@link CategAmountCellProto} builds (cheaply) a {@link CategAmountCell} instance which is then output to the client code.<br />
 * Thus, the fetching of AMP's funding columns is a multiprocess step like this:
 * <ol>
 * <li>the necessary cell prototypes are fetched differentially and stored in the {@link DifferentialCache} instance which is a member of {@link FundingFetcherContext}. Please see {@link AmpReportsScratchpad#differentiallyImportCells(org.dgfoundation.amp.algo.timing.InclusiveTimer, String, DifferentialCache, java.util.function.Function)}</li>
 * <li>all the relevant prototypes (which is a superset of the just-fetched cells) are materialized. This cheap operation can be performed because at report runtime, the 3 parameters driving materialization are all known </li>
 * </ol>
 * Please note that the materialized cells are ephemeral: running two consecutive reports with identical (calendar, currency, locale) settings will lead to materialization being rerun. 
 * This is a low-hanging fruit for dashboards-like scenarios where the same funding column fetching settings are being used repeatedly in a matter of seconds. <br />
 * 
 * An another point of this class is that, at initialization, it scans the backing view for missing columns and will not attempt to read those from the view. 
 * Some minor functionality is configured hardcodedly via the fetcher looking up the column it is fetching. The following constants are a non-exhaustive list of names
 * of columns which are fetched by this column: {@link #ENTITY_COMPONENT_FUNDING}, {@link #ENTITY_DONOR_FUNDING}, {@link #ENTITY_PLEDGE_FUNDING}. 
 * Some columns (most notably, the MTEF family of columns) do not have dedicated entries in the column code
 * 
 * @author Dolghier Constantin
 *
 */
public class AmpFundingColumn extends PsqlSourcedColumn<CategAmountCell> {

    /**
     * {@link #getName()} in case this column is used to fetch "Donor Funding"
     */
    public final static String ENTITY_DONOR_FUNDING = "Funding";
    
    /**
     * {@link #getName()} in case this column is used to fetch "Pledge Funding"
     */
    public final static String ENTITY_PLEDGE_FUNDING = "Pledge Funding";
    
    /**
     * {@link #getName()} in case this column is used to fetch "Component Funding"
     */
    public final static String ENTITY_COMPONENT_FUNDING = "Component Funding";

    /**
     * {@link #getName()} in case this column is used to fetch GPI Funding"
     */
    public final static String ENTITY_GPI_FUNDING = "GPI Funding";

    /**
     * {@link #getName()} in case this column is used to fetch "Regional Funding"
     */
    public static final String ENTITY_REGIONAL_FUNDING = "Regional Funding";

    /*
     * the cell prototypes cache, plus some auxiliary info
     */
    protected final ExpiringCacher<Boolean, NiReportsEngine, FundingFetcherContext> cacher;
    protected final ActivityInvalidationDetector invalidationDetector;
    
    /**
     * the sync obj for non-thread-safe destructive operations
     */
    protected final Object CACHE_SYNC_OBJ = new Object();
    
    /**
     * the view-columns which should not be read by the fetching code. 
     * An entry lands in this set either programmatically or through the column not existing in the view 
     */
    protected final Set<String> ignoredColumns;
        
    /**
     * the number of seconds to keep fetched prototypes in memory 
     */
    public final static int CACHE_TTL_SECONDS = 10 * 60;

    private SubDimensions subDimensions;

    /**
     * delegates to {@link #AmpFundingColumn(String, LocalizableLabel, String, Behaviour, SubDimensions)}
     * with {@link TrivialMeasureBehaviour} as a behaviour
     */
    public AmpFundingColumn(String columnName, String viewName, SubDimensions subDimensions) {
        this(columnName, new LocalizableLabel(columnName), viewName, TrivialMeasureBehaviour.getInstance(), subDimensions);
    }

    /**
     * constructs an instance which will fetch a given AMP entity from a given PostgreSQL view with a given {@link Behaviour}.
     * As part of initialisation, the view is scanned for columns which are supported by AMP Funding but are missing from the view. 
     * Any such viewcolumns (and their associated NiDimensionUsage's) will be ignored by this instance upon fetching
     * @param columnName the name of the AMP entity (column) to fetch
     * @param viewName the name of the PostgreSQL view to fetch data from
     * @param behaviour the behaviour of the column
     */
    protected AmpFundingColumn(String columnName, LocalizableLabel label, String viewName, Behaviour<?> behaviour, SubDimensions subDimensions) {
        super(columnName, label, null, viewName, behaviour);
        this.invalidationDetector = new ActivityInvalidationDetector();
        this.subDimensions = subDimensions;
        Set<String> ic = new HashSet<>();
        for(String col : subDimensions.getColumnIdNames().values())
            if (!this.viewColumns.contains(col))
                ic.add(col); // ignore missing NiDimension-bearing viewcolumns
        for(String col:AmpCollections.relist(longColumnsToFetch, z -> z.v))
            if (!this.viewColumns.contains(col))
                ic.add(col); // ignore missing generic-numbers-bearing viewcolumns
        this.ignoredColumns = Collections.unmodifiableSet(ic); // specified-generic columns minus columns which do not exist in the view
        this.cacher = new ExpiringCacher<>("funding cacher " + columnName, (key, engine) -> resetCache(engine), invalidationDetector, CACHE_TTL_SECONDS * 1000);
    }

    /**
     * returns true IFF the column has been specified as being transaction-level and the current funding view has not it blacklisted.
     * This is a hack to accommodate AMP's "multiple schemas per schema"
     * @param col
     * @return
     */
    public boolean isTransactionLevelHierarchy(NiReportColumn<?> col) {
        if (!col.isTransactionLevelHierarchy())
            return false;
        String viewName = subDimensions.getColumnIdNames().get(col.name);
        if (viewName == null)
            return true;
        return !ignoredColumns.contains(viewName);
    }

    /**
     * columns of type long which are optional
     */
    protected static List<ImmutablePair<MetaCategory, String>> longColumnsToFetch = Arrays.asList(
        new ImmutablePair<>(MetaCategory.TRANSACTION_TYPE, "transaction_type"),         
        new ImmutablePair<>(MetaCategory.TYPE_OF_ASSISTANCE, "terms_assist_id"),
        new ImmutablePair<>(MetaCategory.MODE_OF_PAYMENT, "mode_of_payment_id"),
        new ImmutablePair<>(MetaCategory.RECIPIENT_ORG, "recipient_org_id"),
        new ImmutablePair<>(MetaCategory.SOURCE_ORG, "donor_org_id"),
        new ImmutablePair<>(MetaCategory.EXPENDITURE_CLASS, "expenditure_class_id"),
        new ImmutablePair<>(MetaCategory.CONCESSIONALITY_LEVEL, "concessionality_level_id"),
        new ImmutablePair<>(MetaCategory.GPI_9B_Q1, "gpi_9b_q1"),
        new ImmutablePair<>(MetaCategory.GPI_9B_Q2, "gpi_9b_q2"),
        new ImmutablePair<>(MetaCategory.GPI_9B_Q3, "gpi_9b_q3"),
        new ImmutablePair<>(MetaCategory.GPI_9B_Q4, "gpi_9b_q4")
    );
    
    /**
     * builds a new l2 cache entry upon invalidation. 
     * @param engine
     * @return
     */
    protected synchronized FundingFetcherContext resetCache(NiReportsEngine engine) {
        engine.timer.putMetaInNode("resetCache", true);
        //Map<Long, String> adjTypeValue = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection, String.format("select acv_id, acv_name from v_ni_category_values where acc_keyname IN ('%s', '%s')", CategoryConstants.ADJUSTMENT_TYPE_KEY, CategoryConstants.SSC_ADJUSTMENT_TYPE_KEY));
        Map<Long, String> acvs = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection,
                String.format("select acv_id, acv_name from v_ni_category_values where acc_keyname "
                        + "IN('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                CategoryConstants.EXPENDITURE_CLASS_KEY, CategoryConstants.TYPE_OF_ASSISTENCE_KEY,
                CategoryConstants.MODE_OF_PAYMENT_KEY, CategoryConstants.ADJUSTMENT_TYPE_KEY,
                CategoryConstants.SSC_ADJUSTMENT_TYPE_KEY, CategoryConstants.CONCESSIONALITY_LEVEL_KEY,
                CategoryConstants.MTEF_PROJECTION_KEY));
        Map<Long, String> roles = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection, String.format("SELECT amp_role_id, role_code FROM amp_role", CategoryConstants.ADJUSTMENT_TYPE_KEY));
        
        return new FundingFetcherContext(new DifferentialCache<CategAmountCellProto>(invalidationDetector.getLastProcessedFullEtl()), roles, acvs);
    }

    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) {
        AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
        AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
        boolean enableDiffing = schema.ENABLE_CACHING;

        List<CategAmountCellProto> protos;
        if (enableDiffing) {
            long start = System.currentTimeMillis();
            synchronized(CACHE_SYNC_OBJ) {
                FundingFetcherContext cache = cacher.buildOrGetValue(true, engine);

                scratchpad.differentiallyImportCells(engine.timer, mainColumn, cache.cache,
                        ids -> fetchSkeleton(engine, ids, cache));

                protos = cache.cache.getCells(scratchpad.getMainIds(engine, this));
                long delta = System.currentTimeMillis() - start;
                engine.timer.putMetaInNode("hot_time", delta);
            }
        } else {
            protos = fetchSkeleton(engine, scratchpad.getMainIds(engine, this), resetCache(engine));
        }

        return materialize(engine, scratchpad, schema, protos);
    }

    /**
     * @param engine
     * @param scratchpad
     * @param schema
     * @param protos
     * @return
     */
    private List<CategAmountCell> materialize(NiReportsEngine engine, AmpReportsScratchpad scratchpad,
            AmpReportsSchema schema, List<CategAmountCellProto> protos) {

        long start = System.currentTimeMillis();

        AmpCurrency usedCurrency = scratchpad.getUsedCurrency();
        CachingCalendarConverter calendar = engine.calendar;
        CurrencyConvertor currencyConvertor = schema.currencyConvertor;
        NiPrecisionSetting precisionSetting = scratchpad.getPrecisionSetting();

        List<CategAmountCell> res = protos.stream()
                .map(cacp -> cacp.materialize(usedCurrency, calendar, currencyConvertor, precisionSetting, false))
                .collect(toCollection(ArrayList::new));

        /*
         * AMP-27571
         * if canSplittingStrategyBeAdded is true we need to duplicate cells in original currency
        */
        if (engine.spec.isShowOriginalCurrency()) {
            // generate cells for original currency only (except used currency)
            protos.stream()
                    .map(cacp -> cacp.materialize(usedCurrency, calendar, currencyConvertor, precisionSetting, true))
                    .forEach(res::add);
        }

        long delta = System.currentTimeMillis() - start;
        engine.timer.putMetaInNode("materialize_time", delta);

        return res;
    }

    protected String buildSupplementalCondition(NiReportsEngine engine, Set<Long> ids, FundingFetcherContext context) {
        return "1=1";
    }

    /**
     * fetches cell prototypes with the given ownerIds
     * @param engine the engine running the report
     * @param ids the ids to fetch
     * @param context the level2 entry which will end up hosting the fetched cells in {@link FundingFetcherContext#cache} 
     * @return
     */
    protected List<CategAmountCellProto> fetchSkeleton(NiReportsEngine engine, Set<Long> ids, FundingFetcherContext context) {
        if (ids.isEmpty())
            return Collections.emptyList();
        AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);

        String query = String.format("SELECT * FROM %s WHERE %s IN (%s) AND (%s)", this.viewName, this.mainColumn, Util.toCSStringForIN(ids), buildSupplementalCondition(engine, ids, context));
        
        //TODO: do not commit this uncommented
        //query = query + " AND (transaction_date >= '2006-05-01') AND (transaction_date <= '2007-06-01')";
                        
        VivificatingMap<Long, AmpCurrency> currencies = new VivificatingMap<Long, AmpCurrency>(new HashMap<>(), CurrencyUtil::getAmpcurrency);
        
        AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
        
        List<CategAmountCellProto> cells = new ArrayList<>();
        MetaInfoGenerator metaGenerator = new MetaInfoGenerator(); // one metainfo generator per differential fetch run - as a tradeoff between a complicated expiring-one-per-cacher and an inefficient one-per-cell
        BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100l);
                
        try(RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
            while (rs.rs.next()) {
                MetaInfoSet metaSet = new MetaInfoSet(metaGenerator);
                Map<NiDimensionUsage, Coordinate> coos = new HashMap<>();
                
                long ownerId = rs.rs.getLong(this.mainColumn);
                    
                // fetch the non-disabled metadata (like adjustment_type etc)
                for(ImmutablePair<MetaCategory, String> longOptionalColumn:longColumnsToFetch)
                    if (!ignoredColumns.contains(longOptionalColumn.v))
                        addMetaIfLongExists(metaSet, longOptionalColumn.k, rs.rs, longOptionalColumn.v);
                
                // fetch the coordinates for the non-disabled NiDimensionUsage's
                addSubActivityCoordinates(coos, engine, rs.rs);

                if (this.name.equals(ENTITY_PLEDGE_FUNDING))
                    addCoordinateIfLongExists(coos, rs.rs, "related_project_id", schema.ACT_LEVEL_COLUMN);
                
                if (this.name.equals(ENTITY_COMPONENT_FUNDING)) {
                    metaSet.add(MetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY);
                }

                if (this.name.equals(ENTITY_REGIONAL_FUNDING)) {
                    metaSet.add(MetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY);
                }

                java.sql.Date transactionMoment = rs.rs.getDate("transaction_date");
                BigDecimal transactionAmount = rs.rs.getBigDecimal("transaction_amount");
                
                long currencyId = rs.rs.getLong("currency_id");
                AmpCurrency srcCurrency = currencies.getOrCreate(currencyId);
                BigDecimal fixed_exchange_rate = getBigDecimal(rs.rs, "fixed_exchange_rate");
                                                                
                BigDecimal capitalSpendPercent = getBigDecimal(rs.rs, "capital_spend_percent");
                if (capitalSpendPercent != null)
                    metaSet.add(MetaCategory.CAPITAL_SPEND_PERCENT.category, capitalSpendPercent.divide(ONE_HUNDRED));
                                
                addMetaIfIdValueExists(metaSet, "recipient_role_id", MetaCategory.RECIPIENT_ROLE, rs.rs, context.roles);
                addMetaIfIdValueExists(metaSet, "source_role_id", MetaCategory.SOURCE_ROLE, rs.rs, context.roles);
                addMetaIfIdValueExists(metaSet, "adjustment_type", MetaCategory.ADJUSTMENT_TYPE, rs.rs, context.acvs);
                addMetaIfIdValueExists(metaSet, "expenditure_class_id", MetaCategory.EXPENDITURE_CLASS, rs.rs, context.acvs);
                addMetaIfIdValueExists(metaSet, "terms_assist_id", MetaCategory.TYPE_OF_ASSISTANCE, rs.rs, context.acvs);
                addMetaIfIdValueExists(metaSet, "mode_of_payment_id", MetaCategory.MODE_OF_PAYMENT, rs.rs, context.acvs);
                addMetaIfIdValueExists(metaSet, "concessionality_level_id", MetaCategory.CONCESSIONALITY_LEVEL, rs.rs, context.acvs);

                // add the directed-transactions meta, if appliable
                if (metaSet.hasMetaInfo(MetaCategory.SOURCE_ROLE.category) && metaSet.hasMetaInfo(MetaCategory.RECIPIENT_ROLE.category)
                    && metaSet.hasMetaInfo(MetaCategory.SOURCE_ORG.category) && metaSet.hasMetaInfo(MetaCategory.RECIPIENT_ORG.category)) 
                {
                        metaSet.add(MetaCategory.DIRECTED_TRANSACTION_FLOW.category,
                            String.format("%s-%s",
                                    ArConstants.userFriendlyNameOfRole(metaSet.getMetaInfo(MetaCategory.SOURCE_ROLE.category).v.toString()), 
                                    ArConstants.userFriendlyNameOfRole(metaSet.getMetaInfo(MetaCategory.RECIPIENT_ROLE.category).v.toString())));
                }
                
                if (transactionMoment == null || transactionAmount == null || srcCurrency == null) 
                    continue; // there are valid cells which should not appear in the view... but sometimes they do (corrupted ancient entries + draft activities). just skip
                CategAmountCellProto cell = new CategAmountCellProto(ownerId, transactionAmount, srcCurrency, transactionMoment, metaSet, coos, fixed_exchange_rate);
                cells.add(cell);
            }
        }
        catch(Exception e) {
            throw AlgoUtils.translateException(e);
        }
        ImmutablePair<Long, Long> metaCacheStats = metaGenerator.getStats();
        engine.timer.putMetaInNode("meta_cache_calls", metaCacheStats.k);
        engine.timer.putMetaInNode("meta_cache_uncached", metaCacheStats.v);
        return cells;
    }
    
    @Override
    protected IdValuePair addMetaIfIdValueExists(MetaInfoSet set, String idColumnName, MetaCategory categ, ResultSet row, Map<Long, String> map) throws SQLException {
        if (viewColumns.contains(idColumnName))
            return super.addMetaIfIdValueExists(set, idColumnName, categ, row, map);
        return null;
    }

    /**
     * fetches a BigDecimal from a view, if the view column exists and the row has a non-null value. 
     * @param colName the view-column to fetch from
     * @return the fetched number OR null if view-column does not exist
     * @throws SQLException
     */
    protected BigDecimal getBigDecimal(ResultSet rs, String colName) throws SQLException {
        if (viewColumns.contains(colName))
            return rs.getBigDecimal(colName);
        
        return null;
    }
    
    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
    
    /**
     * the context data used for fetching funding. 
     * Given that any "big" database change will lead to this entry being reset and rebuilt, it is safe to keep any non-activity-level data here
     * @author Dolghier Constantin
     *
     */
    static class FundingFetcherContext {
        /**
         * the differential cache for cell prototypes
         */
        final DifferentialCache<CategAmountCellProto> cache;
        
        /**
         * the organization roles (select * from amp_role)
         */
        final Map<Long, String> roles;
        
        /**
         * the AmpCategoryValues
         */
        final Map<Long, String> acvs;
                
        public FundingFetcherContext(DifferentialCache<CategAmountCellProto> cache, Map<Long, String> roles, Map<Long, String> acvs) {
            this.cache = cache;
            this.roles = roles;
            this.acvs = acvs;
        }
    }
    
    /**
     * extracts the CapitalMultiplier off a cell
     * @param cell
     * @return
     */
    public final static BigDecimal getCapitalMultiplier(CategAmountCell cell) {
        MetaInfo mInfo = cell.getMetaInfo().getMetaInfo(MetaCategory.CAPITAL_SPEND_PERCENT.category);
        return mInfo == null ? null : (BigDecimal) mInfo.v; 
    }
    
    /**
     * extracts the RecurrentMultiplier off a cell
     * @param cell
     * @return
     */
    public final static BigDecimal getRecurrentMultiplier(CategAmountCell cell) {
        BigDecimal capMult = getCapitalMultiplier(cell);
        return capMult == null ? null : BigDecimal.ONE.subtract(capMult);
    }

}
