package org.dgfoundation.amp.onepager.helper.structure;

import org.digijava.kernel.ampapi.endpoints.util.ObjectMapperUtils;

import java.util.List;

public class MapData {
    
    private StructureData structure;
    
    private List<ColorData> structureColors;
    
    public StructureData getStructure() {
        return structure;
    }
    
    public void setStructure(StructureData structure) {
        this.structure = structure;
    }
    
    public List<ColorData> getStructureColors() {
        return structureColors;
    }
    
    public void setStructureColors(List<ColorData> structureColors) {
        this.structureColors = structureColors;
    }
    
    public String asJsonString() {
        return ObjectMapperUtils.valueToString(this);
    }
    
}
