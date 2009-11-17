package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.helper.AmpContactsWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ContactPropertyHelper;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ActivityContactInformationAction extends Action {
	
	public static final String ROOT_TAG = "CONTACTS";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		EditActivityForm eaForm=(EditActivityForm)form;
		String action=request.getParameter("toDo");		
		String contactType=request.getParameter("contType");
		eaForm.getContactInformation().setAction(action);
		if(action!=null && action.equalsIgnoreCase("add")){ //goto add contact page			
			clearForm(eaForm);
			//It's a must that contact should have at least one email, so we can create it's empty property
			if(eaForm.getContactInformation().getEmails()==null){
				eaForm.getContactInformation().setEmails(new ContactPropertyHelper[1]);
				eaForm.getContactInformation().getEmails()[0]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_EMAIL);
			}
			eaForm.getContactInformation().setEmailsSize(eaForm.getContactInformation().getEmails().length);
			eaForm.getContactInformation().setContactType(contactType);
			eaForm.getContactInformation().setPrimaryAllowed(isPrimaryContactAllowed(eaForm, contactType));			
			return mapping.findForward("forward");
		}
		if(action!=null && action.equalsIgnoreCase("edit")){
			eaForm.getContactInformation().setTemporaryId(request.getParameter("tempId"));
			eaForm.getContactInformation().setContactType(contactType);
			eaForm.getContactInformation().setPrimaryAllowed(isPrimaryContactAllowed(eaForm, contactType));
			processEdit(eaForm);
			return mapping.findForward("forward");
		}
        if (action != null && action.equalsIgnoreCase("addOrganization")) {
        	if (eaForm.getContactInformation().getTemporaryId()==null||eaForm.getContactInformation().getTemporaryId().length()==0) {
        		eaForm.getContactInformation().setAction("add");
            } else {
            	eaForm.getContactInformation().setAction("edit");
            }
            return mapping.findForward("forward");
        }
        if (action != null && action.equalsIgnoreCase("removeOrganizations")) {
        	Long[] ids = eaForm.getContactInformation().getSelContactOrgs();
            if (ids != null) {
            	for (Long id : ids) {
            		AmpOrganisation organization = DbUtil.getOrganisation(id);
                    eaForm.getContactInformation().getOrganizations().remove(organization);
                }
            }
            if (eaForm.getContactInformation().getTemporaryId() == null || eaForm.getContactInformation().getTemporaryId().length() == 0) {
            	eaForm.getContactInformation().setAction("add");
            } else {
            	eaForm.getContactInformation().setAction("edit");
            }
            return mapping.findForward("forward");
        }
		if(action!=null && action.equalsIgnoreCase("save")){
			processSave(eaForm);
		}
		if(action!=null && action.equalsIgnoreCase("delete")){			
			eaForm.getContactInformation().setTemporaryId(request.getParameter("tempId"));
			eaForm.getContactInformation().setContactType(contactType);
			processDelete(eaForm);
		}
		if(action!=null && action.equalsIgnoreCase("search")){
			String keyword=request.getParameter("keyword");
			if(keyword!=null){
				List<AmpContact> foundContacts=ContactInfoUtil.searchContacts(keyword);
				eaForm.getContactInformation().setContacts(foundContacts);
			}
			return mapping.findForward("forward");
		}
		if(action!=null && action.equalsIgnoreCase("addSelectedConts")){//add contact from existing contacts list
			Long[] contIds=eaForm.getContactInformation().getContactIds();
			if(contIds!=null && contIds.length>0){
				List<AmpActivityContact> activityContacts=eaForm.getContactInformation().getActivityContacts();
				for (Long contId : contIds) {
					AmpContact contact=ContactInfoUtil.getContact(contId);
					AmpActivityContact actContact=new AmpActivityContact();
					actContact.setContactType(eaForm.getContactInformation().getContactType());
					actContact.setContact(contact);
					if(activityContacts==null){
						activityContacts=new ArrayList<AmpActivityContact>();
						eaForm.getContactInformation().setActivityContacts(activityContacts);
					}
					activityContacts.add(actContact);
					//set contact's temporary id in case someone want to create or delete it. temporary id is "-"+ ordinal number in list
					actContact.getContact().setTemporaryId("-"+activityContacts.size());					
				}
				AmpContactsWorker.copyContactsToSubLists(activityContacts,eaForm);
			}
			clearForm(eaForm);
			eaForm.getContactInformation().setContactType(null);
		}
		if(action!=null && action.equalsIgnoreCase("checkDulicateEmail")){
			String params=request.getParameter("params");
			int emailCount=0;
			String contactEmail=null;
			if(params !=null){
				String[] emails=params.split(";");
				AmpContact contact=null;				
				if(eaForm.getContactInformation().getTemporaryId()!=null&&eaForm.getContactInformation().getTemporaryId().length()>0){
					contact = getContactFromList(eaForm.getContactInformation().getTemporaryId(), eaForm.getContactInformation().getActivityContacts());									
				}
				for (int i = 0; i < emails.length; i++) {
					//check that such email doesn't exist in db
					if(contact!=null && contact.getId()!=null){
						emailCount=ContactInfoUtil.getContactsCount(emails[i],contact.getId());
					}else{
						emailCount=ContactInfoUtil.getContactsCount(emails[i],null);
					}
					
					if(emailCount>0){
						contactEmail="exists";
						break;
					}else{
						if(i==emails.length-1){
							contactEmail="notExists";
							break;
						}
					}
					//if it's not in db,we should check whether such email already exist in form.(there are contacts in form,which are not yet saved in db)
//					if(contactEmail.equals("notExists")){
//						for (AmpActivityContact cont : eaForm.getContactInformation().getActivityContacts()) {
//							for (AmpContactProperty property : cont.getContact().getProperties()) {
//								if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().equals(emails[i])){
//									contactEmail="exists";
//									break;
//								}
//							}
//							if(contactEmail.equals("exists")){
//								break;
//							}
//						}
//					}
				}
			}			 

			//creating xml that will be returned			
    		response.setContentType("text/xml");
    		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
    		PrintWriter out = new PrintWriter(outputStream, true);
    		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    		xml += "<" + ROOT_TAG +">";
    		xml+="<"+"contact email=\""+contactEmail+"\" />";
    		xml+="</"+ROOT_TAG+">";
    		out.println(xml);
			out.close();
			// return xml
			outputStream.close();
			return null;
		}
		if(action!=null && action.equalsIgnoreCase("addNewData")){
			String dataName=request.getParameter("data");
			ActivityContactInfo contactInfo=eaForm.getContactInformation();			
			//user clicked Add new Email
			if(dataName.equalsIgnoreCase("email")){
				ContactPropertyHelper [] contactEmails=contactInfo.getEmails();
				contactInfo.setEmails(buildContactPropertiesForAddNewData(contactEmails,Constants.CONTACT_PROPERTY_NAME_EMAIL,contactInfo.getContEmail(),null));
				contactInfo.setEmailsSize(eaForm.getContactInformation().getEmails().length);
				contactInfo.setPhones(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_PHONE,contactInfo.getContPhoneNumber(),contactInfo.getContPhoneType()));
				contactInfo.setFaxes(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_FAX,contactInfo.getContFaxes(),null));
			}
			//user clicked Add new Phone
			if(dataName.equalsIgnoreCase("phone")){
				ContactPropertyHelper [] contactPhones=contactInfo.getPhones();
				contactInfo.setPhones(buildContactPropertiesForAddNewData(contactPhones,Constants.CONTACT_PROPERTY_NAME_PHONE,contactInfo.getContPhoneNumber(),contactInfo.getContPhoneType()));
				contactInfo.setPhonesSize(contactInfo.getPhones().length);
				contactInfo.setEmails(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_EMAIL,contactInfo.getContEmail(),null));
				contactInfo.setFaxes(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_FAX,contactInfo.getContFaxes(),null));
			}
			//user clicked Add New Fax
			if(dataName.equalsIgnoreCase("fax")){
				ContactPropertyHelper [] contactFaxes=eaForm.getContactInformation().getFaxes();
				contactInfo.setFaxes(buildContactPropertiesForAddNewData(contactFaxes,Constants.CONTACT_PROPERTY_NAME_FAX,contactInfo.getContFaxes(),null));				 
				contactInfo.setFaxesSize(contactInfo.getFaxes().length);
				contactInfo.setEmails(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_EMAIL,contactInfo.getContEmail(),null));
				contactInfo.setPhones(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_PHONE,contactInfo.getContPhoneNumber(),contactInfo.getContPhoneType()));
			}
			if (eaForm.getContactInformation().getTemporaryId() == null || eaForm.getContactInformation().getTemporaryId().length() == 0) {
            	eaForm.getContactInformation().setAction("add");
            } else {
            	eaForm.getContactInformation().setAction("edit");
            }			
			return mapping.findForward("forward");
		}
		if(action!=null && action.equalsIgnoreCase("removeData")){
			ActivityContactInfo contactInfo=eaForm.getContactInformation();
			String dataName=request.getParameter("dataName");
			String ind=request.getParameter("index");
			int index=new Integer(ind).intValue();
			String [] myArray=null;
			if(dataName!=null){
				if(dataName.equalsIgnoreCase("email")){
					myArray=new String[contactInfo.getEmails().length-1];
					if(myArray.length!=0){
						int j=0;
						for(int i=0; i< contactInfo.getEmails().length; i++){
							if(index!=i){
								myArray[j]=contactInfo.getEmails(i).getValue();
								j++;
							}
						}
					}
					contactInfo.setContEmail(myArray);
				}else if(dataName.equalsIgnoreCase("phone")){
					myArray=new String[contactInfo.getPhones().length-1];
					if(myArray.length!=0){
						int j=0;
						for(int i=0; i< contactInfo.getPhones().length; i++){
							if(index!=i){
								myArray[j]=contactInfo.getPhones(i).getValue();
								j++;
							}
						}
						contactInfo.setContPhoneNumber(myArray);					
					}else{
						contactInfo.setContPhoneNumber(null);					
					}
				}else if(dataName.equalsIgnoreCase("fax")){
					myArray=new String[contactInfo.getFaxes().length-1];
					if(myArray.length!=0){
						int j=0;
						for(int i=0; i< contactInfo.getFaxes().length; i++){
							if(index!=i){
								myArray[j]=contactInfo.getFaxes(i).getValue();
								j++;
							}
						}
						contactInfo.setContFaxes(myArray);
					}else{
						contactInfo.setContFaxes(null);
					}
				}
			}
			//fill form
			fillContactProperties(eaForm);
			 if (eaForm.getContactInformation().getTemporaryId() == null || eaForm.getContactInformation().getTemporaryId().length() == 0) {
	            	eaForm.getContactInformation().setAction("add");
	            } else {
	            	eaForm.getContactInformation().setAction("edit");
	            }
			return mapping.findForward("forward");
		}
		return mapping.findForward("step8");
	}

	private boolean isPrimaryContactAllowed(EditActivityForm eaForm,String contactType) {
		boolean retValue=true;
		List<AmpActivityContact> activityContacts=null;
		if(contactType.equals(Constants.DONOR_CONTACT)){
			if(eaForm.getContactInformation().getDonorContacts()!=null && eaForm.getContactInformation().getDonorContacts().size()>0){
				activityContacts=eaForm.getContactInformation().getDonorContacts();
				for (AmpActivityContact activityContact : activityContacts) {
					//if any contact is already primary,then user shouldn't be able to create another primary contact
					if(activityContact.getPrimaryContact()!=null && activityContact.getPrimaryContact()){
						retValue=false;
						break;
					}
				}
			}
		}else if(contactType.equals(Constants.MOFED_CONTACT)){
			if(eaForm.getContactInformation().getMofedContacts()!=null && eaForm.getContactInformation().getMofedContacts().size()>0){
				activityContacts=eaForm.getContactInformation().getMofedContacts();
				for (AmpActivityContact activityContact : activityContacts) {
					//if any contact is already primary,then user shouldn't be able to create another primary contact
					if(activityContact.getPrimaryContact()!=null && activityContact.getPrimaryContact()){
						retValue=false;
						break;
					}
				}
			}
		}else if(contactType.equals(Constants.PROJECT_COORDINATOR_CONTACT)){
			if(eaForm.getContactInformation().getProjCoordinatorContacts()!=null && eaForm.getContactInformation().getProjCoordinatorContacts().size()>0){
				activityContacts=eaForm.getContactInformation().getProjCoordinatorContacts();
				for (AmpActivityContact activityContact : activityContacts) {
					//if any contact is already primary,then user shouldn't be able to create another primary contact
					if(activityContact.getPrimaryContact()!=null && activityContact.getPrimaryContact()){
						retValue=false;
						break;
					}
				}
			}
		}else if(contactType.equals(Constants.SECTOR_MINISTRY_CONTACT)){
			if(eaForm.getContactInformation().getSectorMinistryContacts()!=null && eaForm.getContactInformation().getSectorMinistryContacts().size()>0){
				activityContacts=eaForm.getContactInformation().getSectorMinistryContacts();
				for (AmpActivityContact activityContact : activityContacts) {
					//if any contact is already primary,then user shouldn't be able to create another primary contact
					if(activityContact.getPrimaryContact()!=null && activityContact.getPrimaryContact()){
						retValue=false;
						break;
					}
				}
			}
		}else if (contactType.equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)){
			if(eaForm.getContactInformation().getImplExecutingAgencyContacts()!=null && eaForm.getContactInformation().getImplExecutingAgencyContacts().size()>0){
				activityContacts=eaForm.getContactInformation().getImplExecutingAgencyContacts();
				for (AmpActivityContact activityContact : activityContacts) {
					//if any contact is already primary,then user shouldn't be able to create another primary contact
					if(activityContact.getPrimaryContact()!=null && activityContact.getPrimaryContact()){
						retValue=false;
						break;
					}
				}
			}
		}
		return retValue;
	}
	
	private void processEdit(EditActivityForm eaForm){
		String tempId=eaForm.getContactInformation().getTemporaryId();
		AmpContact contact=getContactFromList(tempId, eaForm.getContactInformation().getActivityContacts());
		eaForm.getContactInformation().setName(contact.getName());
		eaForm.getContactInformation().setLastname(contact.getLastname());
        if(contact.getTitle()!=null){
        	eaForm.getContactInformation().setTitle(contact.getTitle().getId());
        }else{
        	eaForm.getContactInformation().setTitle(null);
        }
		eaForm.getContactInformation().setOrganisationName(contact.getOrganisationName());
		eaForm.getContactInformation().setFunction(contact.getFunction());
		eaForm.getContactInformation().setOfficeaddress(contact.getOfficeaddress());
        eaForm.getContactInformation().setOrganizations(new ArrayList<AmpOrganisation>());
        if(contact.getOrganizations()!=null){
        	eaForm.getContactInformation().getOrganizations().addAll(contact.getOrganizations());
        }
        
        List<ContactPropertyHelper> contactProperties=AmpContactsWorker.buildHelperContactProperties(contact.getProperties()); //properties can't be null, cos contact has to have at lets 1 email
		
		List<ContactPropertyHelper> emails=null;
		List<ContactPropertyHelper> phones=null;
		List<ContactPropertyHelper> faxes=null;
		if(contactProperties!=null){
			for (ContactPropertyHelper property : contactProperties) {
				if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
					if(emails==null){
						emails=new ArrayList<ContactPropertyHelper>();
					}
					emails.add(property);
				}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)){
					if(phones==null){
						phones=new ArrayList<ContactPropertyHelper>();
					}
					phones.add(property);
				}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX)){
					if(faxes==null){
						faxes=new ArrayList<ContactPropertyHelper>();
					}
					faxes.add(property);
				}
			}
		}
		
		if(emails!=null){
			eaForm.getContactInformation().setEmails(emails.toArray(new ContactPropertyHelper[emails.size()]));
			eaForm.getContactInformation().setEmailsSize(eaForm.getContactInformation().getEmails().length);
		}
		if(phones!=null){
			eaForm.getContactInformation().setPhones(phones.toArray(new ContactPropertyHelper[phones.size()]));
			eaForm.getContactInformation().setPhonesSize(eaForm.getContactInformation().getPhones().length);
		}
		if(faxes!=null){
			eaForm.getContactInformation().setFaxes(faxes.toArray(new ContactPropertyHelper[faxes.size()]));
			eaForm.getContactInformation().setFaxesSize(eaForm.getContactInformation().getFaxes().length);
		}
        
		//get activity contact
		AmpActivityContact actContact=getActivityContactFromList(tempId,eaForm.getContactInformation().getActivityContacts());
		if(actContact.getPrimaryContact()!=null && actContact.getPrimaryContact()){
			eaForm.getContactInformation().setPrimaryContact("true");
		}else{
			eaForm.getContactInformation().setPrimaryContact("false");
		}
	}
	
	private void processDelete(EditActivityForm eaForm){
		String tempId=eaForm.getContactInformation().getTemporaryId();
		String contactType=eaForm.getContactInformation().getContactType();
		List<AmpActivityContact> allContacts=eaForm.getContactInformation().getActivityContacts();	
		//this list won't be null, cos if we are removing some record, it means that list contains at least that record
		for (AmpActivityContact ampActivityContact : allContacts) {
			if(ampActivityContact.getContact().getTemporaryId().equals(tempId) && ampActivityContact.getContactType().equals(contactType)){
				allContacts.remove(ampActivityContact);
				break;
			}			
		}		
		AmpContactsWorker.copyContactsToSubLists(eaForm.getContactInformation().getActivityContacts(),eaForm);
	}
	
	private void processSave(EditActivityForm eaForm){
		AmpContact contact=null;
		String tempId=eaForm.getContactInformation().getTemporaryId();
		if(tempId!=null && tempId.length()>0){ //tempId!=null means that we are editing contact. So all contact's list  already contains current contact 
			contact=getContactFromList(tempId,eaForm.getContactInformation().getActivityContacts());
		}else{
			//create new  contact
			contact=new AmpContact();
		}			
		contact.setName(eaForm.getContactInformation().getName().trim());
		contact.setLastname(eaForm.getContactInformation().getLastname().trim());
		if(eaForm.getContactInformation().getTitle()!=null){
			contact.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getContactInformation().getTitle()));
		}else{
			contact.setTitle(null);
		}
		if(eaForm.getContactInformation().getOrganisationName()!=null){
			contact.setOrganisationName(eaForm.getContactInformation().getOrganisationName().trim());
		}else{
			contact.setOrganisationName(null);
		}	
		if(eaForm.getContactInformation().getFunction()!=null){
			contact.setFunction(eaForm.getContactInformation().getFunction().trim());
		}else{
			contact.setFunction(null);
		}	
		if(eaForm.getContactInformation().getOfficeaddress()!=null){
			contact.setOfficeaddress(eaForm.getContactInformation().getOfficeaddress().trim());
		}else{
			contact.setOfficeaddress(null);
		}
        if(contact.getOrganizations()!=null){
        	contact.getOrganizations().clear();
        }else{
        	contact.setOrganizations(new HashSet<AmpOrganisation>());
        }
        if(eaForm.getContactInformation().getOrganizations()!=null){
        	contact.getOrganizations().addAll(eaForm.getContactInformation().getOrganizations());
        }
        
        Set<AmpContactProperty> contactProperties=new HashSet<AmpContactProperty>();
        if(eaForm.getContactInformation().getEmails()!=null){
        	contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(eaForm.getContactInformation().getEmails()));
        }        
		if(eaForm.getContactInformation().getFaxes()!=null){
			contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(eaForm.getContactInformation().getFaxes()));
		}
		if(eaForm.getContactInformation().getPhones()!=null){
			contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(eaForm.getContactInformation().getPhones()));
		}
		contact.setProperties(contactProperties);
		
		if(tempId==null || tempId.equals("")){ // we are adding contact,not editing. So we should put it in the list of all contacts
			//create activity contact
			AmpActivityContact actContact=new AmpActivityContact();	
			actContact.setContact(contact);
			actContact.setContactType(eaForm.getContactInformation().getContactType());			
			//get activity's all contact's list and add there newly created activity contact
			List<AmpActivityContact> activityContacts=eaForm.getContactInformation().getActivityContacts();
			if(activityContacts==null){
				activityContacts=new ArrayList<AmpActivityContact>();
				eaForm.getContactInformation().setActivityContacts(activityContacts);
			}
			activityContacts.add(actContact);
			//set contact's temporary id in case someone want to create or delete it. temporary id is "-"+ ordinal number in list
			actContact.getContact().setTemporaryId("-"+activityContacts.size());
			AmpContactsWorker.copyContactsToSubLists(activityContacts,eaForm);
		}
		
		clearForm(eaForm);
		eaForm.getContactInformation().setContactType(null);
	}
	
	private void fillContactProperties(EditActivityForm myForm){
		ActivityContactInfo contactInfo=myForm.getContactInformation();
		contactInfo.setEmails(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_EMAIL,contactInfo.getContEmail(),null));
		contactInfo.setPhones(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_PHONE,contactInfo.getContPhoneNumber(),contactInfo.getContPhoneType()));
		contactInfo.setFaxes(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_FAX,contactInfo.getContFaxes(),null));
		if(contactInfo.getEmails()!=null){
			contactInfo.setEmailsSize(contactInfo.getEmails().length);
		}else{
			contactInfo.setEmailsSize(0);
		}
		if(contactInfo.getPhones()!=null){
			contactInfo.setPhonesSize(contactInfo.getPhones().length);
		}else{
			contactInfo.setPhonesSize(0);
		}
		if(contactInfo.getFaxes()!=null){
			contactInfo.setFaxesSize(contactInfo.getFaxes().length);
		}else{
			contactInfo.setFaxesSize(0);
		}
		
	}
	
	private AmpContact getContactFromList(String contactTemporaryId,List<AmpActivityContact> actContacts){
		AmpContact contact=null;
		for (AmpActivityContact ampActivityContact : actContacts) {
			if(ampActivityContact.getContact().getTemporaryId().equals(contactTemporaryId)){
				contact=ampActivityContact.getContact();
				break;
			}
		}
		return contact;
	}
	
	private AmpActivityContact getActivityContactFromList(String contactTemporaryId,List<AmpActivityContact> actContacts){
		AmpActivityContact actCont=null;
		for (AmpActivityContact ampActivityContact : actContacts) {
			if(ampActivityContact.getContact().getTemporaryId().equals(contactTemporaryId)){
				actCont=ampActivityContact;
				break;
			}
		}
		return actCont;
	}

	//fills properties array from form submitted values  ---- ADD NEW DATA !
	private ContactPropertyHelper[] buildContactPropertiesForAddNewData(ContactPropertyHelper [] oldProperties, String propertyName, String [] submittedValues, String[] phoneTypes){
		ContactPropertyHelper [] retVal=null;
		if(oldProperties==null){
			retVal=new ContactPropertyHelper[1];
			retVal[0]=AmpContactsWorker.createProperty(propertyName);
		}else{
			retVal=new ContactPropertyHelper [oldProperties.length+1];
			for(int i=0; i<oldProperties.length;i++){
				oldProperties[i].setValue(submittedValues[i]);
				if(propertyName.equals(Constants.CONTACT_PROPERTY_NAME_PHONE)){
					oldProperties[i].setPhoneType(phoneTypes[i]);
				}
			}
			System.arraycopy(oldProperties, 0, retVal, 0, oldProperties.length);
			retVal[oldProperties.length]=AmpContactsWorker.createProperty(propertyName);			
		}	
		return retVal;
	}
	
	private ContactPropertyHelper[] buildContactPropertiesBeforeRemoveData(String propertyName, String [] submittedValues, String[] phoneTypes){
		ContactPropertyHelper [] retVal=null;
		if(submittedValues!=null){
			retVal=new ContactPropertyHelper [submittedValues.length];
			for(int i=0; i<submittedValues.length;i++){
				if(propertyName.equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && phoneTypes!=null){
					retVal[i]=AmpContactsWorker.createProperty(propertyName, submittedValues[i], phoneTypes[i]);
				}else{
					retVal[i]=AmpContactsWorker.createProperty(propertyName, submittedValues[i], null);
				}
			}
		}
		return retVal;
	}
	
	private void clearForm(EditActivityForm form){
		form.getContactInformation().setName(null);
		form.getContactInformation().setLastname(null);
		form.getContactInformation().setTitle(null);
		form.getContactInformation().setOrganisationName(null);
		form.getContactInformation().setTemporaryId(null);
		form.getContactInformation().setPrimaryAllowed(null);
		form.getContactInformation().setContactIds(null);
		form.getContactInformation().setKeyword(null);
		form.getContactInformation().setPrimaryContact("n");
		form.getContactInformation().setContacts(null);
		form.getContactInformation().setFunction(null);
		form.getContactInformation().setOfficeaddress(null);
        form.getContactInformation().setOrganizations(null);
        form.getContactInformation().setEmails(null);
        form.getContactInformation().setPhones(null);
        form.getContactInformation().setFaxes(null);
        form.getContactInformation().setEmailsSize(0);
        form.getContactInformation().setPhonesSize(0);
        form.getContactInformation().setFaxesSize(0);
        form.getContactInformation().setContEmail(null);
        form.getContactInformation().setContPhoneType(null);
        form.getContactInformation().setContPhoneNumber(null);
        form.getContactInformation().setContFaxes(null);
	}
}
