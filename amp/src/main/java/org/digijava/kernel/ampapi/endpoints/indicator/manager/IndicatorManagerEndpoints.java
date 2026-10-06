package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.digijava.kernel.ampapi.endpoints.gpi.ValidationUtils;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

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

    @PUT
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
    @ApiOperation(value = "Retrieve and provide a list of programs "
            + "nested with their program schemes used by indicators.")
    public List<ProgramSchemeDTO> getPrograms() {
        return new IndicatorManagerService().getProgramScheme();
    }

    @GET
    @Path("/categoryValues")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getCategoryValues")
    @ApiOperation(value = "Retrieve and provide a list of category values.")
    public List<AmpCategoryValueDTO> getCategoryValues() {
        return new IndicatorManagerService().getCategoryValues();
    }

    @GET
    @Path("/responsibleOrgs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.IN_ADMIN, id = "getResponsibleOrgs")
    @ApiOperation(value = "Retrieve and provide a list of responsible organizations.")
    public Set<ResponsibleOrgDTO> getResponsibleOrgs() {
        return new IndicatorManagerService().getResponsibleOrganizations();
    }

}
