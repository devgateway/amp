package org.digijava.module.aim.dbentity ;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.SUBMIT;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.validator.groups.Submit;

@TranslatableClass (displayName = "Measure")
public class AmpMeasure  implements Serializable, Cloneable {
    //IATI-check: seems used only in issues -- commenting out (to be ignored).

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampMeasureId;

    @TranslatableField
    @Interchangeable(fieldTitle = "Name", label = "Measure", importable = true,
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class))
    private String name ;

    @InterchangeableBackReference
    private AmpIssues issue;

    @Interchangeable(fieldTitle = "Actors", importable = true,
            fmPath = "/Activity Form/Issues Section/Issue/Measure/Actor")
    private Set<AmpActor> actors = new HashSet<>();

    @Interchangeable(fieldTitle = "Measure Date", importable = true,
            fmPath = "/Activity Form/Issues Section/Issue/Measure/Date")
    private Date measureDate;
    
    
    public Date getMeasureDate() {
        return measureDate;
    }
    public void setMeasureDate(Date measureDate) {
        this.measureDate = measureDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getAmpMeasureId() {
        return ampMeasureId;
    }
    public void setAmpMeasureId(Long ampMeasureId) {
        this.ampMeasureId = ampMeasureId;
    }
    
    public AmpIssues getIssue() {
        return issue;
    }
    public void setIssue(AmpIssues issue) {
        this.issue = issue;
    }

    public Set<AmpActor> getActors() {
        return actors;
    }
    public void setActors(Set<AmpActor> actors) {
        this.actors = actors;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

}
