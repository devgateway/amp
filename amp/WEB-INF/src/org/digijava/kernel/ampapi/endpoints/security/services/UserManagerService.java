package org.digijava.kernel.ampapi.endpoints.security.services;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.gpi.GPIEPConstants;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.security.SecurityService;
import org.digijava.kernel.ampapi.endpoints.security.dto.*;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.CreateUserRequest;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.LoggedUserInformation;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.UpdateUserInformation;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.UserManager;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.security.PasswordPolicyValidator;
import org.digijava.kernel.services.AmpVersionInfo;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;

public class UserManagerService {
    private static final Logger logger = Logger.getLogger(SecurityService.class);

    private static UserManagerService userManagerService;

    public UserManagerService() {
    }

    public static UserManagerService getInstance() {
        if (userManagerService == null) {
            userManagerService = new UserManagerService();
        }

        return userManagerService;
    }

    public LoggedUserInformation getLoggedUserInformation()
    {
        User user = TeamUtil.getCurrentUser();

        LoggedUserInformation userManager = new LoggedUserInformation(user.getFirstNames(), user.getLastName(), user.getEmail());
        userManager.setId(user.getId());
        userManager.setEmail(user.getEmail());
        userManager.setNotificationEmailEnabled(user.isNotificationEmailEnabled());
        if(userManager.getNotificationEmailEnabled() != null) {
            if(userManager.getNotificationEmailEnabled()){
                userManager.setNotificationEmail(user.getNotificationEmail());
            }
        }
        userManager.setAddress(user.getAddress());
        userManager.setCountry(user.getCountry().getIso());
        userManager.setLanguageCode(user.getRegisterLanguage().getCode());

        userExtention(user, userManager);

        return userManager;
    }

    public LoggedUserInformation createUser(CreateUserRequest createUser) {
        // Get session of logged in user and check if its an admin

        HttpServletRequest request = TLSUtils.getRequest();
        String languageCode = TLSUtils.getLangCode();

        String firstName = createUser.getFirstName();
        String lastName = createUser.getLastName();
        String email = createUser.getEmail();
        String confirmEmail = createUser.getEmailConfirmation();
        String password = createUser.getPassword().trim();
        String passwordConfirmation = createUser.getPasswordConfirmation().trim();
        String notificationEmail = createUser.getNotificationEmail();
        String repeatNotificationEmail = createUser.getRepeatNotificationEmail();
        boolean notificationEmailEnabled = createUser.getNotificationEmailEnabled();
        boolean isUpdateUserEmail = true;
        boolean isUpdateUser = false;

        // Validation
        validateUserFields(firstName, lastName, email, confirmEmail, password, passwordConfirmation,
                notificationEmail, repeatNotificationEmail, notificationEmailEnabled, isUpdateUserEmail, isUpdateUser);

        // Convert to user data
        User user = new User();
        user.setFirstNames(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setSalt(password);
        user.setNotificationEmailEnabled(notificationEmailEnabled);
        if(notificationEmailEnabled){
            user.setNotificationEmail(notificationEmail);
        }
        user.setBanned(true);
        // set client IP address
        user.setModifyingIP(RequestUtils.getRemoteAddress(request));
        // register through
        user.setRegisteredThrough(RequestUtils.getSite(request));
        // set default language
        user.setRegisterLanguage(RequestUtils
                .getNavigationLanguage(request));

        user.setEmailVerified(false);

        SiteDomain siteDomain = (SiteDomain) request
                .getAttribute(org.digijava.kernel.Constants.CURRENT_SITE);

        // ------------- SET USER LANGUAGES
        UserLangPreferences userLangPreferences = new UserLangPreferences(
                user, DgUtil.getRootSite(siteDomain.getSite()));

        Locale language = new Locale();
        language.setCode(languageCode);

        // set alert language
        userLangPreferences.setAlertsLanguage(language);
        //set navigation language
        userLangPreferences.setNavigationLanguage(language);
        user.setUserLangPreferences(userLangPreferences);

        // Send verification email
        boolean isMailActivate = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.USER_REGISTRATION_BY_MAIL);

        if (isMailActivate) {
            System.out.println("send activation email");
            sendMail(language, siteDomain, user, createUser);
        } else {
            user.setEmailVerified(true);
        }

        try {
            DbUtil.registerUser(user);
        } catch (UMException e) {
            logger.error("Exception from register new user", e);
            throw new RuntimeException(e);
        }
        DgUtil.saveUserLanguagePreferences(user, request, language);

        //save amp user extensions;
        AmpUserExtension userExt=new AmpUserExtension();
        AmpUserExtensionPK extPK=new AmpUserExtensionPK(user);
        userExt.setAmpUserExtId(extPK);
        try {
            AmpUserUtil.saveAmpUserExtension(userExt);
        } catch (AimException e) {
            logger.error("Exception from creating request user extentions", e);
            throw new RuntimeException(e);
        }
        return createUserProfileInformation(user);
    }

