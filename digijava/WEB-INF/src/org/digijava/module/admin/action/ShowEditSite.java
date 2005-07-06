/*
 *   ShowEditSite.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 16, 2003
 * 	 CVS-ID: $Id: ShowEditSite.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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
import javax.security.auth.Subject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteConfigManager;
import org.digijava.module.admin.form.SiteForm;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import java.util.Collection;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.translator.util.TrnCountry;
import java.util.*;
import org.digijava.kernel.dbentity.Country;

public class ShowEditSite
    extends Action {

    private static final Comparator siteDomainInfoComparator =
        new Comparator() {

        public int compare(Object o1,
                           Object o2) {
            SiteForm.SiteDomainInfo s1 = (SiteForm.SiteDomainInfo) o1;
            SiteForm.SiteDomainInfo s2 = (SiteForm.SiteDomainInfo) o2;

            return s1.getId().compareTo(s2.getId());
        }

    };

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        Site currentSite = RequestUtils.getSite(request);
        Site realSite = DbUtil.getSite(currentSite.getId());
        ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(currentSite);

        String templateName = viewConfig.getTemplateName();

        SiteForm siteForm = (SiteForm) form;
        siteForm.setSiteName(realSite.getName());
        siteForm.setSiteKey(realSite.getSiteId());
        siteForm.setPriority(realSite.getPriority());
        siteForm.setSecure(realSite.isSecure());
        siteForm.setInvisible(realSite.isInvisible());
        siteForm.setInheritSecurity(realSite.isInheritSecurity());
        siteForm.setMetaDescription(realSite.getMetaDescription());
        siteForm.setMetaKeywords(realSite.getMetaKeywords());
        siteForm.setMission(realSite.getMission());
        siteForm.setId(realSite.getId());
        siteForm.setTemplate(templateName);
        siteForm.setFolderName(realSite.getFolder());
        //email alerts

        if (realSite.getSendAlertsToAdmin()!=null) {
          if (realSite.getSendAlertsToAdmin().booleanValue()) {
            siteForm.setRecieveEmailAlerts("1");
          }else {
            siteForm.setRecieveEmailAlerts("2");
          }
        }else {
          siteForm.setRecieveEmailAlerts("3");
        }

        if (realSite.getParentId() != null) {
            Site parentSite = DbUtil.getSite(realSite.getParentId());
            siteForm.setParentSiteName(parentSite.getName());
        }
        else {
            siteForm.setParentSiteName(null);
        }

        siteForm.setSiteDomains(new ArrayList());
        int index = 0;
        if (realSite.getSiteDomains() != null) {
            Iterator iter = realSite.getSiteDomains().iterator();
            while (iter.hasNext()) {
                SiteDomain siteDomain = (SiteDomain) iter.next();
                if (siteDomain.isDefaultDomain()) {
                    siteForm.setDefDomain(index);
                }
                SiteForm.SiteDomainInfo info = new SiteForm.SiteDomainInfo();
                info.setDomain(siteDomain.getSiteDbDomain());

                info.setPath(siteDomain.getSitePath());
                info.setId(new Long(siteDomain.getSiteDomainId()));

                if (siteDomain.getLanguage() == null) {
                    info.setLangCode(null);
                }
                else {
                    info.setLangCode(siteDomain.getLanguage().getCode());
                }
                info.setIndex(index);
                siteForm.getSiteDomains().add(info);

                index++;
            }
        }
        Collections.sort(siteForm.getSiteDomains(), siteDomainInfoComparator);

        if (realSite.getParentId() == null) {
            siteForm.setParentSiteName(null);
        }
        else {
            Site parentSite = DbUtil.getSite(realSite.getParentId());
        }

        if (realSite.getUserLanguages() != null) {
            String[] userLanguages = new String[realSite.getUserLanguages().
                size()];
            Iterator iter = realSite.getUserLanguages().iterator();
            int i = 0;
            while (iter.hasNext()) {
                Locale language = (Locale) iter.next();
                userLanguages[i] = language.getCode();
                i++;
            }
            siteForm.setUserLanguages(userLanguages);
        }
        else {
            siteForm.setUserLanguages(null);
        }

        if (realSite.getTranslationLanguages() != null) {
            String[] translationLanguages = new String[realSite.
                getTranslationLanguages().size()];
            Iterator iter = realSite.getTranslationLanguages().iterator();
            int i = 0;
            while (iter.hasNext()) {
                Locale language = (Locale) iter.next();
                translationLanguages[i] = language.getCode();
                i++;
            }
            siteForm.setTransLanguages(translationLanguages);
        }
        else {
            siteForm.setTransLanguages(null);
        }

        if (realSite.getDefaultLanguage() != null) {
            siteForm.setDefaultLanguage(realSite.getDefaultLanguage().
                                           getCode());
        }
        else {
            siteForm.setDefaultLanguage(null);
        }

        siteForm.setLanguages(DbUtil.getAvailableLanguages());

        siteForm.setChildren(new ArrayList());

        Subject subject = DgSecurityManager.getSubject(request);

        Iterator iter = SiteUtils.getChildSites(currentSite.getId().longValue()).iterator();
        while (iter.hasNext()) {
            Site childSite = (Site)iter.next();
            SiteForm.SiteInfo info = new SiteForm.SiteInfo();
            siteForm.getChildren().add(info);
            info.setSite(childSite);
            info.setViewSite(DgUtil.getSiteUrl(childSite, request));
            if (DgSecurityManager.permitted(subject, childSite,
                ResourcePermission.INT_ADMIN) ) {
                info.setAdmin(DgUtil.getSiteUrl(childSite, request) + "/admin");
            } else {
                info.setAdmin(null);
            }
        }
        //get countries list
        if (siteForm.getCountries() == null || siteForm.getCountries().size() == 0) {
          Collection countries = TrnUtil.getCountries(RequestUtils.
                                                      getNavigationLanguage(request).
                                                      getCode());
          ArrayList sortedCountries = new ArrayList(countries);
          Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
          TrnCountry global = new TrnCountry(Site.globalCountryIso,
                                             Site.globalCountryName);
          sortedCountries.add(0, global);
          siteForm.setCountries(sortedCountries);
        }

        Set siteCountries = realSite.getCountries();
        if (siteCountries != null) {
          Iterator countryIter = siteCountries.iterator();
          while (countryIter.hasNext()) {
            Country item = (Country)countryIter.next();
            siteForm.setCountry(item.getIso());

            break;
          }
        } else {
          siteForm.setCountry(null);
        }


        return mapping.findForward("forward");
    }

}