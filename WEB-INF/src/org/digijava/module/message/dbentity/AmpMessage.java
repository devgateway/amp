package org.digijava.module.message.dbentity;

import java.util.Date;

/**
 * General AMP Message.
 * @author Dare Roinishvili
 *
 */
public class AmpMessage {
	private Long id;
	
	/**
	 * name or subject of message.
	 */
	private String name;
	private Long priorityLevel; //low, high e.t.c. comes from Category Manager
	private Long messageType; //alert,approvals,system message e.t.c.
	private String senderType;	
	private Long senderId;  //if user sends alert, that it is that user's id... vtqvat user daregistrirda,anu User manager agzavnis da romeli useric daregistrirda imis,id iqneba
	private Date creationDate; //date when it was created
	
	/**
	 * Is the message alredy read or not.
	 */
	private Boolean read;
	
	/**
	 * emails should be sent.
	 */
	private Boolean emailable;
	
	/**
	 * Text of the message.
	 */
	private String description;
	
	private String receiverType; //user,team or team member
	private Long receiverId;
	
	private Long selectedTeamId ; //Used if in add Message page user selected Team Member in receiver Type. This field holds selected teamId
	
	
	/**
	 * This method is used to define whether user should be able to edit message or not.
	 * It Message is of SystemMessage type,that user shouldn't be able to edit it.
	 * 
	 */
	public String getClassName(){
		return "m";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	public Boolean getEmailable() {
		return emailable;
	}
	public void setEmailable(Boolean emailable) {
		this.emailable = emailable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiverType() {
		return receiverType;
	}
	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}
	public Long getSelectedTeamId() {
		return selectedTeamId;
	}
	public void setSelectedTeamId(Long selectedTeamId) {
		this.selectedTeamId = selectedTeamId;
	}
	
	
}