    public LoggedUserInformation updateUserProfile(Long userId, UpdateUserInformation updateUser) {
        HttpServletRequest request = TLSUtils.getRequest();

        User user = new User();
        Long updateUserId = updateUser.getId();
        String firstName = updateUser.getFirstName();
        String lastName = updateUser.getLastName();
        String email = updateUser.getEmail();
        String password = null;
        String passwordConfirmation = null;
        String notificationEmail = updateUser.getNotificationEmail();
        String repeatNotificationEmail = updateUser.getRepeatNotificationEmail();
        boolean notificationEmailEnabled = updateUser.getNotificationEmailEnabled();
        boolean isUpdateUserEmail = true;
        boolean isUpdateUser = true;

            // confirm if user exists for security purpose
        if(userId != null && updateUser.getId() != null){
            if (!userId.equals(updateUser.getId())) {
                ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.USER_ID_INVALID);
            }
        }else {
            ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.USER_ID_INVALID);
        }

        // For security purposes also make sure that only user can edit their profiles
        User loggedUser = TeamUtil.getCurrentUser();
        if(loggedUser.getId() != updateUser.getId()){
            ApiErrorResponseService.reportForbiddenAccess(SecurityErrors.NOT_ALLOWED);
        } else {
            // if its the same email don't update it since it will cause unique type error
            if(Objects.equals(loggedUser.getEmail(), updateUser.getEmail())) {
                isUpdateUserEmail = false;
            }
        }
        // Validation
        validateUserFields(firstName, lastName, email, email, password, passwordConfirmation,
                notificationEmail, repeatNotificationEmail, notificationEmailEnabled, isUpdateUserEmail, isUpdateUser);

        // User preparation
        if (updateUserId != null) {
            user = UserUtils.getUser(updateUserId);
        }
        user.setId(updateUserId);
        user.setFirstNames(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setNotificationEmailEnabled(notificationEmailEnabled);
        if(notificationEmailEnabled){
            user.setNotificationEmail(notificationEmail);
        }
        // set client IP address
        user.setModifyingIP(RequestUtils.getRemoteAddress(request));

        Country country = org.digijava.module.aim.util.DbUtil.getDgCountry(updateUser.getCountryIso());
        user.setCountry(country);

        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);
        UserLangPreferences userLangPreferences = new UserLangPreferences(user, DgUtil.getRootSite(siteDomain.getSite()));

        Locale language = new Locale();
        language.setCode(updateUser.getLanguageCode());

        userLangPreferences.setAlertsLanguage(language);
        userLangPreferences.setNavigationLanguage(RequestUtils.getNavigationLanguage(request));

        user.setUserLangPreferences(userLangPreferences);

        AmpUserExtension userExt= null;
        try {
            userExt = AmpUserUtil.getAmpUserExtension(user);
        } catch (AimException e) {
            logger.error("Exception from getAmpUserExtension", e);
            throw new RuntimeException(e);
        }
        if (userExt==null){
                userExt=new AmpUserExtension(new AmpUserExtensionPK(user));
            }

            if (userExt!=null){
                AmpOrgType orgType=org.digijava.module.aim.util.DbUtil.getAmpOrgType(updateUser.getOrganizationTypeId());
                userExt.setOrgType(orgType);
                AmpOrgGroup orgGroup=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(updateUser.getOrganizationGroupId());
                userExt.setOrgGroup(orgGroup);
                AmpOrganisation organ = org.digijava.module.aim.util.DbUtil.getOrganisation(updateUser.getOrganizationId());
                userExt.setOrganization(organ);
                user.setOrganizationName(organ.getName());

                try {
                    AmpUserUtil.saveAmpUserExtension(userExt);
                } catch (AimException e) {
                    logger.error("Exception from Amp User Extention", e);
                    throw new RuntimeException(e);
                }
            }

        try {
            DbUtil.updateUser(user);
        } catch (UMException e) {
            logger.error("Exception from Update user profile api.", e);
            throw new RuntimeException(e);
        }

        return  createUserProfileInformation(user);
    }

    private void validateUserFields(String firstName, String lastName, String email, String confirmEmail, String password, String passwordConfirmation,
                                    String notificationEmail, String repeatNotificationEmail, boolean notificationEmailEnabled, boolean isUpdateUserEmail, boolean isUpdateUser){
        if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName) ||
                StringUtils.isBlank(email) || StringUtils.isBlank(confirmEmail))
        {
            ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.FILL_FORM_CORRECTLY);
        } else if(!isUpdateUser){
            if(StringUtils.isBlank(password) || StringUtils.isBlank(passwordConfirmation)){
                ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.PASSWORD_FIELD_REQUIRED);
            }
        }
            // Check if its a valid email and if use with the same email exists
            if(!isValidEmail(email) || !isValidEmail(confirmEmail)){
                ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.NOT_VALID_EMAIL);
            } else if(!email.equals(confirmEmail)){
                ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.EMAIL_NOT_EQUAL);
            }else if(isUpdateUserEmail){
                User userExists = UserUtils.getUserByEmailAddress(email);
                if(userExists != null){
                    ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.USER_EMAIL_EXISTS);
                }
            }


        if (notificationEmailEnabled){
            if(StringUtils.isBlank(notificationEmail) || StringUtils.isBlank(repeatNotificationEmail)){
                ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.NOTIFICATION_EMAIL_NOT_NULL);
            } else {
                if (!notificationEmail.equals(repeatNotificationEmail)) {
                    ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.NOTIFICATION_EMAIL_NOT_EQUAL);
                } else if(!isValidEmail(notificationEmail) || !isValidEmail(repeatNotificationEmail)){
                    ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.NOT_VALID_EMAIL);
                }
            }
        }

        // Password validator
        if (!PasswordPolicyValidator.isValid(password, email)) {
            ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.PASSWORD_VALIDATION);
        }
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendMail(Locale language, SiteDomain siteDomain, User user, CreateUserRequest createUser){
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
        String cri = ""+'\n'+'\t'+'\t'+cri1;
        String pti = ""+'\n'+'\n'+ pti1;

        String description = des + user.getEmail() + cri + createUser.getPassword() + pti;
        String title = TranslatorWorker.translateText("Registration Confirmation", langCode, siteDomain.getSite());
        try {
            DgEmailManager.sendMail(user.getEmail(), title, description);
        } catch (Exception e) {
            logger.error("Exception from sending registration email", e);
            throw new RuntimeException(e);
        }
    }

    public LoggedUserInformation createUserProfileInformation(User user) {

        LoggedUserInformation userProfileInformation = new LoggedUserInformation();

        userProfileInformation.setId(user.getId());
        userProfileInformation.setFirstName(user.getFirstNames());
        userProfileInformation.setLastName(user.getLastName());
        userProfileInformation.setEmail(user.getEmail());
        userProfileInformation.setAddress(user.getAddress());
        if(user.getCountry() != null){
            userProfileInformation.setCountry(user.getCountry().getIso());
        }
        if(user.getRegisterLanguage() != null){
            userProfileInformation.setLanguageCode(user.getRegisterLanguage().getCode());
        }

        userProfileInformation.setAddress(user.getAddress());
        userProfileInformation.setNotificationEmailEnabled(user.isNotificationEmailEnabled());
        userProfileInformation.setBanned(user.isBanned());
        if(userProfileInformation.getNotificationEmailEnabled() != null) {
            if(userProfileInformation.getNotificationEmailEnabled()){
                userProfileInformation.setNotificationEmail(user.getNotificationEmail());
            }
        }

        userExtention(user, userProfileInformation);
        return userProfileInformation;
    }

    public void userExtention(User user, LoggedUserInformation userInfo){
        try {
            AmpUserExtension userExt = AmpUserUtil.getAmpUserExtension(user);
            if(userExt != null){
                if (userExt.getOrgGroup() != null) {
                    userInfo.setOrganizationGroupId(userExt.getOrgGroup().getAmpOrgGrpId());
                }
                if (userExt.getOrgType() != null) {
                    userInfo.setOrganizationTypeId(userExt.getOrgType().getAmpOrgTypeId());
                }
                if (userExt.getOrganization() != null) {
                    userInfo.setOrganizationName(userExt.getOrganization().getName());
                    userInfo.setOrganizationId(userExt.getOrganization().getAmpOrgId());
                }
            }
        } catch (AimException e) {
            logger.error("Exception from getting User extention", e);
            throw new RuntimeException(e);
        }
    }
}
