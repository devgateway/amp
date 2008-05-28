package org.digijava.module.message.dbentity;

/**
 * Calendar event Type
 * this is subclass of {@link AmpMessage} entity which defines User Message(message which is created by user) type messages in AMP.
 * @author Dare Roinishvili
 *
 */
public class UserMessage extends AmpMessage{
	
	private Integer userMessageType;
	
		
	public Integer getUserMessageType() {
		return userMessageType;
	}


	public void setUserMessageType(Integer userMessageType) {
		this.userMessageType = userMessageType;
	}


	/**
	 * This method is used to define whether user should be able to edit message or not.	
	 */
	public String getClassName() {
		return "u";
	}
}
