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

package org.digijava.module.admin.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.CacheForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ShowCaches
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CacheForm formBean = (CacheForm) form;

        boolean clearing = false;

        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AbstractCache i1 = (AbstractCache) o1;
                AbstractCache i2 = (AbstractCache) o2;

                return i1.getName().compareTo(i2.getName());
            }
        };

        formBean.setCustomCachesList(new ArrayList());
        formBean.setHibernateCachesList(new ArrayList());
        formBean.setCustomTotalCount(0);
        formBean.setHibernateTotalCount(0);

        if (DigiSecurityManager.isGlobalAdminSubject(RequestUtils.getSubject(
            request))) {
            Collection<AbstractCache> caches = DigiCacheManager.getInstance().getCaches();
            if (caches != null) {
                for (AbstractCache cache : caches) {
                    
                    formBean.getHibernateCachesList().add(cache);

                    if (formBean.getHibernateKey() != null &&
                        formBean.getHibernateKey().equals(cache.getName())) {
                        cache.clear();
                        clearing = true;
                    }

                    formBean.setHibernateTotalCount(formBean.
                        getHibernateTotalCount() + cache.getSize());
                }
            }
            Collections.sort(formBean.getHibernateCachesList(), comparator);

            formBean.setCustomCacheCount(formBean.getCustomCachesList().size());
            formBean.setHibernateCacheCount(formBean.getHibernateCachesList().
                                            size());

            if (clearing) {
                return new ActionForward("/admin/showCaches.do", true);
            }
            else {
                return mapping.findForward("forward");
            }

        }
        else {
            return new ActionForward("/admin/index.do", true);
        }

    }

}
