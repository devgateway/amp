package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;

@TranslatableClass(displayName = "Regional Observation Actor")
public class AmpRegionalObservationActor implements Serializable, Cloneable, Identifiable {
    @InterchangeableId
    @Interchangeable(fieldTitle = "ID")
    private Long ampRegionalObservationActorId;
    @Interchangeable(fieldTitle = "Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @TranslatableField
    private String name;
    private String nameTrimmed;
    @InterchangeableBackReference
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

    @Override
    public Object getIdentifier() {
        return ampRegionalObservationActorId;
    }
}
