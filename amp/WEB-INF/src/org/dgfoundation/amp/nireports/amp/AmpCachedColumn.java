package org.dgfoundation.amp.nireports.amp;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

import org.dgfoundation.amp.diffcaching.DatabaseChangedDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.ContextKey;
import org.dgfoundation.amp.nireports.amp.diff.KeyBuilder;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;


/**
 * an AMP column which keeps a keyed cache of a column, subject to invalidation on db changes or a timeout
 * @author Dolghier Constantin
 *
 * @param <K> the type of the generated cells
 * @param <T> the type of the key
 */
public abstract class AmpCachedColumn<K extends Cell, T> extends AmpSqlSourcedColumn<K> {
	
	public final KeyBuilder<T> cacheKeyBuilder;
	public final ExpiringCacher<ContextKey<T>, List<K>> cacher;
	
	public AmpCachedColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName, String mainColumnId, KeyBuilder<T> cacheKeyBuilder, Behaviour<?> behaviour) {
		super(columnName, levelColumn, viewName, mainColumnId, behaviour);
		this.cacheKeyBuilder = cacheKeyBuilder;
		this.cacher = new ExpiringCacher<>(String.format("column %s cacher", columnName), cacheKey -> origFetch(cacheKey.context, cacheKey.key), new DatabaseChangedDetector(), 3 * 60 * 1000);
	}
	
	@Override
	public final List<K> fetch(NiReportsEngine engine) {
		List<K> res = cacher.buildOrGetValue(cacheKeyBuilder.buildKeyPair(engine, this));
		return res;
	}
	
	protected List<K> origFetch(NiReportsEngine engine, T key) {
		return super.fetch(engine);
	}
	
	protected abstract K extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException;
}
