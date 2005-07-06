/*
 *   ShowUserRegisterGateway.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowUserRegisterGateway.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.HowDidYouHear;
import org.digijava.kernel.entity.Interests;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.InterestsCallback;
import org.digijava.module.um.util.HowDidYouHearCallback;
import org.digijava.module.um.util.UmUtil;
import java.util.Collections;
import java.util.HashMap;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnCountry;

public class ShowUserRegisterGateway extends Action {

    private static Logger logger = Logger.getLogger(ShowUserRegisterGateway.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserRegisterForm registerForm = (UserRegisterForm) form;

        if (registerForm.getSiteId() == null){
            registerForm.setSiteId(RequestUtils.getSite(request).getId());
        }

        // Topics
        Site site = SiteUtils.getSite(Constants.ALL_TOPICS_SITE);
        if( site == null )
            logger.warn("site " + Constants.ALL_TOPICS_SITE + " could not found");

        Site curSite = RequestUtils.getSite(request);
        List sites = DbUtil.getTopicsSites(site);
        ArrayList sets = UmUtil.getGenerateInterests(null, sites, null,request);
        if ( sets != null ) {
            //populate Locale according to Navigation Language
            Locale locale = new Locale();
            locale.setCode(RequestUtils.getNavigationLanguage(request).getCode());
            if (logger.isDebugEnabled())
                logger.debug(" Locale " + locale.getCode());

                //sort Topics Array
            List sortedSets = TrnUtil.sortByTranslation(sets, locale,
                new InterestsCallback());
            ArrayList sortedSetsArray = new ArrayList(sortedSets);

            registerForm.setTopicitems(sortedSetsArray);
            registerForm.setTopicselectedItems(null);

/*            if (sortedSetsArray != null && sortedSetsArray.size() > 0) {
                String[] selectedTopics = new String[sortedSetsArray.size()];
                for (int i = 0; i < sortedSetsArray.size(); i++) {
                    selectedTopics[i] = ( (Interests) sortedSetsArray.get(i)).
                        getSite().getId().toString();
                }

                registerForm.setTopicselectedItems(selectedTopics);

            }*/
        }


        // set default web site
        registerForm.setWebSite("http://");


        Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));

        HashMap translations = new HashMap();
        Iterator iterator = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
        while (iterator.hasNext()) {
            TrnLocale item = (TrnLocale)iterator.next();
            translations.put(item.getCode(), item);
        }
       //sort languages
        List sortedLanguages = new ArrayList();
        iterator = languages.iterator();
        while (iterator.hasNext()) {
            Locale item = (Locale)iterator.next();
            sortedLanguages.add(translations.get(item.getCode()));
        }
        Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);


        List howDidYouHear = DbUtil.getHowDidYouHear();
        if (howDidYouHear != null) {
            //populate Locale according to Navigation Language
            Locale locale = new Locale();
            locale.setCode(RequestUtils.getNavigationLanguage(request).getCode());
            if (logger.isDebugEnabled())
                logger.debug(" Locale " + locale.getCode());
                //sort how did you hear Array
            howDidYouHear = TrnUtil.sortByTranslation(howDidYouHear,
                locale,
                new HowDidYouHearCallback(RequestUtils.getSite(request).
                                          getSiteId()),true);
        }


        List organizationType = DbUtil.getOrganizationTypes();
        Collections.sort(organizationType,UmUtil.organizationNameComparator);
        List countries = DbUtil.getCountries();

        HashMap countriesMap = new HashMap();
        iterator = TrnUtil.getCountries(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
        while (iterator.hasNext()) {
            TrnCountry item = (TrnCountry)iterator.next();
            countriesMap.put(item.getIso(), item);
        }
       //sort countries
        List sortedCountries = new ArrayList();
        iterator = countries.iterator();
        while (iterator.hasNext()) {
            Country item = (Country)iterator.next();
            sortedCountries.add(countriesMap.get(item.getIso()));
        }
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);


        // set country resident data
        if( sortedCountries != null ) {
//            sortedCountries.add(0, new Country( new Long( 0 ),null,"Select a country",null,null,null,null ) );
            sortedCountries.add(0, new TrnCountry( "-1","Select a country") );
        }
        registerForm.setCountryResidence(sortedCountries);
        // --------------------------

        // set organization type data
        if( organizationType != null ) {
            organizationType.add(0, new OrganizationType( "-1", "-none-") );
        }
        registerForm.setOrganizationType(organizationType);

        // set Navigation languages
        registerForm.setNavigationLanguages(sortedLanguages);

        // set Content languages
        registerForm.setContentLanguages(sortedLanguages);
        registerForm.setSelectedLanguage("en");

        String []selcontentl = new String [languages.size()];
        Iterator iter = languages.iterator();
        int i = 0;
        while (iter.hasNext()) {
            selcontentl[i++] = ((Locale)iter.next()).getCode();
        }
        registerForm.setContentSelectedLanguages(selcontentl);
        // ---------------------

        // set How did you hear
        if( howDidYouHear != null ) {
            howDidYouHear.add(0, new HowDidYouHear( "","Select one") );
        }
        registerForm.setHowDidyouhear(howDidYouHear);

        return null;
    }
}
