/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpBooleanChoiceField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
@SuppressWarnings("serial")
public class AmpDonorCommitmentsFormTableFeature extends
		AmpDonorFormTableFeaturePanel {

	private boolean alertIfDisbursmentBiggerCommitments = false;
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	@SuppressWarnings("serial")
	public AmpDonorCommitmentsFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
		super(id, model, fmName, Constants.COMMITMENT, 7);

		list = new ListEditor<AmpFundingDetail>("listCommitments", setModel, new AmpFundingDetail.FundingDetailComparator()) {
			@Override
			protected void onPopulateItem(
					ListItem<AmpFundingDetail> item) {
				item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));

				AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
				item.add(amountComponent);

                IModel<List<FundingPledges>> pledgesModel = new LoadableDetachableModel<List<FundingPledges>>() {
					protected java.util.List<FundingPledges> load() {
						return PledgesEntityHelper
								.getPledgesByDonorGroup(model.getObject()
										.getAmpDonorOrgId().getOrgGrpId().getAmpOrgGrpId());
					};
				};

				appendFixedExchangeRateToItem(item);

				item.add(new AmpSelectFieldPanel<FundingPledges>("pledge",
						new PropertyModel<FundingPledges>(item.getModel(),
								"pledgeid"), pledgesModel,
								"Pledges", false, true, new ChoiceRenderer<FundingPledges>() {
					@Override
					public Object getDisplayValue(FundingPledges arg0) {
						return arg0.getEffectiveName();
					}
				}, false));
				item.add(new ListEditorRemoveButton("delCommitment", "Delete Commitment"){
					protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
						AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
						super.onClick(target);
						parent.getFundingInfo().checkChoicesRequired(list.getCount());
						target.add(parent.getFundingInfo());
						target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
						target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
					};
				});
				//we create the role selector for recipient organization for commitments
				item.add(OnePagerUtil.getFundingFlowRoleSelector(model, item.getModel()));
				
				//disaster response marker
				final AmpBooleanChoiceField disasterResponse = new AmpBooleanChoiceField("disasterResponse", new PropertyModel<Boolean>(
						item.getModel(), "disasterResponse"),"Disaster Response");
				item.add(getDisasterValidator(disasterResponse));

				item.add(disasterResponse);	
			}

			
		};
		add(list);
	}

	@Override
	protected void enableFixedRateOnAjaxOnUpdate(AjaxRequestTarget target) {
		onFundingDetailChanged(target);
	}

	@Override
	protected void exchangeRateOnAjaxOnUpdate(AjaxRequestTarget target) {
		onFundingDetailChanged(target);
	}

	private void onFundingDetailChanged(AjaxRequestTarget target) {
		AmpComponentPanel parentPanel = findParent(AmpFundingItemFeaturePanel.class);
		parentPanel.visitChildren(AmpCollectionValidatorField.class, new IVisitor<AmpCollectionValidatorField, Void>() {
			@Override
			public void component(AmpCollectionValidatorField component,
								  IVisit<Void> visit) {
				component.reloadValidationField(target);
				visit.dontGoDeeper();
			}
		});

		send(getPage(), Broadcast.BREADTH, new OverallFundingTotalsEvents(target));
	}
}
