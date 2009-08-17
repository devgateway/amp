package org.digijava.module.aim.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.form.AddressBookForm;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.contacts.jaxb.Contact;
import org.digijava.module.contacts.jaxb.Contacts;
import org.digijava.module.contacts.jaxb.ObjectFactory;

public class AmpContactsExportImport extends DispatchAction{
	
	public ActionForward gotoExportImportPage (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
		return mapping.findForward("gotoPage");
	}
	
	public ActionForward exportContacts (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
		JAXBContext jc = JAXBContext.newInstance("org.digijava.module.contacts.jaxb");
		Marshaller m = jc.createMarshaller();
		response.setContentType("text/xml");
		response.setHeader("content-disposition", "attachment; filename=exportLanguage.xml");
		ObjectFactory objFactory = new ObjectFactory();
		Contacts contacts = objFactory.createContacts();
		Vector<Contact> rsAux=new Vector<Contact>();
		
		List<AmpContact> ampContacts=ContactInfoUtil.getContacts();
		rsAux=buildContactsXML(ampContacts);
		contacts.getContact().addAll(rsAux);
		
		m.marshal(contacts,response.getOutputStream());		
		return null;
	}
	
	public ActionForward importContacts (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
		AddressBookForm myForm=(AddressBookForm)form;
		FormFile myFile = myForm.getFileUploaded();
        byte[] fileData    = myFile.getFileData();
        InputStream inputStream= new ByteArrayInputStream(fileData);        
        
        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.contacts.jaxb");
        Unmarshaller m = jc.createUnmarshaller();
        Contacts contacts;	        
        try {
        	contacts=(Contacts)m.unmarshal(inputStream);
        	if(contacts!=null){
        		List<Contact> conts=contacts.getContact();
        		if(conts!=null){
        			for (Contact contact : conts) {
						AmpContact ampContact=new AmpContact();
						ampContact.setName(contact.getName());
						ampContact.setLastname(contact.getLastname());
						ampContact.setEmail(contact.getEmail());
						ampContact.setTitle(contact.getTitle());
						ampContact.setOrganisationName(contact.getOrganisationName());
						ampContact.setPhone(contact.getPhone());
						ampContact.setFax(contact.getFax());
						ContactInfoUtil.saveOrUpdateContact(ampContact);
					}
        		}
        	}
        }catch(Exception ex){
        	ex.printStackTrace(System.out);
        }
		return mapping.findForward("forward");
	}
	
	private Vector<Contact> buildContactsXML(List<AmpContact> ampContacts){
		Vector<Contact> result=new Vector<Contact>();
		if(ampContacts!=null && ampContacts.size()>0){
			for(AmpContact contact: ampContacts){
				try {
					Contact cont=contact.createContact();
					result.add(cont);
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		}		
		return result;
	}
}
