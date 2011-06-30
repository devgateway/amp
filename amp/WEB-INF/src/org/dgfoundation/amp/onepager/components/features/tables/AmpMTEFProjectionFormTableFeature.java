/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public class AmpMTEFProjectionFormTableFeature extends
		AmpFormTableFeaturePanel<AmpFunding,AmpFundingMTEFProjection> {

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpMTEFProjectionFormTableFeature(String id, String fmName,
			IModel<AmpFunding> model) throws Exception {
		super(id, model, fmName);
		final IModel<Set<AmpFundingMTEFProjection>> setModel = new PropertyModel<Set<AmpFundingMTEFProjection>>(
				model, "mtefProjections");
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet<AmpFundingMTEFProjection>());

		setTitleHeaderColSpan(5);
		AbstractReadOnlyModel<List<AmpFundingMTEFProjection>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);

		list = new ListView<AmpFundingMTEFProjection>("listMTEF",
				listModel) {

			@Override
			protected void populateItem(
					final ListItem<AmpFundingMTEFProjection> item) {
			
				AmpCategoryGroupFieldPanel projected;
				try {
					projected = new AmpCategoryGroupFieldPanel("projected",
							CategoryConstants.MTEF_PROJECTION_KEY,
							new PropertyModel<AmpCategoryValue>(
									item.getModel(), "projected"),
							CategoryConstants.MTEF_PROJECTION_NAME, true,
							false, true);
					item.add(projected);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				item.add(new AmpFundingAmountComponent<AmpFundingMTEFProjection>(
						"fundingAmount", item.getModel(), "Amount", "amount",
						"Currency", "ampCurrency", "Date", "projectionDate"));

				AmpDeleteLinkField delMtef = new AmpDeleteLinkField(
						"delMtef", "Delete MTEF Projection") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpMTEFProjectionFormTableFeature.this);
						list.removeAll();
					}
				};
				item.add(delMtef);

			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
