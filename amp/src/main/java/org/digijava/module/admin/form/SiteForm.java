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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.ValidatorForm;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteConfigUtils;

public class SiteForm
    extends ValidatorForm {

    public static class SiteDomainInfo
        implements Comparable {
        private String domain;
        private String path;
        private String langCode;
        private int enableSecurity = -1;
        private Long id;
        private int index;

        public SiteDomainInfo() {}

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getLangCode() {
            return langCode;
        }

        public void setLangCode(String langCode) {
            this.langCode = langCode;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getEnableSecurity() {
            return enableSecurity;
        }

        public void setEnableSecurity(int enableSecurity) {
            this.enableSecurity = enableSecurity;
        }

        public int compareTo(Object o) {
            SiteDomainInfo info = (SiteDomainInfo) o;

            String domain1 = this.domain == null ? "" : this.domain;
            int result = domain1.compareTo(info.domain);
            if (result == 0) {
                String path1 = this.path == null ? "" : this.path;
                return path1.compareTo(info.path);
            }
            else {
                return result;
            }
        }
    }

    public static class SiteInfo {

        private Site site;
        private String viewSite;
        private String admin;

        public Site getSite() {
            return site;
        }

        public void setSite(Site site) {
            this.site = site;
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

    }

    private Long id;
    private String siteName;
    private String siteKey;
    private boolean secure;
    private boolean invisible;
    private boolean inheritSecurity;
    private int priority;
    private String metaDescription;
    private String metaKeywords;
    private String mission;
    private Collection languages;
    private String[] userLanguages;
    private String[] transLanguages;
    private String defaultLanguage;
    private Collection templates;
    private String template;
    private Collection sites;
    private String parentSiteName;
    private boolean topLevel;
    private static final Collection priorities;
    private int defDomain;
    private java.util.ArrayList siteDomains;
    private Collection children;
    private String targetAction;
    private Long parentId;
    private String folderName;

    private String recieveEmailAlerts;

    private String country;
    private List countries;

    static {
        priorities = new ArrayList();
        for (int i = 0; i <= Site.MAX_PRIORITY; i++) {
            priorities.add(new Integer(i));
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        transLanguages = userLanguages = null;
        template = SiteConfigUtils.BLANK_TEMPLATE_NAME;
        siteDomains = new ArrayList();
        parentId = null;
        parentSiteName = null;
        siteName = null;
        siteKey = null;

        recieveEmailAlerts = "2";
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        // Validate using Struts validator plugin
        ActionErrors errors = super.validate(actionMapping, httpServletRequest);

        if (errors == null) {
            errors = new ActionErrors();
        }
        if ( (siteDomains == null) || (siteDomains.size() == 0)) {
            errors.add(null,
                       new ActionMessage("error.admin.siteDomainsEmpty"));
        }

        Iterator iter = siteDomains.iterator();
        TreeSet definedDomains = new TreeSet();
        HashSet unSecureDomainLanguages = new HashSet();
        HashSet secureDomainLanguages = new HashSet();
        boolean hasDefault = false;
        while (iter.hasNext()) {
            SiteDomainInfo item = (SiteDomainInfo) iter.next();
            if ( (item.getDomain() == null) ||
                (item.getDomain().trim().length() == 0)) {
                errors.add(null,
                           new ActionMessage("error.admin.domainEmpty"));
                break;
            }
            if ( (item.getPath() != null) &&
                (item.getPath().trim().length() != 0)) {
                if (!item.getPath().startsWith("/")) {
                    Object[] params = {
                        item.getPath()};
                    errors.add(null,
                               new ActionMessage("error.admin.domainPathStart",
                                               params));
                }
                if (item.getPath().endsWith("/")) {
                    Object[] params = {
                        item.getPath()};
                    errors.add(null,
                               new ActionMessage("error.admin.domainPathEnd",
                                               params));
                }
            }
            if (defDomain == item.getIndex()) {
                hasDefault = true;
            }
            if (definedDomains.contains(item)) {
                Object[] params = {
                    item.getDomain(),
                    item.getPath()};
                errors.add(null,
                           new ActionMessage("error.admin.siteDomainTwise",
                                           params));
            }
            else {
                definedDomains.add(item);
            }
            if ( (item.getLangCode() != null) &&
                (item.getLangCode().trim().length() != 0)) {
               boolean secure;
               boolean unsecure;
               switch (item.getEnableSecurity()) {
                   case 0:
                       secure = false;
                       unsecure = true;
                       break;
                   case 1:
                       secure = true;
                       unsecure = false;
                       break;
                   default:
                       secure = unsecure = true;
               }
               if ( (secure && secureDomainLanguages.contains(item.getLangCode())) ||
                   (unsecure &&
                    unSecureDomainLanguages.contains(item.getLangCode()))) {
                   errors.add(null,
                              new ActionMessage(
                                  "error.admin.oneDomainPerLanguage"));
               }
               else {
                   if (secure) {
                       secureDomainLanguages.add(item.getLangCode());

                   }
                   if (unsecure) {
                       unSecureDomainLanguages.add(item.getLangCode());
                   }
               }
           }
        }

        if (!hasDefault) {
            errors.add(null,
                       new ActionMessage("error.admin.defaultSiteDomainMissing"));
        }


        return errors.isEmpty() ? null : errors;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Collection getLanguages() {
        return languages;
    }

    public void setLanguages(Collection languages) {
        this.languages = languages;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Collection getTemplates() {
        return templates;
    }

    public void setTemplates(Collection templates) {
        this.templates = templates;
    }

    public Collection getSites() {
        return sites;
    }

    public void setSites(Collection sites) {
        this.sites = sites;
    }

    public java.util.ArrayList getSiteDomains() {
        return siteDomains;
    }

    public void setSiteDomains(java.util.ArrayList siteDomains) {
        this.siteDomains = siteDomains;
    }

    public SiteDomainInfo getSiteDomain(int index) {
        SiteDomainInfo info = null;
        int actualSize = siteDomains.size();
        if (index >= actualSize) {
            // Expand the list
            for (int i = 0; i <= index - actualSize; i++) {
                siteDomains.add(new SiteDomainInfo());
            }
        }

        return (SiteDomainInfo) siteDomains.get(index);
    }

    public String[] getUserLanguages() {
        return userLanguages;
    }

    public void setUserLanguages(String[] userLanguages) {
        this.userLanguages = userLanguages;
    }

    public String getParentSiteName() {
        return parentSiteName;
    }

    public void setParentSiteName(String parentSiteName) {
        this.parentSiteName = parentSiteName;
    }

    public String[] getTransLanguages() {
        return transLanguages;
    }

    public void setTransLanguages(String[] transLanguages) {
        this.transLanguages = transLanguages;
    }

    public boolean isTopLevel() {
        return topLevel;
    }

    public void setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
    }

    public boolean isInheritSecurity() {
        return inheritSecurity;
    }

    public void setInheritSecurity(boolean inheritSecurity) {
        this.inheritSecurity = inheritSecurity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public Collection getPriorities() {
        return priorities;
    }

    public int getDefDomain() {
        return defDomain;
    }

    public void setDefDomain(int defDomain) {
        this.defDomain = defDomain;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Collection getChildren() {
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public String getTargetAction() {
        return targetAction;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getRecieveEmailAlerts() {
        return recieveEmailAlerts;
    }

    public void setRecieveEmailAlerts(String recieveEmailAlerts) {
        this.recieveEmailAlerts = recieveEmailAlerts;
    }

    public List getCountries() {
        return countries;
    }

    public void setCountries(List countries) {
        this.countries = countries;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
