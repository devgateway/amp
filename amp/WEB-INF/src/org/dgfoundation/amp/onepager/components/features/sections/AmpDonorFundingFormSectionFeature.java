/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingGroupFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.models.AmpFundingGroupModel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;

/**
 * The donor funding section of the activity form. Includes selecting an org,
 * adding funding item, showing already added items
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpDonorFundingFormSectionFeature extends
		AmpFormSectionFeaturePanel {
	private static final long serialVersionUID = 1L;
	private TreeMap<AmpOrganisation, AmpFundingGroupFeaturePanel> listItems = new TreeMap<AmpOrganisation, AmpFundingGroupFeaturePanel>();
	protected ListEditor<AmpOrganisation> list;
	private IModel<Set<AmpOrganisation>> setModel;
	private AbstractReadOnlyModel<List<AmpFunding>> listModel;
	private PropertyModel<Set<AmpFunding>> fundingModel;

	public ListEditor<AmpOrganisation> getList() {
		return list;
	}

	public IModel<Set<AmpOrganisation>> getSetModel() {
		return setModel;
	}
	
	public void switchOrg(ListItem item, AmpFunding funding, AmpOrganisation newOrg, AjaxRequestTarget target){
		AmpFundingGroupFeaturePanel existingFundGrp = listItems.get(funding.getAmpDonorOrgId());
		
		int idx = item.getIndex();
		existingFundGrp.getList().remove(item);
		existingFundGrp.getList().items.remove(idx);
		existingFundGrp.getList().updateModel();
		
		funding.setAmpDonorOrgId(newOrg);
		
		if (listItems.containsKey(newOrg)){
			AmpFundingGroupFeaturePanel fg = listItems.get(newOrg);
			fg.getList().addItem(funding);
		}
		else{
			fundingModel.getObject().add(funding);
			list.origAddItem(newOrg);
		}
		
		target.add(list.getParent());
		target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(list.getParent()));
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
		
		
		//group fields in FM under "Proposed Project Cost"
		AmpProposedProjectCost propProjectCost = new AmpProposedProjectCost("propProjCost", "Proposed Project Cost", am);
		add(propProjectCost);

		fundingModel = new PropertyModel<Set<AmpFunding>>(am, "funding");
		if (fundingModel.getObject() == null)
			fundingModel.setObject(new LinkedHashSet<AmpFunding>());
		
		setModel = new AmpFundingGroupModel(fundingModel);
		
		list = new ListEditor<AmpOrganisation>("listFunding", setModel) {
			@Override
			protected void onPopulateItem(ListItem<AmpOrganisation> item) {
				AmpFundingGroupFeaturePanel fg = new AmpFundingGroupFeaturePanel("fundingItem", "Funding Group", fundingModel, item.getModel(), am, AmpDonorFundingFormSectionFeature.this);
				listItems.put(item.getModelObject(), fg);
				item.add(fg);
			}
			
			@Override
			public void addItem(AmpOrganisation org) {
				AmpFunding funding = new AmpFunding();
				funding.setAmpDonorOrgId(org);
				funding.setAmpActivityId(am.getObject());
				funding.setMtefProjections(new HashSet<AmpFundingMTEFProjection>());
				funding.setFundingDetails(new HashSet<AmpFundingDetail>());
				funding.setGroupVersionedFunding(System.currentTimeMillis());
				list.updateModel();

				if (listItems.containsKey(org)){
					AmpFundingGroupFeaturePanel fg = listItems.get(org);
					fg.getList().addItem(funding);
				}
				else{
					fundingModel.getObject().add(funding);
					super.addItem(org);
				}
			}
			
			public void addItem(AmpFunding org) {
				
			}
		};
		add(list);

		
		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete","Search Organizations", true, AmpOrganisationSearchModel.class) {
			@Override
			protected String getChoiceValue(AmpOrganisation choice) {
				return DbUtil.filter(choice.getName());
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
				list.addItem(choice);

				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
				target.add(list.getParent());
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		AmpSearchOrganizationComponent searchOrganization = new AmpSearchOrganizationComponent("searchFundingOrgs", new Model<String> (),
				"Search Funding Organizations",   searchOrgs );
		add(searchOrganization);

	}

}
