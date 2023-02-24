package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ContactPropertyHelper;

public class AddressBookForm   extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    
    private List<AmpContact> contactsForPage;
    /**
     * contact information
     */
    private Long contactId;
    private String name;
    private String lastname;
    private Long title;
    private String organisationName;
    private String function;    
    private String officeaddress;
    
    private ContactPropertyHelper[] phones;
    private int phonesSize; 
    private ContactPropertyHelper[] emails;
    private int emailsSize;
    private ContactPropertyHelper[] faxes;
    private int faxesSize;  
    
    /**
     * filter elements
     */
    private String keyword;
    private Integer resultsPerPage; 
    private String sortBy;
    private String[] contactNames;
    private String sortDir;
    private Integer startIndex;
     /**
      * selected letter
      */
    private String currentAlpha;
    private String[] alphaPages = null; //massive of letters 
    /**
     * pagination
     */
    private Collection pages = null;
    private Integer currentPage;
    private int offset;
    private int pagesSize;
    
    //for import
    private FormFile fileUploaded;
    private Set<AmpOrganisation> organizations;
    
    //private List<AmpOrganisationContact> contactOrganizations;
    private List<AmpContact> probablyDuplicatedContacs;
    private Long contactIdToOverWrite;
    
    private Long[] selOrgs;

    public Long[] getSelOrgs() {
        return selOrgs;
    }

    public void setSelOrgs(Long[] selOrgs) {
        this.selOrgs = selOrgs;
    }
    public int getPagesSize() {
        return pagesSize;
    }
    public void setPagesSize(int pagesSize) {
        this.pagesSize = pagesSize;
    }
    public Collection getPages() {
        return pages;
    }
    public void setPages(Collection pages) {
        this.pages = pages;
        if(pages!=null)
        {    
            this.pagesSize=pages.size();
        }
    }
    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public int getOffset() {
        int value;
        if (getCurrentPage()> (Constants.PAGES_TO_SHOW/2)){
            value = (this.getCurrentPage() - (Constants.PAGES_TO_SHOW/2))-1;
        }
        else {
            value = 0;
        }
        setOffset(value);
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Long getContactId() {
        return contactId;
    }
    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }   
    public Long getTitle() {
        return title;
    }
    public void setTitle(Long title) {
        this.title = title;
    }
    public String getOrganisationName() {
        return organisationName;
    }
    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }
    public List<AmpContact> getContactsForPage() {
        return contactsForPage;
    }
    public void setContactsForPage(List<AmpContact> contactsForPage) {
        this.contactsForPage = contactsForPage;
    }
    public FormFile getFileUploaded() {
        return fileUploaded;
    }
    public void setFileUploaded(FormFile fileUploaded) {
        this.fileUploaded = fileUploaded;
    }
    public String[] getContactNames() {
        return contactNames;
    }
    public void setContactNames(String[] contactNames) {
        this.contactNames = contactNames;
    }
    public String getCurrentAlpha() {
        return currentAlpha;
    }
    public void setCurrentAlpha(String currentAlpha) {
        this.currentAlpha = currentAlpha;
    }
    public String[] getAlphaPages() {
        return alphaPages;
    }
    public void setAlphaPages(String[] alphaPages) {
        this.alphaPages = alphaPages;
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

    public ContactPropertyHelper[] getPhones() {
        return phones;
    }
    
    public ContactPropertyHelper getPhones(int index) {
        return phones[index];
   }

    public void setPhones(ContactPropertyHelper[] phones) {
        this.phones = phones;
    }

    public ContactPropertyHelper[] getEmails() {
        return emails;
    }
    
    public ContactPropertyHelper getEmails(int index) {
        return emails[index];
   }

    public void setEmails(ContactPropertyHelper[] emails) {
        this.emails = emails;
    }

    public ContactPropertyHelper[] getFaxes() {
        return faxes;
    }

    public void setFaxes(ContactPropertyHelper[] faxes) {
        this.faxes = faxes;
    }
    
    public ContactPropertyHelper getFaxes(int index) {
        return faxes[index];
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

    public Set<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    public void setProbablyDuplicatedContacs(
            List<AmpContact> probablyDuplicatedContacs) {
        this.probablyDuplicatedContacs = probablyDuplicatedContacs;
    }

    public List<AmpContact> getProbablyDuplicatedContacs() {
        return probablyDuplicatedContacs;
    }

    public void setContactIdToOverWrite(Long contactIdToOverWrite) {
        this.contactIdToOverWrite = contactIdToOverWrite;
    }

    public Long getContactIdToOverWrite() {
        return contactIdToOverWrite;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }   
}
