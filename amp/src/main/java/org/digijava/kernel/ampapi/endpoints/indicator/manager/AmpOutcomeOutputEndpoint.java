package org.digijava.kernel.ampapi.endpoints.indicator.manager;

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
public class AmpOutcomeOutputEndpoint {
    private final AmpOutcomeOutputService service = new AmpOutcomeOutputService();

    @GET
    @Path("/outcomes")
    public List<AmpOutcomeDTO> getAllOutcomes() {
        return service.getAllOutcomes();
    }

    @GET
    @Path("/outputs")
    public List<AmpOutputDTO> getAllOutputs() {
        return service.getAllOutputs();
    }

    @POST
    @Path("/outcome")
    public Response createOutcome(AmpOutcomeDTO dto) {
        AmpOutcomeDTO created = service.createOutcome(dto);
        return Response.ok(created).build();
    }

    @POST
    @Path("/output")
    public Response createOutput(AmpOutputDTO dto) {
        if (dto.getOutcomeIds() == null || dto.getOutcomeIds().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Output must be linked to at least one Outcome.").build();
        }
        AmpOutputDTO created = service.createOutput(dto);
        return Response.ok(created).build();
    }

    @PUT
    @Path("/outcome/{id}")
    public Response updateOutcome(@PathParam("id") Long id, AmpOutcomeDTO dto) {
        AmpOutcomeDTO updated = service.updateOutcome(id, dto);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @PUT
    @Path("/output/{id}")
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
    public Response deleteOutcome(@PathParam("id") Long id) {
        boolean deleted = service.deleteOutcome(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/output/{id}")
    public Response deleteOutput(@PathParam("id") Long id) {
        boolean deleted = service.deleteOutput(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
