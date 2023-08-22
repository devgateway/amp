package org.digijava.kernel.ampapi.endpoints.security.services;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.menu.MenuConstants;
import org.dgfoundation.amp.menu.MenuItem;
import org.dgfoundation.amp.menu.MenuUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.gpi.GPIEPConstants;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.security.SecurityService;
import org.digijava.kernel.ampapi.endpoints.security.dto.*;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.CreateUserRequest;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.LoggedUserInformation;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.UserManager;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.security.PasswordPolicyValidator;
import org.digijava.kernel.services.AmpVersionInfo;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.*;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
        userManager.setNotificationEmailEnabled(user.isNotificationEmailEnabled());
        if(userManager.getNotificationEmailEnabled() != null) {
            if(userManager.getNotificationEmailEnabled()){
                userManager.setNotificationEmail(user.getNotificationEmail());
            }
        }
        userManager.setAddress(user.getAddress());
        userManager.setCountry(user.getCountry().getIso());
        userManager.setLanguageCode(user.getRegisterLanguage().getCode());

        try {
            AmpUserExtension userExt = AmpUserUtil.getAmpUserExtension(user);
            if (userExt.getOrgGroup() != null) {
                userManager.setOrganizationGroupId(userExt.getOrgGroup().getAmpOrgGrpId());
            }
            if (userExt.getOrgType() != null) {
                userManager.setOrganizationTypeId(userExt.getOrgType().getAmpOrgTypeId().toString());
            }
            if (userExt.getOrganization() != null) {
                userManager.setOrganizationName(userExt.getOrganization().getName());
            }
        } catch (AimException e) {
            logger.error("Exception from getting User extention", e);
            throw new RuntimeException(e);
        }

        return userManager;
    }

    public UserManager createUser(CreateUserRequest createUser) {
        // Get session of logged in user and check if its an admin
        try{
            HttpServletRequest request = TLSUtils.getRequest();
            String adminSession = (String) TLSUtils.getRequest().getSession().getAttribute("ampAdmin");
            logger.info("Creating user is admin: " + adminSession);

            User user = new User();
            String firstName = createUser.getFirstName();
            String lastName = createUser.getLastName();
            String email = createUser.getEmail();
            String confirmEmail = createUser.getEmailConfirmation();
            String password = createUser.getPassword().trim();
            String passwordConfirmation = createUser.getPasswordConfirmation().trim();
            String notificationEmail = createUser.getNotificationEmail();
            String repeatNotificationEmail = createUser.getRepeatNotificationEmail();
            boolean notificationEmailEnabled = createUser.getNotificationEmailEnabled();

            if ( adminSession == null || adminSession.equals("no") ) {
                ApiErrorResponseService.reportForbiddenAccess(SecurityErrors.NOT_ALLOWED);
            }

            // Validation
            validateUserFields(firstName, lastName, email, confirmEmail, password, passwordConfirmation,
                    notificationEmail, repeatNotificationEmail, notificationEmailEnabled);

            // Convert to user data
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
            language.setCode(createUser.getSelectedLanguage());

            // set alert language
            userLangPreferences.setAlertsLanguage(language);
            //set navigation language
            userLangPreferences.setNavigationLanguage(language);
            user.setUserLangPreferences(userLangPreferences);

            DbUtil.registerUser(user);
            DgUtil.saveUserLanguagePreferences(user, request, language);

            // Send verification email
            boolean isMailActivate = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.USER_REGISTRATION_BY_MAIL);

            if (isMailActivate) {
                System.out.println("send activation email");
                sendMail(language, siteDomain);
            } else {
                user.setEmailVerified(true);
            }

            return new UserManager(firstName, lastName, email);
        } catch (Exception e) {
            logger.error("Exception from RegisterUser", e);
            throw new RuntimeException(e);
        }

    }

    public LoggedUserInformation updateUserProfile(Long id, LoggedUserInformation updateUser) {
        // Get session of logged in user and check if its an admin
        try {
            HttpServletRequest request = TLSUtils.getRequest();
            String adminSession = (String) TLSUtils.getRequest().getSession().getAttribute("ampAdmin");
            logger.info("Creating user is admin: " + adminSession);

            User user = new User();

            return new LoggedUserInformation();
        } catch (Exception e) {
            logger.error("Exception from RegisterUser", e);
            throw new RuntimeException(e);
        }
    }

        private void validateUserFields(String firstName, String lastName, String email, String confirmEmail, String password, String passwordConfirmation,
                                    String notificationEmail, String repeatNotificationEmail, boolean notificationEmailEnabled){
        if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName) ||
                StringUtils.isBlank(email) || StringUtils.isBlank(confirmEmail) ||
                StringUtils.isBlank(password) || StringUtils.isBlank(passwordConfirmation))
        {
            ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.FILL_FORM_CORRECTLY);
        } else {
            // Check if its a valid email and if use with the same email exists
            if(!isValidEmail(email) || !isValidEmail(confirmEmail)){
                ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.NOT_VALID_EMAIL);
            } else if(!email.equals(confirmEmail)){
                ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.EMAIL_NOT_EQUAL);
            }else {
                User user = UserUtils.getUserByEmailAddress(email);
                if(user != null){
                    ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.USER_EMAIL_EXISTS);
                }
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

    private void sendMail(Locale language, SiteDomain siteDomain){
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
    }
}
