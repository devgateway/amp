/*
 *   SearchSiteForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: SearchSiteForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

import java.util.ArrayList;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SearchSiteForm
    extends ActionForm {

    private String siteKey;
    private ArrayList sites;


    public static class SiteInfo {

        private Long id;

        private String siteName;
        private String siteId;
        private Set siteDomains;
        private String adminLink;
        private boolean delete;

        public SiteInfo() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public Set getSiteDomains() {
            return siteDomains;
        }

        public void setSiteDomains(Set siteDomains) {
            this.siteDomains = siteDomains;
        }

        public String getAdminLink() {
            return adminLink;
        }

        public void setAdminLink(String adminLink) {
            this.adminLink = adminLink;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        sites = null;
        siteKey = null;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = new ActionErrors();
        if ( (siteKey == null) || (siteKey.trim().length() == 0)) {
            errors.add(null, new ActionError("error.admin.siteKeyEmpty"));
        }

        return errors.isEmpty() ? null : errors;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public ArrayList getSites() {
        return sites;
    }

    public void setSites(ArrayList sites) {
        this.sites = sites;
    }


}