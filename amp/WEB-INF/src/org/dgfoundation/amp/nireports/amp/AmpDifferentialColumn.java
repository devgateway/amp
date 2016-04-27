package org.dgfoundation.amp.nireports.amp;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.diffcaching.ActivityInvalidationDetector;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.ContextKey;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.amp.diff.KeyBuilder;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;


/**
 * an AMP column which keeps a keyed cache of a column indexed by activity_id, subject to invalidation on db changes or a timeout
 * @author Dolghier Constantin
 *
 * @param <K> the type of the generated cells
 * @param <T> the type of the key
 */
public abstract class AmpDifferentialColumn<K extends Cell, T> extends AmpSqlSourcedColumn<K> {
	
	public final static Logger logger = Logger.getLogger(AmpDifferentialColumn.class);
	public final static int CACHE_TTL_SECONDS = 10 * 60;
	
	protected final KeyBuilder<T> cacheKeyBuilder;
	protected final ExpiringCacher<ContextKey<T>, DifferentialCache<K>> cacher;
	protected final ActivityInvalidationDetector invalidationDetector;
	
	public AmpDifferentialColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName, KeyBuilder<T> cacheKeyBuilder, Behaviour<?> behaviour) {
		super(columnName, levelColumn, viewName, behaviour);
		this.cacheKeyBuilder = cacheKeyBuilder;
		this.invalidationDetector = new ActivityInvalidationDetector();
		this.cacher = new ExpiringCacher<>(String.format("column %s cacher", columnName), cacheKey -> origFetch(cacheKey.context, cacheKey.key), this.invalidationDetector, CACHE_TTL_SECONDS * 1000);
	}
	
	/**
	 * returns 
	 * @return
	 */
	public ImmutablePair<Set<Long>, DifferentialCache<K>> differentiallyImportCells(NiReportsEngine engine, Function<Set<Long>, List<K>> fetcher) {
		DifferentialCache<K> cache = cacher.buildOrGetValue(cacheKeyBuilder.buildKeyPair(engine, this));
		return new ImmutablePair<>(AmpReportsScratchpad.get(engine).differentiallyImportCells(engine.timer, mainColumn, cache, fetcher), cache);
	}
	
	@Override
	public synchronized List<K> fetch(NiReportsEngine engine) {
		DifferentialCache<K> cache = differentiallyImportCells(engine, idsToReplace -> super.fetch(engine, idsToReplace)).v;
		return cache.getCells(engine.getMainIds());
	}
	
	protected synchronized DifferentialCache<K> origFetch(NiReportsEngine engine, T key) {
		engine.timer.putMetaInNode("resetCache", true);
		return new DifferentialCache<K>(invalidationDetector.getLastProcessedFullEtl());
	}
		
	protected abstract K extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException;
}
