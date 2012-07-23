/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionsSumComparatorValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpTransactionTypeDonorFundingDetailModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorCommitmentsFormTableFeature extends
		AmpDonorFormTableFeaturePanel {

	private boolean alertIfDisbursmentBiggerCommitments = false;
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param ampDonorCommitmentsSubsectionFeature 
	 * @throws Exception
	 */
	public AmpDonorCommitmentsFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName) throws Exception {
		super(id, model, fmName, Constants.COMMITMENT, 7);

		
		 if( FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_DISBURSMENT_BIGGER_COMMITMENTS).equalsIgnoreCase("TRUE"))
		    alertIfDisbursmentBiggerCommitments = true;

		AbstractReadOnlyModel<List<AmpFundingDetail>> setAmountListModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);
		AbstractReadOnlyModel<List<AmpFundingDetail>> disbursementModel =  OnePagerUtil
				.getReadOnlyListModelFromSetModel(new AmpTransactionTypeDonorFundingDetailModel(parentModel, Constants.DISBURSEMENT));
		
		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		
		final AmpCollectionsSumComparatorValidatorField amountSumComparator=
				new AmpCollectionsSumComparatorValidatorField("amountSumComparator",setAmountListModel,"checkCommitmentSum", "AmpCommitmentsCollectionsSumComparatorValidator"); 
		wmc.add(amountSumComparator.getIndicatorAppender());
		amountSumComparator.setSecondCollectionModel(disbursementModel);
		amountSumComparator.setAlertIfCurrentModelAmountSumBig(false);
		add(amountSumComparator);			
			
		
		
		list = new ListEditor<AmpFundingDetail>("listCommitments", setModel, new AmpFundingDetail.FundingDetailComparator()) {
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFundingDetail> item) {
				item.add(getAdjustmentTypeComponent(item.getModel()));

				AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());

				if(alertIfDisbursmentBiggerCommitments)
					amountComponent.setAmountValidator(amountSumComparator); 	
				item.add(amountComponent);

				IModel<List<FundingPledges>> pledgesModel = new LoadableDetachableModel<List<FundingPledges>>() {
					protected java.util.List<FundingPledges> load() {
						return PledgesEntityHelper
								.getPledgesByDonorGroup(model.getObject()
										.getAmpDonorOrgId().getOrgGrpId().getAmpOrgGrpId()); 
					};
				};

				AmpTextFieldPanel<Double> exchangeRate = new AmpTextFieldPanel<Double>("exchangeRate",
						new PropertyModel<Double>(item.getModel(), "fixedExchangeRate"), "Exchange Rate",true,true);

				exchangeRate.getTextContainer().add(new MinimumValidator<Double>(0.001d));
				exchangeRate.getTextContainer().add(new AttributeModifier("size", true, new Model<String>("6")));
				item.add(exchangeRate);

				item.add(new AmpSelectFieldPanel<FundingPledges>("pledge",
						new PropertyModel<FundingPledges>(item.getModel(),
								"pledgeid"), pledgesModel,
								"Pledges", true, true, new ChoiceRenderer<FundingPledges>() {
					@Override
					public Object getDisplayValue(FundingPledges arg0) {
						return arg0.getTitle();
					}
				}));
				item.add(new ListEditorRemoveButton("delCommitment", "Delete Commitment"){
					protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
						AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
						super.onClick(target);
						parent.getFundingInfo().checkChoicesRequired(list.getCount());
						target.add(parent.getFundingInfo());
					};
				});
			}
		};
		add(list);
	}
}
