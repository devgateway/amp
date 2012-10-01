/**
 * Copyright (c) 2012 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.models.AmpFundingItemsModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;


/**
 * @author aartimon@dginternational.org since Jul 4, 2012
 */
public class AmpFundingGroupFeaturePanel extends AmpFeaturePanel<AmpOrganisation> {
	private static final long serialVersionUID = 1L;
	private ListEditor<AmpFunding> list;
	
	public ListEditor<AmpFunding> getList() {
		return list;
	}

	public AmpFundingGroupFeaturePanel(String id, String fmName,
			IModel<Set<AmpFunding>> fundsModel, final IModel<AmpOrganisation> model,final IModel<AmpActivityVersion> am, final AmpDonorFundingFormSectionFeature parent) {
		super(id, model, fmName, true);
		
		add(new Label("donorOrg", model.getObject().getName()));
		AmpFundingItemsModel setModel = new AmpFundingItemsModel(fundsModel, model.getObject());
		
		list = new ListEditor<AmpFunding>("listFunding", setModel) {
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFunding> item) {
				AmpFundingItemFeaturePanel fundingItemFeature;
				try {
					fundingItemFeature = new AmpFundingItemFeaturePanel(
							"fundingItem", "Funding Item",
							item.getModel(), am, parent);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				item.add(fundingItemFeature);
			}
		};
		add(list);
		
	}
}
