/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.features.items.AmpAddContactFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpContactFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.util.ContactInfoUtil;

/**
 * @author dmihaila@dginternational.org
 * since Dec 6, 2010
 */
public class AmpContactsSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpActivityVersion> implements IHeaderContributor {

    private static final long serialVersionUID = -2114204838953838609L;
    protected ListView<AmpActivityContact> idsList;

    public ListView<AmpActivityContact> getIdsList() {
        return idsList;
    }
    private List<TransparentWebMarkupContainer> sliders;
    private AmpAddContactFeaturePanel newContactDetails;
    
    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpContactsSubsectionFeaturePanel(String id, String fmName, final IModel<AmpActivityVersion> am, final String contactType) throws Exception {
        //super(id, contactModel, fmName, true);
        super(id, fmName, am);
        final IModel<Set<AmpActivityContact>> setModel = new PropertyModel<Set<AmpActivityContact>>(am, "activityContacts");
        final String specificType = contactType;
        
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet<AmpActivityContact>());
		
        newContactDetails = new AmpAddContactFeaturePanel("createContactContainer", am, "Add Contact",  new Model<AmpActivityContact>(new AmpActivityContact()));
        newContactDetails.setVisible(false);
      
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

        sliders = new ArrayList<TransparentWebMarkupContainer>();

