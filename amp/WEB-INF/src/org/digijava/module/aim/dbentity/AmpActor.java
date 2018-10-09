package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

@TranslatableClass (displayName = "Actor")
public class AmpActor  extends AbstractAuditLogger implements Serializable, Cloneable
{
    //IATI-check: not used in IATI
//  @Interchangeable(fieldTitle="ID", importable = true)
    private Long ampActorId ;
//  @Interchangeable(fieldTitle="Name", importable = true)
    @TranslatableField
    private String name;
//  @Interchangeable(fieldTitle="Name Trimmed", importable = true)
    private String nameTrimmed;
//  @Interchangeable(fieldTitle="Measure")
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
    

}
