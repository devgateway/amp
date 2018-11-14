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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api("security")
public class Security implements ErrorReportingEndpoint {
    private static final Logger logger = Logger.getLogger(Security.class);
    private static String SITE_CONFIG_PATH = "TEMPLATE" + System.getProperty("file.separator") + "ampTemplate"
            + System.getProperty("file.separator") + "site-config.xml";

    public static String getSiteConfigPath() {
        return SITE_CONFIG_PATH;
    }
    @Context
    private HttpServletRequest httpRequest;

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

    @POST
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(value = "Authenticate user with via API.",
            notes = "<p>This endpoint is used to authenticate users via API. Mandatory fields are username and "
                    + "password. Password value is a sha1 hash of the actual password. Third parameter is "
                    + "workspaceId which allows to preselect active workspace.</p>\n"
                    + "Workspace parameter is optional. If specified all with calls issued with the provided token "
                    + "will be handled for respective workspace.\n"
                    + "<h3>Sample input:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "  \"username\": \"atl@amp.org\",\n"
                    + "  \"password\": \"a7848b4c1b75cb7bb7449069fe0e114b730c0448\",\n"
                    + "  \"workspaceId\": 4\n"
                    + "}\n"
                    + "</pre>\n"
                    + "<h3>Sample output:</h3>\n"
                    + "<pre>\n"
                    + "{\n"
                    + "  \"token\": \"34bf1c55-f2d1-43bb-bef4-e98b6077f66f\",\n"
                    + "  \"token-expiration\": 1483433179305,\n"
                    + "  \"url\": \"http://localhost:8080/showLayout.do?layout=login\",\n"
                    + "  \"team\": \"Espace de Travail Cellule Technique du COMOREX\",\n"
                    + "  \"user-name\": \"atl@amp.org\",\n"
                    + "  \"user-id\": 2,\n"
                    + "  \"is-admin\": false,\n"
                    + "  \"add-activity\": true,\n"
                    + "  \"view-activity\": true\n"
                    + "}\n"
                    + "</pre>")

    public JsonBean authenticate(@ApiParam("username/password/workspace") final JsonBean authentication) {
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

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "users", name = "Users", authTypes = {AuthRule.AUTHENTICATED})
    @ApiOperation("Provides a list of users information")
    public List<org.digijava.kernel.ampapi.endpoints.security.dto.User> getUsersInfo(
            @ApiParam("User ids. Invalid ids are ignored. If list is empty then all users are returned.")
            @DefaultValue("") @QueryParam("ids") ListOfLongs ids) {
        return SpringUtil.getBean(UserService.class).getUserInfo(ids);
    }

    @GET
    @Path("/menus") 
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Menu", name = "Menu")
    @ApiOperation("menu structure for the current view, user and state")
    public List<JsonBean> getMenu() {
        return SecurityService.getMenu();
    }
    
    @GET
    @Path("/layout")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Layout", name = "Layout")
    @ApiOperation(value = "",
            notes = "For the user response if there'is a logged user it returns:\n"
                    + "<code>\n"
                    + " {\n"
                    + " \"id\": \"45678\",\n"
                    + " \"email\": \"atl@amp.org\",\n"
                    + " \"firstName\": \"ATL\",\n"
                    + " \"lastName\": \"ATL\",\n"
                    + " \"workspace\": \"Ministry of Finance\",\n"
                    + " \"administratorMode\": \"false\"\n"
                    + " }   \n"
                    + " </code>"
                    + "if not logged it it returns"
                    + "<code>\n"
                    + " {\n"
                    + "     username: null\n"
                    + " }\n"
                    + " </code>")
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
    
    @GET
    @Path("/workspace-member")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "workspace-member", name = "Workspace Member", authTypes = {AuthRule.AUTHENTICATED})
    @ApiOperation("Provides a list of workspace member definition")
    public List<WorkspaceMember> getWorkspaceMembers(
            @ApiParam("workspace member ids, if empty all members will be returned")
            @DefaultValue("") @QueryParam("ids") ListOfLongs ids) {
        return SpringUtil.getBean(WorkspaceMemberService.class).getWorkspaceMembers(ids);
    }

    @GET
    @Path("/workspace-settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "workspace-settings", name = "Workspace Settings", authTypes = AuthRule.AUTHENTICATED)
    @ApiOperation("Returns workspace settings for a specified workspaces.")
    public List<AmpApplicationSettings> getWorkspaceSettings(
            @DefaultValue("") @QueryParam("workspace-ids") ListOfLongs ids) {
        return DbUtil.getTeamAppSettings(ids);
    }

    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id="workspaces", ui=false, authTypes = AuthRule.AUTHENTICATED)
    @ApiOperation(
            value = "Return the list of workspaces the user has access to.",
            notes = "<h3>Sample Output:</h3>\n"
                    + "<pre>\n"
                    + "[\n"
                    + "  {\n"
                    + "    \"id\": 1,\n"
                    + "    \"name\": \"Main workspace\"\n"
                    + "  },\n"
                    + "  {\n"
                    + "    \"id\": 2,\n"
                    + "    \"name\": \"Test workspace\"\n"
                    + "  }\n"
                    + "]\n"
                    + "</pre>")
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
