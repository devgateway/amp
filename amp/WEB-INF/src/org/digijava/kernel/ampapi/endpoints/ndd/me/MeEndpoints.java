package org.digijava.kernel.ampapi.endpoints.ndd.me;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}
