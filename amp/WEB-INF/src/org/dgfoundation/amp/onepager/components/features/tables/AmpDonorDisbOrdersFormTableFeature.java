/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorDisbOrdersSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.Constants;

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
			final IModel<AmpFunding> model, String fmName) throws Exception {
		super(id, model, fmName, Constants.DISBURSEMENT_ORDER, 8);

		AbstractReadOnlyModel<List<AmpFundingDetail>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel,new AmpFundingDetail.FundingDetailComparator());

		list = new ListView<AmpFundingDetail>("listDisbOrders", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpFundingDetail> item) {

				item.add(getAdjustmentTypeComponent(item.getModel()));
				item.add(getFundingAmountComponent(item.getModel()));

				AmpTextFieldPanel<String> disbOrderId = new AmpTextFieldPanel<String>(
						"disbOrderId", new PropertyModel<String>(
								item.getModel(), "disbOrderId"),
						"Disbursement Order ID",true);
				disbOrderId.setEnabled(false);
				disbOrderId.getTextContainer().add(new AttributeModifier("size", true, new Model<String>("5")));
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
						contractList,
						"Contract", true, true));

				AmpCheckBoxFieldPanel rejected = new AmpCheckBoxFieldPanel(
						"rejected", new PropertyModel<Boolean>(item.getModel(),
								"disbursementOrderRejected"), "Rejected", true);
				item.add(rejected);

				AmpDeleteLinkField delDisbOrder = new AmpDeleteLinkField(
						"delDisbOrder", "Delete Disbursement Order") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						parentModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpDonorDisbOrdersFormTableFeature.this);
						list.removeAll();
						AmpDonorDisbOrdersSubsectionFeature subsection=(AmpDonorDisbOrdersSubsectionFeature) AmpDonorDisbOrdersFormTableFeature.this.getParent();
						subsection.updateDisbOrderPickers(target);
					}
				};

				item.add(delDisbOrder);

			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
