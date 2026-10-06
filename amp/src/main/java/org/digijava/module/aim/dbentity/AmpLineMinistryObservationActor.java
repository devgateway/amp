package org.digijava.module.aim.dbentity;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;

import java.io.Serializable;

public class AmpLineMinistryObservationActor implements Serializable, Cloneable {

    //IATI-check: to be ignored

    @InterchangeableId
    @Interchangeable(fieldTitle = "ID")
    private Long ampLineMinistryObservationActorId;

    @Interchangeable(fieldTitle = "Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private String name;

    private String nameTrimmed;

    @InterchangeableBackReference
    private AmpLineMinistryObservationMeasure measure;

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
