package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import io.swagger.annotations.Api;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.service.DisaggregationService;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/indicator_disaggregation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("Amp Indicator Disaggregation Manager")
public class DisaggregationEndpoints {

    DisaggregationService disaggregationService = new DisaggregationService();

    @GET
    @Path("/options/{categoryValueId}")
    public List<AmpCategoryValueDTO> getDisaggregationOptions(@PathParam("categoryValueId") Long categoryValueId) {
        return disaggregationService.getDisaggregationOptions(categoryValueId);
    }

    @POST
    @Path("/options/{categoryValueId}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "addDisaggregationOption")
    public AmpCategoryValueDTO addDisaggregationOption(@PathParam("categoryValueId") Long categoryValueId, AmpCategoryValueDTO option) {
        return disaggregationService.addDisaggregationOption(categoryValueId, option);
    }

    @PUT
    @Path("/options/{optionId}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateDisaggregationOption")
    public AmpCategoryValueDTO updateDisaggregationOption(@PathParam("optionId") Long optionId, AmpCategoryValueDTO option) {
        return disaggregationService.updateDisaggregationOption(optionId, option);
    }

    @DELETE
    @Path("/options/{optionId}")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "deleteDisaggregationOption")
    public void deleteDisaggregationOption(@PathParam("optionId") Long optionId) {
        disaggregationService.deleteDisaggregationOption(optionId);
    }
}
