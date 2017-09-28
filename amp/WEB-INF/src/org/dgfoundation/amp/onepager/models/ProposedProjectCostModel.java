package org.dgfoundation.amp.onepager.models;
 
import java.util.Date;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.util.DecimalWraper;
 
public class ProposedProjectCostModel implements IModel{
 
        private IModel<Double> totalsModel;
        private IModel<Set<AmpAnnualProjectBudget>> setModel;
        private IModel<String> currencyModel;

        public ProposedProjectCostModel(IModel<String> currencyModel,IModel<Double> totalsModel, IModel<Set<AmpAnnualProjectBudget>> setModel) {
                this.totalsModel = totalsModel;
                this.setModel = setModel;
                this.currencyModel = currencyModel;

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
                            if(b.getYear()!=null && b.getAmpCurrencyId()!=null && b.getAmount()!=null && currencyModel.getObject()!=null){ 
                                result+=doCalculations(b.getAmount(),currencyModel.getObject(),b.getYear(),b.getAmpCurrencyId().getCurrencyCode()).doubleValue();
                            }                        }
                        return result;
                }
        }
 
        @Override
        public void setObject(Object object) {
                totalsModel.setObject(getObject());
        }
        //toCurrCode is the currency of the total
      //frmCurrCode is the currency of each annual item
        private DecimalWraper doCalculations(Double ammount,String toCurrCode,Date date,String frmCurrCode) {
            java.sql.Date dt = new java.sql.Date(date.getTime());

        
            double frmExRt;
                frmExRt = Util.getExchange(frmCurrCode, dt);
            double toExRt;

            if (frmCurrCode.equalsIgnoreCase(toCurrCode)) {
                toExRt = frmExRt;
            } else {
                toExRt = Util.getExchange(toCurrCode, dt);
            }
            return CurrencyWorker.convertWrapper(ammount, frmExRt, toExRt, dt);

        }
}
