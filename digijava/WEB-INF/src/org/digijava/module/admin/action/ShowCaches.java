/*
 *   ShowCaches.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Mar 26, 2004
 * 	 CVS-ID: $Id: ShowCaches.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
package org.digijava.module.admin.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.admin.form.CacheForm;
import net.sf.swarmcache.CacheFactory;
import net.sf.swarmcache.ObjectCache;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import net.sf.hibernate.Session;
import org.digijava.commons.hibernate.cache.CacheRegistry;
import org.digijava.commons.hibernate.cache.Cache;

public class ShowCaches
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CacheForm formBean = (CacheForm) form;


        Map cachesMap = CacheFactory.getAllCaches();
        List cachesList = new ArrayList();
        User user = RequestUtils.getUser(request);
        long totalCount = 0;
        boolean clearing = false;

        if (user.isGlobalAdmin()) {

            Iterator iter = cachesMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry item = (Map.Entry) iter.next();

                ObjectCache cache = (ObjectCache) item.getValue();

                CacheForm.CacheInfo ci = new CacheForm.CacheInfo();

                ci.setKey(item.getKey().toString());
                ci.setSize(cache.size());
                ci.setCache(cache);

                cachesList.add(ci);
                totalCount += cache.size();
            }

            iter = CacheRegistry.getCaches().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry item = (Map.Entry) iter.next();

                Cache cache = (Cache) item.getValue();

                CacheForm.CacheInfo ci = new CacheForm.CacheInfo();

                ci.setKey(item.getKey().toString());
                ci.setSize(cache.size());
                ci.setHibernateCache(cache);

                cachesList.add(ci);
                totalCount += cache.size();
            }

            Comparator comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    CacheForm.CacheInfo i1 = (CacheForm.CacheInfo) o1;
                    CacheForm.CacheInfo i2 = (CacheForm.CacheInfo) o2;

                    return i1.getKey().compareTo(i2.getKey());
                }
            };

            Collections.sort(cachesList, comparator);

            if (formBean.getIndex() >= 0) {
                CacheForm.CacheInfo ci = (CacheForm.CacheInfo) cachesList.get(
                    formBean.getIndex());
                if (ci.getCache() != null) {
                    ci.getCache().clearAll();
                } else {
                    ci.getHibernateCache().clear();
                }
                clearing = true;
            }
	    formBean.setCachesList(cachesList);
	    formBean.setCacheCount(cachesList.size());
	    formBean.setTotalCount(totalCount);

	    if (clearing) {
		return new ActionForward("/admin/showCaches.do", true);
	    } else {
		return mapping.findForward("forward");
	    }

        } else {
	    return new ActionForward("/admin/index.do", true);
	}


    }

}