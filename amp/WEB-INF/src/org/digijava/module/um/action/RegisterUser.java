/*
 * RegisterUser.java
 */

package org.digijava.module.um.action;


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.TruBudgetIntent;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.PasswordPolicyValidator;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.um.form.AddUserForm;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.digijava.module.aim.util.DbUtil.getTruBudgetIntentsByName;

public class RegisterUser extends Action {

    private static Logger logger = Logger.getLogger(RegisterUser.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {

        
        /**
         * Test if user is administrator
         */
        HttpSession session     = request.getSession();
        String ampAdmin         = (String)session.getAttribute("ampAdmin"); 
        if ( ampAdmin == null || ampAdmin.equals("no") ) {
            return mapping.findForward("login");
        }
        
        AddUserForm userRegisterForm = (AddUserForm) form;

        logger.debug("In UserRegisterAction");

        if(!userRegisterForm.getErrors().isEmpty())
            return (mapping.getInputForward());
        try {

            boolean isMailAvtive = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.USER_REGISTRATION_BY_MAIL);
            
            User user = new User(userRegisterForm.getEmail().toLowerCase(),
                    userRegisterForm.getFirstNames(), userRegisterForm
                            .getLastName());
            String[] intents = userRegisterForm.getSelectedTruBudgetIntents();
            List<TruBudgetIntent> truBudgetIntents = getTruBudgetIntentsByName(intents);
            logger.info("Intents: "+ truBudgetIntents);

            user.setTruBudgetIntents(new HashSet<>(truBudgetIntents));
            // set client IP address

            user.setModifyingIP(RequestUtils.getRemoteAddress(request));

            if (!PasswordPolicyValidator.isValid(userRegisterForm.getPassword(), userRegisterForm.getEmail())) {
                userRegisterForm.addError("error.strong.validation", "Please enter a password which meets the minimum password requirements");
                request.setAttribute(PasswordPolicyValidator.SHOW_PASSWORD_POLICY_RULES, true);
                return (mapping.getInputForward());
            }
            // set password
            user.setPassword(userRegisterForm.getPassword().trim());
            user.setSalt(userRegisterForm.getPassword().trim());

            // set Website
            user.setUrl(userRegisterForm.getWebSite());

            // register through
            user.setRegisteredThrough(RequestUtils.getSite(request));

            // set mailing address
            user.setAddress(userRegisterForm.getMailingAddress());

            // set organization name
            user.setOrganizationName(userRegisterForm.getOrganizationName());

            user.setPledger(userRegisterForm.getPledger());
            //only if its a pledge user it can be a super user
            user.setPledgeSuperUser(userRegisterForm.getPledger() && userRegisterForm.getPledgeSuperUser());
            user.setExemptFromDataFreezing(userRegisterForm.getExemptFromDataFreezing());
            
            user.setNotificationEmailEnabled(userRegisterForm.getNotificationEmailEnabled());
            if (userRegisterForm.getNotificationEmailEnabled()) {
                user.setNotificationEmail(userRegisterForm.getNotificationEmail());
            }
            
            user.setOrganizationTypeOther(" ");
            

            // set country
            user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(userRegisterForm.getSelectedCountryResidence()));
            //////System.out.println(" this is the default country.... "+countryIso);
            //user.setCountry(new Country(countryIso));
            //user.setCountry(new Country(org.digijava.module.aim.helper.Constants.COUNTRY_ISO));

            // set default language
            user.setRegisterLanguage(RequestUtils
                    .getNavigationLanguage(request));
                        user.setEmailVerified(false);
                        user.setBanned(false);

            SiteDomain siteDomain = (SiteDomain) request
                    .getAttribute(Constants.CURRENT_SITE);

            // ------------- SET USER LANGUAGES
            UserLangPreferences userLangPreferences = new UserLangPreferences(
                    user, DgUtil.getRootSite(siteDomain.getSite()));

            Locale language = new Locale();
            language.setCode(userRegisterForm.getSelectedLanguage());

            // set alert language
            userLangPreferences.setAlertsLanguage(language);

            // set navigation language
            userLangPreferences.setNavigationLanguage(language);
            user.setUserLangPreferences(userLangPreferences);
            
            // ===== start user extension setup =====
            AmpUserExtension userExt=new AmpUserExtension();
            // org type
            AmpOrgType orgType=org.digijava.module.aim.util.DbUtil.getAmpOrgType(userRegisterForm.getSelectedOrgType());
            userExt.setOrgType(orgType);
            AmpOrgGroup orgGroup=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(userRegisterForm.getSelectedOrgGroup());
            userExt.setOrgGroup(orgGroup);
            AmpOrganisation organ = org.digijava.module.aim.util.DbUtil.getOrganisation(userRegisterForm.getSelectedOrganizationId());
            userExt.setOrganization(organ);
            // ===== end user extension setup =====

            // if email register get error message

            if (DbUtil.isRegisteredEmail(user.getEmail())) {
                userRegisterForm.addError("error.registration.emailexits", "Email already exists");
                //return (new ActionForward(mapping.getInput()));
                return (mapping.getInputForward());
            } else {
                
                /* Ignore on MEGE !! */
                String des1 = "Welcome to AMP!";
                String des2 = "AMP Administrator has created your user profile.";
                String des3 = "Your login information:";
                String des4 = "Username: ";
                String cri1 = "password: ";
                String pti1 = "Please change your password when you first login to AMP in order to keep it private.";
                
                
                String langCode = language.getCode();
                des1 = TranslatorWorker.translateText(des1, langCode, siteDomain.getSite());
                des2 = TranslatorWorker.translateText(des2, langCode, siteDomain.getSite());
                des3 = TranslatorWorker.translateText(des3, langCode, siteDomain.getSite());
                des4 = TranslatorWorker.translateText(des4, langCode, siteDomain.getSite());
                cri1 = TranslatorWorker.translateText(cri1, langCode, siteDomain.getSite()); 
                pti1 = TranslatorWorker.translateText(pti1, langCode, siteDomain.getSite()); 
                
                
                String des = des1+ '\n'+'\n'+des2 +'\n'+ des3 +'\n'+'\n'+'\t'+'\t'+ des4;
                String cri = String.valueOf('\n')+'\t'+'\t'+cri1;
                String pti = String.valueOf('\n')+'\n'+ pti1;
                
                DbUtil.registerUser(user);
                DgUtil.saveUserLanguagePreferences(user, request, language);

                if (isMailAvtive) {
                    if(userRegisterForm.isSendEmail()) {
                        String description = des + user.getEmail() + cri + userRegisterForm.getPassword() + pti;
                        String title = TranslatorWorker.translateText("Registration Confirmation", langCode, siteDomain.getSite());
                        DgEmailManager.sendMail(user.getEmail(), title, description);
                    }
                } else {
                    user.setEmailVerified(true);
                }
                
                 /* END - Ignore on MEGE !! */ 
                Site site = RequestUtils.getSite(request);
                Group memberGroup = org.digijava.module.aim.util.DbUtil.getGroup(Group.MEMBERS,site.getId());
                Long[] uid = new Long[1];
                uid[0] = user.getId();
                org.digijava.module.admin.util.DbUtil.addUsersToGroup(memberGroup.getId(),uid);
                

                if (userRegisterForm.getNationalCoordinator()) {
                    Group nationalCoordGroup = org.digijava.module.admin.util.DbUtil.getGroupByKey(Group.NATIONAL_COORDINATORS);
                    org.digijava.module.admin.util.DbUtil.addUsersToGroup(nationalCoordGroup.getId(),uid);                                          
                } 

                //save amp user extensions;
                AmpUserExtensionPK extPK=new AmpUserExtensionPK(user);
                userExt.setAmpUserExtId(extPK);
                AmpUserUtil.saveAmpUserExtension(userExt);
            }
        } catch (Exception e) {
            logger.error("Exception from RegisterUser", e);
        }
        if(userRegisterForm.isAddWorkspace()){
            userRegisterForm.setAssignedWorskpaces(null);
            userRegisterForm.addError("error.aim.addUser.success", TranslatorWorker.translateText("User registered successfully"));
            //in this case the registration went ok so we will add the message to the user
            return mapping.findForward("forward");
        }
        else{
            userRegisterForm.addError("error.aim.addUser.success", TranslatorWorker.translateText("User registered successfully"));
            userRegisterForm.reset(mapping, request);
            return (mapping.findForward("index"));
        }
    }
}
