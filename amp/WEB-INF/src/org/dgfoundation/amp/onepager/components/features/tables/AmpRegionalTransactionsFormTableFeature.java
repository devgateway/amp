/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.helper.Constants;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpRegionalTransactionsFormTableFeature extends
		AmpRegionalFormTableFeaturePanel {

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param transactionType 
	 * @param cvLocationModel 
	 * @throws Exception
	 */
	public AmpRegionalTransactionsFormTableFeature(String id,
			final IModel<Set<AmpRegionalFunding>> model, String fmName, int transactionType, IModel<AmpCategoryValueLocations> cvLocationModel) throws Exception {
		super(id, model, fmName, transactionType, 6,cvLocationModel);

		AbstractReadOnlyModel<List<AmpRegionalFunding>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);

		list = new ListView<AmpRegionalFunding>("listTransactions", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpRegionalFunding> item) {

				item.add(getAdjustmentTypeComponent(item.getModel()));
				item.add(getFundingAmountComponent(item.getModel()));
			

				item.add(getDeleteLinkField("delTransaction",
						"Delete Item", item));

			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
