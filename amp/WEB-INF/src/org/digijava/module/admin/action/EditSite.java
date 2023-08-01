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

import org.apache.struts.action.*;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.kernel.util.SiteManager;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.module.admin.form.SiteForm;
import org.digijava.module.admin.util.DbUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class EditSite
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        Site currentSite = RequestUtils.getSite(request);
        Site realSite = DbUtil.getSite(currentSite.getId());
        ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(
            currentSite);

        String templateName = viewConfig.getTemplateName();

        SiteForm siteForm = (SiteForm) form;
        ActionMessages errors = new ActionMessages();

        if (!siteForm.getSiteKey().equals(realSite.getSiteId())) {
            Site someSite = SiteCache.lookupByName(siteForm.getSiteKey());
            if (someSite != null) {
                errors.add(null,
                           new ActionMessage("error.admin.siteKeyExists"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        }
        // Pre-cache languages
        Iterator iter = DbUtil.getAvailableLanguages().iterator();
        HashMap languages = new HashMap();
        while (iter.hasNext()) {
            Locale language = (Locale) iter.next();
            languages.put(language.getCode(), language);
        }

        // Synchronize SiteDomain objects
        HashMap siteDomainsToDelete = new HashMap();
        iter = realSite.getSiteDomains().iterator();
        while (iter.hasNext()) {
            SiteDomain item = (SiteDomain) iter.next();
            siteDomainsToDelete.put(new Long(item.getSiteDomainId()), item);
        }

        HashSet siteDomainLangs = new HashSet();
        iter = siteForm.getSiteDomains().iterator();
        while (iter.hasNext()) {
            SiteForm.SiteDomainInfo item = (SiteForm.SiteDomainInfo) iter.next();

            SiteDomain siteDomain = (SiteDomain) siteDomainsToDelete.get(item.
                getId());
            if (siteDomain == null) {
                siteDomain = new SiteDomain();
                siteDomain.setSite(realSite);
                realSite.getSiteDomains().add(siteDomain);
            }
            else {
                siteDomainsToDelete.remove(item.getId());
            }
            if (siteForm.getDefDomain() == item.getIndex()) {
                siteDomain.setDefaultDomain(true);
            }
            else {
                siteDomain.setDefaultDomain(false);
            }
            siteDomain.setSiteDbDomain(item.getDomain());
            if ( (item.getPath() == null) ||
                (item.getPath().trim().length() == 0)) {
                siteDomain.setSitePath(null);
            }
            else {
                siteDomain.setSitePath(item.getPath());
            }

            Site tmpSite = DbUtil.getSite(siteDomain.getSiteDomain(),
                                          siteDomain.getSitePath());
            if ( (tmpSite != null) &&
                (!tmpSite.getId().equals(currentSite.getId()))) {
                Object[] params = {
                    item.getDomain(),
                    item.getPath(), tmpSite.getName()};

                errors.add(null,
                           new ActionMessage("error.admin.siteDomainExists", params));
            }

            if (item.getLangCode() != null) {
                Locale lang = (Locale) languages.get(item.getLangCode());
                siteDomain.setLanguage(lang);
                if (lang != null) {
                    siteDomainLangs.add(item.getLangCode());
                }
            }
            else {
                siteDomain.setLanguage(null);
            }
            switch (item.getEnableSecurity()) {
                case 0:
                    siteDomain.setEnableSecurity(Boolean.FALSE);
                    break;
                case 1:
                    siteDomain.setEnableSecurity(Boolean.TRUE);
                    break;
                default:
                    siteDomain.setEnableSecurity(null);
            }

        }
        iter = siteDomainsToDelete.values().iterator();
        while (iter.hasNext()) {
            SiteDomain item = (SiteDomain) iter.next();
            item.setSite(null);
            iter.remove();
        }
        // Assign language preferences
        Locale defLanguage = (Locale)
            languages.get(siteForm.getDefaultLanguage());

        HashSet transLangs = new HashSet();
        HashSet transLangIds = new HashSet();
        if (siteForm.getTransLanguages() != null) {
            for (int i = 0; i < siteForm.getTransLanguages().length; i++) {
                Locale language = (Locale)
                    languages.get(siteForm.getTransLanguages()[i]);
                if (language != null) {
                    transLangs.add(language);
                    transLangIds.add(language.getCode());
                }
            }
        }

        // Validate languages here, not in action form
        HashSet userLangs = new HashSet();
        HashSet userLangIds = new HashSet();
        boolean defaultFound = false;
        if (siteForm.getUserLanguages() != null) {
            for (int i = 0; i < siteForm.getUserLanguages().length; i++) {
                Locale language = (Locale)
                    languages.get(siteForm.getUserLanguages()[i]);
                if (language != null) {
                    userLangs.add(language);
                    userLangIds.add(language.getCode());
                    // Verify, is this language included in translation language
                    // set or not
                    if (!transLangIds.contains(language.getCode())) {
                        Object[] param = {
                            language.getName()};
                        errors.add(null,
                                   new ActionMessage(
                                       "error.admin.userLangMustBeTranslationLang",
                                       param));
                    }
                    if ( (defLanguage != null) &&
                        (language.getCode().equals(defLanguage.getCode()))) {
                        defaultFound = true;
                    }
                }
            }
        }
        if (defLanguage != null && !defaultFound) {
            errors.add(null,
                       new ActionMessage("error.admin.defaultLangMustBeUserLang"));
        }

        if ( (defLanguage == null) && (userLangs.size() > 0)) {
            errors.add(null,
                       new ActionMessage("error.admin.defaultLangMustBeSet"));
        }

        // Validate domain languages against user languages
        String domainLangErrorKey = "error.admin.domainLangMustBeUserLang";
        if (defLanguage == null) {
            domainLangErrorKey = "error.admin.domainLangMustBeParentUserLang";
            userLangIds = new HashSet();
            Site parent = SiteCache.getInstance().getParentSite(currentSite);
            if (parent == null) {
                userLangIds.add("en");
            }
            else {
                iter = SiteCache.getInstance().getUserLanguages(parent).
                    iterator();
                while (iter.hasNext()) {
                    Locale parentLang = (Locale) iter.next();
                    userLangIds.add(parentLang.getCode());
                }
            }
        }

        iter = siteDomainLangs.iterator();
        while (iter.hasNext()) {
            String domainLang = (String) iter.next();
            if (!userLangIds.contains(domainLang)) {
                Locale lang = (Locale) languages.get(domainLang);
                Object[] param = {
                    lang != null ? lang.getName() : domainLang};
                errors.add(null,
                           new ActionMessage(
                               domainLangErrorKey,
                               param));
            }
        }
        //set country for site
        realSite.setCountries(new HashSet());
        if (siteForm.getCountry() != null &&
            !siteForm.getCountry().equals(Site.globalCountryIso)) {
            if (siteForm.getCountries() != null) {
                Iterator countryIter = siteForm.getCountries().iterator();
                while (countryIter.hasNext()) {
                    TrnCountry item = (TrnCountry) countryIter.next();
                    if (item.getIso().equals(siteForm.getCountry())) {
                        Country c = new Country();
                        c.setIso(item.getIso());
                        c.setCountryName(item.getName());

                        realSite.getCountries().add(c);

                        break;
                    }

                }
            }
        }

        if (errors.isEmpty()) {
            realSite.setDefaultLanguage(defLanguage);
            realSite.setUserLanguages(userLangs);
            realSite.setTranslationLanguages(transLangs);

            // Other properties
            realSite.setPriority(siteForm.getPriority());
            realSite.setSecure(siteForm.isSecure());
            realSite.setInvisible(siteForm.isInvisible());
            realSite.setMetaDescription(siteForm.getMetaDescription());
            realSite.setMetaKeywords(siteForm.getMetaKeywords());
            realSite.setMission(siteForm.getMission());
            realSite.setName(siteForm.getSiteName());
            realSite.setFolder(siteForm.getFolderName());

            //recieve email alerts
            if (siteForm.getRecieveEmailAlerts().equals("1")) {
                realSite.setSendAlertsToAdmin(new Boolean(true));
            }
            else if (siteForm.getRecieveEmailAlerts().equals("3")) {
                realSite.setSendAlertsToAdmin(null);
            }
            else {
                realSite.setSendAlertsToAdmin(new Boolean(false));
            }

            SiteManager.createSiteFolder(this.getServlet().getServletContext().
                                         getRealPath(SiteConfigUtils.SITE_DIR +
                "/" +
                realSite.getFolder())
                                         , templateName);

            // Force reloading site configuration
            viewConfig.reload();

            // Store site
            DbUtil.editSite(realSite);
            // Reload site cache
            SiteCache.getInstance().load();
        }
        else {
            saveErrors(request, errors);
        }
        return mapping.findForward("forward");
    }
}
