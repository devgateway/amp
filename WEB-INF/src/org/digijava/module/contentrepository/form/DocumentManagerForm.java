/**
 * 
 */
package org.digijava.module.contentrepository.form;

import java.util.Collection;
import java.util.HashMap;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.helper.DocumentData;

/**
 * @author Alex Gartner
 *
 */
public class DocumentManagerForm extends ActionForm {
	
	private String type						= "private";
	private String docTitle					= null;
	private String docDescription				= null;
	private String docNotes					= null;
	private FormFile fileData					= null;
	private String uuid						= null;
	private Collection<DocumentData> myPersonalDocuments	= null;
	private Collection<DocumentData> myTeamDocuments		= null;
	
	
	private Collection<DocumentData> otherDocuments		= null;
	private String otherUsername			= null;
	private Long otherTeamId				= null;
	private String docListInSession			= null;
	
	private TeamMember teamMember			= null;
	private Collection teamMembers			= null;
	
	private boolean teamLeader				= false;
	
	private boolean ajaxDocumentList		= false;
	
	private String webLink					= null;
	private boolean webResource				= false;
	
	private Long docType			= new Long(0);
	private Long docLang			= null;
	
	private Boolean pageCloseFlag	= false;
	
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

	public Collection getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(Collection teamMembers) {
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


	
	
}
