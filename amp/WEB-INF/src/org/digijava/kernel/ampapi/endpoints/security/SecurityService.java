/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

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
import org.digijava.kernel.ampapi.endpoints.security.dto.*;
import org.digijava.kernel.ampapi.endpoints.security.dto.UserManager;
import org.digijava.kernel.ampapi.endpoints.util.AmpApiToken;
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
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.util.DbUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Security Endpoint related services like menu, footer, user
 * 
 * @author Nadejda Mandrescu
 */
public class SecurityService {
    
    private static final Logger logger = Logger.getLogger(SecurityService.class);

    private static String ampVersion;
    private static String releaseDate;
    
    private static SecurityService securityService;
    
    private SecurityService() {
    
    }
    
    public static SecurityService getInstance() {
        if (securityService == null) {
            securityService = new SecurityService();
        }
        
        return securityService;
    }
    
    /**
     * @return json structure for the current view + user + state menu
     */
    public List<MenuItemStructure> getMenuStructures() {
        List<MenuItem> items = MenuUtils.getCurrentRequestMenuItems();
        return getMenuStructures(items);
    }
    
    /**
     * Converts menu items to menu item structures
     *  
     * @param items
     * @return menu item structures
     */
    private List<MenuItemStructure> getMenuStructures(List<MenuItem> items) {
        List<MenuItemStructure> menuItemStructures = new ArrayList<MenuItemStructure>();
        
        for (MenuItem item : items) {
            MenuItemStructure menuStructureItem = new MenuItemStructure();
            // we use old menu names definition to use existing translations
            String name = TranslatorWorker.translateText(item.title);
            // AMP-20030: do top menu item All caps and all menu items underneath it capitalized
            if (item.getParent() == null || item.getParent().getParent() == null) { // we have a common root parent, that's why we check for grandparent
                name = name.toUpperCase();
            }
            menuStructureItem.setName(name);
            if (item.tooltip != null) {
                menuStructureItem.setTooltip(TranslatorWorker.translateText(item.tooltip));
            }
            menuStructureItem.setUrl(item.url);
            menuStructureItem.setPopup(item.isPopup);
            menuStructureItem.setTab(item.isTab);
            menuStructureItem.setPopup(item.isPost);
            
            if (item.getChildren().size() > 0) {
                menuStructureItem.setChildren(getMenuStructures(item.getChildren()));
            }
            // special case to allow GIS/Dashboards to treat language action in their custom way 
            if (MenuConstants.LANGUAGE_ITEM.equals(item.name) || MenuConstants.PUBLIC_LANGUAGE_ITEM.equals(item.name)) {
                menuStructureItem.setLanguage(true);
            }
            
            menuItemStructures.add(menuStructureItem);
        }
        
        return menuItemStructures;
    }
    
