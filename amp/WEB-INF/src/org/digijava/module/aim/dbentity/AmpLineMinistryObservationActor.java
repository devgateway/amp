package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
@TranslatableClass (displayName = "Line Ministry Observation Actor")
public class AmpLineMinistryObservationActor implements Serializable, Cloneable {

    //IATI-check: to be ignored
    
//  @Interchangeable(fieldTitle="ID")
    private Long ampLineMinistryObservationActorId;
//  @Interchangeable(fieldTitle="Name")
    @TranslatableField
    private String name;
//  @Interchangeable(fieldTitle="Name Trimmed")
    private String nameTrimmed;
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
