/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorExpendituresFormTableFeature extends
		AmpDonorFormTableFeaturePanel {
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param transactionType 
	 * @throws Exception
	 */
	public AmpDonorExpendituresFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
		super(id, model, fmName, Constants.EXPENDITURE, 6);
		
		list = new ListEditor<AmpFundingDetail>("listExp", setModel, new AmpFundingDetail.FundingDetailComparator()) {
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFundingDetail> item) {

				item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));
				final AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
				item.add(amountComponent);
				
				AmpTextFieldPanel<String> classification = new AmpTextFieldPanel<String>(
						"classification", new PropertyModel<String>(
								item.getModel(), "expCategory"),
						"Expenditure Classification", false, true);
				classification.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
				classification.setTextContainerDefaultMaxSize();
				item.add(classification);
				item.add(new ListEditorRemoveButton("delExp", "Delete Expenditure"){
					protected void onClick(final org.apache.wicket.ajax.AjaxRequestTarget target) {
						AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
						super.onClick(target);
						parent.getFundingInfo().checkChoicesRequired(list.getCount());
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
