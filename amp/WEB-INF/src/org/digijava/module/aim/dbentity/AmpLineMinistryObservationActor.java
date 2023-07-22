package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_LINE_MINISTRY_OBS_ACTOR")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class AmpLineMinistryObservationActor implements Serializable, Cloneable {

    //IATI-check: to be ignored
    @Id
    @InterchangeableId
    @Interchangeable(fieldTitle = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_LINE_MINISTRY_OBS_ACTOR_seq")
    @SequenceGenerator(name = "AMP_LINE_MINISTRY_OBS_ACTOR_seq", sequenceName = "AMP_LINE_MINISTRY_OBS_ACTOR_seq", allocationSize = 1)
    @Column(name = "amp_line_ministry_obs_actor_id")
    private Long ampLineMinistryObservationActorId;
    @Interchangeable(fieldTitle = "Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @Column(name = "name", columnDefinition = "text")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @InterchangeableBackReference
    @JoinColumn(name = "amp_line_ministry_obs_measure_id", referencedColumnName = "amp_line_ministry_obs_measure_id")
    private AmpLineMinistryObservationMeasure measure;

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

    public Long getAmpLineMinistryObservationActorId() {
        return ampLineMinistryObservationActorId;
    }

    public void setAmpLineMinistryObservationActorId(Long ampLineMinistryObservationActorId) {
        this.ampLineMinistryObservationActorId = ampLineMinistryObservationActorId;
    }

    public AmpLineMinistryObservationMeasure getMeasure() {
        return measure;
    }

    public void setMeasure(AmpLineMinistryObservationMeasure measure) {
        this.measure = measure;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

}
