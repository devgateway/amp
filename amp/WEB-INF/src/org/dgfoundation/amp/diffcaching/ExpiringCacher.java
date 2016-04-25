package org.dgfoundation.amp.diffcaching;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.log4j.Logger;

/**
 * a cache which invalidates every time a timeout happens OR one of the resetting queries happens
 * @author Dolghier Constantin
 *
 * @param <K>
 * @param <V>
 */
public class ExpiringCacher<K, V> {
	public static Logger logger = Logger.getLogger(ExpiringCacher.class);
	
	protected long lastTimeTokenCleared = -1;
	protected final String name;
	protected final Function<K, V> valueGenerator;
	protected final Supplier<Boolean> cacheInvalidator;
	
	public ExpiringCacher(String name, Function<K, V> valueGenerator, Supplier<Boolean> cacheInvalidator, long timeout) {
		this.timeout = timeout;
		this.name = name;
		this.valueGenerator = valueGenerator;
		this.cacheInvalidator = cacheInvalidator;
	}
	
	/** the timeout, in millies, to clear the cache after */
	final long timeout;
	
	final ConcurrentHashMap<K, V> entries = new ConcurrentHashMap<>();
	
	public V buildOrGetValue(K key) {
		checkCache();
		return entries.computeIfAbsent(key, this::generateValue);
	}
	
	protected V generateValue(K key) {
		logger.debug(String.format("cacher %s: computing value for key %s", this.name, key));
		return valueGenerator.apply(key);
	}
	
	protected void checkCache() {
		long now = System.currentTimeMillis();
		if (cacheInvalidator.get() || lastTimeTokenCleared < 0 || now - lastTimeTokenCleared >= timeout) {
			logger.debug(String.format("cacher %s: clearing cache", name));
			entries.clear();
			lastTimeTokenCleared = now;
			return;
		}
	}
}
