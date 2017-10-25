package org.dgfoundation.amp.nireports.amp;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

import org.dgfoundation.amp.diffcaching.DatabaseChangedDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.KeyBuilder;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;


/**
 * an AMP column which keeps a keyed cache of a column, subject to invalidation on db changes or a timeout. 
 * Each instance of this class has an {@link ExpiringCacher} instance, the key being built by a callback specified at construction time. 
 * For this class, the cache is non-granular at the activityId level, thus <i>you should subclass it only when your fetcher ignores ampActivityIds and fetches columns entirely</i>. <br /> 
 * Please see {@link ExpiringCacher} for more details. <br />
 * @author Dolghier Constantin
 *
 * @param <K> the type of the generated cells
 */
public abstract class AmpCachedColumn<K extends Cell> extends AmpSqlSourcedColumn<K> {
    
    private final KeyBuilder cacheKeyBuilder;
    private final ExpiringCacher<String, NiReportsEngine, List<K>> cacher;
    
    public AmpCachedColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName,
            KeyBuilder cacheKeyBuilder, Behaviour<?> behaviour) {
        super(columnName, levelColumn, viewName, behaviour, false);
        this.cacheKeyBuilder = cacheKeyBuilder;
        this.cacher = new ExpiringCacher<>(String.format("column %s cacher", columnName), this::origFetch, new DatabaseChangedDetector(), 3 * 60 * 1000);
    }
    
    @Override
    public final List<K> fetch(NiReportsEngine engine) {
        List<K> res = cacher.buildOrGetValue(cacheKeyBuilder.buildKey(engine, this), engine);
        return res;
    }
    
    protected List<K> origFetch(String key, NiReportsEngine engine) {
        return super.fetch(engine);
    }
    
    protected abstract K extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException;
}
