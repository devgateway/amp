package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import io.swagger.annotations.Api;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutcomeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutputDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.service.AmpOutcomeOutputService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/amp-outcome-output")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("Amp Outcome Output")
public class AmpOutcomeOutputEndpoints {
    private final AmpOutcomeOutputService service = new AmpOutcomeOutputService();

    @GET
    @Path("/outcomes")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getAllOutcomes")
    @ApiOperation(value = "Get all outcomes", notes = "Returns a list of all outcomes")
    public List<AmpOutcomeDTO> getAllOutcomes() {
        return service.getAllOutcomes();
    }

    @GET
    @Path("/outputs")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getAllOutputs")
    @ApiOperation(value = "Get all outputs", notes = "Returns a list of all outputs")
    public List<AmpOutputDTO> getAllOutputs() {
        return service.getAllOutputs();
    }

    @POST
    @Path("/outcome")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "createOutcome")
    @ApiOperation(value = "Create outcome", notes = "Creates a new outcome")
    public Response createOutcome(AmpOutcomeDTO dto) {
        AmpOutcomeDTO created = service.createOutcome(dto);
        return Response.ok(created).build();
    }

    @POST
    @Path("/output")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "createOutput")
    @ApiOperation(value = "Create output", notes = "Creates a new output")
    public Response createOutput(AmpOutputDTO dto) {
        if (dto.getOutcomeIds() == null || dto.getOutcomeIds().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Output must be linked to at least one Outcome.").build();
        }
        AmpOutputDTO created = service.createOutput(dto);
        return Response.ok(created).build();
    }

    @PUT
    @Path("/outcome/{id}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateOutcome")
    @ApiOperation(value = "Update outcome", notes = "Updates an existing outcome")
    public Response updateOutcome(@PathParam("id") Long id, AmpOutcomeDTO dto) {
        AmpOutcomeDTO updated = service.updateOutcome(id, dto);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @PUT
    @Path("/output/{id}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateOutput")
    @ApiOperation(value = "Update output", notes = "Updates an existing output")
    public Response updateOutput(@PathParam("id") Long id, AmpOutputDTO dto) {
        if (dto.getOutcomeIds() == null || dto.getOutcomeIds().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Output must be linked to at least one Outcome.").build();
        }
        AmpOutputDTO updated = service.updateOutput(id, dto);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/outcome/{id}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "deleteOutcome")
    @ApiOperation(value = "Delete outcome", notes = "Deletes an outcome by ID")
    public Response deleteOutcome(@PathParam("id") Long id) {
        boolean deleted = service.deleteOutcome(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/output/{id}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "deleteOutput")
    @ApiOperation(value = "Delete output", notes = "Deletes an output by ID")
    public Response deleteOutput(@PathParam("id") Long id) {
        boolean deleted = service.deleteOutput(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
