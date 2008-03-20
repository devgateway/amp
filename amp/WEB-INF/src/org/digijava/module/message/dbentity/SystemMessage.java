package org.digijava.module.message.dbentity;

/**
 * System Message
 * this is subclass of {@link AmpMessage} entity which defines System Message type messages in AMP.
 * @author Dare Roinishvili
 *
 */
public class SystemMessage extends AmpMessage {
	private Integer systemMessageType;

	public Integer getSystemMessageType() {
		return systemMessageType;
	}

	public void setSystemMessageType(Integer systemMessageType) {
		this.systemMessageType = systemMessageType;
	}
	
	/**
	 * This method is used to define whether user should be able to edit message or not.
	 *  It Message is of SystemMessage type,that user shouldn't be able to edit it.
	 */
	public String getClassName() {
		return "s";
	}
	
}
