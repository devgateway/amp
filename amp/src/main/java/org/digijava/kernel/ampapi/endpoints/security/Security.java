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
    @ApiOperation("Get user session information.")
    public UserSessionInformation getUserSessionInformation() {
        return SecurityService.getInstance().getUserSessionInformation();
    }

    @POST
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(value = "Authenticate user with via API.",
            notes = "<p>This endpoint is used to authenticate users via API. Mandatory fields are username and "
                    + "password. Password value is a sha1 hash of the actual password. Third parameter is "
                    + "workspaceId which allows to preselect active workspace.</p>\n"
                    + "Workspace parameter is optional.\n")

    public UserSessionInformation authenticate(@ApiParam(required = true) AuthenticationRequest authRequest) {
        return securityService.authenticate(authRequest);
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "users", name = "Users", authTypes = {AuthRule.AUTHENTICATED})
    @ApiOperation("Provides a list of users information.")
    public List<org.digijava.kernel.ampapi.endpoints.security.dto.User> getUsersInfo(
            @ApiParam("User ids. Invalid ids are ignored. If list is empty then all users are returned.")
            @DefaultValue("") @QueryParam("ids") ListOfLongs ids) {
        return SpringUtil.getBean(UserService.class).getUserInfo(ids);
    }

    @GET
    @Path("/menus")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Menu", name = "Menu")
    @ApiOperation("Get menu structure for the current view, user and state.")
    public List<MenuItemStructure> getMenu() {
        return securityService.getMenuStructures();
    }

    @GET
    @Path("/layout")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Layout", name = "Layout")
    @ApiOperation(value = "Get layout information for logged in user.")
    public LayoutInformation getLayout() {
        return securityService.getLayout();
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
    @ApiOperation("Return the list of workspaces the user has access to.")
    public List<WorkspaceInfo> getWorkspaces() {
        return securityService.getWorkspaces();
    }

    @GET
    @Path("/ampTeam/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Get AmpTeam specification")
    @ApiMethod(id = "ampTeam", authTypes = AuthRule.IN_ADMIN)
    public final AmpTeam getWorkspace(@PathParam("id") Long id) {
        return TeamUtil.getAmpTeam(id);
    }

}
