package org.digijava.kernel.ampapi.endpoints.sectorMapping;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.kernel.ampapi.endpoints.sectorMapping.dto.GenericSelectObjDTO;
import org.digijava.kernel.ampapi.endpoints.sectorMapping.dto.MappingConfigurationDTO;
import org.digijava.kernel.ampapi.endpoints.sectorMapping.dto.SchemaClassificationDTO;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Sector;
import org.digijava.module.aim.util.SectorUtil;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Diego Rossi
 */
public class SectorMappingService {

    private static final Logger LOGGER = Logger.getLogger(ValueConverter.class);


    public List<GenericSelectObjDTO> getSectorsByScheme(final Long schemeId) {
        List<GenericSelectObjDTO> sectors = new ArrayList<>();

        SectorUtil.getSectorsByScheme(schemeId.intValue()).forEach(sector -> {
            AmpSector sec = (AmpSector) sector;
            GenericSelectObjDTO objDTO =new GenericSelectObjDTO(sec.getAmpSectorId(), sec.getName());
            if (sec.getParentSectorId()!=null) {
                objDTO.parent = new GenericSelectObjDTO(sec.getParentSectorId().getAmpSectorId(), sec.getParentSectorId().getName());
            }
//            Collection<Sector> childSectors = SectorUtil.getSubSectors(((AmpSector) sector).getAmpSectorId());
//            childSectors.forEach(s -> objDTO.children.add(new GenericSelectObjDTO(s.getSectorId(), s.getSectorName())));
            sectors.add(objDTO);
        });

        return sectors;
    }

    public Collection getAllSectorMappings() {
        return SectorUtil.getAllSectorMappings();
    }

    public List<SchemaClassificationDTO> getAllSchemes() {
        return SectorUtil.getAllSectorSchemesAndClassification();
    }

    public void createSectorMappings(List<AmpSectorMapping> mappings) throws DgException {
        SectorUtil.createSectorMappings(mappings);
    }

    public void deleteSectorMapping(Long id) throws DgException {
        SectorUtil.deleteSectorMapping(id);
    }

    public MappingConfigurationDTO getMappingsConf() {
        MappingConfigurationDTO mappingConf = new MappingConfigurationDTO();

        // Schemes with children
        List<SchemaClassificationDTO> schemes = SectorUtil.getAllSectorSchemesAndClassification();
        schemes.forEach(schemaClassif -> {
            schemaClassif.children = getSectorsByScheme(schemaClassif.id);
        });
        mappingConf.allSchemes = schemes;

        // All Mappings
        mappingConf.sectorMapping = SectorUtil.getAllSectorMappings();
        if (mappingConf.sectorMapping != null && !mappingConf.sectorMapping.isEmpty()) {
            AmpSector srcSector = ((AmpSectorMapping) ((ArrayList<?>)mappingConf.sectorMapping).get(0)).getSrcSector();
            AmpSector dstSector = ((AmpSectorMapping) ((ArrayList<?>)mappingConf.sectorMapping).get(0)).getDstSector();

            Long srcSchemeId = srcSector.getAmpSecSchemeId().getAmpSecSchemeId();
            if (srcSchemeId != null) {
                mappingConf.srcSchemeSector = findSchemeById(mappingConf.allSchemes, srcSchemeId);
            }

            Long dstSchemeId = dstSector.getAmpSecSchemeId().getAmpSecSchemeId();
            if (dstSchemeId != null) {
                mappingConf.dstSchemeSector = findSchemeById(mappingConf.allSchemes, dstSchemeId);
            }
        }

        return mappingConf;
    }

    //region Private methods
    private static AmpClassificationConfiguration findByName(List<AmpClassificationConfiguration> list, String name) {
        for (AmpClassificationConfiguration item : list) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    private static GenericSelectObjDTO findSchemeById(List<SchemaClassificationDTO> list, Long idScheme) {
        List<SchemaClassificationDTO> filteredSchemes = list.stream()
                .filter(scheme -> scheme.id.equals(idScheme))
                .collect(Collectors.toList());

        if (filteredSchemes != null && !filteredSchemes.isEmpty()) {
            SchemaClassificationDTO scheme = filteredSchemes.get(0);
            return new GenericSelectObjDTO(scheme.id, scheme.value);
        } else return null;
    }

    //endregion
}
