package org.digijava.module.translation.entity;

import java.util.Collection;
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
	 * @param key
	 */
	public MessageGroup(String key){
		this.key = key;
		this.messages = new HashMap<String, Message>();
	}

	/**
	 * Constructs group using first message which will be added in this group.
	 * @param message
	 */
	public MessageGroup(Message message){
		this(message.getKey());
		addMessage(message);
	}
	
	/**
	 * Adds message to the group.
	 * If one tries to add message with different key then exception is thrown.
	 * @param message
	 */
	public void addMessage(Message message){
		if (message==null || !message.getKey().equals(this.key)){
			throw new IllegalArgumentException("Cannot add null message or message with different key");
		}
		doPutMessage(message);
	}
	
	/**
	 * puts message in group.
	 * This protected method does not check if keys are same. This is required for hash code grouping
	 * @param message
	 */
	protected void doPutMessage(Message message){
		getMessages().put(message.getLocale(), message);
	}

	public String getKey() {
		return key;
	}

	/**
	 * Return key because key is the hash code itself.
	 * @return
	 */
	public String getHashKey() {
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
	
	public Message getMessageByLocale(String locale){
		return messages.get(locale);
	}
	
	public Collection<Message> getAllMessages(){
		return this.messages.values();
	}
	
	/**
	 * Adds all messages from other group to this on.
	 * Uses {@link #doPutMessage(Message)} method for adding to run adding logic 
	 * of the class or subclasses if they override adding logic.
	 * @param group
	 */
	public void addMessagesFrom(MessageGroup group){
		Collection<Message> otherMessages = group.getAllMessages();
		for (Message otherMessage : otherMessages) {
			this.doPutMessage(otherMessage);
		}
	}
}
