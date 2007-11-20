/**
 * 
 */
package org.digijava.module.contentrepository.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.helper.TeamMember;

/**
 * @author Alex Gartner
 *
 */
public class DocumentManagerForm extends ActionForm {
	
	private String type						= null;
	private String docTitle					= null;
	private String docDescription				= null;
	private String docNotes					= null;
	private FormFile fileData					= null;
	private String uuid						= null;
	private Collection myPersonalDocuments	= null;
	private Collection myTeamDocuments		= null;
	
	
	private Collection otherDocuments		= null;
	private String otherUsername			= null;
	private Long otherTeamId				= null;
	private String docListInSession		= null;
	
	private TeamMember teamMember			= null;
	private Collection teamMembers		= null;
	
	private boolean teamLeader				= false;
	
	private boolean ajaxDocumentList		= false;
	
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

	public Collection getMyPersonalDocuments() {
		return myPersonalDocuments;
	}

	public void setMyPersonalDocuments(Collection myPersonalDocuments) {
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

	public Collection getMyTeamDocuments() {
		return myTeamDocuments;
	}

	public void setMyTeamDocuments(Collection myTeamDocuments) {
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

	public Collection getOtherDocuments() {
		return otherDocuments;
	}

	public void setOtherDocuments(Collection otherDocuments) {
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



	

	
	
}
