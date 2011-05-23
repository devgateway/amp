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
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContactsSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpAddContactFeaturePanel extends AmpFeaturePanel<AmpActivityContact> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private AmpActivityContact actContact;
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
			String fmName, IModel<AmpActivityContact> activityContact, boolean visible) throws Exception {
		super(id, fmName);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setFilter(new ContainerFeedbackMessageFilter(AmpAddContactFeaturePanel.this));
		feedback.setOutputMarkupId(true);

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
		add(contactTitle);
		TextField<String> name=new TextField<String>("name");
		name.setRequired(true);
		TextField<String> lastname=new TextField<String>("lastname");
		lastname.setRequired(true);
		add(name);
		add(lastname);
		add(detailEmail);
		add(new TextField<String>("function"));
		add(new TextField<String>("organisationName"));
		add(contactOrganizations);
		add(detailPhone);
		add(detailFax);
		add(new TextArea<String>("officeaddress"));
		final AjaxButton saveContactButton = new AjaxButton("saveContact") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpContact contact = (AmpContact) this.getParent().getDefaultModelObject();
				if (am.getObject().getActivityContacts() == null) {
					am.getObject().setActivityContacts(new HashSet<AmpActivityContact>());
				}
				final Set<AmpContactProperty> contactProperties = contact.getProperties();
				boolean duplication=false;
				for (AmpContactProperty property : contactProperties) {
					if (property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
						int contactWithSameEmail=-1;
						try {
							contactWithSameEmail=ContactInfoUtil.getContactsCount(property.getValue(),null);
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
					AmpContactsSubsectionFeaturePanel section=(AmpContactsSubsectionFeaturePanel)this.getParent().getParent();
					section.getIdsList().removeAll();
					target.appendJavascript(OnePagerConst.getToggleChildrenJS(section));
					this.getParent().setVisible(false);
					target.addComponent(section);
					target.addComponent(this.getParent());
				}

			}
		};
		final AjaxButton cancelContactButton = new AjaxButton("cancelSave") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpContactsSubsectionFeaturePanel section=(AmpContactsSubsectionFeaturePanel)this.getParent().getParent();
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(section));
				this.getParent().setVisible(false);
				target.addComponent(section);
				target.addComponent(this.getParent());
			}
		};
		cancelContactButton.setDefaultFormProcessing(false);
		saveContactButton.setVisible(visible);
		cancelContactButton.setVisible(visible);
		add(saveContactButton);
		add(cancelContactButton);

		this.setOutputMarkupId(true);
		this.setOutputMarkupPlaceholderTag(true);
	}

	public AmpAddContactFeaturePanel(String id, String fmName, IModel<AmpActivityVersion> am, IModel<AmpActivityContact> activityContact) throws Exception{
		this(id, am, fmName, activityContact,false);
	}

}
