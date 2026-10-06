package org.digijava.kernel.ampapi.endpoints.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.security.dto.*;
import org.digijava.kernel.ampapi.endpoints.security.services.UserService;
import org.digijava.kernel.ampapi.endpoints.security.services.WorkspaceMemberService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.types.ListOfLongs;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This class should have all security / permissions related methods
 *
 * @author jdeanquin@developmentgateway.org
 *
 */
@Path("security")
@Api("security")
public class Security {

    private static String SITE_CONFIG_PATH = "TEMPLATE" + System.getProperty("file.separator") + "ampTemplate"
            + System.getProperty("file.separator") + "site-config.xml";

    public static String getSiteConfigPath() {
        return SITE_CONFIG_PATH;
    }

    private SecurityService securityService = SecurityService.getInstance();

    @GET
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "user", ui = false)
    @ApiOperation(
            value = "Retrieve current user session information",
            notes = "This endpoint returns detailed information about the currently logged-in user's session.\n\n" +
                    "The response includes user identification details, permissions, active workspace information, " +
                    "and other session-related data that client applications can use to personalize the user experience.\n\n" +
                    "This endpoint requires authentication and will return an error if no valid session exists.")
    public UserSessionInformation getUserSessionInformation() {
        return SecurityService.getInstance().getUserSessionInformation();
    }

    @POST
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Authenticate user via API",
            notes = "This endpoint authenticates a user and establishes a session that can be used for subsequent API calls.\n\n" +
                    "**Required parameters:**\n" +
                    "- **username**: The user's login name\n" +
                    "- **password**: SHA-1 hash of the user's password (not the plain text password)\n\n" +
                    "**Optional parameters:**\n" +
                    "- **workspaceId**: ID of the workspace to activate after login.\n\n" +
                    "Upon successful authentication, the response includes a session token and detailed user information " +
                    "similar to the /user/ endpoint. This information includes user details, permissions, and active workspace data.\n\n" +
                    "If authentication fails, an appropriate error message will be returned.")
    public UserSessionInformation authenticate(@ApiParam(required = true) AuthenticationRequest authRequest) {
        return securityService.authenticate(authRequest);
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "users", name = "Users", authTypes = {AuthRule.AUTHENTICATED})
    @ApiOperation(
            value = "Retrieve information about multiple users",
            notes = "This endpoint returns detailed information about users in the system.\n\n" +
                    "You can request information for specific users by providing their IDs in the 'ids' query parameter. " +
                    "If no IDs are provided, information about all users in the system will be returned.\n\n" +
                    "The response includes user details such as name, email, organization, and other profile information " +
                    "for each requested user. Invalid user IDs are ignored.\n\n" +
                    "This endpoint requires authentication.")
    public List<org.digijava.kernel.ampapi.endpoints.security.dto.User> getUsersInfo(
            @ApiParam("User ids. Invalid ids are ignored. If list is empty then all users are returned.")
            @DefaultValue("") @QueryParam("ids") ListOfLongs ids) {
        return SpringUtil.getBean(UserService.class).getUserInfo(ids);
    }

    @GET
    @Path("/menus")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Menu", name = "Menu")
    @ApiOperation(
            value = "Retrieve menu structure for the current user",
            notes = "This endpoint returns the navigation menu structure that should be displayed to the current user.\n\n" +
                    "The menu structure is dynamically generated based on the user's permissions, active workspace, " +
                    "and current application state. It includes all available menu items, their hierarchy, and metadata " +
                    "such as labels, URLs, and visibility flags.\n\n" +
                    "Client applications can use this information to build navigation menus that reflect the user's " +
                    "permissions and current context. The menu structure is returned as a hierarchical tree of menu items.")
    public List<MenuItemStructure> getMenu() {
        return securityService.getMenuStructures();
    }

    @GET
    @Path("/layout")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Layout", name = "Layout")
    @ApiOperation(
            value = "Retrieve UI layout information for the current user",
            notes = "This endpoint returns layout configuration information that client applications can use " +
                    "to customize the user interface for the current user.\n\n" +
                    "The layout information includes user interface preferences, theme settings, component visibility, " +
                    "and other display-related configurations that may be specific to the user's role, permissions, " +
                    "or personal settings.\n\n" +
                    "Client applications can use this information to adapt their user interface to match the user's " +
                    "expected experience in AMP.")
    public LayoutInformation getLayout() {
        return securityService.getLayout();
    }

    @GET
    @Path("/workspace-member")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "workspace-member", name = "Workspace Member", authTypes = {AuthRule.AUTHENTICATED})
    @ApiOperation(
            value = "Retrieve information about workspace members",
            notes = "This endpoint returns detailed information about members of workspaces in the AMP system.\n\n" +
                    "You can request information for specific workspace members by providing their IDs in the 'ids' " +
                    "query parameter. If no IDs are provided, information about all workspace members will be returned.\n\n" +
                    "The response includes details such as the user associated with each workspace membership, " +
                    "their role in the workspace, permissions, and other workspace-specific settings.\n\n" +
                    "This information is useful for understanding who has access to which workspaces and what " +
                    "level of permissions they have. This endpoint requires authentication.")
    public List<WorkspaceMember> getWorkspaceMembers(
            @ApiParam("workspace member ids, if empty all members will be returned")
            @DefaultValue("") @QueryParam("ids") ListOfLongs ids) {
        return SpringUtil.getBean(WorkspaceMemberService.class).getWorkspaceMembers(ids);
    }

    @GET
    @Path("/workspace-settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "workspace-settings", name = "Workspace Settings", authTypes = AuthRule.AUTHENTICATED)
    @ApiOperation(
            value = "Retrieve settings for specified workspaces",
            notes = "This endpoint returns configuration settings for one or more workspaces in the AMP system.\n\n" +
                    "You can request settings for specific workspaces by providing their IDs in the 'workspace-ids' " +
                    "query parameter. If no IDs are provided, settings for all workspaces will be returned.\n\n" +
                    "The response includes workspace-specific settings such as default currency, fiscal calendar, " +
                    "validation rules, approval processes, and other configuration options that affect how activities " +
                    "are managed within each workspace.\n\n" +
                    "This information is useful for understanding the configuration of different workspaces and " +
                    "adapting client behavior accordingly. This endpoint requires authentication.")
    public List<AmpApplicationSettings> getWorkspaceSettings(
            @DefaultValue("") @QueryParam("workspace-ids") ListOfLongs ids) {
        return DbUtil.getTeamAppSettings(ids);
    }

    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id="workspaces", ui=false, authTypes = AuthRule.AUTHENTICATED)
    @ApiOperation(
            value = "Retrieve all workspaces accessible to the current user",
            notes = "This endpoint returns a list of all workspaces that the currently authenticated user " +
                    "has permission to access.\n\n" +
                    "The response includes basic information about each workspace such as its ID, name, " +
                    "description, and the user's role within that workspace. Additional metadata about each " +
                    "workspace may also be included.\n\n" +
                    "This information is useful for allowing users to select which workspace they want to work in, " +
                    "or for displaying workspace-specific information in client applications.\n\n" +
                    "This endpoint requires authentication and will return an error if no valid session exists.")
    public List<WorkspaceInfo> getWorkspaces() {
        return securityService.getWorkspaces();
    }

    @GET
    @Path("/ampTeam/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Retrieve detailed information about a specific workspace",
            notes = "This endpoint returns comprehensive information about a single workspace (AmpTeam) " +
                    "identified by its ID.\n\n" +
                    "The response includes detailed workspace configuration such as workspace type, permissions, " +
                    "members, associated organizations, workflow settings, and other metadata that defines " +
                    "the workspace's behavior and capabilities.\n\n" +
                    "This information is primarily used by administrators to inspect workspace configurations. " +
                    "Note that this endpoint requires administrator privileges and is not available to regular users.")
    @ApiMethod(id = "ampTeam", authTypes = AuthRule.IN_ADMIN)
    public final AmpTeam getWorkspace(@PathParam("id") Long id) {
        return TeamUtil.getAmpTeam(id);
    }

}