        idsList = new ListView<AmpActivityContact>("contactsList", listModel) {

            private static final long serialVersionUID = 7218457979728871528L;

            @Override
            protected void populateItem(final ListItem<AmpActivityContact> item) {

                item.add(new Label("contactName", item.getModelObject().getContact().getNameAndLastName()));

                item.add(new AmpDeleteLinkField("removeContact", "Remove Contact Link") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setModel.getObject().remove(item.getModelObject());
                        target.addComponent(AmpContactsSubsectionFeaturePanel.this);
                        target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
                        idsList.removeAll();
                    }
                });
                AmpContactDetailsSubsectionFeaturePanel contactDetails = null;
                try {
                    contactDetails = new AmpContactDetailsSubsectionFeaturePanel("contactDetails", "Contact Details", am, item.getModel());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                contactDetails.setOutputMarkupId(true);
                sliders.add(contactDetails.getSlider());

                item.add(contactDetails);
            }
        };
        idsList.setReuseItems(true);
        add(idsList);


        final AmpTextFieldPanel<String> contactname = new AmpTextFieldPanel<String>("contactname", new Model<String>(), "Contact name",true,true);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setFilter(new ContainerFeedbackMessageFilter(AmpContactsSubsectionFeaturePanel.this));
        feedback.setOutputMarkupPlaceholderTag(true);

        final AmpTextFieldPanel<String> contactLast = new AmpTextFieldPanel<String>("contactlastname", new Model<String>(), "Contact lastname",true,true);
        final AmpContactFormTableFeature contactDuplicationTable = new AmpContactFormTableFeature("duplicationTable", am, "Duplications table", null);
        final WebMarkupContainer buttonsContainer = new WebMarkupContainer("contactButtonContainer");
        buttonsContainer.setOutputMarkupPlaceholderTag(true);
        buttonsContainer.setOutputMarkupId(true);
        
        final AmpAjaxLinkField addContact = new AmpAjaxLinkField("addContactButton", "Add Contact", "Add Contact") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                try {
                    AmpActivityContact aaContact = new AmpActivityContact();
                    AmpActivityVersion act = am.getObject();
                    aaContact.setActivity(act);
                    RadioGroup<AmpContact> group=contactDuplicationTable.getContactsGroup();
                    if (group.getModelObject() == null) {
                        error("Please select contact!");
                    } else {
                        Session.get().cleanupFeedbackMessages();
                        AmpContact choice = (AmpContact) group.getDefaultModelObject();
                        aaContact.setContact(choice);
                        if (act.getActivityContacts() == null) {
                            act.setActivityContacts(new HashSet<AmpActivityContact>());
                        }
                        aaContact.setContactType(contactType);
                        act.getActivityContacts().add(aaContact);
                        idsList.removeAll();
                        contactname.setDefaultModel(new Model<String>());
                        contactname.setDefaultModelObject(null);
                        contactLast.setDefaultModel(new Model<String>());
                        contactLast.setDefaultModelObject(null);
                        buttonsContainer.setVisible(false);
                        contactDuplicationTable.setVisible(false);
                        target.addComponent(AmpContactsSubsectionFeaturePanel.this);
                        //target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
                        target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpContactsSubsectionFeaturePanel.this));
                    }
                    target.addComponent(feedback);
                    
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };


        final AmpAjaxLinkField createContact = new AmpAjaxLinkField("createContactButton", "Create New Contact", "Create New Contact") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                try {
                    AmpContact newContact = new AmpContact();
                    TextField<String> name=contactname.getTextContainer();
                    TextField<String> lastname=contactLast.getTextContainer();
                    newContact.setOrganizationContacts(new HashSet<AmpOrganisationContact>());
                    newContact.setActivityContacts(new HashSet<AmpActivityContact>());
                    newContact.setProperties(new HashSet<AmpContactProperty>());
                    newContact.setName(name.getDefaultModelObjectAsString());
                    newContact.setLastname(lastname.getDefaultModelObjectAsString());
                    AmpActivityContact activityContact=new AmpActivityContact();
                    activityContact.setContact(newContact);
                    activityContact.setContactType(contactType);
                    newContact.getActivityContacts().add(activityContact);
                    
                    AmpAddContactFeaturePanel tempContactDetailsPanel = new AmpAddContactFeaturePanel("createContactContainer", am, "Add Contact", new Model<AmpActivityContact>(activityContact));
                    newContactDetails.replaceWith(tempContactDetailsPanel);
                    newContactDetails=tempContactDetailsPanel;

                    name.setDefaultModel(new Model<String>());
                    name.setDefaultModelObject(null);
                    lastname.setDefaultModel(new Model<String>());
                    lastname.setDefaultModelObject(null);
                    contactDuplicationTable.getList().getList().clear();
                    newContactDetails.setVisible(true);
                    target.addComponent(newContactDetails);
                    contactDuplicationTable.setVisible(false);
                    target.addComponent(contactDuplicationTable);
                    buttonsContainer.setVisible(false);
                    target.addComponent(buttonsContainer);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        AmpAjaxLinkField searchContact = new AmpAjaxLinkField("searchContact", "Search Contact", "Search Contact") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                final String name =(String) contactname.getTextContainer().getDefaultModelObject();
                final String lastname = (String) contactLast.getTextContainer().getDefaultModelObject();
                try {
                    ListView<AmpContact> list = contactDuplicationTable.getList();
                    
                    LoadableDetachableModel<List<AmpContact>> contactsModel = new LoadableDetachableModel<List<AmpContact>>() {
						@Override
						protected List<AmpContact> load() {
							
							List<AmpContact> contacts = null;
							try {
								contacts = ContactInfoUtil.getContactsByNameLastName(name, lastname);
							} catch (Exception e) {
								logger.error(e);
							}
							
							return contacts;
						}
					};
                    
                    //list.setList(contacts);
                    list.setModel(contactsModel);
                    
                    if(!list.getList().isEmpty()){
	                    contactDuplicationTable.setVisible(true);
	                    target.addComponent(contactDuplicationTable);
	                    addContact.setVisible(true);
                    }
                    else{
                    	addContact.setVisible(false);
                    }
                    newContactDetails.setVisible(false);
                    buttonsContainer.setVisible(true);
                    target.addComponent(buttonsContainer);
                    target.addComponent(newContactDetails);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        add(feedback);
        add(contactname);
        add(contactLast);
        add(searchContact);
        contactDuplicationTable.setOutputMarkupId(true);
        contactDuplicationTable.setOutputMarkupPlaceholderTag(true);
        contactDuplicationTable.setVisible(false);
        add(contactDuplicationTable);
        buttonsContainer.setVisible(false);
        buttonsContainer.add(addContact);
        buttonsContainer.add(createContact);
        add(buttonsContainer);
        add(newContactDetails);
     
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        for (TransparentWebMarkupContainer c : sliders) {
            //	response.renderOnDomReadyJavascript(OnePagerConst.getToggleJS(c));
            ;//System.out.println("-------"+OnePagerConst.getToggleJS(c));
        }
        ;

    }
}
