package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpStructureCoordinate  implements Serializable,  Cloneable {
 
    private static final long serialVersionUID = -6217182726089147778L;
    private Long ampStructureCoordinateId;     
    private Double latitude;
    private Double longitude;
    private AmpStructure structure;
    
    public Long getAmpStructureCoordinateId() {
        return ampStructureCoordinateId;
    }
    public void setAmpStructureCoordinateId(Long ampStructureCoordinateId) {
        this.ampStructureCoordinateId = ampStructureCoordinateId;
    }     
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public AmpStructure getStructure() {
        return structure;
    }
    public void setStructure(AmpStructure structure) {
        this.structure = structure;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
