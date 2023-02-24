/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpEUAmountsComponent;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * @author aartimon@dginternational.org
 * @since Feb 8, 2011
 */
public class AmpContractFundingAllocationSubsectionFeature extends
        AmpSubsectionFeaturePanel<IPAContract> {
    
    private static Logger logger = Logger.getLogger(AmpContractFundingAllocationSubsectionFeature.class);

    public AmpContractFundingAllocationSubsectionFeature(String id,
            final IModel<IPAContract> model, String fmName){
        super(id, fmName, model);
        
        
        AmpTextFieldPanel<Double> totalValue = new AmpTextFieldPanel<Double>("totalValue", new PropertyModel<Double>(model, "contractTotalValue"), "Contract Total Value");
        add(totalValue);
        
        AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
            @Override
            public List<AmpCurrency> getObject() {
                return (List<AmpCurrency>) CurrencyUtil.getActiveAmpCurrencyByCode();
            }
        };
        
        AmpSelectFieldPanel<AmpCurrency> valueCurrency = new AmpSelectFieldPanel<AmpCurrency>("valueCurrency",
                new PropertyModel<AmpCurrency>(model, "totalAmountCurrency"),
                currencyList,
                "Currency", false, false);
        add(valueCurrency);
        
        add(new AmpEUAmountsComponent("EUAmounts", model, "EU Amounts"));

    }

}
