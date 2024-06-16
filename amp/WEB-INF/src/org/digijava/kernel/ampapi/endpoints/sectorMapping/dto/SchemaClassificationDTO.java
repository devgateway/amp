package org.digijava.kernel.ampapi.endpoints.sectorMapping.dto;

import java.util.List;

public class SchemaClassificationDTO {
    public Long id;
    public String value;
    public Long classificationId;
    public String classificationName;
    public List<GenericSelectObjDTO> children;

    public boolean isVisible() {
        return classificationId != null;
    }
}
