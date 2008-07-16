/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
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

	private static Log log = LogFactory.getLog(StaleTolerantQueryCache.class);
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
        log.debug("isUpToDate() called. Spaces: " + spaces + " timestamp: " + timestamp);
		if ( tolerableSpaces.containsAll(spaces) ) {
            if (log.isDebugEnabled()) {
                log.debug("Tolerable spaces are defined for the target set: " + spaces);
            }
			return true;
		}
		else {
			return super.isUpToDate(spaces, timestamp);
		}
	}

	private Collection parseTolerableSpaces(Properties props) {
		String spaces = props.getProperty("hibernate.cache.stale_tolerable_query_spaces");
        if (spaces == null) {
            log.info("Stale tolerant query spaces are not defined");
            return new ArrayList();
        } else {
            log.info("Attempting to parse given stale tolerant query spaces [" +
                     spaces + "]");
            return Arrays.asList(StringHelper.split(", ", spaces));
        }
	}

}
