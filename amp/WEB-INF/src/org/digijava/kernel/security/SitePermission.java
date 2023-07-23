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

package org.digijava.kernel.security;

import java.io.Serializable;
import java.security.Permission;

import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.permission.ObjectPermission;
import org.digijava.kernel.util.SiteCache;

import javax.persistence.Entity;

@Entity
public class SitePermission
    extends ObjectPermission implements Serializable {

    private static Logger logger = Logger.getLogger(SitePermission.class);
    //private static SiteSecurityManager instance = new SiteSecurityManager();

    private static final long serialVersionUID = 1;

    private String siteKey;

    public SitePermission(Site site, String actions) {
        super(Site.class.getName(), site.getId(), actions);
        this.siteKey = site.getSiteId();
    }

    public SitePermission(Site site, int action) {
        super(Site.class.getName(), site.getId(), action);
        this.siteKey = site.getSiteId();
    }



    public SitePermission(Long siteId, Integer action) {
        this(SiteCache.getInstance().getSite(siteId), action.intValue());
    }

    public SitePermission(Long siteId, int action) {
        this(SiteCache.getInstance().getSite(siteId), action);
    }

    public Long getSiteId() {
        return (Long)instanceId;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public boolean implies(Permission permission) {
        boolean result = false;
        if (permission instanceof SitePermission) {
            result = implies((SitePermission)permission);
        }
        if (result) {
            logger.debug("Implication happens from " + this.toString() + " to " +
                         permission);
        }
        return result;
    }

    private boolean implies(SitePermission sitePermission) {
        boolean result = false;
        Long siteId = getSiteId();
        Long otherSiteId = sitePermission.getSiteId();
        if (siteId != null &&  otherSiteId!= null &&
            !siteId.equals(otherSiteId)) {

            SiteCache siteCache = SiteCache.getInstance();
            Site otherSite = siteCache.getSite(otherSiteId);
            if (otherSite == null) {
                return false;
            }

            Site othersParent;
            if (otherSite.isInheritSecurity() &&
                ( (othersParent = siteCache.getParentSite(otherSite)) != null)) {
                SitePermission newPermission =
                    new SitePermission(othersParent,
                                       sitePermission.actionMask);
                result = implies(newPermission);
            }
        } else {
            result = super.implies(sitePermission);
        }

        return result;
    }
    /**
     * Returns array of objects, which will be persisted into database and then
     * used to reconstruct this permission object. Values are sorted by types
     * and order of constructor's parameter
     * @return array of objects
     */
    public Object[] getParameters() {
        return new Object[] { getSiteId(), new Integer(actionMask)};
    }

    protected int modifySecurityAction(int oldAction) {
        if (oldAction == 0x7) {
            return INT_ADMIN;
        }
        return oldAction;
    }

/*
    public AbstractObjectSecurityManager getSecurityManager() {
        return instance;
    }
 */
}
/*
class SiteSecurityManager
    extends AbstractObjectSecurityManager {
    public ObjectPermission createPermission(Class clazz, Object key,
                                             int action) {
        return new SitePermission( (Long) key, action);
    }
}
 */
