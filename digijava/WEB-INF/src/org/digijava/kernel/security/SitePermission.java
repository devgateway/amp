/*
 *   SitePermission.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: SitePermission.java,v 1.1 2005-07-06 10:34:22 rahul Exp $
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
package org.digijava.kernel.security;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.permission.ObjectPermission;
import java.security.Permission;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import java.io.Serializable;

public class SitePermission
    extends ObjectPermission implements Serializable {

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
        } else
        if (permission instanceof ModuleInstancePermission) {
            result = implies((ModuleInstancePermission)permission);
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

    private boolean implies(ModuleInstancePermission moduleInstancePermission) {
        if (moduleInstancePermission.getSiteId() != null) {
            Site site = SiteCache.getInstance().getSite(
                moduleInstancePermission.getSiteId());
            if (site == null) {
                return false;
            }

            SitePermission sitePermission = new SitePermission(site,
                moduleInstancePermission.actionMask);
            return implies(sitePermission);
        }
        else {
            return false;
        }
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

}
