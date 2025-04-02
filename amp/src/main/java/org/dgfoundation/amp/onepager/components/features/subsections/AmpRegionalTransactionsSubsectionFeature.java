/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpRegionalTransactionsFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpRegionalTransactionsSubsectionFeature extends
        AmpSubsectionFeaturePanel<Set<AmpRegionalFunding>> {

    protected AmpRegionalTransactionsFormTableFeature transactionsTableFeature;

    /**
     * @param id
     * @param am 
     * @param fmName
     * @param model
     * @param cvLocationModel 
     * @param cvlocationModel 
     * @throws Exception
     */
    public AmpRegionalTransactionsSubsectionFeature(String id,
            final IModel<AmpActivityVersion> am, final IModel<Set<AmpRegionalFunding>> model, String fmName,
            final int transactionType, final IModel<AmpCategoryValueLocations> cvLocationModel) throws Exception {
        super(id, fmName, model);
        transactionsTableFeature = new AmpRegionalTransactionsFormTableFeature(
                "transactionsTableFeature", model, fmName + " Table",
                transactionType,cvLocationModel);
        add(transactionsTableFeature);

        AmpAjaxLinkField addCommit = new AmpAjaxLinkField("addTransaction",
                "Add Transaction", "Add Transaction") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpRegionalFunding fd = new AmpRegionalFunding();
                //fd.setTransactionAmount(0d);
                //fd.setAdjustmentType(Constants.ACTUAL);
                //fd.setTransactionDate(new Date(System.currentTimeMillis()));
                fd.setTransactionType(transactionType);
                fd.setRegionLocation(cvLocationModel.getObject());
                fd.setActivity(am.getObject());
                fd.setCurrency(CurrencyUtil.getWicketWorkspaceCurrency());
                transactionsTableFeature.getListEditor().addItem(fd);
                target.add(transactionsTableFeature);
            }
        };
        add(addCommit);
    }

}
