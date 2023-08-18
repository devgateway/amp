package org.digijava.kernel.ampapi.endpoints.security;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.CreateUserRequest;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.LoggedUserInformation;
import org.digijava.kernel.ampapi.endpoints.security.services.UserManagerService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

/**
 * This class should have all user manager endpoints
 *
 * @author Denis Mbugua, dmbugua@developmentgateway.com / dmbugua66@gmail.com
 *
 */
@Path("usermanager")
@Api("usermanager")
public class UserManager {
    private UserManagerService userManagerService = UserManagerService.getInstance();

    @GET
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "user", ui = false)
    @ApiOperation("Get user session information for update.")
    public LoggedUserInformation getLoggedUserInformation() {
        return userManagerService.getLoggedUserInformation();
    }

    @POST
    @Path("/create/user")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED}, id = "createAmpUser", ui = false)
    @ApiOperation(value = "Create amp user API.",
            notes = "<p>This endpoint creates a new amp user in user manager. All the fields are required.</p>\n")

    public org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.UserManager createUser(@ApiParam(required = true) CreateUserRequest user) {
        return userManagerService.createUser(user);
    }
}
