package org.digijava.kernel.ampapi.endpoints.ndd.me;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.IndicatorManagerService;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.SectorDTO;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("me")
@Api("me")
public class MeEndpoints {
    @POST
    @Path("programReport")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeProgramReport")
    @ApiOperation("")
    public final MeReportDTO getMeProgramReport(SettingsAndFiltersParameters params) {
        return MeService.generateProgramsByValueReport(params);
    }

    @GET
    @Path("indicatorsByProgram/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeIndicatorsByProgramReport")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators by program.")
    public final List<MEIndicatorDTO> getIndicatorsByProgram(@PathParam("id") Long programId) {
        return MeService.getIndicatorsByProgram(programId);
    }

    @GET
    @Path("sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeSectors")
    @ApiOperation(value = "Retrieve and provide a list of M&E sectors.")
    public final List<SectorDTO> getSectors() {
        return new IndicatorManagerService().getSectors();
    }

    @GET
    @Path("indicatorsBySector/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMeIndicatorsBySectorReport")
    @ApiOperation(value = "Retrieve and provide a list of M&E indicators by sector.")
    public final List<MEIndicatorDTO> getIndicatorsBySector(@PathParam("id") Long sectorId) {
        return MeService.getIndicatorsBySector(sectorId);
    }

    @POST
    @Path("indicatorsReport")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "indicatorReport")
    @ApiOperation("")
    public final MeReportDTO getIndicatorsByProgramReport(SettingsAndFiltersParameters params) {
        return MeService.generateIndicatorsReport(params);
    }
}
