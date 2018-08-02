package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

public class AmpStructureCoordinate implements Serializable, Cloneable {

    private static final long serialVersionUID = -6217182726089147778L;

    private Long ampStructureCoordinateId;

    @Interchangeable(fieldTitle = "Latitude", importable = true)
    private String latitude;

    @Interchangeable(fieldTitle = "Longitude", importable = true)
    private String longitude;

    private AmpStructure structure;

    public Long getAmpStructureCoordinateId() {
        return ampStructureCoordinateId;
    }

    public void setAmpStructureCoordinateId(Long ampStructureCoordinateId) {
        this.ampStructureCoordinateId = ampStructureCoordinateId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
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
