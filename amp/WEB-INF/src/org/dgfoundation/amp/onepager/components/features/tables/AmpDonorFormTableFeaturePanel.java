/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;


import java.util.Set;


import org.apache.log4j.Logger;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.models.AmpTransactionTypeDonorFundingDetailModel;
import org.digijava.module.aim.action.EditActivity;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author mpostelnicu@dgateway.org since Nov 12, 2010
 */
public abstract class AmpDonorFormTableFeaturePanel extends
	AmpFundingFormTableFeaturePanel<AmpFunding, AmpFundingDetail> {

	 private static Logger logger = Logger.getLogger(AmpDonorFormTableFeaturePanel.class);
	
	protected IModel<Set<AmpFundingDetail>> parentModel;
	protected IModel<Set<AmpFundingDetail>> setModel;
	
	public AmpFundingItemFeaturePanel getParentFundingItem() {
		AmpFundingItemFeaturePanel parent=(AmpFundingItemFeaturePanel) this.getParent().getParent();
		return parent;
	}

	public AmpDonorFormTableFeaturePanel(String id,
			final IModel<AmpFunding> model, String fmName, int transactionType,
			int titleHeaderColSpan) throws Exception {
		super(id, model, fmName);

		getTableId().add(new SimpleAttributeModifier("width", "620"));
		
		setTitleHeaderColSpan(titleHeaderColSpan);
		parentModel = new PropertyModel<Set<AmpFundingDetail>>(model,
				"fundingDetails");

		setModel = new AmpTransactionTypeDonorFundingDetailModel(parentModel, transactionType);
	}


	protected AmpCategoryGroupFieldPanel getAdjustmentTypeComponent(
			IModel<AmpFundingDetail> model) {
		try{
		
			AmpCategoryGroupFieldPanel adjustmentTypes = new AmpCategoryGroupFieldPanel(
				"adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
						new PropertyModel<AmpCategoryValue>(model,"adjustmentType"),
						CategoryConstants.ADJUSTMENT_TYPE_NAME, //fmname
						 false, false, true);
		adjustmentTypes.getChoiceContainer().setRequired(true);
		return adjustmentTypes;
		}catch(Exception e)
		{
			logger.error("AmpCategoryGroupFieldPanel initialization failed");
		}
		return null;

		
	}

	protected AmpFundingAmountComponent getFundingAmountComponent(
			IModel<AmpFundingDetail> model) {
		return new AmpFundingAmountComponent<AmpFundingDetail>("fundingAmount",
				model, "Amount", "transactionAmount", "Currency",
				"ampCurrencyId", "Transaction Date", "transactionDate");
	}

	
	/**
	 * Deprecated
	 * 
	protected AmpDeleteLinkField getDeleteLinkField(String id, String fmName,
			final ListItem<AmpFundingDetail> item) {
		return new AmpDeleteLinkField(id, fmName) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				parentModel.getObject().remove(item.getModelObject());
				target.addComponent(AmpDonorFormTableFeaturePanel.this);
				list.removeAll();
				target.addComponent(getParentFundingItem().getFundingInfo());
				target.appendJavascript(OnePagerUtil.getToggleChildrenJS(getParentFundingItem().getFundingInfo()));
			}
		};
	}
	 *	
	 */
}
