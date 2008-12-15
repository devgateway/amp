package org.digijava.module.translation.entity;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.entity.Message;
import org.digijava.module.translation.util.HashKeyPatch;

/**
 * Group of messages with same translation key.
 * They also should have same hash key but generated preferably from English translation
 * @author Irakli Kobiashvili
 * @see HashKeyPatch
 *
 */
public class MessageGroup {
	private String key = null;
	private Map<String, Message> messages = null;
	
	/**
	 * Constructs group for particular key.
	 * This is only constructor for class.
	 * @param key
	 */
	public MessageGroup(String key){
		this.key = key;
		this.messages = new HashMap<String, Message>();
	}
	
	/**
	 * Adds message to the group.
	 * @param message
	 */
	public void addMessage(Message message){
		if (message==null || !message.getKey().equals(this.key)){
			throw new IllegalArgumentException("Cannot add null message or message with different key");
		}
		getMessages().put(message.getLocale(), message);
	}

	public String getKey() {
		return key;
	}

	public boolean equals(Object obj) {
		if (obj==null) return false;
		if (!(obj instanceof MessageGroup)) return false;
		MessageGroup anotherGroup = (MessageGroup) obj;
		return this.hashCode()==anotherGroup.hashCode();
	}

	public int hashCode() {
		//TODO this is not good idea because key generation may change in TranslatorWorker.generateKey()
		return key.hashCode();
	}

	protected Map<String, Message> getMessages() {
		return messages;
	}

}
