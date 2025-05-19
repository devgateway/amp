package org.digijava.kernel.ampapi.endpoints.ndd;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpThemeMapping;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.digijava.module.esrigis.dbentity.ApiStateType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@Path("ndd")
@Api("ndd")
public class NDDEndpoints {

    private final NDDService nddService = new NDDService();

    @GET
    @Path("indirect-programs-mapping-config")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIndirectProgramsMappingConfiguration")
    @ApiOperation("Returns configuration for mapping indirect programs.")
    public IndirectProgramMappingConfiguration getIndirectProgramsMappingConfiguration() {
        return nddService.getIndirectProgramMappingConfiguration();
    }

    @GET
    @Path("programs-mapping-config")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getProgramsMappingConfiguration")
    @ApiOperation("Returns configuration for mapping programs.")
    public ProgramMappingConfiguration getProgramsMappingConfiguration() {
        return nddService.getProgramMappingConfiguration();
    }

    @POST
    @Path("indirect-programs-mapping")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateIndirectProgramsMapping")
    @ApiOperation("Update indirect programs mapping.")
    public void updateIndirectProgramsMapping(List<AmpIndirectTheme> mapping) {
        nddService.updateIndirectProgramsMapping(mapping);
    }

    @POST
    @Path("programs-mapping")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateProgramsMapping")
    @ApiOperation("Update programs mapping.")
    public void updateProgramsMapping(List<AmpThemeMapping> mapping) {
        nddService.updateProgramsMapping(mapping);
    }

    @POST
    @Path("update-source-destination-indirect-programs")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateSrcDstIndirectPrograms")
    @ApiOperation("Update the Primary Program (source) and Indirect Program (destination) in GS.")
    public void updateSrcDstIndirectPrograms(AmpIndirectTheme mapping) {
        nddService.updateMainIndirectProgramsMapping(mapping);
    }

    @POST
    @Path("update-source-destination-programs")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "updateSrcDstPrograms")
    @ApiOperation("Update the Source Program and Destination Program in GS.")
    public void updateSrcDstPrograms(AmpThemeMapping mapping) {
        nddService.updateMainProgramsMapping(mapping);
    }

    @GET
    @Path("available-indirect-programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAvailableIndirectPrograms")
    @ApiOperation("Returns the list of programs we can use as Primary and Indirect.")
    public List<NDDService.SingleProgramData> getAvailableIndirectPrograms() {
        return nddService.getSinglePrograms(true);
    }

    @GET
    @Path("available-programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAvailablePrograms")
    @ApiOperation("Returns the list of programs we can use as source and destination for mapping.")
    public List<NDDService.SingleProgramData> getAvailablePrograms() {
        return nddService.getSinglePrograms(false);
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

    @POST
    @Path("activity-detail-report")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getActivityDetailReport")
    @ApiOperation("")
    public List<DetailByYear> getActivityDetailReport(SettingsAndFiltersParameters params) {
        return DashboardService.getActivityDetailReport(params);
    }
}
