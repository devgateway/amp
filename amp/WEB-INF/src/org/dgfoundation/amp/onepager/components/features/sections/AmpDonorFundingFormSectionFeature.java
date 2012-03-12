/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
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

	protected ListEditor<AmpFunding> list;
	private IModel<Set<AmpFunding>> setModel;
	private AbstractReadOnlyModel<List<AmpFunding>> listModel;

	public ListEditor<AmpFunding> getList() {
		return list;
	}

	public IModel<Set<AmpFunding>> getSetModel() {
		return setModel;
	}

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpDonorFundingFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		setModel = new PropertyModel<Set<AmpFunding>>(
				am, "funding");
		if (setModel.getObject() == null)
			setModel.setObject(new LinkedHashSet<AmpFunding>());
		
		
		//group fields in FM under "Proposed Project Cost"
		AmpProposedProjectCost propProjectCost = new AmpProposedProjectCost("propProjCost", "Proposed Project Cost", am);
		add(propProjectCost);
		
		
		list = new ListEditor<AmpFunding>("listFunding", setModel) {
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFunding> item) {
				AmpFundingItemFeaturePanel fundingItemFeature;
				try {
					fundingItemFeature = new AmpFundingItemFeaturePanel(
							"fundingItem", "Funding Item",
							item.getModel(),am,AmpDonorFundingFormSectionFeature.this);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				item.add(fundingItemFeature);

				String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this funding item?");
				
				item.add(new ListEditorRemoveButton("delFunding", "Delete Funding Item", translatedMessage){
					@Override
					protected void onClick(AjaxRequestTarget target) {
						super.onClick(target);
						target.addComponent(AmpDonorFundingFormSectionFeature.this);
						target.appendJavascript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
					}
				});

			}
		};

		add(list);

		
		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchFundingOrgs","Search Funding Organizations",AmpOrganisationSearchModel.class) {			
			@Override
			protected String getChoiceValue(AmpOrganisation choice) {
				return choice.getName();
			}
			
			@Override
			protected boolean showAcronyms() {
				return true;
			}
			
			@Override
			protected String getAcronym(AmpOrganisation choice) {
				return choice.getAcronym();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpOrganisation choice) {
				AmpFunding funding = new AmpFunding();
				funding.setAmpDonorOrgId(choice);
				funding.setAmpActivityId(am.getObject());

				funding.setMtefProjections(new HashSet<AmpFundingMTEFProjection>());
				funding.setFundingDetails(new HashSet<AmpFundingDetail>());
				funding.setGroupVersionedFunding(System.currentTimeMillis());

				list.addItem(funding);
				target.addComponent(list.getParent());
				target.appendJavascript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
				list.updateModel();
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
