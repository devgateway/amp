/*
*
* @Author steve.ebersole@jboss.com
* CVS-ID: $Id: StaleTolerantQueryCacheFactory.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
*
*/


package org.digijava.kernel.persistence;

import net.sf.hibernate.cache.QueryCacheFactory;
import net.sf.hibernate.cache.QueryCache;
import net.sf.hibernate.cache.CacheProvider;
import net.sf.hibernate.cache.UpdateTimestampsCache;
import net.sf.hibernate.HibernateException;

import java.util.Properties;

/**
 */
public class StaleTolerantQueryCacheFactory implements QueryCacheFactory {
	public QueryCache getQueryCache(String regionName, CacheProvider provider, UpdateTimestampsCache updateTimestampsCache, Properties props) throws HibernateException {
		return new StaleTolerantQueryCache(provider, props, updateTimestampsCache, regionName);
	}
}
