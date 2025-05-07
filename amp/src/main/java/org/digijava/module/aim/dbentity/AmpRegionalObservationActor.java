package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

import java.io.Serializable;
@TranslatableClass (displayName = "Regional Observation Actor")
public class AmpRegionalObservationActor implements Serializable, Cloneable {

    //IATI-check: to be ignored
//  @Interchangeable(fieldTitle="ID")
    private Long ampRegionalObservationActorId;
    @TranslatableField
//  @Interchangeable(fieldTitle="Name")
    private String name;
//  @Interchangeable(fieldTitle="Name Trimmed")
    private String nameTrimmed;
//  @Interchangeable(fieldTitle="Measure")
    private AmpRegionalObservationMeasure measure;

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
