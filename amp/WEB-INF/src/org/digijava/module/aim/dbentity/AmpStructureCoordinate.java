package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_STRUCTURE_COORDINATE")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpStructureCoordinate implements Serializable, Cloneable {

    private static final long serialVersionUID = -6217182726089147778L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_STRUCTURE_COORDINATE_seq")
    @SequenceGenerator(name = "AMP_STRUCTURE_COORDINATE_seq", sequenceName = "AMP_STRUCTURE_COORDINATE_seq", allocationSize = 1)
    @Column(name = "amp_structure_coordinate_id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampStructureCoordinateId;

    @Column(name = "latitude", columnDefinition = "text")
    @Interchangeable(fieldTitle = "Latitude", importable = true)

    private String latitude;

    @Column(name = "longitude", columnDefinition = "text")
    @Interchangeable(fieldTitle = "Longitude", importable = true)

    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_structure_id")
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

}
