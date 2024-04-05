/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactDetailFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactOrganizationFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.ContactChangedEvent;
import org.dgfoundation.amp.onepager.models.AmpContactSearchModel;
import org.dgfoundation.amp.onepager.models.AmpReadOnlyModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.dgfoundation.amp.onepager.yui.contacts.AmpContactAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Hibernate;

import java.util.*;

/**
 * @author dmihaila@dginternational.org
 * since Dec 6, 2010
 */
public class AmpContactsFromTableFeature extends AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivityContact>  {

    private static final long serialVersionUID = -2114204838953838609L;
    //protected ListView<AmpActivityContact> idsList;
    private AjaxCheckBox primaryContact=null;
    
    /*public ListView<AmpActivityContact> getIdsList() {
        return idsList;
    }*/

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
                if (allContacts != null){
                    Iterator<AmpActivityContact> it = allContacts.iterator();
                    while (it.hasNext()) {
                        AmpActivityContact ampActContact = (AmpActivityContact) it.next();
                        if (specificType.compareTo(ampActContact.getContactType()) == 0) {
                            specificContacts.add(ampActContact);
                        }
                    }
                }
                return new ArrayList<AmpActivityContact>(specificContacts);
            }
        };

        list = new ListView<AmpActivityContact>("contactsList", listModel) {

            private static final long serialVersionUID = 7218457979728871528L;

            @Override
            protected void populateItem(final ListItem<AmpActivityContact> item) {
                try {
                    final MarkupContainer listParent=this.getParent();
                    final AmpActivityContact actContact=item.getModelObject();
                   

                    AmpContact contactModel = actContact.getContact();
                    item.add(new Label("contactName", contactModel.getNameAndLastName()));
                    final AjaxCheckBox primary = new AjaxCheckBox(
                            "primaryContact", new PropertyModel<Boolean>(
                                    item.getModel(), "primaryContact")) {
                        private static final long serialVersionUID = 1L;

                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            if (getModelObject()) {
                                if (primaryContact != null) {
                                    primaryContact.clearInput();
                                    primaryContact.setModelObject(Boolean.FALSE);
                                    target.add(primaryContact);
                                }
                                primaryContact = this;
                            } else {
                                primaryContact = null;
                            }
                        }
                    };
                    final Boolean isPrimaryContact=primary.getModelObject();
                    if(isPrimaryContact!=null&&isPrimaryContact){
                        primaryContact=primary;
                    }
                    primary.setOutputMarkupId(true);
                    item.add(primary);
                    AmpDeleteLinkField delContact = new AmpDeleteLinkField("delContact", "Delete Contact") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            setModel.getObject().remove(item.getModelObject());
                            target.add(listParent);
                            list.removeAll();
                            if (primaryContact != null && primaryContact.equals(primary)) {
                                primaryContact = null;
                            }

                        }
                    };
                    item.add(delContact);
                    

                    final AmpCategorySelectFieldPanel contactTitle = new AmpCategorySelectFieldPanel(
                                    "title",
                                    CategoryConstants.CONTACT_TITLE_KEY,
                                    new PropertyModel<AmpCategoryValue>(contactModel, "title"),
                                    CategoryConstants.CONTACT_TITLE_NAME, true, true, false);
                    contactTitle.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            send(getPage(), Broadcast.BREADTH,
                                    new ContactChangedEvent(target));
                            
                        }
                    });
                    item.add(contactTitle);
                    
                    
                    final AmpTextFieldPanel<String> name=new AmpTextFieldPanel<String>("name",new PropertyModel<String>(actContact.getContact(),"name"),"contact first name",false,true);
                    name.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            send(getPage(), Broadcast.BREADTH,
                                    new ContactChangedEvent(target));
                        }
                    });
                
                    name.getTextContainer().setRequired(true);
                    name.getTextContainer().add(new AttributeModifier("size", "50"));
                    name.setTextContainerDefaultMaxSize();
                    item.add(name);
                    final AmpTextFieldPanel<String> lastname=new AmpTextFieldPanel<String>("lastname",new PropertyModel<String>(contactModel,"lastname"),"contact lastname",false,true);
                    lastname.getTextContainer().setRequired(true);
                    lastname.getTextContainer().add(new AttributeModifier("size", "50"));
                    lastname.setTextContainerDefaultMaxSize();
                    lastname.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            send(getPage(), Broadcast.BREADTH,
                                    new ContactChangedEvent(target));
                        }
                    });
                    item.add(lastname);
                    
                    IModel<AmpContact> contactReadOnlyModel = new AmpReadOnlyModel(actContact.getContact());
                    AmpContactDetailFeaturePanel detailEmail=new AmpContactDetailFeaturePanel("addContactEmail", contactReadOnlyModel, "Add Contact Email",false,Constants.CONTACT_PROPERTY_NAME_EMAIL);
                    item.add(detailEmail);
                    
                    final AmpTextFieldPanel<String> function=new  AmpTextFieldPanel<String>("function",new PropertyModel<String>(contactModel,"function"),"contact function",false);
                    function.setTextContainerDefaultMaxSize();
                    function.getTextContainer().add(new AttributeModifier("size", "50"));
                    function.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            send(getPage(), Broadcast.BREADTH,
                                    new ContactChangedEvent(target));
                        }
                    });
                    item.add(function);
                    
                    final AmpTextFieldPanel<String> organisationName=new  AmpTextFieldPanel<String>("organisationName",new PropertyModel<String>(contactModel,"organisationName"),"organisationName",false);
                    organisationName.setTextContainerDefaultMaxSize();
                    organisationName.getTextContainer().add(new AttributeModifier("size", "50"));
                    organisationName.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            send(getPage(), Broadcast.BREADTH,
                                    new ContactChangedEvent(target));
                        }
                    });
                    
                    item.add(organisationName);
                    
                    AmpContactOrganizationFeaturePanel contactOrganizations = new AmpContactOrganizationFeaturePanel("contactOrganizations",contactReadOnlyModel, "Contact Organizations", false);
                    contactOrganizations.setOutputMarkupId(true);
                    item.add(contactOrganizations);
                    
                    AmpContactDetailFeaturePanel detailPhone=new AmpContactDetailFeaturePanel("addContactPhone", contactReadOnlyModel,"Add Contact Phone",false,Constants.CONTACT_PROPERTY_NAME_PHONE); 
                    item.add(detailPhone);

                    AmpContactDetailFeaturePanel detailFax=new AmpContactDetailFeaturePanel("addContactFax", contactReadOnlyModel,"Add Contact Fax",false,Constants.CONTACT_PROPERTY_NAME_FAX);  
                    item.add(detailFax);
                  
                    final AmpTextAreaFieldPanel office = new AmpTextAreaFieldPanel("officeaddress",new PropertyModel<String>(contactModel,"officeaddress"),"contact office address",false, false, true);
                    office.getTextAreaContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            send(getPage(), Broadcast.BREADTH,
                                    new ContactChangedEvent(target));
                        }
                    });
                    item.add(office);
                } catch (Exception ex) {

                }
            }
        };
        list.setReuseItems(true);
        list.setOutputMarkupId(true);
        add(list);


        final AmpAutocompleteFieldPanel<AmpContact> searchContacts = new AmpContactAutocompleteFieldPanel("searchContact", "Search Contact",false, AmpContactSearchModel.class,id,true) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSelect(AjaxRequestTarget target, AmpContact choice) {
                boolean contactExists = false;
                if (setModel.getObject() == null){
                    setModel.setObject(new HashSet<AmpActivityContact>());
                }
                
                Set<AmpActivityContact> actContacts = setModel.getObject();
                if(choice.getId()!=null){
                    for (AmpActivityContact actCont : actContacts) {
                        if(actCont.getContact().getId()!=null && actCont.getContact().getId().equals(choice.getId()) 
                                && contactType.equals(actCont.getContactType())){
                            contactExists = true;
                            break;
                        }
                    }
                }
                
                if(!contactExists){
                    AmpActivityContact activityContact = new AmpActivityContact();
                    activityContact.setContact(choice);         
                    activityContact.setActivity(am.getObject());
                    activityContact.setContactType(contactType);
                    activityContact.setPrimaryContact(false);
                    Hibernate.initialize(choice.getActivityContacts()); //lazy init:)
                    if(choice.getActivityContacts() == null) {
                        choice.setActivityContacts(new TreeSet<AmpActivityContact>());
                    }
                    choice.getActivityContacts().add(activityContact);
                    
                    setModel.getObject().add(activityContact);
                }
                
                list.removeAll();
                target.add(list.getParent());
            }

        };
        searchContacts.setReuseObjects(true);
        add(searchContacts);

    }
    
}
