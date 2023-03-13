package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.gpi.ValidationUtils;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("indicatorManager")
@Api("indicatorManager")
public class IndicatorManagerEndpoints {

    @GET
    @Path("/indicators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getIndicators")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators.")
    public List<MEIndicatorDTO> getMEIndicators() {
        return new IndicatorManagerService().getMEIndicators();
    }

    @GET
    @Path("/indicators/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getMEIndicatorById")
    @ApiOperation(value = "Retrieve the M&E indicator by id.")
    public MEIndicatorDTO getMEIndicatorById(@PathParam("id") Long id) {
        return new IndicatorManagerService().getMEIndicatorById(id);
    }

    @POST
    @Path("/indicators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "createMEIndicator")
    @ApiOperation(value = "Save new M&E indicator")
    public MEIndicatorDTO createMEIndicator(MEIndicatorDTO indicatorRequest) {
        ValidationUtils.requireValid(indicatorRequest);
        return new IndicatorManagerService().createMEIndicator(indicatorRequest);
    }

    @POST
    @Path("/indicators/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateMEIndicator")
    @ApiOperation(value = "Update M&E indicator")
    public MEIndicatorDTO updateMEIndicator(@PathParam("id") Long id, MEIndicatorDTO indicatorRequest) {
        ValidationUtils.requireValid(indicatorRequest);
        return new IndicatorManagerService().updateMEIndicator(id, indicatorRequest);
    }

    @DELETE
    @Path("/indicators/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getIndicatorById")
    @ApiOperation(value = "Delete M&E indicator by id.")
    public void deleteMEIndicatorById(@PathParam("id") Long id) {
        new IndicatorManagerService().deleteMEIndicator(id);
    }

    @GET
    @Path("/sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getSectors")
    @ApiOperation(value = "Retrieve and provide a list of sectors used by indicators.")
    public List<SectorDTO> getSectors() {
        return new IndicatorManagerService().getSectors();
    }

    @GET
    @Path("/programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getPrograms")
    @ApiOperation(value = "Retrieve and provide a list of programs used by indicators.")
    public List<ProgramDTO> getPrograms() {
        return new IndicatorManagerService().getPrograms();
    }


}
