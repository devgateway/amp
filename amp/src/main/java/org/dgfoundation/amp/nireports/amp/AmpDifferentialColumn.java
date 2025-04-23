package org.dgfoundation.amp.nireports.amp;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.diffcaching.ActivityInvalidationDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.amp.diff.KeyBuilder;
import org.dgfoundation.amp.nireports.amp.diff.SSCKeyBuilder;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;

import java.util.List;
import java.util.Set;
import java.util.function.Function;


/**
 * an AMP column which keeps a keyed cache of a column indexed by activity_id in a {@link DifferentialCache}, subject to invalidation on db changes or a timeout. <br />
 * Implementation-level. it is a two-level cache of cells subject to invalidation. The first level is a cache-of-differential-caches keyed in a construction-specified way. 
 * This first level is configured by {@link #cacheKeyBuilder}. <br /> 
 * The second level is a {@link DifferentialCache} which caches on an activity-id-level and fetches/invalidates/refills on a per-column-per-key based accounting.
 * For a visual representation of the way this 2-level cache works and invalidates, please visit documentation <a href='https://wiki.dgfoundation.org/display/AMPDOC/4.+The+AMP+NiReports+Schema#id-4.TheAMPNiReportsSchema-4.2.4.1AmpDifferentialColumn'>here</a>. <br /> 
 * The differential behaviour can be disabled by setting {@link AmpReportsSchema#ENABLE_CACHING} to false. This would effectively transform this class into {@link AmpSqlSourcedColumn}, thus should only happen during schema debugging
 * 
 * @author Dolghier Constantin
 *
 * @param <K> the type of the generated cells
 */
public abstract class AmpDifferentialColumn<K extends Cell> extends AmpSqlSourcedColumn<K> {
    
    public final static Logger logger = Logger.getLogger(AmpDifferentialColumn.class);
    
    /**
     * the unconditional timeout of the level 1 cache. This is technically not needed - it is a safeguard against some missing postgresql-side trigger
     */
    public final static int CACHE_TTL_SECONDS = 10 * 60;
    
    /**
     * the key builder for the level 1 cache
     */
    protected final KeyBuilder cacheKeyBuilder;
    
    /**
     * the level 1 cache, having as its entries the individual level 2 caches (which are {@link DifferentialCache} instances)
     */
    protected final ExpiringCacher<String, NiReportsEngine, DifferentialCache<K>> cacher;
    
    /**
     * the level 1 cache invalidator. Also used as a storage of the "last fetched etl event id" that is used to drive {@link DifferentialCache}
     */
    protected final ActivityInvalidationDetector invalidationDetector;

    public AmpDifferentialColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName,
            KeyBuilder cacheKeyBuilder, Behaviour<?> behaviour) {
        this(columnName, levelColumn, viewName, cacheKeyBuilder, behaviour, false);
    }

    public AmpDifferentialColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName,
            KeyBuilder cacheKeyBuilder, Behaviour<?> behaviour, boolean sscEnabledColumn) {
        super(columnName, levelColumn, viewName, behaviour, sscEnabledColumn);
        this.cacheKeyBuilder = keyBuilder(cacheKeyBuilder, sscEnabledColumn);
        this.invalidationDetector = new ActivityInvalidationDetector();
        this.cacher = new ExpiringCacher<>(String.format("column %s cacher", columnName), this::origFetch, this.invalidationDetector, CACHE_TTL_SECONDS * 1000);
    }

    private KeyBuilder keyBuilder(KeyBuilder cacheKeyBuilder, boolean sscEnabledColumn) {
        if (sscEnabledColumn) {
            return new SSCKeyBuilder(this, cacheKeyBuilder);
        } else {
            return cacheKeyBuilder;
        }
    }
    
    /**
     * (re)fetches the needed cells from the database, populates the relevant level-1 and level-2 cache entries and returns the level-2 cache
     * @return the 2-tuple (fetched ids, level-2 cache)
     */
    protected ImmutablePair<Set<Long>, DifferentialCache<K>> differentiallyImportCells(NiReportsEngine engine, Function<Set<Long>, List<K>> fetcher) {
        DifferentialCache<K> cache = cacher.buildOrGetValue(cacheKeyBuilder.buildKey(engine, this), engine);
        return new ImmutablePair<>(AmpReportsScratchpad.get(engine).differentiallyImportCells(engine.timer, mainColumn, cache, fetcher), cache);
    }
    
    @Override
    public synchronized List<K> fetch(NiReportsEngine engine) {
        if (((AmpReportsSchema) engine.schema).ENABLE_CACHING) {
            DifferentialCache<K> cache =
                    differentiallyImportCells(engine, idsToReplace -> fetch(engine, idsToReplace)).v;
            return cache.getCells(engine.schemaSpecificScratchpad.getMainIds(engine, this));
        }
        else
            return super.fetch(engine);
    }
    
    /**
     * builds an empty level2 cache entry. 
     * @param key
     * @param engine
     * @return
     */
    protected synchronized DifferentialCache<K> origFetch(String key, NiReportsEngine engine) {
        engine.timer.putMetaInNode("resetCache", true);
        return new DifferentialCache<K>(invalidationDetector.getLastProcessedFullEtl());
    }
}
