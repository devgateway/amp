package org.digijava.kernel.ampapi.endpoints.sectorMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.gpi.ValidationUtils;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpSectorMapping;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Diego Rossi
 */
@Path("sectors-mapping")
@Api("sectors-mapping")
public class SectorMappingEndpoints {

    private final SectorMappingService smService = new SectorMappingService();

    @GET
    @Path("all-mappings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAllSectorMappings")
    @ApiOperation("Returns all sector mappings.")
    public Collection getAllSectorMappings() {
        return smService.getAllSectorMappings();
    }

    @GET
    @Path("sectors-classified/{classSector}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getClassifiedSectors")
    @ApiOperation("Returns primary or secondary sectors by parameter. 1: Primary, 2: Secondary")
    public List<SectorMappingService.SingleSectorData> getClassifiedSectors(@ApiParam("Property value") @PathParam("classSector") Long classSector) {
        List<Long> paramValuesValid = Arrays.asList(1L, 2L);
        ValidationUtils.valuesValid(paramValuesValid, classSector);
        return smService.getClassifiedSectors(classSector);
    }

    @GET
    @Path("secondaries-by-primary/{primarySectorId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getSecondarySectorsByPrimary")
    @ApiOperation("Returns a list of secondary sectors by primary sector id.")
    public List<SectorMappingService.SingleSectorData> getSecondarySectorsByPrimary(@ApiParam("Property value") @PathParam("primarySectorId") Long primarySectorId) {
        return smService.getSecondSectorsByPrimary(primarySectorId);
    }

    @POST
    @Path("")
    @ApiMethod(id = "createSectorMapping") //TODO: add -> authTypes = AuthRule.IN_ADMIN,
    @ApiOperation("Create a sector mapping.")
    public void createSectorMapping(AmpSectorMapping mapping) throws DgException {
        smService.createSectorsMapping(mapping);
    }

    @DELETE
    @Path("/{idMapping}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getClassifiedSectors") //TODO: add -> authTypes = AuthRule.IN_ADMIN,
    @ApiOperation("Delete a sector mapping.")
    public void deleteSectorMapping(@ApiParam("Property value") @PathParam("idMapping") Long idMapping) throws DgException {
        smService.deleteSectorMapping(idMapping);
    }
}
