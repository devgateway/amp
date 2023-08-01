/**
 * 
 */
package org.digijava.module.contentrepository.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.TeamMemberMail;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alex Gartner
 *
 */
public class DocumentManagerForm extends ActionForm {
    
    private String type;
    private String docTitle                 = null;
    private String docDescription               = null;
    private String docNotes                 = null;
    private FormFile fileData                   = null;
    private String uuid                     = null;
    private Collection<DocumentData> myPersonalDocuments    = null;
    private Collection<DocumentData> myTeamDocuments        = null;
    private Collection<DocumentData> sharedDocuments        = null; //shared documents
    
    private Collection<DocumentData> otherDocuments     = null;
    private String otherUsername            = null;
    private Long otherTeamId                = null;
    
    /* Filter related */
    private Long filter                     = null;
    private Long[] filterDocTypeIds         = null;
    private String[] filterFileTypes        = null;
    private String[] filterOwners           = null;
    private Long[] filterTeamIds            = null;
    private String[] filterLabelsUUID       = null;
    private String [] filterKeywords        = null;
    private String filterFromDate           = null;
    private String filterToDate             = null;
    
    private String docListInSession         = null;
    private String showSharedDocs           = null;
    private String filterOrganisations = null;
    
    private Long keywordMode = null;

    
    private String docIndex = null;
    private String docCategory = null;
    
    private TeamMember teamMember           = null;
    private List<TeamMemberMail> teamMembers          = null;
    
    private boolean teamLeader              = false;
    
    private boolean ajaxDocumentList        = false;
    private Boolean showActions =true;
    
    private String webLink                  = null;
    private boolean webResource             = false;
    
    private Long docType            = new Long(0);
    private Long docLang            = null;
    
    private Collection<Long> years;
    private Long yearOfPublication;
    
    private Boolean pageCloseFlag   = false;
    
    private Boolean sharedDocsTabVisible ;
    private Boolean publicDocsTabVisible ;
    private Boolean privateDocsExist;
    private Boolean teamDocsExist;
    
    
    
    public Boolean getPageCloseFlag() {
        return pageCloseFlag;
    }

    public void setPageCloseFlag(Boolean pageCloseFlag) {
        this.pageCloseFlag = pageCloseFlag;
    }

    public boolean getAjaxDocumentList() {
        return ajaxDocumentList;
    }

    public void setAjaxDocumentList(boolean ajaxDocumentList) {
        this.ajaxDocumentList = ajaxDocumentList;
    }

