package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ContactInfoUtil;

public class ActivityContactInformationAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		EditActivityForm eaForm=(EditActivityForm)form;
		String action=request.getParameter("toDo");		
		String contactType=request.getParameter("contType");
		eaForm.getContactInformation().setAction(action);
		if(action!=null && action.equalsIgnoreCase("add")){ //goto add contact page			
			clearForm(eaForm);			
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
				ContactInfoUtil.copyContactsToSubLists(activityContacts,eaForm);
			}
			clearForm(eaForm);
			eaForm.getContactInformation().setContactType(null);
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
		}
		return retValue;
	}
	
	private void processEdit(EditActivityForm eaForm){
		String tempId=eaForm.getContactInformation().getTemporaryId();
		AmpContact contact=getContactFromList(tempId, eaForm.getContactInformation().getActivityContacts());
		eaForm.getContactInformation().setName(contact.getName());
		eaForm.getContactInformation().setLastname(contact.getLastname());
		eaForm.getContactInformation().setEmail(contact.getEmail());
		eaForm.getContactInformation().setTitle(contact.getTitle());
		eaForm.getContactInformation().setOrganisationName(contact.getOrganisationName());
		eaForm.getContactInformation().setPhone(contact.getPhone());
		eaForm.getContactInformation().setFax(contact.getFax());
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
		ContactInfoUtil.copyContactsToSubLists(eaForm.getContactInformation().getActivityContacts(),eaForm);
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
		contact.setName(eaForm.getContactInformation().getName());
		contact.setLastname(eaForm.getContactInformation().getLastname());
		contact.setEmail(eaForm.getContactInformation().getEmail());
		contact.setTitle(eaForm.getContactInformation().getTitle());
		contact.setOrganisationName(eaForm.getContactInformation().getOrganisationName());
		contact.setPhone(eaForm.getContactInformation().getPhone());
		contact.setFax(eaForm.getContactInformation().getFax());
		
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
			ContactInfoUtil.copyContactsToSubLists(activityContacts,eaForm);
		}
		
		clearForm(eaForm);
		eaForm.getContactInformation().setContactType(null);
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
	
	private void clearForm(EditActivityForm form){
		form.getContactInformation().setName(null);
		form.getContactInformation().setLastname(null);
		form.getContactInformation().setEmail(null);
		form.getContactInformation().setTitle(null);
		form.getContactInformation().setOrganisationName(null);
		form.getContactInformation().setPhone(null);
		form.getContactInformation().setFax(null);
		form.getContactInformation().setTemporaryId(null);
		form.getContactInformation().setPrimaryAllowed(null);
		form.getContactInformation().setContactIds(null);
		form.getContactInformation().setKeyword(null);
		form.getContactInformation().setPrimaryContact("n");
		form.getContactInformation().setContacts(null);		
	}
}
