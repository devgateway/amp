/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.WildcardListModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.FundingListEditor;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.FreezingUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetailComparator;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorDisbOrdersFormTableFeature extends
        AmpDonorFormTableFeaturePanel {

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpDonorDisbOrdersFormTableFeature(String id,
            final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
        super(id, model, fmName, Constants.DISBURSEMENT_ORDER, 8);

        list = new FundingListEditor<AmpFundingDetail>("listDisbOrders", setModel, FundingDetailComparator
                .getFundingDetailComparator()) {
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpFundingDetail> item) {
                item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));
                addFreezingvalidator(item);
                item.add(getFundingAmountComponent(item.getModel()));
                item.add(UpdateEventBehavior.of(FreezingUpdateEvent.class));
                AmpTextFieldPanel<String> disbOrderId = new AmpTextFieldPanel<String>(
                        "disbOrderId", new PropertyModel<String>(
                                item.getModel(), "disbOrderId"),
                        "Disbursement Order ID",false, false);
                disbOrderId.setEnabled(false);
                disbOrderId.getTextContainer().add(new AttributeModifier("size", new Model<String>("5")));
                item.add(disbOrderId);
                
                ArrayList<IPAContract> contractList;
                if (model.getObject().getAmpActivityId() != null && model.getObject().getAmpActivityId().getContracts() != null)
                    contractList = new ArrayList<IPAContract>(model.getObject()
                        .getAmpActivityId().getContracts());
                else
                    contractList = new ArrayList<IPAContract>();

                item.add(new AmpSelectFieldPanel<IPAContract>("contract",
                        new PropertyModel<IPAContract>(item.getModel(),
                                "contract"),
                        new WildcardListModel<IPAContract>(contractList),
                        "Contract", false, true, null, false));

                AmpCheckBoxFieldPanel rejected = new AmpCheckBoxFieldPanel(
                        "rejected", new PropertyModel<Boolean>(item.getModel(),
                                "disbursementOrderRejected"), "Rejected", false, false);
                item.add(rejected);

                item.add(new ListEditorRemoveButton("delDisbOrder", "Delete Disbursement Order"){
                    protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
                        AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                        super.onClick(target);
                        parent.getFundingInfo().configureRequiredFields();
                        target.add(parent.getFundingInfo());
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                        target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                    };
                });
            }
        };
        add(list);

    }

}
