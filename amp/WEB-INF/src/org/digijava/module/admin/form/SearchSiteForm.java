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

package org.digijava.module.admin.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Set;

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
            errors.add(null, new ActionMessage("error.admin.siteKeyEmpty"));
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
