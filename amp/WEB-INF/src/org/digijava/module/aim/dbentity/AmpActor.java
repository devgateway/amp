package org.digijava.module.aim.dbentity ;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.SUBMIT;

import java.io.Serializable;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.validator.groups.Submit;

@TranslatableClass (displayName = "Actor")
public class AmpActor  implements Serializable, Cloneable, Identifiable {
    //IATI-check: not used in IATI

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampActorId;

    @Interchangeable(fieldTitle = "Name", label = "Actor", importable = true,
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class))
    @TranslatableField
    private String name;

    private String nameTrimmed;

    @InterchangeableBackReference
    private AmpMeasure measure;
    

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getAmpActorId() {
        return ampActorId;
    }
    public void setAmpActorId(Long ampActorId) {
        this.ampActorId = ampActorId;
    }
    
    public AmpMeasure getMeasure() {
        return measure;
    }
    public void setMeasure(AmpMeasure measure) {
        this.measure = measure;
    }
    public String getNameTrimmed() {
        return nameTrimmed;
    }
    public void setNameTrimmed(String name) {
        if (name == null)
            this.nameTrimmed = "";
        else this.nameTrimmed = name.replace(" ", "");
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public Object getIdentifier() {
        return ampActorId;
    }
}
