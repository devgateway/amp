/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
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
	 * @throws Exception
	 */
	public AmpDonorExpendituresFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName) throws Exception {
		super(id, model, fmName, Constants.EXPENDITURE, 6);

		AbstractReadOnlyModel<List<AmpFundingDetail>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel,new AmpFundingDetail.FundingDetailComparator());

		list = new ListView<AmpFundingDetail>("listExp", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpFundingDetail> item) {

				item.add(getAdjustmentTypeComponent(item.getModel()));
				item.add(getFundingAmountComponent(item.getModel()));
				
				AmpTextFieldPanel<String> classification = new AmpTextFieldPanel<String>(
						"classification", new PropertyModel<String>(
								item.getModel(), "expCategory"),
						"Expenditure Classification",true);
				classification.getTextContainer().add(new AttributeModifier("size", true, new Model<String>("12")));
				item.add(classification);
				
				item.add(getDeleteLinkField("delExp",
						"Delete Expenditure", item));

			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
