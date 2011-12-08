/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public class AmpMTEFProjectionFormTableFeature extends
		AmpFundingFormTableFeaturePanel<AmpFunding,AmpFundingMTEFProjection> {

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpMTEFProjectionFormTableFeature(String id, String fmName,
			IModel<AmpFunding> model) throws Exception {
		super(id, model, fmName);
		
		getTableId().add(new SimpleAttributeModifier("width", "620"));
		
		final IModel<Set<AmpFundingMTEFProjection>> setModel = new PropertyModel<Set<AmpFundingMTEFProjection>>(
				model, "mtefProjections");
		if (setModel.getObject() == null)
			setModel.setObject(new TreeSet<AmpFundingMTEFProjection>());

		setTitleHeaderColSpan(5);
		AbstractReadOnlyModel<List<AmpFundingMTEFProjection>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel, new AmpFundingMTEFProjection.FundingMTEFProjectionComparator());

		list = new ListEditor<AmpFundingMTEFProjection>("listMTEF",
				setModel, new AmpFundingMTEFProjection.FundingMTEFProjectionComparator()) {

			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFundingMTEFProjection> item) {
			
				AmpCategoryGroupFieldPanel projected;
				try {
					projected = new AmpCategoryGroupFieldPanel("projected",
							CategoryConstants.MTEF_PROJECTION_KEY,
							new PropertyModel<AmpCategoryValue>(
									item.getModel(), "projected"),
							CategoryConstants.MTEF_PROJECTION_NAME, true,
							false, true);
					projected.getChoiceContainer().setRequired(true);
					item.add(projected);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				item.add(new AmpFundingAmountComponent<AmpFundingMTEFProjection>(
						"fundingAmount", item.getModel(), "Amount", "amount",
						"Currency", "ampCurrency", "Date", "projectionDate"));

				item.add(new ListEditorRemoveButton("delMtef", "Delete MTEF Projection"));
			}
		};
		add(list);
	}

}
