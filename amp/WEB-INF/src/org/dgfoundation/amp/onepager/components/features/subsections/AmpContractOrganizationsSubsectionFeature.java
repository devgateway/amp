/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author aartimon@dginternational.org
 * @since Feb 8, 2011
 */
public class AmpContractOrganizationsSubsectionFeature extends
		AmpSubsectionFeaturePanel<IPAContract> {
	
	private static Logger logger = Logger.getLogger(AmpContractOrganizationsSubsectionFeature.class);

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpContractOrganizationsSubsectionFeature(String id,
			IModel<IPAContract> model, String fmName){
		super(id, fmName, model);

		final IModel<Set<AmpOrganisation>> orgs = new PropertyModel<Set<AmpOrganisation>>(model, "organizations");
		
		if (orgs.getObject() == null)
			orgs.setObject(new HashSet<AmpOrganisation>());
		
		AbstractReadOnlyModel<List<AmpOrganisation>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(orgs);

		final ListView<AmpOrganisation> list = new ListView<AmpOrganisation>("list", listModel) {
				@Override
				protected void populateItem(final ListItem<AmpOrganisation> item) {
					AmpOrganisation org = item.getModelObject();
					
					Label orgName = new Label("name", org.getAcronymAndName());
					item.add(orgName);
					
					AmpDeleteLinkField delete = new AmpDeleteLinkField(
							"delete", "Delete Organisation") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							orgs.getObject().remove(item.getModelObject());
							target.addComponent(AmpContractOrganizationsSubsectionFeature.this);
							target.appendJavascript(OnePagerUtil.getToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
							target.appendJavascript(OnePagerUtil.getClickToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
						}
					};
					item.add(delete);
				}
		};
		list.setReuseItems(true);
		add(list);

		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("search","Search Funding Organizations",AmpOrganisationSearchModel.class) {			
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpOrganisation choice) {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpOrganisation choice) {
				orgs.getObject().add(choice);
				list.removeAll();
				target.addComponent(AmpContractOrganizationsSubsectionFeature.this);
				target.appendJavascript(OnePagerUtil.getToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
				target.appendJavascript(OnePagerUtil.getClickToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				return null;
			}
		};

		add(searchOrgs);
	}

}
