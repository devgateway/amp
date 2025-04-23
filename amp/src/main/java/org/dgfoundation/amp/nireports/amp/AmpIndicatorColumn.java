package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.diffcaching.ActivityInvalidationDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.kernel.translator.LocalizableLabel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorColumn extends AmpAmountColumn {

    /*
     * the cell prototypes cache, plus some auxiliary info
     */
    protected final ExpiringCacher<Boolean, NiReportsEngine, FundingFetcherContext> cacher;
    protected final ActivityInvalidationDetector invalidationDetector;

    /**
     * the sync obj for non-thread-safe destructive operations
     */
    protected final Object cacheSyncObj = new Object();

    /**
     * the view-columns which should not be read by the fetching code.
     * An entry lands in this set either programmatically or through the column not existing in the view
     */
    protected final Set<String> ignoredColumns;

    /**
     * the number of seconds to keep fetched prototypes in memory
     */
    public static final int CACHE_TTL_SECONDS = 10 * 60;

    private final SubDimensions subDimensions;

    /**
     * delegates to {@link #AmpIndicatorColumn(String, LocalizableLabel, String, Behaviour, SubDimensions)}
     * with {@link TrivialMeasureBehaviour} as a behaviour
     */
    public AmpIndicatorColumn(String columnName, String viewName, SubDimensions subDimensions) {
        this(columnName, new LocalizableLabel(columnName), viewName, TrivialMeasureBehaviour.getInstance(),
                subDimensions);
    }

    /**
     * constructs an instance which will fetch a given AMP entity from a given PostgreSQL view with
     * a given {@link Behaviour}.
     * As part of initialisation, the view is scanned for columns which are supported by AMP Funding
     * but are missing from the view.
     * Any such viewcolumns (and their associated NiDimensionUsage's) will be ignored by this instance upon fetching
     *
     * @param columnName the name of the AMP entity (column) to fetch
     * @param viewName   the name of the PostgreSQL view to fetch data from
     * @param behaviour  the behaviour of the column
     */
    protected AmpIndicatorColumn(String columnName, LocalizableLabel label, String viewName, Behaviour<?> behaviour,
                                 SubDimensions subDimensions) {
        super(columnName, label, null, viewName, behaviour);
        this.invalidationDetector = new ActivityInvalidationDetector();
        this.subDimensions = subDimensions;
        Set<String> ic = new HashSet<>();
        for (String col : subDimensions.getColumnIdNames().values()) {
            if (!this.viewColumns.contains(col)) {
                ic.add(col); // ignore missing NiDimension-bearing viewcolumns
            }
        }
        for (String col : AmpCollections.relist(longColumnsToFetch, z -> z.v)) {
            if (!this.viewColumns.contains(col)) {
                ic.add(col); // ignore missing generic-numbers-bearing viewcolumns
            }
        }
        // specified-generic columns minus columns which do not exist in the view
        this.ignoredColumns = Collections.unmodifiableSet(ic);
        this.cacher = new ExpiringCacher<>("funding cacher " + columnName, (key, engine) -> resetCache(engine),
                invalidationDetector, CACHE_TTL_SECONDS * MILLIS_PER_SECOND);
    }

    /**
     * returns true IFF the column has been specified as being transaction-level
     * and the current funding view has not it blacklisted.
     * This is a hack to accommodate AMP's "multiple schemas per schema"
     *
     * @param col
     * @return
     */
    public boolean isTransactionLevelHierarchy(NiReportColumn<?> col) {
        if (!col.isTransactionLevelHierarchy()) {
            return false;
        }
        String viewName = subDimensions.getColumnIdNames().get(col.name);
        if (viewName == null) {
            return true;
        }
        return !ignoredColumns.contains(viewName);
    }

    /**
     * columns of type long which are optional
     */
    protected static List<ImmutablePair<MetaCategory, String>> longColumnsToFetch = Arrays.asList(
            new ImmutablePair<>(MetaCategory.INDICATOR_VALUE_TYPE, "value_type")
    );

    /**
     * builds a new l2 cache entry upon invalidation.
     *
     * @param engine
     * @return
     */
    protected synchronized FundingFetcherContext resetCache(NiReportsEngine engine) {
        engine.timer.putMetaInNode("resetCache", true);
        return new FundingFetcherContext(new DifferentialCache<>(invalidationDetector.getLastProcessedFullEtl()));
    }

    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) {
        AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
        AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
        boolean enableDiffing = schema.ENABLE_CACHING;

        List<CategAmountCell> cells;
        if (enableDiffing) {
            long start = System.currentTimeMillis();
            synchronized (cacheSyncObj) {
                FundingFetcherContext cache = cacher.buildOrGetValue(true, engine);

                scratchpad.differentiallyImportCells(engine.timer, mainColumn, cache.cache,
                        ids -> fetchSkeleton(engine, ids, cache));

                cells = cache.cache.getCells(scratchpad.getMainIds(engine, this));
                long delta = System.currentTimeMillis() - start;
                engine.timer.putMetaInNode("hot_time", delta);
            }
        } else {
            cells = fetchSkeleton(engine, scratchpad.getMainIds(engine, this), resetCache(engine));
        }

        return cells;
    }

    /**
     * fetches cell prototypes with the given ownerIds
     *
     * @param engine  the engine running the report
     * @param ids     the ids to fetch
     * @param context the level2 entry which will end up hosting the fetched cells
     *                in {@link FundingFetcherContext#cache}
     * @return
     */
    protected List<CategAmountCell> fetchSkeleton(NiReportsEngine engine, Set<Long> ids,
                                                  FundingFetcherContext context) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);

        String query = String.format("SELECT * FROM %s WHERE %s IN (%s)", this.viewName, this.mainColumn,
                Util.toCSStringForIN(ids));

        List<CategAmountCell> cells = new ArrayList<>();
        MetaInfoGenerator metaGenerator = new MetaInfoGenerator();

        try (RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
            while (rs.rs.next()) {
                MetaInfoSet metaSet = new MetaInfoSet(metaGenerator);
                Map<NiDimension.NiDimensionUsage, NiDimension.Coordinate> coos = new HashMap<>();

                long ownerId = rs.rs.getLong(this.mainColumn);

                // fetch the non-disabled metadata (like adjustment_type etc)
                for (ImmutablePair<MetaCategory, String> longOptionalColumn : longColumnsToFetch) {
                    if (!ignoredColumns.contains(longOptionalColumn.v)) {
                        addMetaIfLongExists(metaSet, longOptionalColumn.k, rs.rs, longOptionalColumn.v);
                    }
                }

                // fetch the coordinates for the non-disabled NiDimensionUsage's
                addSubActivityCoordinates(coos, engine, rs.rs);

                java.sql.Date transactionMoment = rs.rs.getDate("value_date");
                BigDecimal transactionAmount = rs.rs.getBigDecimal("value");

                if (transactionMoment == null || transactionAmount == null) {
                    continue;
                }

                MonetaryAmount ma = new MonetaryAmount(transactionAmount, null, null,
                        transactionMoment.toLocalDate(), scratchpad.getPrecisionSetting());
                TranslatedDate date = engine.calendar.translate(transactionMoment);

                CategAmountCell cell = new CategAmountCell(ownerId, ma, metaSet, coos, date);
                cells.add(cell);
            }
        } catch (Exception e) {
            throw AlgoUtils.translateException(e);
        }
        ImmutablePair<Long, Long> metaCacheStats = metaGenerator.getStats();
        engine.timer.putMetaInNode("meta_cache_calls", metaCacheStats.k);
        engine.timer.putMetaInNode("meta_cache_uncached", metaCacheStats.v);
        return cells;
    }

    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }

    /**
     * the context data used for fetching funding.
     * Given that any "big" database change will lead to this entry being reset and rebuilt,
     * it is safe to keep any non-activity-level data here
     *
     * @author Dolghier Constantin
     */
    static class FundingFetcherContext {
        /**
         * the differential cache for cells
         */
        private final DifferentialCache<CategAmountCell> cache;

        FundingFetcherContext(DifferentialCache<CategAmountCell> cache) {
            this.cache = cache;
        }
    }
}
