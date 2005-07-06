
/*
 *   UserUpdateGateway.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: UserUpdateGateway.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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


import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.um.form.UserUpdateForm;
import org.digijava.module.um.util.DbUtil;
import java.util.Iterator;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.Interests;
import java.util.*;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.um.util.UmUtil;
import org.digijava.kernel.entity.ContentAlert;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.request.Site;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UpdateUserProfile
 */

public class UserUpdateGateway
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(UserUpdateGateway.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest
                                 request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserUpdateForm userRegisterForm = (UserUpdateForm) form;

        if (userRegisterForm == null) {
            String debugKey = "Module.Um.UserRegisterAction.formnull";
            logger.l7dlog(Level.WARN, debugKey, null, null);
        }

        // get user object from session
        User user = (User) request.getSession(true).getAttribute(Constants.USER);

        if( user == null ) return null;

        // set first name
        user.setFirstNames(userRegisterForm.getFirstNames() );

        // set last name
        user.setLastName(userRegisterForm.getLastName() );

        // set email
        user.setEmail(userRegisterForm.getEmail());

        // set url
        user.setUrl(userRegisterForm.getWebSite());

        // set mailing address
        user.setAddress(userRegisterForm.getMailingAddress());

        // set organization name
        user.setOrganizationName(userRegisterForm.getOrganizationName());

        // set other organization from input box
        user.setOrganizationTypeOther(userRegisterForm.getOrganizationTypeOther());


        // set organization type
        Iterator iter = userRegisterForm.getOrganizationType().iterator();
        while (iter.hasNext()) {
            OrganizationType item = (OrganizationType)iter.next();
            if( item.getId().equals(userRegisterForm.getSelectedOrganizationType()) ) {
               user.setOrganizationType(item);
               break;
            }
        }

        // set country
        iter = userRegisterForm.getCountryResidence().iterator();
        while (iter.hasNext()) {
            TrnCountry item = (TrnCountry)iter.next();
            if( item.getIso().equals(userRegisterForm.getSelectedCountryResidence()) ) {
                Country country = new Country( item.getIso() );
                country.setCountryName(item.getName());
                user.setCountry( country );
               break;
            }
        }

        // ----- Fill user language preferences
        UserLangPreferences userLangPreferences = user.getUserLangPreferences();
        if( userLangPreferences != null ) {
            iter = userRegisterForm.getNavigationLanguages().iterator();
            while (iter.hasNext()) {
                TrnLocale item = (TrnLocale)iter.next();
                if( item.getCode().equals(userRegisterForm.getSelectedLanguage()) ) {
                    Locale language = new Locale();
                    language.setCode(item.getCode());
                    language.setName(item.getName());
                    // set DefaultLanguage
                    userLangPreferences.setAlertsLanguage(language);
                   break;
                }
            }


            Locale language;
            Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
            Set list = new HashSet();
            for( int i =  0; i < userRegisterForm.getContentSelectedLanguages().length; i++ ) {
                language = new Locale();
                language.setCode(userRegisterForm.getContentSelectedLanguages()[i]);
                Iterator langIter = languages.iterator();
                while(langIter.hasNext()) {
                    Locale item = (Locale) langIter.next();
                    if (item.getCode().equals(language.getCode())) {
                        language.setName(item.getName());
                        break;
                    }
                }
                list.add(language);
                logger.debug("--------- START DUMP LANGUAGES ----------");
                logger.debug("Language Code: " + language.getCode());
                logger.debug("Language Name: " + language.getName());
                logger.debug("--------- END DUMP LANGUAGES ----------");
            }
            userLangPreferences.setContentLanguages(list);
        }
       // -----

       // ----- Fill user preferences
       UserPreferences userPreferences = user.getUserPreference();
       if( userPreferences != null ) {
           userPreferences.setPublicProfile(userRegisterForm.getMembersProfile());
           userPreferences.setReceiveAlerts(userRegisterForm.isNewsLetterRadio());
       }
      // -----

      Iterator iterInterests = user.getInterests().iterator();
      while (iterInterests.hasNext()) {
          Interests item = (Interests)iterInterests.next();
          item.setUser(null);
      }

      Set topics = new HashSet();
      for( int i =  0; i < userRegisterForm.getSelectedTopics().length; i++ ) {
          Iterator iter2 = userRegisterForm.getReceiveTopics().iterator();
          while (iter2.hasNext()) {
              Interests item = (Interests) iter2.next();
              if (userRegisterForm.getSelectedTopics()[i] != null) {
                  if (Long.parseLong(userRegisterForm.getSelectedTopics()[i]) ==
                      item.getSite().getId().longValue()) {

                      Iterator iter3 = userRegisterForm.getContentAlerts().
                          iterator();
                      while (iter3.hasNext()) {
                          ContentAlert contentAlert = (ContentAlert) iter3.
                              next();
                          if (contentAlert.getValue().longValue() ==
                              item.getContentAlert().getValue().longValue()) {
                              item.getContentAlert().setName(contentAlert.
                                  getName());
                              break;
                          }
                      }

                      Interests interest = new Interests();
                      interest.setContentAlert(new ContentAlert(item.getContentAlert().getValue(),item.getContentAlert().getName()));
                      interest.setReceiveAlerts(item.isReceiveAlerts());
                      interest.setSite(item.getSite());
                      interest.setUser(user);
                      interest.setJoinDate(new Date());
                      interest.setSiteUrl(item.getSiteUrl());
                      interest.setSiteDescription(item.getSiteDescription());
                      topics.add(interest);
                  }
              }
          }
      }

      boolean found = false;
      iterInterests = topics.iterator();
      while (iterInterests.hasNext()) {
          found = false;
          Interests item = (Interests)iterInterests.next();
          Iterator iterTopics = user.getInterests().iterator();
          while (iterTopics.hasNext()) {
              Interests item2 = (Interests)iterTopics.next();
              if( item2.getSite().getId().equals(item.getSite().getId())) {
                  item2.setUser(user);
                  item2.setReceiveAlerts(item.isReceiveAlerts());
                  item2.setContentAlert(item.getContentAlert());
                  found = true;
              }
          }
          if( !found )
              user.getInterests().add(item);
      }


          Site site = SiteUtils.getSite(Constants.ALL_TOPICS_SITE);
          Site parentSite = null;
          if (site.getParentId() != null) {
              Interests interests = null;
              parentSite = SiteUtils.getSite(site.getParentId());
              if( userRegisterForm.isNewsLetterRadio() ) {
                  interests = new Interests();
                  interests.setSite(parentSite);
                  interests.setUser(user);
                  interests.setJoinDate(new Date());
                  interests.setContentAlert((new ContentAlert(new Long(1296000),"")));
                  interests.setReceiveAlerts(true);
                  user.getInterests().add(interests);
              } else {
                  boolean alreadyExists = false;
                  interests = DbUtil.getInterestBySite(parentSite,user);
                  if( interests != null ) {
                      iter = user.getInterests().iterator();
                      while (iter.hasNext()) {
                          Interests item = (Interests) iter.next();
                          if (item.getId().longValue() ==
                              interests.getId().longValue()) {
                              alreadyExists = true;
                              item.setUser(null);
                          }
                      }
                      if (!alreadyExists) {
                          interests.setUser(null);
                          user.getInterests().add(interests);
                      }
                  }
              }
          }

//       user.setInterests(topics);

       // register user in database
       DbUtil.updateUser(user);

       return mapping.findForward("success");
    }
}
