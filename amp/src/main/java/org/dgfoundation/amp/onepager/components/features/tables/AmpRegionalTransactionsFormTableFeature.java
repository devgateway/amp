/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;

import java.util.Set;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpRegionalTransactionsFormTableFeature extends
        AmpRegionalFormTableFeaturePanel {

    /**
     * @param id
     * @param model
     * @param fmName
     * @param transactionType 
     * @param cvLocationModel 
     * @throws Exception
     */
    public AmpRegionalTransactionsFormTableFeature(String id,
            final IModel<Set<AmpRegionalFunding>> model, String fmName, int transactionType, IModel<AmpCategoryValueLocations> cvLocationModel) throws Exception {
        super(id, model, fmName, transactionType, 6,cvLocationModel);

        list = new ListEditor<AmpRegionalFunding>("listTransactions", setModel) {
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpRegionalFunding> item) {
                item.add(getAdjustmentTypeComponent(item.getModel()));
                AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
                item.add(amountComponent);

                item.add(new ListEditorRemoveButton("delTransaction", "Delete Item"));
            }
        };
        add(list);
    }
}
