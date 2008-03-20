package org.digijava.module.message.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.message.dbentity.AmpMessage;

public class AmpMessageForm extends ActionForm {
	private Long messageId;
	private String messageName;
	private Long priorityLevel;
	private Long messageType;
	private String senderType; //activity , User e.t.c.
	private Long senderId;
	private String creationDate;
	private boolean editingMessage=false;
	private String description;
	private String receiverType;
	private Long receiverId; //here comes Id from the receivers dropdown
	
	private List<AmpMessage> allMessages;
	private List<LabelValueBean> receivers; //it's used to hold receivers list loaded according to receiverType(Here Maybe held Teams,Users)
	
	private List<LabelValueBean> teamsForTeamMembers; //Used if in add Message page user selects Team Member in receiver Type
	private Long selectedTeamId; 	//Used if in add Message page user selects Team Member in receiver Type. This field holds selected teamId
	
	private String className;


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messagetId) {
		this.messageId = messagetId;
	}

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	
	public Long getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(Long priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public Long getMessageType() {
		return messageType;
	}

	public void setMessageType(Long messageType) {
		this.messageType = messageType;
	}

	public String getSenderType() {
		return senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isEditingMessage() {
		return editingMessage;
	}

	public void setEditingMessage(boolean editingMessage) {
		this.editingMessage = editingMessage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<AmpMessage> getAllMessages() {
		return allMessages;
	}

	public void setAllMessages(List<AmpMessage> allMessages) {
		this.allMessages = allMessages;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public List<LabelValueBean> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<LabelValueBean> receivers) {
		this.receivers = receivers;
	}

	public List<LabelValueBean> getTeamsForTeamMembers() {
		return teamsForTeamMembers;
	}

	public void setTeamsForTeamMembers(List<LabelValueBean> teamsForTeamMembers) {
		this.teamsForTeamMembers = teamsForTeamMembers;
	}

	public Long getSelectedTeamId() {
		return selectedTeamId;
	}

	public void setSelectedTeamId(Long selectedTeamId) {
		this.selectedTeamId = selectedTeamId;
	}

}
