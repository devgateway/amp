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
        try {
            if (dto.getOutcomeIds() == null || dto.getOutcomeIds().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Output must be linked to at least one Outcome.").build();
            }
            AmpOutputDTO created = service.createOutput(dto);
            return Response.ok(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating output.").build();
        }
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
    @Path("/outcome/delete/{id}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "deleteOutcome")
    @ApiOperation(value = "Delete outcome", notes = "Deletes an outcome by ID")
    public Response deleteOutcome(@PathParam("id") Long id) {
        service.deleteOutcome(id);
        return Response.ok().build();
    }

    @DELETE
    @Path("/output/delete/{id}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "deleteOutput")
    @ApiOperation(value = "Delete output", notes = "Deletes an output by ID")
    public Response deleteOutput(@PathParam("id") Long id, @QueryParam("forceDelete") @DefaultValue("false") boolean forceDelete) {
        service.deleteOutput(id, forceDelete);
        return Response.ok().build();
    }

    @GET
    @Path("/output/{id}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getOutputById")
    @ApiOperation(value = "Get output by ID", notes = "Returns output details for editing")
    public Response getOutputById(@PathParam("id") Long id) {
        AmpOutputDTO output = service.getOutputById(id);
        if (output == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(output).build();
    }

    @GET
    @Path("/outputs/{outputId}/indicators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getIndicatorsByOutputId")
    @ApiOperation(value = "Get indicators linked to a specific output.")
    public List<MEIndicatorDTO> getIndicatorsByOutputId(@PathParam("outputId") Long outputId) {
        return  service.getIndicatorsByOutputId(outputId);
    }

    @GET
    @Path("/outcomes/{outcomeId}/outputs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getOutputsByOutcomeId")
    @ApiOperation(value = "Get outputs linked to a specific outcome.")
    public List<AmpOutputDTO> getOutputsByOutcomeId(@PathParam("outcomeId") Long outcomeId) {
        return  service.getOutputsByOutcomeId(outcomeId);
    }


}
