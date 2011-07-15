/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpContactSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author dmihaila@dginternational.org
 * since Dec 6, 2010
 */
public class AmpContactsFromTableFeature extends AmpFormTableFeaturePanel<AmpActivityVersion,AmpContact>  {

    private static final long serialVersionUID = -2114204838953838609L;
    protected ListView<AmpActivityContact> idsList;

    public ListView<AmpActivityContact> getIdsList() {
        return idsList;
    }

    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpContactsFromTableFeature(String id, String fmName, final IModel<AmpActivityVersion> am, final String contactType) throws Exception {
        //super(id, contactModel, fmName, true);
        super(id, am, fmName);
        final IModel<Set<AmpActivityContact>> setModel = new PropertyModel<Set<AmpActivityContact>>(am, "activityContacts");
        final String specificType = contactType;

        if (setModel.getObject() == null) {
            setModel.setObject(new HashSet<AmpActivityContact>());
        }
        final IModel<List<AmpActivityContact>> listModel = new AbstractReadOnlyModel<List<AmpActivityContact>>() {

            private static final long serialVersionUID = 3706184421459839210L;

            @Override
            public List<AmpActivityContact> getObject() {
                Set<AmpActivityContact> allContacts = setModel.getObject();
                Set<AmpActivityContact> specificContacts = new TreeSet<AmpActivityContact>();
                Iterator<AmpActivityContact> it = allContacts.iterator();
                while (it.hasNext()) {
                    AmpActivityContact ampActContact = (AmpActivityContact) it.next();
                    if (specificType.compareTo(ampActContact.getContactType()) == 0) {
                        specificContacts.add(ampActContact);
                    }
                }
                return new ArrayList<AmpActivityContact>(specificContacts);
            }
        };

        idsList = new ListView<AmpActivityContact>("contactsList", listModel) {

            private static final long serialVersionUID = 7218457979728871528L;

            @Override
            protected void populateItem(final ListItem<AmpActivityContact> item) {
                try {
                    final MarkupContainer listParent=this.getParent();
                    AmpContact contact = item.getModelObject().getContact();
                    item.add(new Label("contactName", contact.getNameAndLastName()));
                    
                    AmpDeleteLinkField delContact = new AmpDeleteLinkField(
                                                    "delContact", "Delete Contact") {

														private static final long serialVersionUID = 1L;

											@Override
                                            public void onClick(AjaxRequestTarget target) {
                                                    setModel.getObject().remove(item.getModelObject());
                                                    target.addComponent(listParent);
                                            }
                                    };
                   
         
                    AmpCategorySelectFieldPanel contactTitle = new AmpCategorySelectFieldPanel(
                                    "title",
                                    CategoryConstants.CONTACT_TITLE_KEY,
                                    new PropertyModel<AmpCategoryValue>(contact, "title"),
                                    CategoryConstants.CONTACT_TITLE_NAME, true, true, true);
                    IModel<AmpContact> contactModel = new Model<AmpContact>(contact);

                    AmpContactDetailFeaturePanel detailPhone=new AmpContactDetailFeaturePanel("addContactPhone", contactModel,"Add Contact Phone",true,Constants.CONTACT_PROPERTY_NAME_PHONE); 
                    AmpContactDetailFeaturePanel detailFax=new AmpContactDetailFeaturePanel("addContactFax", contactModel,"Add Contact Fax",true,Constants.CONTACT_PROPERTY_NAME_FAX);  
                    AmpContactDetailFeaturePanel detailEmail=new AmpContactDetailFeaturePanel("addContactEmail", contactModel, "Add Contact Email",true,Constants.CONTACT_PROPERTY_NAME_EMAIL);


                    AmpContactOrganizationFeaturePanel contactOrganizations = new AmpContactOrganizationFeaturePanel("contactOrganizations",contactModel, "Contact Organizations", true);
                    contactOrganizations.setOutputMarkupId(true);
                    item.add(contactTitle);
                    AmpTextFieldPanel<String> name=new AmpTextFieldPanel<String>("name",new PropertyModel<String>(contactModel,"name"),"contact name",true);
                    name.getTextContainer().setRequired(true);
                    AmpTextFieldPanel<String> lastname=new AmpTextFieldPanel<String>("lastname",new PropertyModel<String>(contactModel,"lastname"),"contact lastname",true);
                    lastname.getTextContainer().setRequired(true);
                    item.add(name);
                    item.add(lastname);
                    item.add(detailEmail);
                    item.add(new  AmpTextFieldPanel<String>("function",new PropertyModel<String>(contactModel,"function"),"contact function",true));
                    item.add(new  AmpTextFieldPanel<String>("organisationName",new PropertyModel<String>(contactModel,"organisationName"),"organisationName",true));
                    item.add(contactOrganizations);
                    item.add(detailPhone);
                    item.add(detailFax);
                    item.add(new AmpTextAreaFieldPanel<String>("officeaddress",new PropertyModel<String>(contactModel,"officeaddress"),"contact office address",false));
                    item.add(delContact);
                } catch (Exception ex) {

                }
            }
        };
        idsList.setReuseItems(true);
        add(idsList);


        final AmpAutocompleteFieldPanel<AmpContact> searchContacts = new AmpAutocompleteFieldPanel<AmpContact>("searchContact", "Search Contact",false, AmpContactSearchModel.class,false,false) {

            @Override
            protected String getChoiceValue(AmpContact choice) {
                if(choice.getId()==null){
                    return "add new contact";
                }
                return choice.getNameAndLastName()+ "("+choice.getId()+")";
            }

            @Override
            public void onSelect(AjaxRequestTarget target, AmpContact choice) {
				AmpActivityContact activityContact = new AmpActivityContact();
				activityContact.setContact(choice);			
				activityContact.setActivity(am.getObject());
				activityContact.setContactType(contactType);
				setModel.getObject().add(activityContact);
				idsList.removeAll();
				target.addComponent(idsList.getParent());
            }

            @Override
            public Integer getChoiceLevel(AmpContact choice) {
                return null;
            }
            @Override
            public String getAdditionalDetails(AmpContact contact) {
             	if(contact.getId()==null) return "";
            	StringBuilder details=new StringBuilder();
            	String emails="";
				String orgs="";
				String phones="";
				String faxes="";
            	if(contact.getTitle()!=null){
            		details.append(contact.getTitle().getValue());
            	}
            	details.append(" ");
        		details.append(contact.getNameAndLastName());
        		details.append("<br/>");
            	if(contact.getProperties()!=null){
					for (AmpContactProperty property : contact.getProperties()) {
						if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().length()>0){
							emails+=property.getValue() +"<br/>";
						}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && property.getValueAsFormatedPhoneNum().length()>0){
							
							phones+=property.getValueAsFormatedPhoneNum()  +"<br/>";
						}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX) && property.getValue().length()>0){
							faxes+=property.getValue()  +"<br/>";
						}
					}
				}
            	details.append("<br/>");
            	details.append("Emails: ");
            	details.append(emails);
            	details.append("<br/>");

            	
            	details.append("<br/>");
            	details.append("Phones: ");
            	details.append(phones);
            	details.append("<br/>");
            	
            	details.append("<br/>");
            	details.append("Faxes: ");
            	details.append(faxes);
            	details.append("<br/>");

				
				if((contact.getOrganizationContacts()!=null && contact.getOrganizationContacts().size()>0) || (contact.getOrganisationName()!=null && contact.getOrganisationName().length()>0)){
					if(contact.getOrganisationName()!=null && contact.getOrganisationName().length()>0){
						orgs+=contact.getOrganisationName()+"<br/>";
					}
					for (AmpOrganisationContact contOrg : contact.getOrganizationContacts()) {
						orgs+=contOrg.getOrganisation().getName()+"<br/>";
					}
				}
				details.append("<br/>");
            	details.append("Organizations: ");
            	details.append(orgs);
            	details.append("<br/>");
                return DbUtil.filter(details.toString());
            }
        };
        add(searchContacts);

    }
}
