package org.dgfoundation.amp.onepager.components.features.items;


import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContactDetailsSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContactsSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.visitors.InternalForm;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpAddContactFeaturePanel extends AmpFeaturePanel<AmpActivityContact> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private AmpContactOrganizationFeaturePanel contactOrganizations;
	private AmpContactDetailFeaturePanel detailFax;
	private AmpContactDetailFeaturePanel detailEmail;
	private AmpContactDetailFeaturePanel detailPhone;



	/**
	 * TODO:
	 * TODO: change to use model to reference ampActivity
	 * TODO: 
	 */
	public AmpAddContactFeaturePanel(String id, final IModel<AmpActivityVersion> am,
			String fmName, IModel<AmpActivityContact> activityContact) throws Exception {
		super(id, fmName);
		final AmpActivityContact actContact=activityContact.getObject();
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setFilter(new ContainerFeedbackMessageFilter(AmpAddContactFeaturePanel.this));
		feedback.setOutputMarkupId(true);
		InternalForm nestedAddContactForm = new InternalForm("nestedAddContactForm");

		AmpContact newContact = null;
		if (activityContact.getObject().getContact() == null) {
			newContact = new AmpContact();
		} else {
			newContact = activityContact.getObject().getContact();
		}
		IModel<AmpContact> contactModel = PersistentObjectModel.getModel(newContact);

		CompoundPropertyModel<AmpContact> compoundPropertyModel = new CompoundPropertyModel<AmpContact>(newContact);
        setDefaultModel(compoundPropertyModel);
        
		AmpCategorySelectFieldPanel contactTitle = new AmpCategorySelectFieldPanel(
				"title",
				CategoryConstants.CONTACT_TITLE_KEY,
				new PropertyModel<AmpCategoryValue>(newContact, "title"),
				CategoryConstants.CONTACT_TITLE_NAME, true, true, true);


		detailPhone=new AmpContactDetailFeaturePanel("addContactPhone", contactModel,"Add Contact Phone",true,Constants.CONTACT_PROPERTY_NAME_PHONE); 
		detailFax=new AmpContactDetailFeaturePanel("addContactFax", contactModel,"Add Contact Fax",true,Constants.CONTACT_PROPERTY_NAME_FAX);  
		detailEmail=new AmpContactDetailFeaturePanel("addContactEmail", contactModel, "Add Contact Email",true,Constants.CONTACT_PROPERTY_NAME_EMAIL);


		contactOrganizations = new AmpContactOrganizationFeaturePanel("contactOrganizations",contactModel, "Contact Organizations", true);
		contactOrganizations.setOutputMarkupId(true);
		add(feedback);
		nestedAddContactForm.add(contactTitle);
		TextField<String> name=new TextField<String>("name");
		name.setRequired(true);
		TextField<String> lastname=new TextField<String>("lastname");
		lastname.setRequired(true);
		nestedAddContactForm.add(name);
		nestedAddContactForm.add(lastname);
		nestedAddContactForm.add(detailEmail);
		nestedAddContactForm.add(new TextField<String>("function"));
		nestedAddContactForm.add(new TextField<String>("organisationName"));
		nestedAddContactForm.add(contactOrganizations);
		nestedAddContactForm.add(detailPhone);
		nestedAddContactForm.add(detailFax);
		nestedAddContactForm.add(new TextArea<String>("officeaddress"));
		final AjaxButton saveContactButton = new AjaxButton("saveContact") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpContact contact = (AmpContact) this.getParent().getParent().getDefaultModelObject();
				if (am.getObject().getActivityContacts() == null) {
					am.getObject().setActivityContacts(new HashSet<AmpActivityContact>());
				}
				final Set<AmpContactProperty> contactProperties = contact.getProperties();
				boolean duplication=false;
				for (AmpContactProperty property : contactProperties) {
					if (property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
						int contactWithSameEmail=-1;
						try {
							contactWithSameEmail=ContactInfoUtil.getContactsCount(property.getValue(),contact.getId());
							if(contactWithSameEmail>0){
								info("Contact with the given email already exists");
								duplication=true;
								target.addComponent(feedback);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						if (property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)) {
							String value = property.getActualValue();
							if (property.getActualValue() != null && property.getCategoryValue() != null) {
								value = property.getCategoryValue().getId() + " " + property.getActualValue();
							}
							property.setValue(value);
						}
					}
					if(duplication){
						break;
					} 
				}
				if(!duplication){
					if (actContact.getId() == null) {
						am.getObject().getActivityContacts().add(actContact);
						actContact.setActivity(am.getObject());
					}
		
					AmpAddContactFeaturePanel addContactPanel=(AmpAddContactFeaturePanel)this.getParent().getParent();
					if(addContactPanel.getParent() instanceof AmpContactsSubsectionFeaturePanel){
						AmpContactsSubsectionFeaturePanel section=(AmpContactsSubsectionFeaturePanel)addContactPanel.getParent();
						addContactPanel.setVisible(false);
						target.addComponent(addContactPanel);
						section.getIdsList().removeAll();
						target.appendJavascript(OnePagerConst.getToggleChildrenJS(section));
						target.addComponent(section);
					}
					else{
						if(addContactPanel.getParent() instanceof AmpContactDetailsSubsectionFeaturePanel){
							info("contact updated sucessfully!");
							target.addComponent(feedback);
						}
					}
							
				}

			}
		};
		final AjaxButton cancelContactButton = new AjaxButton("cancelSave") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpAddContactFeaturePanel addContactPanel=(AmpAddContactFeaturePanel)this.getParent().getParent();
				AmpContactsSubsectionFeaturePanel section=(AmpContactsSubsectionFeaturePanel)addContactPanel.getParent();
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(section));
				addContactPanel.setVisible(false);
				target.addComponent(section);
				target.addComponent(addContactPanel);
			}
		};
		cancelContactButton.setDefaultFormProcessing(false);
		nestedAddContactForm.add(saveContactButton);
		nestedAddContactForm.add(cancelContactButton);
		add(nestedAddContactForm);

		this.setOutputMarkupId(true);
		this.setOutputMarkupPlaceholderTag(true);
	}

	public AmpAddContactFeaturePanel(String id, String fmName, IModel<AmpActivityVersion> am, IModel<AmpActivityContact> activityContact) throws Exception{
		this(id, am, fmName, activityContact);
	}
	
}
