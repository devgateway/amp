/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorDisbOrdersFormTableFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorDisbursementsFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.CurrencyUtil;

import java.util.*;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorDisbOrdersSubsectionFeature extends
        AmpSubsectionFeaturePanel<AmpFunding> {

    protected AmpDonorDisbOrdersFormTableFeature disbOrdersTableFeature;
    protected Random randomGenerator = new Random();
    protected AmpDonorDisbursementsSubsectionFeature disbursements;

    public AmpDonorDisbursementsSubsectionFeature getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(
            AmpDonorDisbursementsSubsectionFeature disbursements) {
        this.disbursements = disbursements;
    }

    /**
     * Refreshes the {@link AmpSelectFieldPanel}S from the
     * {@link AmpDonorDisbursementsFormTableFeature} that has the name
     * "disbOrderId" when the list of disbursement orders is changed
     * (added/deleted).
     * 
     * @param target
     */
    public void updateDisbOrderPickers(AjaxRequestTarget target) {
        Iterator<Component> iterator = disbursements.getDisbursementsTableFeature()
                .getEditorList().iterator();
        while (iterator.hasNext()) {
            ListItem<AmpFundingDetail> listItem = (ListItem<AmpFundingDetail>) iterator
                    .next();
            Component component = listItem.get("disbOrderId");
            target.add(component);
        }
    }

    /**
     * Generates a unique random int between 0 and 100, to populate
     * {@link AmpFundingDetail#getDisbOrderId()}
     * 
     * @param fundingDetails
     * @return
     */
    protected String generateNewDisbOrderId(
            Collection<AmpFundingDetail> fundingDetails) {
        String ret = null;
        boolean found = false;
        do {
            ret = Integer.toString(randomGenerator.nextInt(100));
            for (AmpFundingDetail ampFundingDetail : fundingDetails) {
                if (ret.equals(ampFundingDetail.getDisbOrderId())) {
                    found = true;
                    break;
                }
            }
        } while (found);
        return ret;
    }

    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpDonorDisbOrdersSubsectionFeature(String id,
            final IModel<AmpFunding> model, int transactionType)
            throws Exception {
        super(id, AmpFundingItemFeaturePanel.FM_NAME_BY_TRANSACTION_TYPE.get(transactionType), model);
        disbOrdersTableFeature = new AmpDonorDisbOrdersFormTableFeature(
                "disbOrdersTableFeature", model, "Disbursement Orders Table", transactionType);
        add(disbOrdersTableFeature);
        
        AmpAjaxLinkField addDisbOrder = new AmpAjaxLinkField("addDisbOrder",
                "Add Disbursement Order", "Add Disbursement Order") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpFundingDetail fd = new AmpFundingDetail();
                fd.setAmpFundingId(model.getObject());
                //fd.setTransactionAmount(0d);
                fd.setReportingDate(new Date(System.currentTimeMillis()));
                fd.setUpdatedDate(new Date(System.currentTimeMillis()));
        //      fd.setAdjustmentType(Constants.ACTUAL);
        //      fd.setTransactionDate(new Date(System.currentTimeMillis()));
                fd.setTransactionType(Constants.DISBURSEMENT_ORDER);
                fd.setAmpCurrencyId(CurrencyUtil.getWicketWorkspaceCurrency());
                Set fundingDetails = model.getObject().getFundingDetails();
                fd.setDisbOrderId(generateNewDisbOrderId(fundingDetails));
                disbOrdersTableFeature.getEditorList().addItem(fd);
                target.add(disbOrdersTableFeature);
                updateDisbOrderPickers(target);
                AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                parent.getFundingInfo().configureRequiredFields();
                target.add(parent.getFundingInfo());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
            }
        };
        addDisbOrder.setAffectedByFreezing(false);
        add(addDisbOrder);
    }

}
