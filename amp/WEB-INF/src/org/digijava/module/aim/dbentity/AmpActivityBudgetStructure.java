package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;

public class AmpActivityBudgetStructure extends AbstractAuditLogger implements Versionable, Serializable, Cloneable {
    
    //TODO: not expected to be used in IATI. commenting for now
    private Long ampActivityBudgetStructureId;
//  @Interchangeable (fieldTitle ="Budget Structure Name", importable = true)
    private String budgetStructureName;
    
//  @Interchangeable (fieldTitle ="Budget Structure Percentage" ,fmPath="/Activity Form/Budget Structure/Budget Structure/budgetStructurePercentage", importable = true)
    //@Validators (percentage = "/Activity Form/Budget Structure/Budget Structure/programPercentageTotal")
    private Float budgetStructurePercentage;
    private AmpActivityVersion activity;
    
    public Long getAmpActivityBudgetStructureId() {
        return ampActivityBudgetStructureId;
    }
    public void setAmpActivityBudgetStructureId(
            Long ampActivityBudgetStructureId) {
        this.ampActivityBudgetStructureId = ampActivityBudgetStructureId;
    }
    public String getBudgetStructureName() {
        return budgetStructureName;
    }
    public void setBudgetStructureName(String budgetStructureName) {
        this.budgetStructureName = budgetStructureName;
    }
    public Float getBudgetStructurePercentage() {
        return budgetStructurePercentage;
    }
    public void setBudgetStructurePercentage(Float budgetStructurePercentage) {
        this.budgetStructurePercentage = budgetStructurePercentage;
    }
    public AmpActivityVersion getActivity() {
        return activity;
    }
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivityBudgetStructure aux = (AmpActivityBudgetStructure) obj;
        if (this.getBudgetStructureName().equals(aux.getBudgetStructureName())) {
            return true;
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(new Output(null, new String[] { "Name" }, new Object[] { this.getBudgetStructureName() }));
        out.getOutputs()
                .add(new Output(null, new String[] { "Percentage" }, new Object[] { this.getBudgetStructurePercentage() }));
        return out;
    }

    @Override
    public Object getValue() {
        String ret = "";
        ret = "" + this.budgetStructurePercentage;
        return ret;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpActivityBudgetStructure aux = (AmpActivityBudgetStructure) clone();
        aux.activity = newActivity;
        aux.setPreviousObjectId(aux.getAmpActivityBudgetStructureId());
        aux.ampActivityBudgetStructureId = null;
        
        return aux;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
    return super.clone();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((budgetStructureName == null) ? 0 : budgetStructureName
                        .hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AmpActivityBudgetStructure other = (AmpActivityBudgetStructure) obj;
        if (budgetStructureName == null) {
            if (other.budgetStructureName != null)
                return false;
        } else if (!budgetStructureName.equals(other.budgetStructureName))
            return false;
        return true;
    }
    
    

}
