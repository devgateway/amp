package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.helper.Constants;

public class AddressBookForm   extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	
	private List<AmpContact> contactsForPage;
	//contact information
	private Long contactId;
	private String name;
	private String lastname;
	private String email;
	private String title;
	private String organisationName;
	private String phone;
	private String fax;	
	//filter elements
	private String keyword;
	private Integer resultsPerPage;	
	private String sortBy;
	private String[] contactNames;
	 //selected letter
    private String currentAlpha;
    private String[] alphaPages = null; //massive of letters 
	//pagination
	private Collection pages = null;
	private Integer currentPage;
	private int offset;
	private int pagesSize;
	
	//for import
	private FormFile fileUploaded;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
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
}
