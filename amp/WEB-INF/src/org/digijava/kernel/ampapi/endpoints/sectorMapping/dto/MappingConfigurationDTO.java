package org.digijava.kernel.ampapi.endpoints.sectorMapping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.List;

public class MappingConfigurationDTO {

    @JsonProperty("src-scheme-sector")
    public GenericSelectObjDTO srcSchemeSector;

    @JsonProperty("dst-scheme-sector")
    public GenericSelectObjDTO dstSchemeSector;

    @JsonProperty("all-schemes")
    public List<SchemaClassificationDTO> allSchemes;

    @JsonProperty("sector-mapping")
    public Collection sectorMapping;

    public MappingConfigurationDTO(){}

    public MappingConfigurationDTO(GenericSelectObjDTO srcSchemeSector, GenericSelectObjDTO dstSchemeSector,
                                   List<SchemaClassificationDTO> allSchemes, Collection sectorMapping) {
        this.srcSchemeSector = srcSchemeSector;
        this.dstSchemeSector = dstSchemeSector;
        this.allSchemes = allSchemes;
        this.sectorMapping = sectorMapping;
    }
}
