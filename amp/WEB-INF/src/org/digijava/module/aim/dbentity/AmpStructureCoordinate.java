package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;

import java.io.Serializable;
import java.util.Objects;

public class AmpStructureCoordinate implements Serializable, Cloneable {

    private static final long serialVersionUID = -6217182726089147778L;

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampStructureCoordinateId;

    @Interchangeable(fieldTitle = "Latitude", importable = true)
    private String latitude;

    @Interchangeable(fieldTitle = "Longitude", importable = true)
    private String longitude;

    @InterchangeableBackReference
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmpStructureCoordinate that = (AmpStructureCoordinate) o;
        return Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(structure.getAmpStructureId(), that.structure.getAmpStructureId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, structure.getAmpStructureId());
    }
}