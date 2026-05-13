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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AllSitesForm
    extends ActionForm {

    List sites;

    private boolean addSite;

    private String parentSiteName;
    private Long selectedSiteId;

    private boolean children;

    public static class SiteInfo {

        private Long id;
        private boolean editableSite;
        private String viewSite;
        private String editSite;
        private String admin;

        private String siteName;
        private String siteId;
        private boolean hasChildren;

        public SiteInfo() {

        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEditSite() {
            return editSite;
        }

        public void setEditSite(String editSite) {
            this.editSite = editSite;
        }

        public String getViewSite() {
            return viewSite;
        }

        public void setViewSite(String viewSite) {
            this.viewSite = viewSite;
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
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

        public boolean isEditableSite() {
            return editableSite;
        }

        public void setEditableSite(boolean editableSite) {
            this.editableSite = editableSite;
        }

        public boolean isHasChildren() {
            return hasChildren;
        }

        public void setHasChildren(boolean hasChildren) {
            this.hasChildren = hasChildren;
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        addSite = false;
        children = false;
        selectedSiteId = null;
        parentSiteName = null;
        sites = null;
    }

    public List getSites() {
        return sites;
    }

    public void setSites(List sites) {
        this.sites = sites;
    }

    public Long getSelectedSiteId() {
        return selectedSiteId;
    }

    public void setSelectedSiteId(Long selectedSiteId) {
        this.selectedSiteId = selectedSiteId;
    }

    public boolean isAddSite() {
        return addSite;
    }

    public void setAddSite(boolean addSite) {
        this.addSite = addSite;
    }

    public boolean isChildren() {
        return children;
    }

    public void setChildren(boolean children) {
        this.children = children;
    }

    public String getParentSiteName() {
        return parentSiteName;
    }

    public void setParentSiteName(String parentSiteName) {
        this.parentSiteName = parentSiteName;
    }
}
