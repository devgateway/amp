package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_REG_OBS_ACTOR")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TranslatableClass (displayName = "Regional Observation Actor")
public class AmpRegionalObservationActor implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_REG_OBS_ACTOR_seq")
    @SequenceGenerator(name = "AMP_REG_OBS_ACTOR_seq", sequenceName = "AMP_REG_OBS_ACTOR_seq", allocationSize = 1)
    @Column(name = "amp_reg_obs_actor_id")
    private Long ampRegionalObservationActorId;

    @Column(name = "name", columnDefinition = "text")
    @TranslatableField
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_reg_obs_measure_id", referencedColumnName = "amp_reg_obs_measure_id")
    private AmpRegionalObservationMeasure measure;
    private String nameTrimmed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameTrimmed() {
        return name.replace(" ", "");
    }

    public void setNameTrimmed(String nameTrimmed) {
        this.nameTrimmed = nameTrimmed;
    }

    public Long getAmpRegionalObservationActorId() {
        return ampRegionalObservationActorId;
    }

    public void setAmpRegionalObservationActorId(Long ampRegionalObservationActorId) {
        this.ampRegionalObservationActorId = ampRegionalObservationActorId;
    }

    public AmpRegionalObservationMeasure getMeasure() {
        return measure;
    }

    public void setMeasure(AmpRegionalObservationMeasure measure) {
        this.measure = measure;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

}
