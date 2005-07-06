/*
 *   ShowUserUpdateGateway.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowUserUpdateGateway.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

package org.digijava.module.um.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.um.form.UserUpdateForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.InterestsCallback;
import org.digijava.module.um.util.UmUtil;
import org.digijava.kernel.entity.Interests;
import java.util.*;

public class ShowUserUpdateGateway
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(
        ShowUserUpdateGateway.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserUpdateForm registerForm = (UserUpdateForm) form;

        User user = (User) request.getSession(true).getAttribute(Constants.USER);

        if (registerForm.getSiteId() == null) {
            registerForm.setSiteId(RequestUtils.getSite(request).getId());
        }

        // ---------- Topics
        Site site = SiteUtils.getSite(Constants.ALL_TOPICS_SITE);
        if (site == null)
            logger.warn("site " + Constants.ALL_TOPICS_SITE +
                        " could not found");
        String[] selectedTopics = null;
        List sites = DbUtil.getTopicsSites(site);
        if (sites != null) {
            selectedTopics = new String[sites.size()];

            // Array of Topics
            ArrayList sets = UmUtil.getGenerateInterests(user.getInterests(),
                sites,
                selectedTopics, request);
            if (sets != null) {

                if (registerForm.isHighlight()) {
                    Site curSite = RequestUtils.getSite(request);
                    boolean notAdd = false;
                    Iterator iter = sets.iterator();
                    while (iter.hasNext()) {
                        Interests item = (Interests) iter.next();
                        if (item.getSite().getId().longValue() ==
                            curSite.getId().longValue()) {
                            item.setReceiveAlerts(true);
                            notAdd = true;
                            break;
                        }
                    }
                    if (!notAdd) {
                        Interests interests = UmUtil.createInterests(curSite,request);
                        interests.setReceiveAlerts(true);
                        sets.add(interests);
                    }

                    notAdd = false;
                    for (int i = 0; i < selectedTopics.length; i++) {
                        if( Long.parseLong(selectedTopics[i]) == curSite.getId().longValue() ) {
                            notAdd = true;
                            break;
                        }
                    }

                    if( !notAdd ) {
                        String[] selectedTopicsNew = new String[selectedTopics.length + 1];
                        for (int i = 0; i < selectedTopics.length; i++) {
                            selectedTopicsNew[i] = selectedTopics[i];
                        }
                        selectedTopicsNew[selectedTopics.length] = curSite.
                            getId().toString();

                        selectedTopics = new String[selectedTopicsNew.length];
                        for (int i = 0; i < selectedTopicsNew.length; i++) {
                            selectedTopics[i] = selectedTopicsNew[i];
                        }
                    }

                }
                //populate Locale according to Navigation Language
                Locale locale = new Locale();
                locale.setCode(RequestUtils.getNavigationLanguage(request).
                               getCode());
                if (logger.isDebugEnabled())
                    logger.debug(" Locale " + locale.getCode());

                    //sort Topics Array
                List sortedSets = TrnUtil.sortByTranslation(sets, locale,
                    new InterestsCallback());
                ArrayList sortedSetsArray = new ArrayList(sortedSets);

                registerForm.setTopics(sortedSetsArray);
                registerForm.setReceiveTopics(sortedSetsArray);
                registerForm.setSelectedTopics(selectedTopics);
            }
        }
        // ----------

        Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
        HashMap translations = new HashMap();
        Iterator iterator = TrnUtil.getLanguages(RequestUtils.
                                                 getNavigationLanguage(request).
                                                 getCode()).iterator();
        while (iterator.hasNext()) {
            TrnLocale item = (TrnLocale) iterator.next();
            translations.put(item.getCode(), item);
        }
        //sort languages
        List sortedLanguages = new ArrayList();
        iterator = languages.iterator();
        while (iterator.hasNext()) {
            Locale item = (Locale) iterator.next();
            sortedLanguages.add(translations.get(item.getCode()));
        }
        Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

        List organizationType = DbUtil.getOrganizationTypes();
        Collections.sort(organizationType, UmUtil.organizationNameComparator);

        List contentAlerts = DbUtil.getContentAlerts();

        registerForm.setContentAlerts(contentAlerts);

        // set First Name
        registerForm.setFirstNames(user.getFirstNames());

        // set Last Name
        registerForm.setLastName(user.getLastName());

        // set E-Mail
        registerForm.setEmail(user.getEmail());

        // set mailing address
        registerForm.setMailingAddress(user.getAddress());

        // set Organization Name
        registerForm.setOrganizationName(user.getOrganizationName());

        // set organization type other
        registerForm.setOrganizationTypeOther(user.getOrganizationTypeOther());


        // set Organization Type
        if (user.getOrganizationType() != null)
            registerForm.setSelectedOrganizationType(user.getOrganizationType().
                getId());

            // set Organization Type
        if (user.getCountry() != null)
            registerForm.setSelectedCountryResidence(user.getCountry().getIso());

            // set URL
        registerForm.setWebSite(user.getUrl());

        Iterator iter = user.getUserLangPreferences().getContentLanguages().
            iterator();
        String[] selcontentl = new String[user.getUserLangPreferences().
            getContentLanguages().size()];
        int i = 0;
        while (iter.hasNext()) {
            Locale item = (Locale) iter.next();
            selcontentl[i++] = item.getCode();
            logger.debug("--------- START DUMP LANGUAGES 3 ----------");
            logger.debug("Language: " + item.getCode());
            logger.debug("Language Name: " + item.getName());
            logger.debug("--------- END DUMP LANGUAGES 3 ----------");
        }
        registerForm.setContentSelectedLanguages(selcontentl);

        // get navigation language
        if (user.getUserLangPreferences().getAlertsLanguage() != null) {
            registerForm.setSelectedLanguage(user.getUserLangPreferences().
                                             getAlertsLanguage().getCode());
        }

        // set country resident data
        List countries = new ArrayList(TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode()));
        Collections.sort(countries, TrnUtil.countryNameComparator);
        registerForm.setCountryResidence(countries);
        registerForm.setCountryResidence(countries);

        // set organization type data
        registerForm.setOrganizationType(organizationType);

        // set Navigation languages
        registerForm.setNavigationLanguages(sortedLanguages);

        // set Content languages
        registerForm.setContentLanguages(sortedLanguages);

        // set public profile
        registerForm.setMembersProfile(user.getUserPreference().isPublicProfile());

        // set receive alerts
        registerForm.setNewsLetterRadio(user.getUserPreference().
                                        isReceiveAlerts());

        return null;
    }

}
