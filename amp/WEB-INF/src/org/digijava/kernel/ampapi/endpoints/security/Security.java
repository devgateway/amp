package org.digijava.kernel.ampapi.endpoints.security;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.dto.WorkspaceMember;
import org.digijava.kernel.ampapi.endpoints.security.services.UserService;
import org.digijava.kernel.ampapi.endpoints.security.services.WorkspaceMemberService;
import org.digijava.kernel.ampapi.endpoints.util.AmpApiToken;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.ampapi.endpoints.util.types.ListOfLongs;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.xml.sax.SAXException;

import com.sun.jersey.spi.container.ContainerRequest;

/**
 * This class should have all security / permissions related methods
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("security")
public class Security implements ErrorReportingEndpoint {
    private static final Logger logger = Logger.getLogger(Security.class);
    private static String SITE_CONFIG_PATH = "TEMPLATE" + System.getProperty("file.separator") + "ampTemplate"
            + System.getProperty("file.separator") + "site-config.xml";

    public static String getSiteConfigPath() {
        return SITE_CONFIG_PATH;
    }
    @Context
    private HttpServletRequest httpRequest;

    /**
     * 
     * @return
     */
    @GET
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "user", ui = false)
    public JsonBean user() {
        AmpApiToken apiToken = SecurityUtil.getTokenFromSession();

        // if the user is logged in without a token, we generate one
        if (apiToken == null) {
            apiToken = SecurityUtil.generateToken();
        }

        boolean isAdmin ="yes".equals(httpRequest.getSession().getAttribute("ampAdmin"));
        
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        String teamName = null;
        boolean addActivity = false;

        User user;

        // if the user is admin the he doesn't have a workspace assigned
        if (!isAdmin) {
            if (tm != null) {
                AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
                AmpTeam team = ampTeamMember.getAmpTeam();
                teamName = team.getName();
                addActivity = FeaturesUtil.isVisibleField("Add Activity Button")
                        && Boolean.TRUE.equals(team.getAddActivity());
            }

            user = apiToken.getUser();
        } else {
            user = UserUtils.getUser(tm.getMemberId());
        }

        return createResponse(isAdmin, apiToken, user, teamName, addActivity);
    }

    private JsonBean createResponse(boolean isAdmin, AmpApiToken apiToken, User user, String team, boolean addActivity) {
        final JsonBean authenticationResult = new JsonBean();
        authenticationResult.set("token", apiToken != null ? apiToken.getToken() : null);
        authenticationResult.set("token-expiration", apiToken != null && apiToken.getExpirationTime() != null ? apiToken.getExpirationTime().getMillis() : null);
        authenticationResult.set("url", getLoginUrl());
        authenticationResult.set("team", team);
        authenticationResult.set("user-name", user.getName());
        authenticationResult.set("user-id", user.getId());
        authenticationResult.set("is-admin", isAdmin);
        authenticationResult.set("add-activity", team != null && addActivity); //to check if the user can add activity in the selected ws
        authenticationResult.set("view-activity", !isAdmin); ///at this stage the user can view activities only if you are not admin
        authenticationResult.set("national-coordinator", user.hasNationalCoordinatorGroup()); //role that allows user to enter gpi indicator 6 data
        return authenticationResult;
    }

    private String getLoginUrl() {
        String scheme = "http" + (TLSUtils.getRequest().isSecure() ? "s" : "");
        return scheme + "://" + TLSUtils.getRequest().getServerName() + getPortPart() + "/showLayout.do?layout=login";
    }

    private String getPortPart() {
        String portPart = "";
        //if we are in secure mode and the port is not 443 or if we are not secure and the port is not 80 we have to add the port to the url
        boolean secure = this.httpRequest.isSecure();
        int port = this.httpRequest.getServerPort();
        if ((secure && port != 443) || (!secure && port != 80)) {
            portPart=":"+ port;
        }
        return portPart;
    }

    /**
     * Authenticate user with via API.
     * <p>This endpoint is used to authenticate users via API. Mandatory fields are username and password. Password
     * value is a sha1 hash of the actual password. Third parameter is workspaceId which allows to preselect
     * active workspace.</p>
     * Workspace parameter is optional. If specified all with calls issued with the provided token will be handled
     * for respective workspace.
     *
     * <h3>Sample input:</h3>
     * <pre>
     * {
     *   "username": "atl@amp.org",
     *   "password": "a7848b4c1b75cb7bb7449069fe0e114b730c0448",
     *   "workspaceId": 4
     * }
     * </pre>
     *
     * <h3>Sample output:</h3>
     * <pre>
     * {
     *   "token": "34bf1c55-f2d1-43bb-bef4-e98b6077f66f",
     *   "token-expiration": 1483433179305,
     *   "url": "http://localhost:8080/showLayout.do?layout=login",
     *   "team": "Espace de Travail Cellule Technique du COMOREX",
     *   "user-name": "atl@amp.org",
     *   "user-id": 2,
     *   "is-admin": false,
     *   "add-activity": true,
     *   "view-activity": true
     * }
     * </pre>
     *
     * @param authentication Json bean with username/password/workspace information
     * @return JSON with the response, error or user info
     */
    @POST
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public JsonBean authenticate(final JsonBean authentication) {
        String username = authentication.getString("username");
        String password = authentication.getString("password");
        Integer workspaceIdInt = (Integer) authentication.get("workspaceId");
        Long workspaceId = (workspaceIdInt == null) ? null : workspaceIdInt.longValue();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            ApiErrorResponse.reportError(BAD_REQUEST, SecurityErrors.INVALID_USER_PASSWORD);
        }

        try {
            User user = UserUtils.getUserByEmail(username);
            if (user == null || !user.getPassword().equals(password)) {
                ApiErrorResponse.reportForbiddenAccess(SecurityErrors.INVALID_USER_PASSWORD);
            }

            ApiErrorMessage result = ApiAuthentication.login(user, this.httpRequest);
            if (result != null) {
                ApiErrorResponse.reportForbiddenAccess(result);
            }

            invalidateExistingSession();

            AmpTeamMember teamMember = getAmpTeamMember(username, workspaceId);
            if (workspaceId != null && teamMember == null) {
                ApiErrorResponse.reportError(BAD_REQUEST, SecurityErrors.INVALID_TEAM);
            }

            storeInSession(username, password, teamMember, user);

            AmpApiToken ampApiToken = SecurityUtil.generateToken();
            String ampTeamName = (teamMember == null) ? null : teamMember.getAmpTeam().getName();
            return createResponse(user.isGlobalAdmin(), ampApiToken, user, ampTeamName, true);
        } catch (DgException e) {
            logger.error("Error trying to login the user", e);
            ApiErrorResponse.reportError(INTERNAL_SERVER_ERROR, SecurityErrors.INVALID_REQUEST);
        }
        return null;
    }

    public void invalidateExistingSession() {
        HttpSession session = httpRequest.getSession(false);
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

    private void storeInSession(final String username, final String password, final AmpTeamMember teamMember, final User user) {
        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        authRequest.setDetails(new WebAuthenticationDetails(this.httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        final HttpSession session = this.httpRequest.getSession();
        PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
        if(teamMember != null) {
            session.setAttribute(Constants.CURRENT_MEMBER, teamMember.toTeamMember());
        }
        session.setAttribute(Constants.CURRENT_USER, user);

        session.setAttribute("ampAdmin", ApiAuthentication.isAdmin(user, TLSUtils.getRequest()) ? "yes": "no");
    }

    /**
     * Provides a list of users information
     * <p>
     * <dl>
     * Each user info JSON structure from the list, can hold the following fields (only those that are not null):
     * <dt><b>id</b><dd> user id
     * <dt><b>first-name</b><dd> user first name
     * <dt><b>last-name</b><dd> user last name
     * <dt><b>email</b><dd> user email address
     * <dt><b>password-changed-at</b><dd> timestamp for the last changed, in time zoned ISO-8601 format
     * <dt><b>is-banned</b><dd> flags if the user is banned
     * <dt><b>is-active</b><dd> flags if the user is active
     * <dt><b>is-pledger</b><dd> flags if the user is pledger
     * <dt><b>is-admin</b><dd> flags if the user is global AMP admin
     * <dt><b>lang-iso2</b><dd> the user preferred language as iso2
     * <dt><b>country-iso2</b><dd> user registered country iso2
     * <dt><b>org-type-id</b><dd> user organization type id
     * <dt><b>org-group-id</b><dd> user organization group id
     * <dt><b>org-id</b><dd> user organization id
     * <dt><b>assigned-org-id</b><dd> user assigned organization id
     * <dt><b>assigned-org-ids</b><dd> user assigned organizations ids
     * <dt><b>group-keys</b><dd> user groups keys
     *
     * <h3> Sample Output: </h3>
     * <pre>
     * {
     *     "id": 225,
     *     "email": "princettav@gmail.com",
     *     "first-name": "Princetta",
     *     "last-name": "Clinton-Varmah",
     *     "password-changed-at": "2016-12-26T20:31:04.828+02:00"
     *     "is-banned": false,
     *     "is-active": false,
     *     "is-pledger": false,
     *     "is-admin": false,
     *     "lang-iso2": "en",
     *     "country-iso2": "lr",
     *     "org-type-id": 1,
     *     "org-group-id": 6,
     *     "org-id": 702,
     *     "group-keys": [
     *         "MEM",
     *         "EDT"
     *     ]
     * }
     * </pre>
     * @param ids a comma separated list of users ids for which to provide the information, invalid ids are ignored,
     *            if list is empty then all users will be returned
     * @return a list of User information
     */
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "users", name = "Users", authTypes = {AuthRule.AUTHENTICATED})
    public List<org.digijava.kernel.ampapi.endpoints.security.dto.User> getUsersInfo(
            @DefaultValue("") @QueryParam("ids") ListOfLongs ids) {
        return SpringUtil.getBean(UserService.class).getUserInfo(ids);
    }

    /**
     * @return menu structure for the current view, user and state
     */
    @GET
    @Path("/menus") 
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Menu", name = "Menu")
    public List<JsonBean> getMenu() {
        return SecurityService.getMenu();
    }
    
    /**
     * For the user response
     * if there'is a logged user it returns
     * 
     * <code>
     *  {
        "id": "45678",
        "email": "atl@amp.org",
        "firstName": "ATL",
        "lastName": "ATL",
        "workspace": "Ministry of Finance",
        "administratorMode": "false"
        }   
        </code> <code>
        if not logged it it returns 
        {
            username: null
        }
        </code>
     * 
     * @return
     */
    @GET
    @Path("/layout")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Layout", name = "Layout")
    public JsonBean getLayout() throws ParserConfigurationException, SAXException, IOException 
    {
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        String ampAdmin = (String) httpRequest.getSession().getAttribute("ampAdmin");
        boolean isAdmin = ampAdmin!=null && ampAdmin.equals("yes");
        SiteDomain currentDomain = RequestUtils.getSiteDomain(httpRequest);
        String siteUrl = SiteUtils.getSiteURL(currentDomain, httpRequest.getScheme(), httpRequest.getServerPort(), httpRequest
                .getContextPath());
        JsonBean layout = SecurityService.getFooter(siteUrl,isAdmin);
        if (tm != null) {
            User u;
            try {
                u = UserUtils.getUserByEmail(tm.getEmail());
            } catch (DgException e) {
                layout.set("email", null);
                return layout;
            }

            boolean siteAdmin = ApiAuthentication.isAdmin(u, TLSUtils.getRequest());
            layout.set("logged",true);
            layout.set("email", u.getEmail());
            layout.set("userId",u.getId());
            layout.set("firstName", u.getFirstNames());
            layout.set("lastName", u.getLastName());
            layout.set("administratorMode", siteAdmin);
            if (!siteAdmin) {
                AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());

                if (ampTeamMember.getAmpTeam() != null) { 
                    layout.set("workspace", ampTeamMember.getAmpTeam().getName());
                    layout.set("workspaceId", ampTeamMember.getAmpTeam().getIdentifier());
                }
                
            } else {
                return layout;
            }
        } else {
            layout.set("email", null);
            if ("true".equals(TLSUtils.getRequest().getSession().getAttribute("isUserLogged"))) {
                layout.set("logged",true);
            }
            else {
                layout.set("logged",false);
            }
            
        }

        return layout;
    }
    
    /**
     * Provides a list of workspace member definition
     * <p>
     * <dl>
     * Each workspace member JSON structure from the list will hold the following fields:
     * <dt><b>id</b><dd> workspace member id
     * <dt><b>user-id</b><dd> user id
     * <dt><b>workspace-id</b><dd> workspace id
     * <dt><b>role-id</b><dd> workspace member role id
     *
     * <h3> Sample Output: </h3>
     * <pre>
     * [
     *   {
     *       "id": 12,
     *       "user-id": 1,
     *       "workspace-id": 100,
     *       "role-id": 1,
     *   },
     *   ...
     * ]
     * </pre>
     * @param ids comma separated list of workspace member ids, if empty all members will be returned
     * @return list of workspace member definitions
     */
    @GET
    @Path("/workspace-member")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "workspace-member", name = "Workspace Member", authTypes = {AuthRule.AUTHENTICATED})
    public List<WorkspaceMember> getWorkspaceMembers(@DefaultValue("") @QueryParam("ids") ListOfLongs ids) {
        return SpringUtil.getBean(WorkspaceMemberService.class).getWorkspaceMembers(ids);
    }

    /**
     * Returns workspace settings for a specified workspaces.
     *
     * <p>Each workspace setting JSON structure from the list will hold the following fields:
     * <dl>
     * <dt><b>id</b><dd> - workspace setting id
     * <dt><b>workspace-id</b><dd> - workspace id
     * <dt><b>default-records-per-page</b><dd> - number of rows in report
     * <dt><b>number-of-pages-to-display</b><dd> - maximum number of pages in report
     * <dt><b>report-start-year</b><dd> - in reports, display year columns starting from this year
     * <dt><b>report-end-year</b><dd> - in reports, display year columns up to this year
     * <dt><b>currency</b><dd> - currency code
     * <dt><b>fiscal-calendar</b><dd> - id of the fiscal calendar
     * <dt><b>language</b><dd> - language
     * <dt><b>validation</b><dd> - activity validation setting
     * <dt><b>show-all-countries</b><dd> - whenever all countries should be displayed in filters
     * <dt><b>default-team-report</b><dd> - default displayed report id
     * <dt><b>default-reports-per-page</b><dd> - number of reports per page
     * <dt><b>allow-add-team-res</b><dd> - documents adding policy (1-3)
     * <dt><b>allow-share-team-res</b><dd> - documents sharing policy (1-2)
     * <dt><b>allow-publishing-resources</b><dd> - documents sharing policy (1-3)
     * </dl>
     *
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "workspace-id": 60,
     *     "currency": "USD",
     *     "language": "en",
     *     "validation": "allEdits",
     *     "id": 71,
     *     "default-records-per-page": 100,
     *     "report-start-year": 0,
     *     "report-end-year": 0,
     *     "fiscal-calendar": 4,
     *     "show-all-countries": false,
     *     "default-team-report": null,
     *     "default-reports-per-page": 0,
     *     "allow-add-team-res": 1,
     *     "allow-share-team-res": 1,
     *     "allow-publishing-resources": 1,
     *     "number-of-pages-to-display": null
     *   }
     * ]
     * </pre>
     */
    @GET
    @Path("/workspace-settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "workspace-settings", name = "Workspace Settings", authTypes = AuthRule.AUTHENTICATED)
    public List<AmpApplicationSettings> getWorkspaceSettings(
            @DefaultValue("") @QueryParam("workspace-ids") ListOfLongs ids) {
        return DbUtil.getTeamAppSettings(ids);
    }

    /**
     * Return the list of workspaces the user has access to.
     *
     * <h3>Sample Output:</h3>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Main workspace"
     *   },
     *   {
     *     "id": 2,
     *     "name": "Test workspace"
     *   }
     * ]
     * </pre>
     */
    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id="workspaces", ui=false, authTypes = AuthRule.AUTHENTICATED)
    public Collection<JsonBean> getWorkspaces() {
        return SecurityService.getWorkspaces();
    }

    /**
     * THIS IS FOR DEBUG ONLY. Must be disabled on production.
     * @param token
     * @return
     */
    /*
    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String echo(@QueryParam("amp_api_token") String token) {
        token = SecurityUtil.generateToken();
        return "Token: " + token;
    }
    */

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return SecurityErrors.class;
    }
}
