package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.SectorPossibleValuesProvider;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.util.Output;

public class AmpActivitySector implements Versionable, Serializable, Cloneable {

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampActivitySectorId;

    @InterchangeableBackReference
    private AmpActivityVersion activityId;

    @PossibleValues(SectorPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Sector", importable = true, pickIdOnly = true, uniqueConstraint = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private AmpSector sectorId;
    
    @Interchangeable(fieldTitle="Sector Percentage", importable = true, percentageConstraint = true, 
            fmPath = FMVisibility.PARENT_FM + "/sectorPercentage",
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Float sectorPercentage;

    private AmpClassificationConfiguration classificationConfig;

    public AmpClassificationConfiguration getClassificationConfig() {
        return classificationConfig;
    }

    public void setClassificationConfig(AmpClassificationConfiguration classificationConfig) {
        this.classificationConfig = classificationConfig;
    }

    public Long getAmpActivitySectorId() {
        return ampActivitySectorId;
    }

    public void setAmpActivitySectorId(Long ampActivitySectorId) {
        this.ampActivitySectorId = ampActivitySectorId;
    }

    public AmpActivityVersion getActivityId() {
        return activityId;
    }

    public void setActivityId(AmpActivityVersion activityId) {
        this.activityId = activityId;
    }

    public AmpSector getSectorId() {
        return sectorId;
    }

    public void setSectorId(AmpSector sectorId) {
        this.sectorId = sectorId;
    }

    public Float getSectorPercentage() {
        return sectorPercentage;
    }

    public void setSectorPercentage(Float sectorPercentage) {
        this.sectorPercentage = sectorPercentage;
    }
    
    public String toString() {
        return sectorId!=null?sectorId.getName():"";
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivitySector aux = (AmpActivitySector) obj;
        if (this.classificationConfig.equals(aux.getClassificationConfig())
                && this.sectorId.getAmpSectorId().equals(aux.getSectorId().getAmpSectorId())) {
            return true;
        }
        return false;
    }
    
    public Object getValue() {
        return this.sectorPercentage;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        String scheme = "[" + this.classificationConfig.getClassification().getSecSchemeName() + "]";
        String name = "";
        if (this.sectorId.getParentSectorId() != null) {
            name = " - " + "[" + this.sectorId.getParentSectorId().toString() + "]";
            if (this.sectorId.getParentSectorId().getParentSectorId() != null) {
                name = " - " + "[" + this.sectorId.getParentSectorId().getParentSectorId().toString() + "]" + name;
                if (this.sectorId.getParentSectorId().getParentSectorId().getParentSectorId() != null) {
                    name = " - " + "["
                            + this.sectorId.getParentSectorId().getParentSectorId().getParentSectorId().toString()
                            + "]" + name;
                }
            } else {
                name += " - [" + this.sectorId.getName() + "]";
            }
        } else {
            name += " - [" + this.sectorId.getName() + "]";
        }
        out.getOutputs().add(new Output(null, new String[] { scheme + name + " - Percentage: "}, new Object[] { this.sectorPercentage }));
        return out;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpActivitySector aux = (AmpActivitySector) clone();
        aux.activityId = newActivity;
        aux.ampActivitySectorId = null;
        //aux.sectorId = (AmpSector) aux.sectorId.clone();
        //this.sectorId.setAmpSectorId(null);
        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    

}