    public boolean getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(boolean teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Collection<DocumentData> getMyPersonalDocuments() {
        return myPersonalDocuments;
    }

    public void setMyPersonalDocuments(Collection<DocumentData> myPersonalDocuments) {
        this.myPersonalDocuments = myPersonalDocuments;
    }

    public FormFile getFileData() {
        return fileData;
    }

    public void setFileData(FormFile fileData) {
        this.fileData = fileData;
    }

    public String getDocDescription() {
        return docDescription;
    }

    public void setDocDescription(String docDescription) {
        this.docDescription = docDescription;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public Collection<DocumentData> getMyTeamDocuments() {
        return myTeamDocuments;
    }

    public void setMyTeamDocuments(Collection<DocumentData> myTeamDocuments) {
        this.myTeamDocuments = myTeamDocuments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Collection<DocumentData> getOtherDocuments() {
        return otherDocuments;
    }

    public void setOtherDocuments(Collection<DocumentData> otherDocuments) {
        this.otherDocuments = otherDocuments;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public Long getOtherTeamId() {
        return otherTeamId;
    }

    public void setOtherTeamId(Long otherTeamId) {
        this.otherTeamId = otherTeamId;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public List<TeamMemberMail> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<TeamMemberMail> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String getDocListInSession() {
        return docListInSession;
    }

    public void setDocListInSession(String docListInSession) {
        this.docListInSession = docListInSession;
    }

    public String getDocNotes() {
        return docNotes;
    }

    public void setDocNotes(String docNotes) {
        this.docNotes = docNotes;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public Long getDocLang() {
        return docLang;
    }

    public void setDocLang(Long docLang) {
        this.docLang = docLang;
    }

    public Long getDocType() {
        return docType;
    }

    public void setDocType(Long docType) {
        this.docType = docType;
    }

    public boolean isWebResource() {
        return webResource;
    }

    public void setWebResource(boolean webResource) {
        this.webResource = webResource;
    }
    
    public String getDocIndex()
    {
        return docIndex;
    }
    
    public void setDocIndex(String docIndex)
    {
        this.docIndex = docIndex;
    }

    public String getDocCategory()
    {
        return docCategory;
    }
    
    public void setDocCategory(String docCategory)
    {
        this.docCategory = docCategory;
    }

    private    HashMap<String,String> errors = new HashMap<String, String>();
       private    HashMap<String,String> messages = new HashMap<String, String>();

       public void addMessage(String key, String value) {
           this.messages.put(key, value) ;
       }

       public void addError(String key, String value) {
           this.errors.put(key, value) ;
       }

       public void clearMessages(){
           this.errors.clear();
           this.messages.clear();
       }

    /**
     * @return the errors
     */
    public HashMap<String, String> getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }

    /**
     * @return the messages
     */
    public HashMap<String, String> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(HashMap<String, String> messages) {
        this.messages = messages;
    }

    public Collection<DocumentData> getSharedDocuments() {
        return sharedDocuments;
    }

    public void setSharedDocuments(Collection<DocumentData> sharedDocuments) {
        this.sharedDocuments = sharedDocuments;
    }

    public String getShowSharedDocs() {
        return showSharedDocs;
    }

    public void setShowSharedDocs(String showSharedDocs) {
        this.showSharedDocs = showSharedDocs;
    }

    public Boolean getShowActions() {
        return showActions;
    }

    public void setShowActions(Boolean showActions) {
        this.showActions = showActions;
    }

    public Collection<Long> getYears() {
        return years;
    }

    public void setYears(Collection<Long> years) {
        this.years = years;
    }

    public Long getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Long yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public Long getFilter() {
        return filter;
    }

    public void setFilter(Long filter) {
        this.filter = filter;
    }


    public Long[] getFilterDocTypeIds() {
        return filterDocTypeIds;
    }

    public void setFilterDocTypeIds(Long[] filterDocTypeIds) {
        this.filterDocTypeIds = filterDocTypeIds;
    }

    public String[] getFilterFileTypes() {
        return filterFileTypes;
    }

    public void setFilterFileTypes(String[] filterFileTypes) {
        this.filterFileTypes = filterFileTypes;
    }

    public String[] getFilterOwners() {
        return filterOwners;
    }

    public void setFilterOwners(String[] filterOwners) {
        this.filterOwners = filterOwners;
    }

    public Long[] getFilterTeamIds() {
        return filterTeamIds;
    }

    public void setFilterTeamIds(Long[] filterTeamIds) {
        this.filterTeamIds = filterTeamIds;
    }

    public String[] getFilterLabelsUUID() {
        return filterLabelsUUID;
    }

    public void setFilterLabelsUUID(String[] filterLabelsUUID) {
        this.filterLabelsUUID = filterLabelsUUID;
    }

    public String getFilterFromDate() {
        return filterFromDate;
    }

    public void setFilterFromDate(String filterFromDate) {
        this.filterFromDate = filterFromDate;
    }

    public String getFilterToDate() {
        return filterToDate;
    }

    public void setFilterToDate(String filterToDate) {
        this.filterToDate = filterToDate;
    }

    public Boolean getSharedDocsTabVisible() {
        return sharedDocsTabVisible;
    }

    public void setSharedDocsTabVisible(Boolean sharedDocsTabVisible) {
        this.sharedDocsTabVisible = sharedDocsTabVisible;
    }

    public Boolean getPublicDocsTabVisible() {
        return publicDocsTabVisible;
    }

    public void setPublicDocsTabVisible(Boolean publicDocsTabVisible) {
        this.publicDocsTabVisible = publicDocsTabVisible;
    }

    public Boolean getPrivateDocsExist() {
        return privateDocsExist;
    }

    public void setPrivateDocsExist(Boolean privateDocsExist) {
        this.privateDocsExist = privateDocsExist;
    }

    public Boolean getTeamDocsExist() {
        return teamDocsExist;
    }

    public void setTeamDocsExist(Boolean teamDocsExist) {
        this.teamDocsExist = teamDocsExist;
    }

    public String[] getFilterKeywords() {
        return filterKeywords;
    }

    public void setFilterKeywords(String[] filterKeywords) {
        this.filterKeywords = filterKeywords;
    }

    public String getFilterOrganisations()
    {
        return this.filterOrganisations;
    }
    
    public void setFilterOrganisations(String org)
    {
        this.filterOrganisations = org;
    }

    public Long getKeywordMode() {
        return keywordMode;
    }

    public void setKeywordMode(Long keywordMode) {
        this.keywordMode = keywordMode;
    }
    
}
