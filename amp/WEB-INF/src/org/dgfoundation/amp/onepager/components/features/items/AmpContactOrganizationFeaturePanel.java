/**
 * 
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;

/**
 * @author dan
 *
 */
public class AmpContactOrganizationFeaturePanel extends AmpFeaturePanel<AmpContact>{

	protected ListView<AmpOrganisationContact> idsList;
	
	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpContactOrganizationFeaturePanel(String id, String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpContactOrganizationFeaturePanel(String id, IModel<AmpContact> model,String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpContactOrganizationFeaturePanel(String id, final IModel<AmpContact> model,String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		
		
		final IModel<Set<AmpOrganisationContact>> setModel=new PropertyModel<Set<AmpOrganisationContact>>(model,"organizationContacts");
		if (setModel.getObject() == null) {
            setModel.setObject(new HashSet<AmpOrganisationContact>());
        }
		
		AbstractReadOnlyModel<List<AmpOrganisationContact>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel);

		idsList = new ListView<AmpOrganisationContact>("listOrgs", listModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AmpOrganisationContact> item) {
				
				final MarkupContainer listParent=this.getParent();
				
				item.add(new Label("orgNameLabel", item.getModelObject().getOrganisation().getAcronymAndName()));			
				
				AmpDeleteLinkField delOrgId = new AmpDeleteLinkField("delOrgId", "Delete Internal Id") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						idsList.removeAll();
					}
				};
				item.add(delOrgId);
				
			}
		};
		add(idsList);

		
		AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchOrgs","Search Organizations",AmpOrganisationSearchModel.class) {			
			
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
			public void onSelect(AjaxRequestTarget target,AmpOrganisation choice) {
				boolean duplicateOrg=false;
				Set<AmpOrganisationContact> set = setModel.getObject();
				for (AmpOrganisationContact ampOrganisationContact : set) {
					if(ampOrganisationContact.getOrganisation().getAmpOrgId().equals(choice.getAmpOrgId())){
						duplicateOrg=true;
						break;
					}
				}
				if(!duplicateOrg){
					AmpOrganisationContact ampOrgCont = new AmpOrganisationContact();
					ampOrgCont.setOrganisation(choice);
					ampOrgCont.setContact(model.getObject());					
					set.add(ampOrgCont);
				}
				
				/*
				idsList.removeAll();
				target.addComponent(idsList.getParent());
				*/
				target.addComponent(idsList.getParent());
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
