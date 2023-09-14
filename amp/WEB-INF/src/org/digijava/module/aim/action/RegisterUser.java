/*
 * RegisterUser.java
 */

package org.digijava.module.aim.action;


import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.TruBudgetIntent;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.PasswordPolicyValidator;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.ShaCrypt;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.triggers.UserRegistrationTrigger;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.UmUtil;

import java.util.HashSet;
import java.util.List;

import static org.digijava.module.um.util.DbUtil.getTruBudgetIntentsByName;

public class RegisterUser extends Action {

    private static Logger logger = Logger.getLogger(RegisterUser.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {

        UserRegisterForm userRegisterForm = (UserRegisterForm) form;

        logger.debug("In UserRegisterAction");

        if(!userRegisterForm.getErrors().isEmpty())
            return (mapping.getInputForward());
        try {

            User user = new User(userRegisterForm.getEmail().toLowerCase(),
                    userRegisterForm.getFirstNames(), userRegisterForm
                            .getLastName());
            String keyGen=UmUtil.generateAESKey(128);
            user.setTruBudgetKeyGen(keyGen);
            String encryptedTruPassword = UmUtil.encrypt(userRegisterForm.getTruBudgetPassword(),keyGen);
            user.setTruBudgetPassword(encryptedTruPassword);
            String[] intents = userRegisterForm.getSelectedTruBudgetIntents();
            List<TruBudgetIntent> truBudgetIntents = getTruBudgetIntentsByName(intents);
            logger.info("Intents: "+ truBudgetIntents);

//            user.getTruBudgetIntents().addAll(new HashSet<>(truBudgetIntents));
            user.setInitialTruBudgetIntents(new HashSet<>(user.getTruBudgetIntents()));
            user.setTruBudgetIntents(new HashSet<>(truBudgetIntents));

            // set client IP address
            user.setModifyingIP(RequestUtils.getRemoteAddress(request));
            ActionMessages errors = new ActionMessages();
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
            
            user.setNotificationEmailEnabled(userRegisterForm.getNotificationEmailEnabled());
            
            if (userRegisterForm.getNotificationEmailEnabled()) {
                user.setNotificationEmail(userRegisterForm.getNotificationEmail());
            }

            // set mailing address
            user.setAddress(userRegisterForm.getMailingAddress());

            // set organization name
            user.setOrganizationName(userRegisterForm.getOrganizationName());

            user.setOrganizationTypeOther(new String(" "));

            // set country
            ;
            user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(userRegisterForm.getSelectedCountryResidence()));
            //////System.out.println(" this is the default country.... "+countryIso);
            //user.setCountry(new Country(countryIso));
            //user.setCountry(new Country(org.digijava.module.aim.helper.Constants.COUNTRY_ISO));

            // set default language
            user.setRegisterLanguage(RequestUtils
                    .getNavigationLanguage(request));
                        user.setEmailVerified(false);
                        user.setBanned(true);

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
            userLangPreferences.setNavigationLanguage(RequestUtils
                    .getNavigationLanguage(request));
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
                DbUtil.registerUser(user);
                //create User Registration Trigger
                 if(FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.USER_REGISTRATION_BY_MAIL)){
                     String link = RequestUtils.getFullModuleUrl(request);
                     String id = ShaCrypt.crypt(user.getEmail().trim()+user.getId()).trim();
                     String description = "Welcome to AMP!"+ '\n'+'\n'+"We must first verify your email address before you become a full registered member (with login privileges)." +'\n'+ "In order to verify your email and complete the registration process, please click on the link below. " +
                                      '\n'+link+ "confirmRegisteration.do?id="+id;
                     try{
                         DgEmailManager.sendMail(user.getEmail(), "Confirm your registration", description);
                     }catch (Exception e) {
                         logger.error("Exception from RegisterUser", e);
                     }
                }
                  
                // save amp user extensions;
                AmpUserExtensionPK extPK=new AmpUserExtensionPK(user);
                userExt.setAmpUserExtId(extPK);
                AmpUserUtil.saveAmpUserExtension(userExt);
                user.setUserExtension(userExt);

                UserRegistrationTrigger urt = new UserRegistrationTrigger(user);

                Site site = RequestUtils.getSite(request);
                Group memberGroup = org.digijava.module.aim.util.DbUtil.getGroup(Group.MEMBERS,site.getId());
                Long[] uid = new Long[1];
                uid[0] = user.getId();
                org.digijava.module.admin.util.DbUtil.addUsersToGroup(memberGroup.getId(),uid);
                
                if (userRegisterForm.getNationalCoordinator()) {
                    Group nationalCoordGroup = org.digijava.module.admin.util.DbUtil.getGroupByKey(Group.NATIONAL_COORDINATORS);
                    org.digijava.module.admin.util.DbUtil.addUsersToGroup(nationalCoordGroup.getId(),uid);                                          
                } 
            }
        } catch (Exception e) {
            logger.error("Exception from RegisterUser", e);
        }

        return (mapping.findForward("forward"));
    }
}
