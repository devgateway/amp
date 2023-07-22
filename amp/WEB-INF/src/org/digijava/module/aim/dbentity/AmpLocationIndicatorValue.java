package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "AMP_LOCATION_INDICATOR_VALUE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class AmpLocationIndicatorValue implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_LOCATION_INDICATOR_VALUE_seq")
    @SequenceGenerator(name = "AMP_LOCATION_INDICATOR_VALUE_seq", sequenceName = "AMP_LOCATION_INDICATOR_VALUE_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private AmpCategoryValueLocations location;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "indicator_id", referencedColumnName = "id", nullable = false)
    private AmpIndicatorLayer indicator;

    @Column(name = "value")
    private Double value;
    
    public AmpCategoryValueLocations getLocation() {
        return location;
    }
    public void setLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }
    public AmpIndicatorLayer getIndicator() {
        return indicator;
    }
    public void setIndicator(AmpIndicatorLayer indicator) {
        this.indicator = indicator;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    

}
