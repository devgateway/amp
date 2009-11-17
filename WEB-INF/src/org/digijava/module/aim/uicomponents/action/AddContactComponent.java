package org.digijava.module.aim.uicomponents.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.AmpContactsWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ContactPropertyHelper;
import org.digijava.module.aim.uicomponents.AddContact;
import org.digijava.module.aim.uicomponents.EditContactLink;
import org.digijava.module.aim.uicomponents.form.AddContactComponentForm;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;


public class AddContactComponent extends DispatchAction{
     private static Logger logger = Logger.getLogger(AddContactComponent.class);
     public static final String ROOT_TAG = "CONTACTS";

    @Override
     protected  ActionForward unspecified(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        return create(mapping, form, request, response);
    }

     public  ActionForward create(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
         AddContactComponentForm createForm = (AddContactComponentForm) form;
         HttpSession session = request.getSession();
         clearForm(createForm);
         Object targetForm = session.getAttribute(AddContact.PARAM_ADD_CONTACT_FORM_NAME);
         createForm.setTargetForm(targetForm);
         String collection = request.getParameter(AddContact.PARAM_COLLECTION_NAME);
         createForm.setTargetCollection(collection);
         createForm.setAction("add");
         //It's a must that contact should have at least one email, so we can create it's empty property
		 if(createForm.getEmails()==null){
			 createForm.setEmails(new ContactPropertyHelper[1]);
			 createForm.getEmails()[0]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_EMAIL);
		 }
		 createForm.setEmailsSize(createForm.getEmails().length);
         return mapping.findForward("forward");
    }

	private void clearForm(AddContactComponentForm createForm) {
		createForm.setKeyword(null);
         createForm.setContacts(null);
         createForm.setLastname(null);
         createForm.setFirstName(null);
         createForm.setOrganisationName(null);
         createForm.setSelContactIds(null);
         createForm.setTitle(null);
         createForm.setFunction(null);
         createForm.setOfficeaddress(null);
         createForm.setContactId(null);
         createForm.setTemporaryId(null);
         createForm.setOrganizations(null);
         createForm.setEmails(null);
         createForm.setPhones(null);
         createForm.setFaxes(null);
         createForm.setEmailsSize(0);
         createForm.setPhonesSize(0);
         createForm.setFaxesSize(0);
         createForm.setContEmail(null);
         createForm.setContPhoneType(null);
         createForm.setContPhoneNumber(null);
         createForm.setContFaxes(null);
	}

      public  ActionForward addOrganizations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         AddContactComponentForm createForm = (AddContactComponentForm) form;
         return setAction(createForm, mapping);
    }

     public  ActionForward removeOrganizations(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
         AddContactComponentForm createForm = (AddContactComponentForm) form;
         Long[] ids=createForm.getSelContactOrgs();
         if(ids!=null){
             for(Long id :ids){
                 AmpOrganisation organization=DbUtil.getOrganisation(id);
                 createForm.getOrganizations().remove(organization);
             }
         }
        return setAction(createForm, mapping);
    }

      public ActionForward edit(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        clearForm(createForm);
        String contId = request.getParameter(EditContactLink.PARAM_CONTACT_ID);
        //AmpContact contact= ContactInfoUtil.getContact(contactId);
        HttpSession session = request.getSession();
        Object targetForm = session.getAttribute(EditContactLink.PARAM_EDIT_CONTACT_FORM_NAME);
        createForm.setTargetForm(targetForm);
        String collection = request.getParameter(EditContactLink.PARAM_COLLECTION_NAME);
        createForm.setTargetCollection(collection);
        Field target = createForm.getTargetForm().getClass().getDeclaredField(createForm.getTargetCollection());
        target.setAccessible(true);
        Collection<AmpContact> targetCollecion = (Collection<AmpContact>) target.get(createForm.getTargetForm());
        Iterator<AmpContact> contcatIter = targetCollecion.iterator();
        while (contcatIter.hasNext()) {
            AmpContact contact = contcatIter.next();
            boolean compareIds=false;
            try{
                  compareIds=contact.getId() != null && contact.getId().equals(Long.valueOf(contId));
                  if(!compareIds){
                       compareIds=contact.getTemporaryId()!=null&&contact.getTemporaryId().equals(contId);
                  }
            }
            catch(NumberFormatException ex){
                compareIds=contact.getTemporaryId()!=null && contact.getTemporaryId().equals(contId);
            }

            if(compareIds){
                createForm.setKeyword(null);
                createForm.setContacts(null);
                createForm.setContactId(contact.getId());
                createForm.setTemporaryId(contact.getTemporaryId());
                createForm.setLastname(contact.getLastname());
                createForm.setFirstName(contact.getName());
                createForm.setOrganisationName(contact.getOrganisationName());
                createForm.setSelContactIds(null);
                if (contact.getTitle() != null) {
                    createForm.setTitle(contact.getTitle().getId());
                }
                createForm.setOrganizations(new ArrayList<AmpOrganisation>());
                if (contact.getOrganizations() != null) {
                    createForm.getOrganizations().addAll(contact.getOrganizations());
                }
                createForm.setFunction(contact.getFunction());
                createForm.setOfficeaddress(contact.getOfficeaddress());
                
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
        			createForm.setEmails(emails.toArray(new ContactPropertyHelper[emails.size()]));
        			createForm.setEmailsSize(createForm.getEmails().length);
        		}
        		if(phones!=null){
        			createForm.setPhones(phones.toArray(new ContactPropertyHelper[phones.size()]));
        			createForm.setPhonesSize(createForm.getPhones().length);
        		}
        		if(faxes!=null){
        			createForm.setFaxes(faxes.toArray(new ContactPropertyHelper[faxes.size()]));
        			createForm.setFaxesSize(createForm.getFaxes().length);
        		}
            }

        }
        return mapping.findForward("forward");
    }
      
    public ActionForward search(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        String keyword = createForm.getKeyword();

        if (keyword != null) {
            List<AmpContact> foundContacts = ContactInfoUtil.searchContacts(keyword);
            createForm.setContacts(foundContacts);
        }
        return mapping.findForward("forward");

    }
    
    public ActionForward checkDulicateEmail(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        String params=request.getParameter("params");
		int emailCount=0;
		String contactEmail=null;
		if(params !=null){
			String[] emails=params.split(";");
			AmpContact contact=null;
			if(createForm.getContactId()!=null){
				contact=ContactInfoUtil.getContact(createForm.getContactId());
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
			}
		}
        //creating xml that will be returned
        response.setContentType("text/xml");
        OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + ROOT_TAG + ">";
        xml += "<" + "contact email=\"" + contactEmail + "\" />";
        xml += "</" + ROOT_TAG + ">";
        out.println(xml);
        out.close();
        // return xml
        outputStream.close();
        return null;

    }
    public ActionForward addSelectedConts(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        Field target = createForm.getTargetForm().getClass().getDeclaredField(createForm.getTargetCollection());
        target.setAccessible(true);
        Collection<AmpContact> sortedtargetCollecion = new TreeSet<AmpContact>(new AmpContactCompare());
        Collection<AmpContact> targetCollecion = (Collection<AmpContact>) target.get(createForm.getTargetForm());
        if (targetCollecion != null) {
            sortedtargetCollecion.addAll(targetCollecion);
        }
        else{
            targetCollecion = new ArrayList<AmpContact>();
        }
        Long[] contIds = createForm.getSelContactIds();
        if (contIds != null && contIds.length > 0) {
            for (int i = 0; i < contIds.length; i++) {
                AmpContact contact = ContactInfoUtil.getContact(contIds[i]);
                if (!sortedtargetCollecion.contains(contact)) {
                    sortedtargetCollecion.add(contact);
                } else {
                    sortedtargetCollecion.remove(contact);
                    sortedtargetCollecion.add(contact);
                }
            }

        }
        targetCollecion.clear();
        targetCollecion.addAll(sortedtargetCollecion);
        target.set(createForm.getTargetForm(), targetCollecion);

        return mapping.findForward("edit");

    }
    
     public ActionForward save(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
          AddContactComponentForm createForm = (AddContactComponentForm) form;
          AmpContact contact=null;
          if(createForm.getContactId()==null||createForm.getContactId()==0){
               contact=new AmpContact();
               if(createForm.getTemporaryId()==null || createForm.getTemporaryId().length()==0){
                   contact.setTemporaryId("_"+new Date().getTime());
               }else{
                   contact.setTemporaryId(createForm.getTemporaryId());
               }            
          }
          else{
               contact=ContactInfoUtil.getContact(createForm.getContactId());
          }
		contact.setName(createForm.getFirstName().trim());
		contact.setLastname(createForm.getLastname().trim());
        if(createForm.getTitle()!=null&&createForm.getTitle()!=-1){
        	contact.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(createForm.getTitle()));
        }		
		if(createForm.getOrganisationName()!=null){
			contact.setOrganisationName(createForm.getOrganisationName().trim());
		}else{
			contact.setOrganisationName(null);
		}
		if(createForm.getFunction()!=null){
			contact.setFunction(createForm.getFunction().trim());
		}else{
			contact.setFunction(null);
		}
		if(createForm.getOfficeaddress()!=null){
			contact.setOfficeaddress(createForm.getOfficeaddress().trim());
		}else{
			contact.setOfficeaddress(null);
		}
        if (contact.getOrganizations() == null) {
        	contact.setOrganizations(new HashSet<AmpOrganisation>());
        }
        if (createForm.getOrganizations() != null) {
        	contact.getOrganizations().clear();
            contact.getOrganizations().addAll(createForm.getOrganizations());
        }
        
        Set<AmpContactProperty> contactProperties=new HashSet<AmpContactProperty>();
        if(createForm.getEmails()!=null){
        	contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(createForm.getEmails()));
        }        
		if(createForm.getFaxes()!=null){
			contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(createForm.getFaxes()));
		}
		if(createForm.getPhones()!=null){
			contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(createForm.getPhones()));
		}
		contact.setProperties(contactProperties);
        
        Field target = createForm.getTargetForm().getClass().getDeclaredField(createForm.getTargetCollection());
        target.setAccessible(true);
        Collection<AmpContact> sortedtargetCollecion = new TreeSet<AmpContact>(new AmpContactCompare());
        Collection<AmpContact> targetCollecion = (Collection<AmpContact>) target.get(createForm.getTargetForm());
        if (targetCollecion != null) {
        	sortedtargetCollecion.addAll(targetCollecion);
        } else {
            targetCollecion = new ArrayList<AmpContact>();
        }
        if (!sortedtargetCollecion.contains(contact)) {
        	sortedtargetCollecion.add(contact);
        } else {
            //removing because properties may be changed...
            sortedtargetCollecion.remove(contact);
            sortedtargetCollecion.add(contact);
        }
        targetCollecion.clear();
        targetCollecion.addAll(sortedtargetCollecion);

        target.set(createForm.getTargetForm(), targetCollecion);
        return mapping.findForward("edit");
        /* ContactInfoUtil.saveOrUpdateContact(contact);
        createForm.setSelContactIds(new Long[]{contact.getId()});
        return addSelectedConts(mapping, form, request, response);*/

     }
     
     public ActionForward addNewData(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	 AddContactComponentForm myForm = (AddContactComponentForm) form;
    	 String dataName=request.getParameter("data");			
			//user clicked Add new Email
			if(dataName.equalsIgnoreCase("email")){
				ContactPropertyHelper [] contactEmails=myForm.getEmails();
				myForm.setEmails(buildContactPropertiesForAddNewData(contactEmails,Constants.CONTACT_PROPERTY_NAME_EMAIL,myForm.getContEmail(),null));
				myForm.setEmailsSize(myForm.getEmails().length);
				myForm.setPhones(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_PHONE,myForm.getContPhoneNumber(),myForm.getContPhoneType()));
				myForm.setFaxes(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_FAX,myForm.getContFaxes(),null));
			}
			//user clicked Add new Phone
			if(dataName.equalsIgnoreCase("phone")){
				ContactPropertyHelper [] contactPhones=myForm.getPhones();
				myForm.setPhones(buildContactPropertiesForAddNewData(contactPhones,Constants.CONTACT_PROPERTY_NAME_PHONE,myForm.getContPhoneNumber(),myForm.getContPhoneType()));
				myForm.setPhonesSize(myForm.getPhones().length);
				myForm.setEmails(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_EMAIL,myForm.getContEmail(),null));
				myForm.setFaxes(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_FAX,myForm.getContFaxes(),null));
			}
			//user clicked Add New Fax
			if(dataName.equalsIgnoreCase("fax")){
				ContactPropertyHelper [] contactFaxes=myForm.getFaxes();
				myForm.setFaxes(buildContactPropertiesForAddNewData(contactFaxes,Constants.CONTACT_PROPERTY_NAME_FAX,myForm.getContFaxes(),null));				 
				myForm.setFaxesSize(myForm.getFaxes().length);
				myForm.setEmails(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_EMAIL,myForm.getContEmail(),null));
				myForm.setPhones(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_PHONE,myForm.getContPhoneNumber(),myForm.getContPhoneType()));
			}
			
			 return setAction(myForm, mapping);
     }
     
     public ActionForward removeData(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	AddContactComponentForm myForm = (AddContactComponentForm) form;
    	String dataName=request.getParameter("dataName");
		String ind=request.getParameter("index");
		int index=new Integer(ind).intValue();
		String [] myArray=null;
		if(dataName!=null){
			if(dataName.equalsIgnoreCase("email")){
				myArray=new String[myForm.getEmails().length-1];
				if(myArray.length!=0){
					int j=0;
					for(int i=0; i< myForm.getContEmail().length; i++){
						if(index!=i){
							myArray[j]=myForm.getContEmail()[i];
							j++;
						}
					}
				}
				myForm.setContEmail(myArray);
			}else if(dataName.equalsIgnoreCase("phone")){
				myArray=new String[myForm.getContPhoneNumber().length-1];
				if(myArray.length!=0){
					int j=0;
					for(int i=0; i< myForm.getContPhoneNumber().length; i++){
						if(index!=i){
							myArray[j]=myForm.getContPhoneNumber()[i];
							j++;
						}
					}
					myForm.setContPhoneNumber(myArray);					
				}else{
					myForm.setContPhoneNumber(null);					
				}
			}else if(dataName.equalsIgnoreCase("fax")){
				myArray=new String[myForm.getContFaxes().length-1];
				if(myArray.length!=0){
					int j=0;
					for(int i=0; i< myForm.getContFaxes().length; i++){
						if(index!=i){
							myArray[j]=myForm.getContFaxes()[i];
							j++;
						}
					}
					myForm.setContFaxes(myArray);
				}else{
					myForm.setContFaxes(null);
				}
			}
		}
		//fill form
		fillContactProperties(myForm);
		return setAction(myForm, mapping);
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

    
 	private ActionForward setAction(AddContactComponentForm createForm, ActionMapping mapping) {
        if (createForm.getContactId() == null || createForm.getContactId() == 0) {
            createForm.setAction("add");
        } else {
            createForm.setAction("edit");
        }
        return mapping.findForward("forward");
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
    
    private void fillContactProperties(AddContactComponentForm myForm){
    	myForm.setEmails(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_EMAIL,myForm.getContEmail(),null));
    	myForm.setPhones(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_PHONE,myForm.getContPhoneNumber(),myForm.getContPhoneType()));
    	myForm.setFaxes(buildContactPropertiesBeforeRemoveData(Constants.CONTACT_PROPERTY_NAME_FAX,myForm.getContFaxes(),null));
		if(myForm.getEmails()!=null){
			myForm.setEmailsSize(myForm.getEmails().length);
		}else{
			myForm.setEmailsSize(0);
		}
		if(myForm.getPhones()!=null){
			myForm.setPhonesSize(myForm.getPhones().length);
		}else{
			myForm.setPhonesSize(0);
		}
		if(myForm.getFaxes()!=null){
			myForm.setFaxesSize(myForm.getFaxes().length);
		}else{
			myForm.setFaxesSize(0);
		}
	}

    class AmpContactCompare implements Comparator<AmpContact> {
        public int compare(AmpContact cont1, AmpContact cont2) {
            if(cont1.getId()!=null&&cont2.getId()!=null){
                return cont1.getId().compareTo(cont2.getId());
            }
            else{
                 if(cont1.getId()==null&&cont2.getId()==null){
                       return cont1.getTemporaryId().compareTo(cont2.getTemporaryId());
                 }
                 else{
                     if(cont1.getId()==null){
                         return -1;
                     }
                     else{
                         return 1;
                     }
                 }
            }            
        }
    }
}
