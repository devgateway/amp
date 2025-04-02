package org.dgfoundation.amp.onepager.models;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpActivityBudgetStructure;

public class AmpBudgetStructureModel implements IModel{

    private IModel<Set<AmpActivityBudgetStructure>> setModel;

    public AmpBudgetStructureModel(IModel<Set<AmpActivityBudgetStructure>> setModel) {
        this.setModel = setModel;
    }
    
    @Override
    public void detach() {
        setModel.detach();
    }

    @Override
    public Double getObject() {
        Double result = new Double(0);
            Set<AmpActivityBudgetStructure> set = setModel.getObject();
            for (AmpActivityBudgetStructure b: set){
                if(b!=null && b.getBudgetStructurePercentage()!=null)
                    result+=b.getBudgetStructurePercentage();
            }
            return result;
    }

    @Override
    public void setObject(Object object) {
    }

}
