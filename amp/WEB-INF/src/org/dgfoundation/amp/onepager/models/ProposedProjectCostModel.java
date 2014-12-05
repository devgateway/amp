package org.dgfoundation.amp.onepager.models;
 
import java.util.Set;
 
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
 
public class ProposedProjectCostModel implements IModel{
 
        private IModel<Double> totalsModel;
        private IModel<Set<AmpAnnualProjectBudget>> setModel;
 
        public ProposedProjectCostModel(IModel<Double> totalsModel, IModel<Set<AmpAnnualProjectBudget>> setModel) {
                this.totalsModel = totalsModel;
                this.setModel = setModel;
        }
        
        @Override
        public void detach() {
        }
 
        @Override
        public Double getObject() {
                Double result = new Double(0);
                if (setModel == null || setModel.getObject() == null || setModel.getObject().size() == 0)
                		//if the set is empty we should return 0
                        return null;
                else{
                        Set<AmpAnnualProjectBudget> set = setModel.getObject();
                        for (AmpAnnualProjectBudget b: set){
                                result+=b.getAmount();
                        }
                        return result;
                }
        }
 
        @Override
        public void setObject(Object object) {
                totalsModel.setObject(getObject());
        }
 
}