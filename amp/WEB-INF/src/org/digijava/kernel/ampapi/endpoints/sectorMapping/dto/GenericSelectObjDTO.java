package org.digijava.kernel.ampapi.endpoints.sectorMapping.dto;

import java.util.List;
public class GenericSelectObjDTO {
    public Long id;
    public String value;
    public List<GenericSelectObjDTO> children;

    public GenericSelectObjDTO(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public GenericSelectObjDTO(Long id, String value, List<GenericSelectObjDTO> children) {
        this.id = id;
        this.value = value;
        this.children = children;
    }

    public GenericSelectObjDTO() {
    }

}
