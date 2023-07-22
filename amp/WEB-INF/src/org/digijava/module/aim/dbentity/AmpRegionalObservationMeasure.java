package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_REG_OBS_MEASURE")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TranslatableClass (displayName = "Regional Observation Measure")
public class AmpRegionalObservationMeasure implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_REG_OBS_MEASURE_seq")
    @SequenceGenerator(name = "AMP_REG_OBS_MEASURE_seq", sequenceName = "AMP_REG_OBS_MEASURE_seq", allocationSize = 1)
    @Column(name = "amp_reg_obs_measure_id")
    private Long ampRegionalObservationMeasureId;

    @Column(name = "name", columnDefinition = "text")
    @TranslatableField
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_regional_observation_id", referencedColumnName = "amp_regional_observation_id")
    private AmpRegionalObservation regionalObservation;

    @OneToMany(mappedBy = "measure", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpRegionalObservationActor> actors;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AmpRegionalObservationActor> getActors() {
        return actors;
    }

    public void setActors(Set<AmpRegionalObservationActor> actors) {
        this.actors = actors;
    }

    public Long getAmpRegionalObservationMeasureId() {
        return ampRegionalObservationMeasureId;
    }

    public void setAmpRegionalObservationMeasureId(Long ampRegionalObservationMeasureId) {
        this.ampRegionalObservationMeasureId = ampRegionalObservationMeasureId;
    }

    public AmpRegionalObservation getRegionalObservation() {
        return regionalObservation;
    }

    public void setRegionalObservation(AmpRegionalObservation regionalObservation) {
        this.regionalObservation = regionalObservation;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        AmpRegionalObservationMeasure aux = (AmpRegionalObservationMeasure) super.clone();
        aux.setAmpRegionalObservationMeasureId(null);
        
        if (aux.actors != null && aux.actors.size() > 0){
            Set<AmpRegionalObservationActor> set = new HashSet<AmpRegionalObservationActor>();
            Iterator<AmpRegionalObservationActor> i = aux.actors.iterator();
            while (i.hasNext()) {
                AmpRegionalObservationActor newActor = (AmpRegionalObservationActor) i.next().clone();
                newActor.setAmpRegionalObservationActorId(null);
                newActor.setMeasure(aux);
                set.add(newActor);
            }
            aux.actors = set;
        }
        else
            aux.actors = null;
        
        return aux;
    }
    
}
