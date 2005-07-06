/*
 *   CacheForm.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Mar 26, 2004
 * 	 CVS-ID: $Id: CacheForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.module.admin.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import net.sf.swarmcache.ObjectCache;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import net.sf.hibernate.cache.*;

public class CacheForm
    extends ActionForm {

    public static class CacheInfo {
        private String key;
        private int size;
        private ObjectCache cache;
        private org.digijava.commons.hibernate.cache.Cache hibernateCache;

        public CacheInfo() {
            this.cache = null;
            this.hibernateCache = null;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public ObjectCache getCache() {
            return cache;
        }

        public org.digijava.commons.hibernate.cache.Cache getHibernateCache() {
            return hibernateCache;
        }

        public void setCache(ObjectCache cache) {
            if (this.hibernateCache != null) {
                throw new IllegalStateException("Unable to set cache property, because hibernateCache property is already set");
            }
            this.cache = cache;
        }

        public void setHibernateCache(org.digijava.commons.hibernate.cache.Cache hibernateCache) {
            if (this.cache != null) {
                throw new IllegalStateException("Unable to set hibernateCache property, because cache property is already set");
            }
            this.hibernateCache = hibernateCache;
        }
    }

    private List cachesList;
    private int cacheCount;
    private long totalCount;

    private int index;

    public List getCachesList() {
        return cachesList;
    }

    public void setCachesList(List cachesList) {
        this.cachesList = cachesList;
    }

    public int getCacheCount() {
        return cacheCount;
    }

    public void setCacheCount(int cacheCount) {
        this.cacheCount = cacheCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        index = -1;
    }

}