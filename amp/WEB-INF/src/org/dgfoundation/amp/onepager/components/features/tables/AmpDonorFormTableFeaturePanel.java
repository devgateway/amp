/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorCommitmentsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoModel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoRenderer;
import org.dgfoundation.amp.onepager.models.AmpTransactionTypeDonorFundingDetailModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author mpostelnicu@dgateway.org since Nov 12, 2010
 */
public abstract class AmpDonorFormTableFeaturePanel extends
		AmpFormTableFeaturePanel<AmpFunding, AmpFundingDetail> {

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

	protected AmpGroupFieldPanel<MetaInfo<Integer>> getAdjustmentTypeComponent(
			IModel<AmpFundingDetail> model) {
		return new AmpGroupFieldPanel<MetaInfo<Integer>>("adjustmentType",
				new AmpMetaInfoModel<Integer>(new PropertyModel<Integer>(model,
						"adjustmentType"), OnePagerConst.adjustmentTypes),
				Arrays.asList(OnePagerConst.adjustmentTypes),
				"Adjustment Type", true, false,
				new AmpMetaInfoRenderer<Integer>());
	}

	protected AmpFundingAmountComponent getFundingAmountComponent(
			IModel<AmpFundingDetail> model) {
		return new AmpFundingAmountComponent<AmpFundingDetail>("fundingAmount",
				model, "Amount", "transactionAmount", "Currency",
				"ampCurrencyId", "Transaction Date", "transactionDate");
	}

	protected AmpDeleteLinkField getDeleteLinkField(String id, String fmName,
			final ListItem<AmpFundingDetail> item) {
		return new AmpDeleteLinkField(id, fmName) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				parentModel.getObject().remove(item.getModelObject());
				target.addComponent(AmpDonorFormTableFeaturePanel.this);
				list.removeAll();
				target.addComponent(getParentFundingItem().getFundingInfo());
			}
		};
	}
}
