package org.digijava.module.message.dbentity;

public class AmpMessageState {
	
	private Long id;
	
	/**
	 * which message's state is current state
	 */	
	private AmpMessage message;
	
	/**
	 * team member Id
	 */
	private Long memberId;
	
	/**
	 * is message already read
	 */
	private Boolean read;
	/**
	 * holds the name of sender
	 */
	private String sender;
	
	/**
	 * this field is used to see sent messages
	 */
	private Long senderId;

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AmpMessage getMessage() {
		return message;
	}

	public void setMessage(AmpMessage message) {
		this.message = message;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}
	
	
}
