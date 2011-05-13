package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.form.AddressBookForm;
import org.digijava.module.aim.helper.AmpContactsWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ContactPropertyHelper;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AddressBookActions extends DispatchAction {
	
	public ActionForward viewAddressBook (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	AddressBookForm myForm=(AddressBookForm)form;
    	
    	if(request.getParameter("reset")!=null && request.getParameter("reset").equals("true")){
    		myForm.setResultsPerPage(10);
    		myForm.setCurrentAlpha(null);
    		myForm.setCurrentPage(1);
    		myForm.setKeyword(null);
    		myForm.setSortBy(null);
    		myForm.setSortDir(null);
    		myForm.setStartIndex(null);
    	}
    	String alpha=null;
    	if(myForm.getCurrentAlpha()!=null && ! myForm.getCurrentAlpha().equals("viewAll")){
    		alpha=myForm.getCurrentAlpha();
    	}
//    	
//    	int contactsAmount=ContactInfoUtil.getContactsSize(myForm.getKeyword(),alpha);
//    	//how many pages
//    	Collection pages = null;
//    	int pagesNum=0;
//    	if(contactsAmount % myForm.getResultsPerPage()==0){
//    		pagesNum=contactsAmount/myForm.getResultsPerPage();
//    	}else{
//    		pagesNum=contactsAmount/myForm.getResultsPerPage() +1;
//    	}
//    	if (pagesNum >= 1) {
//	          pages = new ArrayList();
//	          for (int i = 0; i < pagesNum; i++) {
//	            Integer pageNum = new Integer(i + 1);
//	            pages.add(pageNum);
//	          }
//	    }
//    	
//    	if(myForm.getResultsPerPage()==null){
//    		myForm.setResultsPerPage(Constants.CONTACTS_PER_PAGE);
//    	}
//    	
//    	List<AmpContact> pagedContacts=null;
//    	pagedContacts=ContactInfoUtil.getPagedContacts(0, myForm.getResultsPerPage(), myForm.getSortBy(),myForm.getKeyword(),alpha);
//    	
//    	//alpha pages
    	if(alpha==null){
    		String[] contactNames=ContactInfoUtil.getContactNames();
    		String[] alphaArray = new String[26];
            int i = 0;
            for (char c = 'A'; c <= 'Z'; c++) {
            	for(int j=0;j<contactNames.length;j++){
            		if (contactNames[j].toUpperCase().indexOf(c) == 0) {
                        alphaArray[i++] = String.valueOf(c);
                        break;
                     }
            	}
            }
            myForm.setAlphaPages(alphaArray);
    	}else{
    		myForm.setAlphaPages(null);
    	}
//    	
//    	myForm.setContactsForPage(pagedContacts);
    	myForm.setCurrentPage(new Integer(1));
    	//myForm.setPages(pages);
    	myForm.setContactNames(ContactInfoUtil.getContactNames());
		return mapping.findForward("showAllContacts");
	}
	
	public ActionForward getContactsJSON (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		
		//total amount of contacts
		String alpha=null;
    	if(myForm.getCurrentAlpha()!=null && ! myForm.getCurrentAlpha().equals("viewAll")){
    		alpha=myForm.getCurrentAlpha();
    	}
    	int contactsAmount=ContactInfoUtil.getContactsSize(myForm.getKeyword(),alpha); //need this to draw correct amount of pages
    	//sorting options
		String sortBy = request.getParameter("sort");
		String sortDir = request.getParameter("dir");
		//pagination options
		String startIndex = request.getParameter("startIndex");
		String results = request.getParameter("results");
		
		myForm.setSortBy(sortBy);
		myForm.setSortDir(sortDir);
		myForm.setStartIndex(new Integer(startIndex));
		myForm.setResultsPerPage(new Integer(results));
		//get contacts
		List<AmpContact> pagedContacts=ContactInfoUtil.getPagedContacts(new Integer(startIndex).intValue(), new Integer(results).intValue(), sortBy,sortDir,myForm.getKeyword(),alpha);


        JSONObject json = null;

        try {
		    json = new JSONObject();
        } catch (Exception ex) {
            System.out.print("a");
        }
		JSONArray jsonArray = new JSONArray();
		//fill array
		if(pagedContacts!=null && pagedContacts.size() > 0){
			for (Iterator<AmpContact> it = pagedContacts.iterator(); it.hasNext();) {
				AmpContact contact = (AmpContact) it.next();
				JSONObject jcontact = new JSONObject();
				jcontact.put("ID", contact.getId());
				jcontact.put("title", contact.getTitle()!=null?contact.getTitle().getValue():"");
				jcontact.put("name", contact.getName()+" "+contact.getLastname());
				String emails="";
				String orgs="";
				String phones="";
				String faxes="";
				if(contact.getProperties()!=null){
					for (AmpContactProperty property : contact.getProperties()) {
						if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().length()>0){
							if(emails.length()==0){
								emails+="<ul>";
							}
							emails+="<li>"+property.getValue() +" </li>";
						}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && property.getValueAsFormatedPhoneNum().length()>0){
							if(phones.length()==0){
								phones+="<ul>";
							}
							phones+="<li>"+property.getValueAsFormatedPhoneNum() +" </li>";
						}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX) && property.getValue().length()>0){
							if(faxes.length()==0){
								faxes+="<ul>";
							}
							faxes+="<li>"+property.getValue() +" </li>";
						}
					}
					if(emails.length()>0){
						emails+="</ul>";
					}
					if(phones.length()>0){
						phones+="</ul>";
					}
					if(faxes.length()>0){
						faxes+="</ul>";
					}
				}
				jcontact.put("email", emails);
				
				if((contact.getOrganizationContacts()!=null && contact.getOrganizationContacts().size()>0) || (contact.getOrganisationName()!=null && contact.getOrganisationName().length()>0)){
					orgs+="<ul>";
					if(contact.getOrganisationName()!=null && contact.getOrganisationName().length()>0){
						orgs+="<li>"+contact.getOrganisationName()+"</li>";
					}
					for (AmpOrganisationContact contOrg : contact.getOrganizationContacts()) {
						orgs+="<li>"+contOrg.getOrganisation().getName()+"</li>";
					}
					orgs+="</ul>";
				}
				jcontact.put("organizations", orgs);
				jcontact.put("function", contact.getFunction());
				jcontact.put("phones", phones);
				jcontact.put("faxes", faxes);
				jsonArray.add(jcontact);
			}			
		}
    	
		json.put("recordsReturned", pagedContacts.size());
		json.put("totalRecords", contactsAmount);
		json.put("startIndex", startIndex);
		json.put("sort", null);
		json.put("dir", "asc");
		json.put("pageSize", 10);
		json.put("rowsPerPage", 10);

		json.put("contacts", jsonArray);

		response.setContentType("text/json-comment-filtered");
		OutputStreamWriter outputStream = null;

		try {
			outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
			outputStream.write(json.toString());
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
		return null;
	}
	
	public ActionForward viewPrintPreview (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		//total amount of contacts
		String alpha=null;
    	if(myForm.getCurrentAlpha()!=null && ! myForm.getCurrentAlpha().equals("viewAll")){
    		alpha=myForm.getCurrentAlpha();
    	}
		//get contacts
		List<AmpContact> pagedContacts=ContactInfoUtil.getPagedContacts(myForm.getStartIndex().intValue(),myForm.getResultsPerPage().intValue(), myForm.getSortBy(),myForm.getSortDir(),myForm.getKeyword(),alpha);
		myForm.setContactsForPage(pagedContacts);
		return mapping.findForward("printPreview");
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
			
			String alpha=null;
	    	if(myForm.getCurrentAlpha()!=null && ! myForm.getCurrentAlpha().equals("viewAll")){
	    		alpha=myForm.getCurrentAlpha();
	    	}
	    	
			List<AmpContact> pagedContacts=null;
	    	pagedContacts=ContactInfoUtil.getPagedContacts(stIndex, myForm.getResultsPerPage(), myForm.getSortBy(),"asc",myForm.getKeyword(),alpha);
	    	
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
			clearForm(myForm);
			if(contact!=null){
				myForm.setContactId(contact.getId());
				myForm.setName(contact.getName());
				myForm.setLastname(contact.getLastname());
				//myForm.setEmail(contact.getEmail());
				List<ContactPropertyHelper> contactProperties=AmpContactsWorker.buildHelperContactProperties(contact.getProperties()); //properties can't be null, cos contact has to have at lets 1 email
				
				List<ContactPropertyHelper> emails=null;
				List<ContactPropertyHelper> phones=null;
				List<ContactPropertyHelper> faxes=null;
                if (contactProperties != null) {
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
					myForm.setEmails(emails.toArray(new ContactPropertyHelper[emails.size()]));
					myForm.setEmailsSize(myForm.getEmails().length);
				}
				if(phones!=null){
					myForm.setPhones(phones.toArray(new ContactPropertyHelper[phones.size()]));
					myForm.setPhonesSize(myForm.getPhones().length);
				}
				if(faxes!=null){
					myForm.setFaxes(faxes.toArray(new ContactPropertyHelper[faxes.size()]));
					myForm.setFaxesSize(myForm.getFaxes().length);
				}
				if(contact.getTitle()!=null){
                	myForm.setTitle(contact.getTitle().getId());
                }else{
                    myForm.setTitle(null);
                }
				myForm.setOrganisationName(contact.getOrganisationName());
				myForm.setFunction(contact.getFunction());
				myForm.setOfficeaddress(contact.getOfficeaddress());
                myForm.setOrganizations(new ArrayList<AmpOrganisation>());
                List<AmpOrganisationContact> contOrgs=ContactInfoUtil.getContactOrganizations(contact.getId());
                if(contOrgs!=null){
                	List<AmpOrganisation> organisations=new ArrayList<AmpOrganisation>();
                	for (AmpOrganisationContact orgContact : contOrgs) {
                		organisations.add(orgContact.getOrganisation());
					}
                	myForm.getOrganizations().addAll(organisations);
                }
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
				List<AmpOrganisationContact> contOrgs=ContactInfoUtil.getContactOrganizations(myForm.getContactId());
	        	if(contOrgs!=null && contOrgs.size()>0){
	        		for (AmpOrganisationContact ampOrganisationContact : contOrgs) {
	        			AmpOrganisation org=ampOrganisationContact.getOrganisation();
	        			org.getOrganizationContacts().remove(ampOrganisationContact);
	        			DbUtil.update(org);
						ContactInfoUtil.deleteOrgContact(ampOrganisationContact);
						
						contact.getOrganizationContacts().remove(ampOrganisationContact);
					}
	        	}
				ContactInfoUtil.deleteContact(contact);
			}
		}
		return viewAddressBook(mapping,myForm,request,response);
	}

        public ActionForward removeOrganization(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
            AddressBookForm myForm = (AddressBookForm) form;
            Long[] ids = myForm.getSelOrgs();
            if (ids != null) {
                for (Long id : ids) {
                    AmpOrganisation organization = DbUtil.getOrganisation(id);
                    myForm.getOrganizations().remove(organization);                	
                }
            }
            return mapping.findForward("addOrEditContact");
        }
   
   public ActionForward addOrganization(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
         return mapping.findForward("addOrEditContact");
   }
	
   public ActionForward saveContact (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		boolean validateData=false;
		AmpContact contact=new AmpContact();
		Set<AmpContactProperty> contactProperties=new HashSet<AmpContactProperty>();
		Set<ContactPropertyHelper> emailsToLookFor=new HashSet<ContactPropertyHelper>();
       if (myForm.getEmails() != null) {
           if (myForm.getContactId() != null) {
               //get contact emails
               List<AmpContactProperty> properties = ContactInfoUtil.getContactProperties(myForm.getContactId());
               Set<AmpContactProperty> emails = null;
               for (AmpContactProperty property : properties) {
                   if (property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)) {
                       if (emails == null) {
                           emails = new HashSet<AmpContactProperty>();
                       }
                       emails.add(property);
                   }
               }
               //if user changed any contact email, we should check that this email doesn't exist in db
                   List<ContactPropertyHelper> contactEmails = AmpContactsWorker.buildHelperContactProperties(emails);
                  if (contactEmails != null) {
                   for (ContactPropertyHelper property : myForm.getEmails()) {
                       if (!contactEmails.contains(property)) {
                           validateData = true;
                           emailsToLookFor.add(property);
                       }
                   }
               }
           } else {
               validateData = true;
               emailsToLookFor.addAll(Arrays.asList(myForm.getEmails()));
           }
       }
				
		
		//check unique email 
		if(validateData){
			ActionMessages errors= new ActionMessages();
			int contactWithSameEmail=0;
			for (ContactPropertyHelper email : emailsToLookFor) {
				contactWithSameEmail=ContactInfoUtil.getContactsCount(email.getValue().trim(),null);
				if(contactWithSameEmail!=0){
					break;
				}
			}
			
			if(contactWithSameEmail!=0){
				Site site = RequestUtils.getSite(request);
				Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
						
				Long siteId = site.getId();
				String locale = navigationLanguage.getCode();
				errors.add("email not unique", new ActionMessage("aim.contact.emailExists","Contact with the given email already exists"));
				
				if (errors.size() > 0){
					//we have all the errors for this step saved and we must throw the amp error
					saveErrors(request, errors);
					return mapping.findForward("addOrEditContact");
				}
			}
		}
		
		//remove old contact and properties and organization contacts
		List<AmpOrganisation> orgsForWhichContactWasPrimary=new ArrayList<AmpOrganisation>();
		Set<AmpActivityContact> activityContacts=null;
		if(myForm.getContactId()!=null){
			AmpContact cont=ContactInfoUtil.getContact(myForm.getContactId());
			List<AmpOrganisationContact> contOrgs=ContactInfoUtil.getContactOrganizations(myForm.getContactId());
        	if(contOrgs!=null && contOrgs.size()>0){
        		for (AmpOrganisationContact ampOrganisationContact : contOrgs) {
        			AmpOrganisation org=ampOrganisationContact.getOrganisation();
        			//get organisations for which contact was primary.
        			if(ampOrganisationContact.getPrimaryContact()!=null && ampOrganisationContact.getPrimaryContact()){
        				orgsForWhichContactWasPrimary.add(org);
        			}
        			org.getOrganizationContacts().remove(ampOrganisationContact);
        			DbUtil.update(org);
					ContactInfoUtil.deleteOrgContact(ampOrganisationContact);
					
					cont.getOrganizationContacts().remove(ampOrganisationContact);
				}
        	}
        	//before removing contact,we should get activity contacts from it
        	activityContacts=cont.getActivityContacts();
        	//delete contact
			ContactInfoUtil.deleteContact(cont);
		}		
		
		contact.setName(myForm.getName().trim());
		contact.setLastname(myForm.getLastname().trim());	
		contact.setFunction(myForm.getFunction().trim());
		contact.setOfficeaddress(myForm.getOfficeaddress().trim());
		if(myForm.getTitle()!=null){
			contact.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(myForm.getTitle()));
		}
		if(myForm.getOrganisationName()!=null){
			contact.setOrganisationName(myForm.getOrganisationName().trim());
		}		
		
		//filling contact properties
       if (myForm.getEmails() != null) {
           contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(myForm.getEmails()));
       }
		if(myForm.getFaxes()!=null){
			contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(myForm.getFaxes()));
		}
		if(myForm.getPhones()!=null){
			contactProperties.addAll(AmpContactsWorker.buildAmpContactProperties(myForm.getPhones()));
		}
        
		//save or update contact
		ContactInfoUtil.saveOrUpdateContact(contact);
		
		//save activity contacts
		if(activityContacts!=null && activityContacts.size()>0){
			for (AmpActivityContact ampActivityContact : activityContacts) {
				AmpActivityContact newActivityContact=new AmpActivityContact();
				newActivityContact.setContact(contact);
				newActivityContact.setActivity(ampActivityContact.getActivity());
				newActivityContact.setPrimaryContact(ampActivityContact.getPrimaryContact());
				newActivityContact.setContactType(ampActivityContact.getContactType());
				ContactInfoUtil.saveOrUpdateActivityContact(newActivityContact);
			}
		}
		
		//save or update contact orgs
		if(myForm.getOrganizations() != null){
			for (AmpOrganisation ampOrganisaion : myForm.getOrganizations()) {
				AmpOrganisationContact orgCont=new AmpOrganisationContact(ampOrganisaion,contact);
				//if contact was linked to organization as primary we shuoldn't loose this link. if it wasn't linked with org and we linked
				//if from address book then it's not primary.
				if(orgsForWhichContactWasPrimary.size()>0 && orgsForWhichContactWasPrimary.contains(ampOrganisaion)){
					orgCont.setPrimaryContact(true);
				}else{
					orgCont.setPrimaryContact(false);
				}				
				ContactInfoUtil.saveOrUpdateOrganisationContact(orgCont);
			}
		}
		//save or update contact details
		for (AmpContactProperty ampContactProperty : contactProperties) {
			ampContactProperty.setContact(contact);
			ContactInfoUtil.saveOrUpdateContactProperty(ampContactProperty);
		}
		//reset filter 
		myForm.setResultsPerPage(10);
		myForm.setKeyword(null);
		return viewAddressBook(mapping,myForm,request,response);
	}
	
	public ActionForward addNewData (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		String dataName=request.getParameter("data");
		//user clicked Add new Email
		if(dataName.equalsIgnoreCase("email")){
			if(myForm.getEmails()==null){
				myForm.setEmails(new ContactPropertyHelper[1]);
				myForm.getEmails()[0]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_EMAIL,"",null);
			}else{
				int size=myForm.getEmails().length+1;
				ContactPropertyHelper[] propertiesArray=new ContactPropertyHelper[size];
				for(int i=0;i<myForm.getEmails().length;i++){
					propertiesArray[i]=myForm.getEmails()[i];
				}				
				propertiesArray[size-1]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_EMAIL,"",null);			
				myForm.setEmails(propertiesArray);
			}
			myForm.setEmailsSize(myForm.getEmails().length);			
		}
		
		//user clicked Add new Phone
		if(dataName.equalsIgnoreCase("phone")){
			if(myForm.getPhones()==null){
				myForm.setPhones(new ContactPropertyHelper[1]);
				myForm.getPhones()[0]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_PHONE,"","");
			}else{
				int size=myForm.getPhones().length+1;
				ContactPropertyHelper[] propertiesArray=new ContactPropertyHelper[size];
				for(int i=0;i<myForm.getPhones().length;i++){
					propertiesArray[i]=myForm.getPhones()[i];
				}				
				propertiesArray[size-1]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_PHONE,"","");				
				myForm.setPhones(propertiesArray);
			}
			myForm.setPhonesSize(myForm.getPhones().length);
		}
		//user clicked add new fax
		if(dataName.equalsIgnoreCase("fax")){
			if(myForm.getFaxes()==null){
				myForm.setFaxes(new ContactPropertyHelper[1]);
				myForm.getFaxes()[0]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_FAX,"",null);
			}else{
				int size=myForm.getFaxes().length+1;
				ContactPropertyHelper[] propertiesArray=new ContactPropertyHelper[size];
				for(int i=0;i<myForm.getFaxes().length;i++){
					propertiesArray[i]=myForm.getFaxes()[i];
				}				
				propertiesArray[size-1]=AmpContactsWorker.createProperty(Constants.CONTACT_PROPERTY_NAME_FAX,"",null);				
				myForm.setFaxes(propertiesArray);
			}
			myForm.setFaxesSize(myForm.getFaxes().length);			
		}	
		
		return mapping.findForward("addOrEditContact");
	}

	public ActionForward removeData (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AddressBookForm myForm=(AddressBookForm)form;
		String dataName=request.getParameter("dataName");
		String ind=request.getParameter("index");
		int index=new Integer(ind).intValue();
		ContactPropertyHelper [] myArray=null;
		if(dataName!=null){
			if(dataName.equalsIgnoreCase("email")){
				myArray=new ContactPropertyHelper[myForm.getEmails().length-1];
				if(myArray.length!=0){
					int j=0;
					for(int i=0; i< myForm.getEmails().length; i++){
						if(index!=i){
							myArray[j]=myForm.getEmails(i);
                            j++;
                        }
                    }
                    myForm.setEmails(myArray);
                } else {
                    myForm.setEmails(null);
                }
				
				myForm.setEmailsSize(myArray.length);
			}else if(dataName.equalsIgnoreCase("phone")){
				myArray=new ContactPropertyHelper[myForm.getPhones().length-1];
				if(myArray.length!=0){
					int j=0;
					for(int i=0; i< myForm.getPhones().length; i++){
						if(index!=i){
							myArray[j]=myForm.getPhones(i);
							j++;
						}
					}
					myForm.setPhones(myArray);					
				}else{
					myForm.setPhones(null);					
				}
				myForm.setPhonesSize(myArray.length);
			}else if(dataName.equalsIgnoreCase("fax")){
				myArray=new ContactPropertyHelper[myForm.getFaxes().length-1];
				if(myArray.length!=0){
					int j=0;
					for(int i=0; i< myForm.getFaxes().length; i++){
						if(index!=i){
							myArray[j]=myForm.getFaxes(i);
							j++;
						}
					}
					myForm.setFaxes(myArray);
				}else{
					myForm.setFaxes(null);
				}
				myForm.setFaxesSize(myArray.length);
			}
		}
		return mapping.findForward("addOrEditContact");
	}
	
	private void clearForm(AddressBookForm form){
		form.setContactId(null);
		form.setKeyword(null);
		form.setLastname(null);
		form.setName(null);
		form.setOrganisationName(null);
		form.setOrganizations(null);
		form.setTitle(null);
		form.setAlphaPages(null);
		form.setCurrentAlpha(null);
		form.setContactNames(null);
		form.setFunction(null);
		form.setOfficeaddress(null);		
		form.setEmails(null);
		form.setPhones(null);
		form.setFaxes(null);
		form.setEmailsSize(0);
		form.setPhonesSize(0);
		form.setFaxesSize(0);
	}
	
	
}