package net.sf.hibernate.cache;

import net.sf.swarmcache.CacheConfiguration;
import net.sf.swarmcache.CacheConfigurationManager;
import net.sf.swarmcache.CacheFactory;

import java.util.Properties;

/**
 * SwarmCacheProvider
 * @author Jason Carreira
 * Created Sep 19, 2003 4:00:12 PM
 */
public class SwarmCacheProvider
    implements CacheProvider {

  public SwarmCacheProvider() {
  }

  public Cache buildCache(String s, Properties properties) throws
      CacheException {
    CacheConfiguration config = CacheConfigurationManager.getConfig(properties);
    CacheFactory factory = new CacheFactory(config);
    return new SwarmCache(factory.createCache(s));
  }

  public long nextTimestamp() {
    return Timestamper.next();
  }
}