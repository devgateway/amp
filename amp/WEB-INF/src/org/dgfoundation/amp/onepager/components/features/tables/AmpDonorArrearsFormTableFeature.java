/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpBooleanChoiceField;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetailComparator;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
@SuppressWarnings("serial")
public class AmpDonorArrearsFormTableFeature extends
		AmpDonorFormTableFeaturePanel {

//	private boolean alertIfDisbursmentBiggerCommitments = false;
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	@SuppressWarnings("serial")
	public AmpDonorArrearsFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
		super(id, model, fmName, Constants.ARREARS, 7);

		list = new ListEditor<AmpFundingDetail>("listArrears", setModel, FundingDetailComparator
				.getFundingDetailComparator()) {
			@Override
			protected void onPopulateItem(
					ListItem<AmpFundingDetail> item) {
				item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));

				AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
				item.add(amountComponent);
				appendFixedExchangeRateToItem(item); 
				item.add(new ListEditorRemoveButton("delArrears", "Delete Arrears Transaction"){
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
			}
		};
		add(list);
	}
}