    /**
     * 
     * @param siteUrl
     * @param isAdmin
     * @return
     */
    private LayoutInformation getFooterInformation(String siteUrl, boolean isAdmin) {
        populateBuildValues();
    
        Boolean trackingEnabled = FeaturesUtil
                .getGlobalSettingValueBoolean(GlobalSettingsConstants.ENABLE_SITE_TRACKING);
        String siteId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.TRACKING_SITE_ID);
        String trackingUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.TRACKING_SITE_URL);
    
        LayoutInformation layout = new LayoutInformation();
        layout.setBuildDate(releaseDate);
        layout.setAmpVersion(ampVersion);
        layout.setTrackingEnabled(trackingEnabled);
        layout.setSiteId(siteId);
        layout.setTrackingUrl(trackingUrl);
        layout.setFooterText(TranslatorWorker.translateText("Developed in partnership with OECD, UNDP, "
                + "WB, Government of Ethiopia and DG"));
        
        if (isAdmin) {
            List<LayoutInformation.LayoutAdminLink> links = new ArrayList<>();
            LayoutInformation.LayoutAdminLink adminLink =
                    new LayoutInformation.LayoutAdminLink("admin", siteUrl + "/admin");

            links.add(adminLink);
            layout.setAdminLinks(links);
        }
        
        return layout;
    }
    
    /**
     * Obtains and populates the values for Amp Version and Build Date
     */
    private void populateBuildValues() {
        if (releaseDate == null || ampVersion == null) {
            AmpVersionService ampVersionService = SpringUtil.getBean(AmpVersionService.class);
            AmpVersionInfo versionInfo = ampVersionService.getVersionInfo();

            ampVersion = versionInfo.getAmpVersion();
            releaseDate = versionInfo.getReleaseDate();

            // if buildSource is empty it shouldn't be added to the footer.
            // if buildDate is empty it shouldn't replace the release date.
            // In PROD and STG this props should be empty
            if (StringUtils.isNotEmpty(versionInfo.getBuildSource())) {
                ampVersion += versionInfo.getBuildSource();
            }

            if (StringUtils.isNotEmpty(versionInfo.getBuildDate())) {
                releaseDate = versionInfo.getBuildDate();
            }
        }
    }
    
    public List<WorkspaceInfo> getWorkspaces() {
        TeamMember tm = TeamUtil.getCurrentMember();
        if (tm != null) {
            List<AmpTeam> workspaces = TeamMemberUtil.getAllTeamsForUser(tm.getEmail());
            
            if (workspaces != null) {
                return workspaces.stream()
                        .map(t -> new WorkspaceInfo(t.getAmpTeamId(), t.getName()))
                        .collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }
    
    public UserSessionInformation authenticate(AuthenticationRequest authRequest) {
        String username = authRequest.getUserName();
        String password = authRequest.getPassword();
        Integer workspaceIdInt = authRequest.getWorkspaceId();
        Long workspaceId = (workspaceIdInt == null) ? null : workspaceIdInt.longValue();
    
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.INVALID_USER_PASSWORD);
        }
    
        User user = UserUtils.getUserByEmailAddress(username);
        if (user == null || !user.getPassword().equals(password)) {
            ApiErrorResponseService.reportForbiddenAccess(SecurityErrors.INVALID_USER_PASSWORD);
        }
    
        ApiErrorMessage result = ApiAuthentication.login(user, TLSUtils.getRequest());
        if (result != null) {
            ApiErrorResponseService.reportForbiddenAccess(result);
        }
    
        invalidateExistingSession();
    
        AmpTeamMember teamMember = getAmpTeamMember(username, workspaceId);
        if (workspaceId != null && teamMember == null) {
            ApiErrorResponseService.reportError(BAD_REQUEST, SecurityErrors.INVALID_TEAM);
        }
    
        storeInSession(username, password, teamMember, user);
        String ampTeamName = (teamMember == null) ? null : teamMember.getAmpTeam().getName();
        boolean isAdmin = user.isGlobalAdmin();
        return SecurityService.getInstance().createUserSessionInformation(isAdmin, user, ampTeamName, true);
    }
    
    public void invalidateExistingSession() {
        HttpSession session = TLSUtils.getRequest().getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
    private AmpTeamMember getAmpTeamMember(String username, Long workspaceId) {
        AmpTeamMember teamMember = null;
        if (workspaceId != null) {
            teamMember = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(username, workspaceId);
        }
        return teamMember;
    }
    
    private void storeInSession(String username, String password, AmpTeamMember teamMember, User user) {
        final UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);
        authRequest.setDetails(new WebAuthenticationDetails(TLSUtils.getRequest()));
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        final HttpSession session = TLSUtils.getRequest().getSession();
        PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
        if (teamMember != null) {
            session.setAttribute(Constants.CURRENT_MEMBER, teamMember.toTeamMember());
        }
        session.setAttribute(Constants.CURRENT_USER, user);
        
        session.setAttribute("ampAdmin", ApiAuthentication.isAdmin(user, TLSUtils.getRequest()) ? "yes" : "no");
    }
    
    public UserSessionInformation getUserSessionInformation() {
        boolean isAdmin = "yes".equals(TLSUtils.getRequest().getSession().getAttribute("ampAdmin"));
    
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        String teamName = null;
        boolean addActivity = false;
        
        User user = TeamUtil.getCurrentUser();
    
        // if the user is admin the he doesn't have a workspace assigned
        if (!isAdmin && tm != null) {
            AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
            AmpTeam team = ampTeamMember.getAmpTeam();
            teamName = team.getName();
            addActivity = FeaturesUtil.isVisibleField("Add Activity Button")
                    && Boolean.TRUE.equals(team.getAddActivity());
        }
        
        return createUserSessionInformation(isAdmin, user, teamName, addActivity);
    }
    
    public UserSessionInformation createUserSessionInformation(boolean isAdmin, User user,
                                                               String teamName, boolean addActivity) {
        
        UserSessionInformation userSessionInformation = new UserSessionInformation();
        
        userSessionInformation.setUrl(getLoginUrl());
        userSessionInformation.setTeamName(teamName);
        userSessionInformation.setUserId(user.getId());
        userSessionInformation.setUserName(user.getName());
        userSessionInformation.setAdmin(isAdmin);
        userSessionInformation.setAddActivity(StringUtils.isNotBlank(teamName) && addActivity);
        userSessionInformation.setViewActivity(!isAdmin);
        userSessionInformation.setNationalCoordinator(user.hasNationalCoordinatorGroup());
        
        return userSessionInformation;
    }
    
    private String getLoginUrl() {
        String scheme = "http" + (TLSUtils.getRequest().isSecure() ? "s" : "");
        return scheme + "://" + TLSUtils.getRequest().getServerName() + getPortPart() + "/showLayout.do?layout=login";
    }
    
    private String getPortPart() {
        String portPart = "";
        //if we are in secure mode and the port is not 443 or if we are not secure and the port is not 80
        // we have to add the port to the url
        boolean secure = TLSUtils.getRequest().isSecure();
        int port = TLSUtils.getRequest().getServerPort();
        if ((secure && port != GPIEPConstants.DEFAULT_HTTPS_PORT)
                || (!secure && port != GPIEPConstants.DEFAULT_HTTP_PORT)) {
            portPart = ":" + port;
        }
        return portPart;
    }
    
    public LayoutInformation getLayout() {
        HttpServletRequest httpRequest = TLSUtils.getRequest();
        TeamMember tm = (TeamMember) httpRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
        String ampAdmin = (String) httpRequest.getSession().getAttribute("ampAdmin");
        boolean isAdmin = ampAdmin != null && ampAdmin.equals("yes");
        SiteDomain currentDomain = RequestUtils.getSiteDomain(httpRequest);
        String siteUrl = SiteUtils.getSiteURL(currentDomain, httpRequest.getScheme(), httpRequest.getServerPort(),
                httpRequest.getContextPath());
        
        LayoutInformation layout = getFooterInformation(siteUrl, isAdmin);
        
        if (tm != null) {
            User u = UserUtils.getUserByEmailAddress(tm.getEmail());
            boolean siteAdmin = ApiAuthentication.isAdmin(u, TLSUtils.getRequest());
        
            layout.setLogged(true);
            layout.setEmail(u.getEmail());
            layout.setUserId(u.getId());
            layout.setFirstName(u.getFirstNames());
            layout.setLastName(u.getLastName());
            layout.setAdministratorMode(siteAdmin);
            
            if (!siteAdmin) {
                AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
            
                if (ampTeamMember.getAmpTeam() != null) {
                    layout.setWorkspace(ampTeamMember.getAmpTeam().getName());
                    layout.setWorkspaceId(ampTeamMember.getAmpTeam().getAmpTeamId());
                }
            } else {
                return layout;
            }
        } else {
            layout.setLogged("true".equals(TLSUtils.getRequest().getSession().getAttribute("isUserLogged")));
        }
    
        return layout;
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
