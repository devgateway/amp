/*
*
* @Author steve.ebersole@jboss.com
* CVS-ID: $Id: StaleTolerantQueryCache.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
*
*/


package org.digijava.kernel.persistence;

import net.sf.hibernate.cache.StandardQueryCache;
import net.sf.hibernate.cache.QueryCache;
import net.sf.hibernate.cache.CacheProvider;
import net.sf.hibernate.cache.UpdateTimestampsCache;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.util.StringHelper;

import java.util.Properties;
import java.util.Collection;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;

/**
 * A query cache implementation that is tolerant of stale query results.
 * Essentially it does this by ignoring the update timestamp cache; the
 * application, then, has full responsibility for clearing this cache (either
 * programatically or through eviction policies in the underlying cache impl).
 */
public class StaleTolerantQueryCache extends StandardQueryCache implements QueryCache {

	private static Log log = LogFactory.getLog(StandardQueryCache.class);
	private Collection tolerableSpaces;

	public StaleTolerantQueryCache(
	        CacheProvider provider,
	        Properties props,
	        UpdateTimestampsCache updateTimestampsCache,
	        String regionName) throws HibernateException {
		super(provider, props, updateTimestampsCache, regionName);
		tolerableSpaces = parseTolerableSpaces(props);
	}

	protected boolean isUpToDate(Set spaces, Long timestamp) throws HibernateException {
		if ( tolerableSpaces.containsAll(spaces) ) {
			return true;
		}
		else {
			return super.isUpToDate(spaces, timestamp);
		}
	}

	private Collection parseTolerableSpaces(Properties props) {
		String spaces = props.getProperty("hibernate.cache.stale_tolerable_query_spaces");
        if (spaces == null) {
            log.warn("Stale tolerant query spaces are not defined");
            return new ArrayList();
        } else {
            log.info("Attempting to parse given stale tolerant query spaces [" +
                     spaces + "]");
            return Arrays.asList(StringHelper.split(", ", spaces));
        }
	}

}
