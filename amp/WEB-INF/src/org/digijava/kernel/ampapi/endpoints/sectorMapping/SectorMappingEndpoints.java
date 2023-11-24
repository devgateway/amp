package org.digijava.kernel.ampapi.endpoints.sectorMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.gpi.ValidationUtils;
import org.digijava.kernel.ampapi.endpoints.ndd.IndirectProgramMappingConfiguration;
import org.digijava.kernel.ampapi.endpoints.sectorMapping.dto.GenericSelectObjDTO;
import org.digijava.kernel.ampapi.endpoints.sectorMapping.dto.MappingConfigurationDTO;
import org.digijava.kernel.ampapi.endpoints.sectorMapping.dto.SchemaClassificationDTO;
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
    @Path("sectors-mapping-config")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getMappingsConf")
    @ApiOperation("Returns configuration saved for sectors mapping.")
    public MappingConfigurationDTO getMappingsConf() {
        return smService.getMappingsConf();
    }

    @GET
    @Path("all-mappings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAllSectorMappings")
    @ApiOperation("Returns all sector mappings.")
    public Collection getAllSectorMappings() {
        return smService.getAllSectorMappings();
    }

    @GET
    @Path("all-schemes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getClassifiedSectors")
    @ApiOperation("Returns all schemes and his classifications.")
    public List<SchemaClassificationDTO> getAllSchemes() {
        return smService.getAllSchemes();
    }

    @GET
    @Path("sectors-by-scheme/{schemeId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getClassifiedSectors")
    @ApiOperation("Returns a list of sectors level 1 by scheme id.")
    public List<GenericSelectObjDTO> getSectorsByScheme(@ApiParam("Property value") @PathParam("schemeId") Long schemeId) {
        return smService.getSectorsByScheme(schemeId);
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
