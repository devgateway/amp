package org.digijava.module.aim.uicomponents.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.ContactPropertyHelper;

public class AddContactComponentForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private Long selContactIds;
    private List<AmpContact> contacts;
    private String firstName;
    private String lastname;
    private Long title;
    private String organisationName;
    private String function;
    private String officeaddress;
    private String keyword;
    private String action;
    private String targetCollection = "";
    private Object targetForm;
    private Long contactId;
    private List<AmpOrganisation> organizations;
    private Long[] selContactOrgs;
    
    private String temporaryId; //contact's temporary id
    
    private ContactPropertyHelper[] emails;
    private ContactPropertyHelper[] phones;
    private ContactPropertyHelper[] faxes;
    private int phonesSize;
    private int emailsSize;
    private int faxesSize;
    
    private String[] contEmail;
    private String[] contPhoneType;

    private String[] contPhoneTypeIds;

    private String[] contPhoneNumber;
    private String[] contFaxes;
    

    
    private String actOrOrgTempId;




    public String[] getContPhoneTypeIds() {
        return contPhoneTypeIds;
    }

    public void setContPhoneTypeIds(String[] contPhoneTypeIds) {
        this.contPhoneTypeIds = contPhoneTypeIds;
    }    


    public Long[] getSelContactOrgs() {
        return selContactOrgs;
    }

    public void setSelContactOrgs(Long[] selContactOrgs) {
        this.selContactOrgs = selContactOrgs;
    }

    public List<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
  

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAction() {
        return action;
    }

    public String getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(String targetCollection) {
        this.targetCollection = targetCollection;
    }

    public Object getTargetForm() {
        return targetForm;
    }

    public void setTargetForm(Object targetForm) {
        this.targetForm = targetForm;
    }

    public void setAction(String action) {
        this.action = action;
    }
 

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

  

    public List<AmpContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<AmpContact> contacts) {
        this.contacts = contacts;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Long getSelContactIds() {
        return selContactIds;
    }

    public void setSelContactIds(Long selContactIds) {
        this.selContactIds = selContactIds;
    }

    public String getTemporaryId() {
        return temporaryId;
    }

    public void setTemporaryId(String temporaryId) {
        this.temporaryId = temporaryId;
    }

    public Long getTitle() {
        return title;
    }

    public void setTitle(Long title) {
        this.title = title;
    }
    
    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
    public String getOfficeaddress() {
        return officeaddress;
    }

    public void setOfficeaddress(String officeaddress) {
        this.officeaddress = officeaddress;
    }
    
    public ContactPropertyHelper[] getEmails() {
        return emails;
    }
    public void setEmails(ContactPropertyHelper[] emails) {
        this.emails = emails;
    }
    public ContactPropertyHelper[] getPhones() {
        return phones;
    }
    public void setPhones(ContactPropertyHelper[] phones) {
        this.phones = phones;
    }
    public ContactPropertyHelper[] getFaxes() {
        return faxes;
    }
    public void setFaxes(ContactPropertyHelper[] faxes) {
        this.faxes = faxes;
    }
    public int getPhonesSize() {
        return phonesSize;
    }
    public void setPhonesSize(int phonesSize) {
        this.phonesSize = phonesSize;
    }
    public int getEmailsSize() {
        return emailsSize;
    }
    public void setEmailsSize(int emailsSize) {
        this.emailsSize = emailsSize;
    }
    public int getFaxesSize() {
        return faxesSize;
    }
    public void setFaxesSize(int faxesSize) {
        this.faxesSize = faxesSize;
    }
    public String[] getContEmail() {
        return contEmail;
    }
    public void setContEmail(String[] contEmail) {
        this.contEmail = contEmail;
    }
    public String[] getContPhoneType() {
        return contPhoneType;
    }
    public void setContPhoneType(String[] contPhoneType) {
        this.contPhoneType = contPhoneType;
    }
    public String[] getContPhoneNumber() {
        return contPhoneNumber;
    }
    public void setContPhoneNumber(String[] contPhoneNumber) {
        this.contPhoneNumber = contPhoneNumber;
    }
    public String[] getContFaxes() {
        return contFaxes;
    }
    public void setContFaxes(String[] contFaxes) {
        this.contFaxes = contFaxes;
    }
    public ContactPropertyHelper getEmails(int index) {
        return emails[index];
    }
    
    public ContactPropertyHelper getFaxes(int index) {
        return faxes[index];
    }
    public ContactPropertyHelper getPhones(int index) {
        return phones[index];
    }



    public String getActOrOrgTempId() {
        return actOrOrgTempId;
    }

    public void setActOrOrgTempId(String actOrOrgTempId) {
        this.actOrOrgTempId = actOrOrgTempId;
    }

    
    
}
