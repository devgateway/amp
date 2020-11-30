package org.digijava.kernel.ampapi.endpoints.ndd;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.digijava.module.esrigis.dbentity.ApiStateType;

/**
 * @author Octavian Ciubotaru
 */
@Path("ndd")
@Api("ndd")
public class NDDEndpoints {

    private final NDDService nddService = new NDDService();

    @GET
    @Path("mapping-config")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getMappingConfiguration")
    @ApiOperation("Returns configuration for mapping indirect programs.")
    public MappingConfiguration getMappingConfiguration() {
        return nddService.getMappingConfiguration();
    }

    @POST
    @Path("mapping")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateMapping")
    @ApiOperation("Update indirect program mapping.")
    public void updateMapping(List<AmpIndirectTheme> mapping) {
        nddService.updateMapping(mapping);
    }

    @POST
    @Path("update-source-destination-programs")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateSrcDstPrograms")
    @ApiOperation("Update the Primary Program (source) and Indirect Program (destination) in GS.")
    public void updateSrcDstPrograms(AmpIndirectTheme mapping) {
        nddService.updateMainProgramsMapping(mapping);
    }

    @GET
    @Path("available-programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getAvailablePrograms")
    @ApiOperation("Returns the list of programs we can use as Primary and Indirect.")
    public List<NDDService.SingleProgramData> getAvailablePrograms() {
        return nddService.getSinglePrograms();
    }

    @POST
    @Path("direct-indirect-report")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getDirectIndirectReport")
    @ApiOperation("")
    public List<NDDSolarChartData> getDirectIndirectReport(SettingsAndFiltersParameters params) {
        return DashboardService.generateDirectIndirectReport(params);
    }

    @POST
    @Path("/save-charts")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SaveChart")
    @ApiOperation("Save the state of a chart")
    public AmpApiState saveChart(@JsonView(AmpApiState.DetailView.class) AmpApiState chart) {
        return EndpointUtils.saveApiState(chart, ApiStateType.NDD);
    }

    @GET
    @Path("/saved-charts/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SavedChart")
    @JsonView(AmpApiState.DetailView.class)
    @ApiOperation("Get the state of a saved chart")
    public AmpApiState savedChart(@ApiParam("Property value") @PathParam("id") Long id) {
        return EndpointUtils.getApiState(id, ApiStateType.NDD);
    }
}
