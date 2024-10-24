/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.FundingListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpBooleanChoiceField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.FreezingUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetailComparator;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorDisbursementsFormTableFeature extends
        AmpDonorFormTableFeaturePanel {
    private static final long serialVersionUID = 1L;
    private final static int SELECTOR_SIZE = 80;
    private final static int DISBURSEMENT_ID_MAX_LENGTH = 255;
    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpDonorDisbursementsFormTableFeature(String id,
            final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
        super(id, model, fmName, Constants.DISBURSEMENT, 8);
    
        final AbstractReadOnlyModel<List<String>> disbOrderIdModel = new AbstractReadOnlyModel<List<String>>() {
            @Override
            public List<String> getObject() {
                List<String> ret=new ArrayList<String>(); 
                for (AmpFundingDetail ampFundingDetail : parentModel.getObject()) 
                    if(ampFundingDetail.getTransactionType().equals(Constants.DISBURSEMENT_ORDER)) ret.add(ampFundingDetail.getDisbOrderId());
                return ret;
            }
        };
        list = new FundingListEditor<AmpFundingDetail>("listDisbursements", setModel, FundingDetailComparator
                .getFundingDetailComparator()) {

            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpFundingDetail> item) {
                super.onPopulateItem(item);
                item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));
                addFreezingvalidator(item);
                item.add(UpdateEventBehavior.of(FreezingUpdateEvent.class));              
                AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
                item.add(amountComponent);
                AmpTextFieldPanel<Float> capitalSpendingPercentage = new AmpTextFieldPanel<Float>(
                                        "capitalSpendingPercentage",
                                        new PropertyModel<Float>(item.getModel(), "capitalSpendingPercentage"), "Capital Spending Percentage", false, false);
                capitalSpendingPercentage.getTextContainer().add(new RangeValidator<Float>(0f, 100f));
                capitalSpendingPercentage.getTextContainer().add(new AttributeModifier("size", new Model<String>("5")));
                item.add(capitalSpendingPercentage);

                AmpSelectFieldPanel<String> disbOrdIdSelector = new AmpSelectFieldPanel<String>("disbOrderId",
                        new PropertyModel<String>(item.getModel(),
                                "disbOrderId")
                                ,disbOrderIdModel,
                        "Disbursement Order Id", false, true, null, false);
                disbOrdIdSelector.getChoiceContainer().add(new AttributeAppender("style", new Model<String>("width: "+SELECTOR_SIZE+"px")));
                item.add(disbOrdIdSelector);

                AmpTextFieldPanel<String> disbursementId = new AmpTextFieldPanel<String>("disbursementId",
                        new PropertyModel<>(item.getModel(), "disbursementId"), "Disbursement Id", false, true);
                disbursementId.setTextContainerMaxSize(DISBURSEMENT_ID_MAX_LENGTH);
                item.add(disbursementId);
                
                ArrayList<IPAContract> contractList;
                if (model.getObject().getAmpActivityId() != null && model.getObject().getAmpActivityId().getContracts() != null)
                    contractList = new ArrayList<IPAContract>(model.getObject()
                        .getAmpActivityId().getContracts());
                else
                    contractList = new ArrayList<IPAContract>();
                AmpSelectFieldPanel<IPAContract> contractSelector = new AmpSelectFieldPanel<IPAContract>("contract",
                        new PropertyModel<IPAContract>(item.getModel(),
                                "contract"),
                        new Model<ArrayList<IPAContract>>(contractList),
                        "Contract", false, true, null, false);
                
                contractSelector.getChoiceContainer().add(new AttributeAppender("style", new Model<String>("width: "+SELECTOR_SIZE+"px")));
                item.add(contractSelector);
                
                IModel<List<FundingPledges>> pledgesModel = new LoadableDetachableModel<List<FundingPledges>>() {
                    protected java.util.List<FundingPledges> load() {
                        return PledgesEntityHelper
                                .getPledgesByDonorGroup(model.getObject()
                                .getAmpDonorOrgId().getOrgGrpId().getAmpOrgGrpId());
                    };
                };
                
                AmpSelectFieldPanel<FundingPledges> pledgeSelector = new AmpSelectFieldPanel<FundingPledges>("pledge",
                        new PropertyModel<FundingPledges>(item.getModel(),
                                "pledgeid"), pledgesModel,
                        "Pledges", false, true, new ChoiceRenderer<FundingPledges>() {
                            @Override
                            public Object getDisplayValue(FundingPledges arg0) {
                                return arg0.getEffectiveName();
                            }
                        }, false);
                pledgeSelector.getChoiceContainer().add(new AttributeAppender("style", new Model<String>("width: "+SELECTOR_SIZE+"px")));
                item.add(pledgeSelector);
                item.add(new ListEditorRemoveButton("delDisbursement", "Delete Disbursement"){
                    protected void onClick(final org.apache.wicket.ajax.AjaxRequestTarget target) {
                        AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                        super.onClick(target);
                        parent.getFundingInfo().configureRequiredFields();
                        target.add(parent.getFundingInfo());
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                        target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                        updateModel();
                    };
                });
                
                appendFixedExchangeRateToItem(item);
                
                item.add(OnePagerUtil.getFundingFlowRoleSelector(model, item.getModel()));
                
                final AmpBooleanChoiceField disasterResponse = new AmpBooleanChoiceField("disasterResponse", new PropertyModel<Boolean>(
                        item.getModel(), "disasterResponse"),"Disaster Response");
                item.add(getDisasterValidator(disasterResponse));

                item.add(disasterResponse);                             
            }
        };
        add(list);
        addExpandableList();
    }

}
