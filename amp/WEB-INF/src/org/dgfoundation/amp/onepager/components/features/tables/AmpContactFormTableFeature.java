package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;

public class AmpContactFormTableFeature extends AmpFormTableFeaturePanel{
	private final RadioGroup<AmpContact> group = new RadioGroup<AmpContact>("contactsContainer", new Model<AmpContact>());
	public AmpContactFormTableFeature(String id, final IModel<AmpActivityVersion> activityModel , 
			String fmName,List<AmpContact> possibleDuplications)
			throws Exception {
		super(id, activityModel, fmName);
		setTitleHeaderColSpan(6);
		
		list = new ListView<AmpContact>("contactsList", possibleDuplications) {

			@Override
			protected void populateItem(final ListItem<AmpContact> item) {	
                            IModel<AmpContact> contactModel=item.getModel();
                            AmpContact contact = contactModel.getObject();
                final Radio<AmpContact> radio = new Radio<AmpContact>("contactId",contactModel,group);
				radio.add(new AjaxEventBehavior("onchange") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onEvent(AjaxRequestTarget target) {
						group.setModel(radio.getModel());
						//target.addComponent(group);
					}
				});

                item.add(radio);
			    
			    Label name=new Label("firstname", contact.getName());
			    item.add(name);
			    item.add(new Label("lastname", contact.getLastname()));
			    List<AmpContactProperty> emails=new ArrayList<AmpContactProperty>();
			    List<AmpContactProperty> phones=new ArrayList<AmpContactProperty>();
			    List<AmpOrganisationContact> organizations=new ArrayList<AmpOrganisationContact>();
			    Set<AmpOrganisationContact> contactOrganization=contact.getOrganizationContacts();
			    if( contactOrganization!=null){
			    	organizations.addAll( contactOrganization);
			    }
			    String orgName=contact.getOrganisationName();
			    if(orgName!=null&&!orgName.trim().equals("")){ //creating dummy AmpOrganisationContact
			    	AmpOrganisation org=new AmpOrganisation();
			    	org.setName(contact.getOrganisationName());
			    	AmpOrganisationContact orgContact=new AmpOrganisationContact();
			    	orgContact.setOrganisation(org);
			    	organizations.add(orgContact);
			    }
			    if(contact.getProperties()!=null){
			    	for(AmpContactProperty property:contact.getProperties()){
			    		if(property.getName().equals("contact email")){
			    			emails.add(property);
			    		}
			    		else{
			    			if(property.getName().equals("contact phone")){
				    			phones.add(property);
				    		}
			    			
			    		}
			    	}
			    }
			    ListView<AmpContactProperty> contactEmailList = new ListView<AmpContactProperty>("emails", emails) {

                    @Override
                    protected void populateItem(ListItem<AmpContactProperty> item) {
                    	AmpContactProperty email = item.getModel().getObject();
                        Label phn = new Label("email", email.getValue());
                        item.add(phn);
                    }
                };
                
                item.add(contactEmailList);
               
                ListView<AmpOrganisationContact> contactOrganizationList = new ListView<AmpOrganisationContact>("organizations", organizations) {

                    @Override
                    protected void populateItem(ListItem<AmpOrganisationContact> item) {
                    	AmpOrganisationContact orgContact = item.getModel().getObject();
                        Label org = new Label("organization", orgContact.getOrganisation().getName());
                        item.add(org);
                    }
                };
                item.add(contactOrganizationList);
			    
			    ListView<AmpContactProperty> contactPhonesList = new ListView<AmpContactProperty>("phones", phones) {

                    @Override
                    protected void populateItem(ListItem<AmpContactProperty> item) {
                    	AmpContactProperty phone = item.getModel().getObject();
                        Label phn = new Label("phone", phone.getValueAsFormatedPhoneNum());
                        item.add(phn);
                    }
                };
                item.add(contactPhonesList);
	
			  		   
			}
		};
		//contactContainer.setOutputMarkupId(true);
		//contactContainer.add(list);
		group.add(list);
		/*group.add(new AjaxFormComponentUpdatingBehavior("onselect"){
			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				//only to update the underneath model
			}
		});*/
		
		//group.setOutputMarkupId(true);
		add(group);
		
	}
	public RadioGroup<AmpContact> getContactsGroup(){
		return group;
	}

}
