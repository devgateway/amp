package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.ws.rs.Consumes;
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
    @ApiOperation(value = "Retrieve and provide a list of indicators.")
    public List<MEIndicatorDTO> getIndicators() {
        return new IndicatorManagerService().getIndicators();
    }

    @POST
    @Path("/indicators")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "crateIndicator")
    @ApiOperation(value = "Save new indicator")
    public MEIndicatorDTO createIndicator(MEIndicatorDTO indicatorRequest) {
        return new IndicatorManagerService().createAmpIndicator(indicatorRequest);
    }

    @GET
    @Path("/indicators/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getIndicatorById")
    @ApiOperation(value = "Retrieve the indicator by id.")
    public MEIndicatorDTO getIndicatorById(@PathParam("id") String id) {
        return new IndicatorManagerService().getIndicatorById(id);
    }

    @GET
    @Path("/sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getSectors")
    @ApiOperation(value = "Retrieve and provide a list of sectors used by indicators.")
    public List<SectorDTO> getSectors() {
        return new IndicatorManagerService().getSectors();
    }


}
