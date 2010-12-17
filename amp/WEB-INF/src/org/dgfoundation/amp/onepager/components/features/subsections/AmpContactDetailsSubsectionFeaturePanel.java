/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactDetailFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactOrganizationFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
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
		
		AmpContactDetailFeaturePanel phones = new AmpContactDetailFeaturePanel("phones",ampCont1 , "Contact Phone", true, Constants.CONTACT_PROPERTY_NAME_PHONE);
		phones.setOutputMarkupId(true);
		add(phones);
		
		AmpContactDetailFeaturePanel faxes = new AmpContactDetailFeaturePanel("faxes",ampCont1 , "Contact Fax", true, Constants.CONTACT_PROPERTY_NAME_FAX);
		faxes.setOutputMarkupId(true);
		add(faxes);

		AmpContactOrganizationFeaturePanel contactOrganizations = new AmpContactOrganizationFeaturePanel("contactOrganizations",ampCont, "Contact Organizations", true);
		add(contactOrganizations);
		
		IModel<String> officeAddress = new PropertyModel<String>(ampActContact.getObject().getContact(), "officeaddress");
		add(new AmpTextAreaFieldPanel<String>("officeAddress", officeAddress, "Office Address", true));
		
		
	}

}

