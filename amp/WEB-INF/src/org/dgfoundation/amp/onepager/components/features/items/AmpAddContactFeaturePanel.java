package org.dgfoundation.amp.onepager.components.features.items;


import java.util.HashSet;
import java.util.List;
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
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;



import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContactsSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ContactPropertyHelper;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpAddContactFeaturePanel extends AmpFeaturePanel<AmpActivityContact> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private AmpActivity act;
    private AmpActivityContact actContact;
    private AmpContactOrganizationFeaturePanel contactOrganizations;
    private AmpContactDetailFeaturePanel detailFax;
    private AmpContactDetailFeaturePanel detailEmail;
    private AmpContactDetailFeaturePanel detailPhone;




    public AmpAddContactFeaturePanel(String id, final AmpActivity ampActivity,
            String fmName,  final AmpActivityContact activityContact, boolean visible) throws Exception {
        super(id, fmName);
        act = ampActivity;
        actContact = activityContact;
        AmpContact newContact = null;
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setFilter(new ContainerFeedbackMessageFilter(AmpAddContactFeaturePanel.this));
        feedback.setOutputMarkupId(true);
        if (activityContact.getContact() == null) {
            newContact = new AmpContact();
        } else {
            newContact = activityContact.getContact();
        }
        final CompoundPropertyModel<AmpContact> contactPropertyModel = new CompoundPropertyModel<AmpContact>(newContact);


        setDefaultModel(contactPropertyModel);
        AmpCategorySelectFieldPanel contactTitle = new AmpCategorySelectFieldPanel(
                "title",
                CategoryConstants.CONTACT_TITLE_KEY,
                new PropertyModel<AmpCategoryValue>(newContact, "title"),
                CategoryConstants.CONTACT_TITLE_NAME, true, true, true);

        
       detailPhone=new AmpContactDetailFeaturePanel("addContactPhone", contactPropertyModel,"Add Contact Phone",true,Constants.CONTACT_PROPERTY_NAME_PHONE); 
       detailFax=new AmpContactDetailFeaturePanel("addContactFax", contactPropertyModel,"Add Contact Fax",true,Constants.CONTACT_PROPERTY_NAME_FAX);  
       detailEmail=new AmpContactDetailFeaturePanel("addContactEmail", contactPropertyModel, "Add Contact Email",true,Constants.CONTACT_PROPERTY_NAME_EMAIL);
      
       
        contactOrganizations = new AmpContactOrganizationFeaturePanel("contactOrganizations",contactPropertyModel, "Contact Organizations", true);
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
                if (act.getActivityContacts() == null) {
                    act.setActivityContacts(new HashSet<AmpActivityContact>());
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
                         act.getActivityContacts().add(actContact);
                         actContact.setActivity(act);
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
     public AmpAddContactFeaturePanel(String id, String fmName,  final AmpActivityContact activityContact) throws Exception{
         this(id,activityContact.getActivity(), fmName,  activityContact,false);
     }
    
}
