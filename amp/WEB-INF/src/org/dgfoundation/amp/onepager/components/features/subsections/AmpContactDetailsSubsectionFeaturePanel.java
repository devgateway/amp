/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactDetailFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactOrganizationFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author dmihaila@dginternational.org
 * since Dec 6, 2010
 */
public class AmpContactDetailsSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpActivityContact>{

	

	protected ListView<AmpContactProperty> emailList, phoneList, faxList;

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpContactDetailsSubsectionFeaturePanel(String id, String fmName, final IModel<AmpActivityContact> ampActContact) throws Exception {
		//super(id, contactModel, fmName, true);
		super(id,fmName, ampActContact);
		final IModel<AmpContact> ampCont = new PropertyModel<AmpContact>(ampActContact.getObject(), "contact");
		Set<AmpCategoryValue> h  = new HashSet<AmpCategoryValue>();
		//h.add(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.CONTACT_TITLE_KEY).iterator().next());
		AmpCategoryValue categVal = ampActContact.getObject().getContact().getTitle();
		if (categVal != null)
			h.add(categVal);
		else{
			/**
			 * HACK, to be fixed by Dan.
			 */
			h.add(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.CONTACT_TITLE_KEY).iterator().next());
		}
		AmpCategorySelectFieldPanel contactTitle = new AmpCategorySelectFieldPanel(
				"title", CategoryConstants.CONTACT_TITLE_KEY,new AmpCategoryValueByKeyModel( new Model((Serializable)h),CategoryConstants.CONTACT_TITLE_KEY), 
				CategoryConstants.CONTACT_TITLE_NAME, true, false, null);
		add(contactTitle);

		
		
		IModel<String> firstName = new PropertyModel<String>(ampActContact.getObject().getContact(), "name");
		add(new AmpTextFieldPanel<String>("firstName", firstName, "First Name", true));
		
		IModel<String> lastName = new PropertyModel<String>(ampActContact.getObject().getContact(), "lastname");
		add(new AmpTextFieldPanel<String>("lastName", lastName, "Last Name", true));

		final IModel<AmpContact> ampCont1 = new PropertyModel<AmpContact>(ampActContact.getObject(), "contact");
		AmpContactDetailFeaturePanel emails = new AmpContactDetailFeaturePanel("emails",ampCont1 , "Contact Email", true, Constants.CONTACT_PROPERTY_NAME_EMAIL);
		emails.setOutputMarkupId(true);
		add(emails);
		
		IModel<String> function = new PropertyModel<String>(ampActContact.getObject().getContact(), "function");
		add(new AmpTextFieldPanel<String>("function", function, "Function", true));
		
		final IModel<List<AmpContactProperty>> listPhoneModel = new AbstractReadOnlyModel<List<AmpContactProperty>>() {

			@Override
			public List<AmpContactProperty> getObject() {
				Set<AmpContactProperty> specificContacts = new HashSet<AmpContactProperty>();  
				Set<AmpContactProperty> contactProperties=ampActContact.getObject().getContact().getProperties();
				if(contactProperties!=null){
					for (AmpContactProperty phone : contactProperties) {
						if(phone.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)){
							specificContacts.add(phone);
						}
					}
				}	
				if(specificContacts.size()<1) {
					AmpContactProperty cProperty = new AmpContactProperty();
					cProperty.setName(Constants.CONTACT_PROPERTY_NAME_PHONE);
					cProperty.setContact(ampActContact.getObject().getContact());
					cProperty.setValue("");
					specificContacts.add(cProperty);
				}
				return new ArrayList<AmpContactProperty>(specificContacts);
			}
		};
		phoneList = new ListView<AmpContactProperty>("phones", listPhoneModel) {

			@Override
			protected void populateItem(final ListItem<AmpContactProperty> item) {
				IModel<String> phone = new PropertyModel<String>(item.getModelObject(), "value");
				item.add(new AmpTextFieldPanel<String>("phone", phone,"Contact Phone", true));
			}
		};
		phoneList.setReuseItems(true);
		add(phoneList);
		
		final IModel<List<AmpContactProperty>> listFaxModel = new AbstractReadOnlyModel<List<AmpContactProperty>>() {

			@Override
			public List<AmpContactProperty> getObject() {
				Set<AmpContactProperty> specificContacts = new HashSet<AmpContactProperty>();  
				Set<AmpContactProperty> contactProperties=ampActContact.getObject().getContact().getProperties();
				if(contactProperties!=null){
					for (AmpContactProperty phone : contactProperties) {
						if(phone.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX)){
							specificContacts.add(phone);
						}
					}
				}	
				if(specificContacts.size()<1) {
					AmpContactProperty cProperty = new AmpContactProperty();
					cProperty.setName(Constants.CONTACT_PROPERTY_NAME_FAX);
					cProperty.setContact(ampActContact.getObject().getContact());
					cProperty.setValue("");
					specificContacts.add(cProperty);
				}
				return new ArrayList<AmpContactProperty>(specificContacts);
			}
		};
		faxList = new ListView<AmpContactProperty>("faxes", listFaxModel) {

			@Override
			protected void populateItem(final ListItem<AmpContactProperty> item) {
				IModel<String> fax = new PropertyModel<String>(item.getModelObject(), "value");
				item.add(new AmpTextFieldPanel<String>("fax", fax,"Contact Fax", true));
			}
		};
		faxList.setReuseItems(true);
		add(faxList);
		
		

		AmpContactOrganizationFeaturePanel contactOrganizations = new AmpContactOrganizationFeaturePanel("contactOrganizations",ampCont, "Contact Organizations", true);
		add(contactOrganizations);
//		IModel<String> orgName = new PropertyModel<String>(ampActContact.getObject().getContact(), "organisationName");
//		add(new AmpTextFieldPanel<String>("contactOrganizations", orgName, "Organization Name", true));
		
		IModel<String> officeAddress = new PropertyModel<String>(ampActContact.getObject().getContact(), "officeaddress");
		add(new AmpTextFieldPanel<String>("officeAddress", officeAddress, "Office Address", true));
		
		
	}

}

//item.add(new TextField<String>("firstName", new PropertyModel<String>(item.getModel(), "additionalInfo")));
//item.add(new Label("firstName", item.getModelObject().getContact().getName()));		
//item.add(new Label("lastName", item.getModelObject().getContact().getLastname()));
////item.add(new Label("email", item.getModelObject().getContact().getProperties()));
//item.add(new Label("email", "email1"));
//item.add(new Label("organization", item.getModelObject().getContact().getOrganisationName()));
//item.add(new Label("phone", "phone1"));
//item.add(new Label("primary", item.getModelObject().getPrimaryContact()==true?"yes":"no"));
//item.add(new Label("actions", "edit/delete"));

//label("name", item.getModelObject().getOrganisation().getAcronymAndName()));			

//item.add(getDeleteLinkField(id, fmName,item,listModel));
