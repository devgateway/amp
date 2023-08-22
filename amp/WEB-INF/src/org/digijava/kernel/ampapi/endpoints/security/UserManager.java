package org.digijava.kernel.ampapi.endpoints.security;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.contact.ContactImporter;
import org.digijava.kernel.ampapi.endpoints.contact.dto.ContactView;
import org.digijava.kernel.ampapi.endpoints.contact.dto.SwaggerContact;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.CreateUserRequest;
import org.digijava.kernel.ampapi.endpoints.security.dto.usermanager.LoggedUserInformation;
import org.digijava.kernel.ampapi.endpoints.security.services.UserManagerService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.dbentity.AmpContact;

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

    @POST
    @Path("/update/user/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED}, id = "updateUser", ui = false)
    @ApiOperation("Update an existing user profile")
    public LoggedUserInformation updateUserProfile(@ApiParam("id of the existing user") @PathParam("id") Long id,
                                                   LoggedUserInformation user) {
        return userManagerService.updateUserProfile(user);
    }
}
