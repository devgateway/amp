/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * The donor funding section of the activity form. Includes selecting an org,
 * adding funding item, showing already added items
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpDonorFundingFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	protected ListView<AmpFunding> list;

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpDonorFundingFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		final IModel<Set<AmpFunding>> setModel = new PropertyModel<Set<AmpFunding>>(
				am, "funding");
		AbstractReadOnlyModel<List<AmpFunding>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);

		list = new ListView<AmpFunding>("listFunding", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpFunding> item) {
				AmpFundingItemFeaturePanel fundingItemFeature;
				try {
					fundingItemFeature = new AmpFundingItemFeaturePanel(
							"fundingItem", "Funding Item",
							item.getModel());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				item.add(fundingItemFeature);

				AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
						"delFunding", "Delete Funding Item",new Model<String>("Do you really want to delete this funding item?")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpDonorFundingFormSectionFeature.this);
						target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
						list.removeAll();
					}
				};
				item.add(deleteLinkField);
			}
		};

		list.setReuseItems(true);
		add(list);

		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchFundingOrgs","Search Funding Organizations",AmpOrganisationSearchModel.class) {			
			@Override
			protected String getChoiceValue(AmpOrganisation choice) {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpOrganisation choice) {
				AmpFunding funding = new AmpFunding();
				funding.setAmpDonorOrgId(choice);
				funding.setAmpActivityId(am.getObject());

				funding.setMtefProjections(new HashSet<AmpFundingMTEFProjection>());
				funding.setFundingDetails(new HashSet<AmpFundingDetail>());

				setModel.getObject().add(funding);
				list.removeAll();
				target.addComponent(AmpDonorFundingFormSectionFeature.this);
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		add(searchOrgs);

	}

}
