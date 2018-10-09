package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

@TranslatableClass (displayName = "Measure")
public class AmpMeasure extends AbstractAuditLogger implements Serializable, Cloneable
{
    //IATI-check: seems used only in issues -- commenting out (to be ignored).
    
//  @Interchangeable(fieldTitle="ID")
    private Long ampMeasureId ;
    @TranslatableField
//  @Interchangeable(fieldTitle="Name")
    private String name ;
    private AmpIssues issue;
//  @Interchangeable(fieldTitle="Actors",fmPath="/Activity Form/Issues Section/Issue/Measure/Actor")
    private Set<AmpActor> actors;
//  @Interchangeable(fieldTitle="Measure Date",fmPath="/Activity Form/Issues Section/Issue/Date")
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
