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
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.helper.AmpContactsWorker;
import org.digijava.module.aim.helper.Constants;

public class ActivityContactInformationAction extends Action {
	
	public static final String ROOT_TAG = "CONTACTS";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		EditActivityForm eaForm=(EditActivityForm)form;
		String action=request.getParameter("toDo");		
		String contactType=request.getParameter("contType");
		if(action!=null && action.equalsIgnoreCase("delete")){			
			eaForm.getContactInformation().setTemporaryId(request.getParameter("tempId"));
			eaForm.getContactInformation().setContactType(contactType);
			processDelete(eaForm);
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

	
	private void processDelete(EditActivityForm eaForm){
		ActivityContactInfo contactInfo=eaForm.getContactInformation();
		String tempId=contactInfo.getTemporaryId();
		String contactType=contactInfo.getContactType();
		
		
		List<AmpActivityContact> allContacts=new ArrayList<AmpActivityContact>(); //eaForm.getContactInformation().getActivityContacts();
		if(contactInfo.getDonorContacts()!=null && contactInfo.getDonorContacts().size()>0){
			allContacts.addAll(contactInfo.getDonorContacts());
		}
		if(contactInfo.getMofedContacts()!=null && contactInfo.getMofedContacts().size()>0){
			allContacts.addAll(contactInfo.getMofedContacts());
		}
		if(contactInfo.getSectorMinistryContacts()!=null && contactInfo.getSectorMinistryContacts().size()>0){
			allContacts.addAll(contactInfo.getSectorMinistryContacts());
		}
		if(contactInfo.getProjCoordinatorContacts()!=null && contactInfo.getProjCoordinatorContacts().size()>0){
			allContacts.addAll(contactInfo.getProjCoordinatorContacts());
		}
		if(contactInfo.getImplExecutingAgencyContacts()!=null && contactInfo.getImplExecutingAgencyContacts().size()>0){
			allContacts.addAll(contactInfo.getImplExecutingAgencyContacts());
		}
		
		//this list won't be null, cos if we are removing some record, it means that list contains at least that record
		for (AmpActivityContact ampActivityContact : allContacts) {
			if(ampActivityContact.getContact().getTemporaryId().equals(tempId) && ampActivityContact.getContactType().equals(contactType)){
				allContacts.remove(ampActivityContact);
				break;
			}			
		}
		contactInfo.setActivityContacts(allContacts);		
		AmpContactsWorker.copyContactsToSubLists(contactInfo.getActivityContacts(),eaForm);
	}
}
