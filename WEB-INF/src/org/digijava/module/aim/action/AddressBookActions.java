package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.form.AddressBookForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ContactInfoUtil;

public class AddressBookActions extends DispatchAction {
	
	public ActionForward viewAddressBook (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	AddressBookForm myForm=(AddressBookForm)form;
    	
    	if(request.getParameter("reset")!=null && request.getParameter("reset").equals("true")){
    		myForm.setResultsPerPage(10);
    		myForm.setKeyword(null);
    	}
    	
    	int contactsAmount=ContactInfoUtil.getContactsSize();    	
    	//how many pages
    	Collection pages = null;
    	int pagesNum=0;
    	if(contactsAmount % myForm.getResultsPerPage()==0){
    		pagesNum=contactsAmount/myForm.getResultsPerPage();
    	}else{
    		pagesNum=contactsAmount/myForm.getResultsPerPage() +1;
    	}
    	if (pagesNum > 1) {
	          pages = new ArrayList();
	          for (int i = 0; i < pagesNum; i++) {
	            Integer pageNum = new Integer(i + 1);
	            pages.add(pageNum);
	          }
	    }
    	
    	if(myForm.getResultsPerPage()==null){
    		myForm.setResultsPerPage(Constants.CONTACTS_PER_PAGE);
    	}
    	
    	List<AmpContact> pagedContacts=null;
    	pagedContacts=ContactInfoUtil.getPagedContacts(0, myForm.getResultsPerPage(), myForm.getSortBy(),myForm.getKeyword());
    	
    	myForm.setContactsForPage(pagedContacts);
    	myForm.setCurrentPage(new Integer(1));
    	myForm.setPages(pages);
		return mapping.findForward("showAllContacts");
	}
	
	public ActionForward searchContacts (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		int page = 0;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		if (myForm.getResultsPerPage() == 0 ) {
			myForm.setResultsPerPage(Constants.CONTACTS_PER_PAGE);				
		} else {
			int stIndex = 0;
			if(page>1){
				stIndex=(page - 1) * myForm.getResultsPerPage();
			}					
			
			List<AmpContact> pagedContacts=null;
	    	pagedContacts=ContactInfoUtil.getPagedContacts(stIndex, myForm.getResultsPerPage(), myForm.getSortBy(),myForm.getKeyword());
	    	
	    	myForm.setContactsForPage(pagedContacts);			
			myForm.setCurrentPage(new Integer(page));
		}
		return mapping.findForward("showAllContacts");
	}
	
	public ActionForward addContact (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		clearForm(myForm);
		return mapping.findForward("addOrEditContact");
	}
	
	public ActionForward editContact (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;		
		if(myForm.getContactId()!=null){
			Long contactId=myForm.getContactId();
			AmpContact contact=ContactInfoUtil.getContact(contactId);
			if(contact!=null){
				myForm.setName(contact.getName());
				myForm.setLastname(contact.getLastname());
				myForm.setEmail(contact.getEmail());
				myForm.setTitle(contact.getTitle());
				myForm.setOrganisationName(contact.getOrganisationName());
				myForm.setPhone(contact.getPhone());
				myForm.setFax(contact.getFax());
			}
		}
		return mapping.findForward("addOrEditContact");
	}
	
	public ActionForward deleteContact (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		if(myForm.getContactId()!=null){
			Long contactId=myForm.getContactId();
			AmpContact contact=ContactInfoUtil.getContact(contactId);
			if(contact!=null){
				ContactInfoUtil.deleteContact(contact);
			}			
		}
		return viewAddressBook(mapping,myForm,request,response);
	}
	
	public ActionForward saveContact (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		boolean validateData=false;		
		AmpContact contact=null;
		if(myForm.getContactId()!=null){
			contact=ContactInfoUtil.getContact(myForm.getContactId());
			if(!contact.getEmail().trim().equals(myForm.getEmail().trim())){ //if user changed contact email, we should check that that email doesn't exist in db
				validateData=true;
			}	
		}else{
			contact=new AmpContact();
			validateData=true;
		}
		//check unique email 
		if(validateData){
			ActionErrors errors= new ActionErrors();
			int contactWithSameEmail=ContactInfoUtil.getContactsCount(myForm.getEmail());
			if(contactWithSameEmail!=0){
				Site site = RequestUtils.getSite(request);
				Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
						
				Long siteId = site.getId();
				String locale = navigationLanguage.getCode();
				errors.add("email not unique", new ActionError("aim.contact.emailExists",TranslatorWorker.translateText("Contact with the given email already exists",locale,siteId)));
				
				if (errors.size() > 0){
					//we have all the errors for this step saved and we must throw the amp error
					saveErrors(request, errors);
					return mapping.findForward("addOrEditContact");
				}
			}
		}
		contact.setName(myForm.getName().trim());
		contact.setLastname(myForm.getLastname().trim());
		contact.setEmail(myForm.getEmail().trim());
		if(myForm.getTitle()!=null){
			contact.setTitle(myForm.getTitle().trim());
		}
		if(myForm.getOrganisationName()!=null){
			contact.setOrganisationName(myForm.getOrganisationName().trim());
		}
		if(myForm.getPhone()!=null){
			contact.setPhone(myForm.getPhone().trim());
		}
		if(myForm.getFax()!=null){
			contact.setFax(myForm.getFax().trim());
		}
		ContactInfoUtil.saveOrUpdateContact(contact);
		//reset filter 
		myForm.setResultsPerPage(10);
		myForm.setKeyword(null);
		return viewAddressBook(mapping,myForm,request,response);
	}
	
	private void clearForm(AddressBookForm form){
		form.setContactId(null);
		form.setEmail(null);
		form.setFax(null);
		form.setKeyword(null);
		form.setLastname(null);
		form.setName(null);
		form.setOrganisationName(null);
		form.setPhone(null);
		form.setTitle(null);
	}
}
