package org.digijava.module.translation.entity;

import java.util.Collection;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.translation.util.HashKeyPatch;

/**
 * Group of messages with same translation key.
 * They also should have same hash key but generated preferably from English translation.
 * Unlike its parent this class is used to patch messages with generated key.
 * @author Irakli Kobiashvili
 * @see HashKeyPatch
 *
 */
public class PatchedMessageGroup extends MessageGroup{

	private String hashKey = null;

	public PatchedMessageGroup(String key) {
		super(key);
	}
	
	/**
	 * Calling this getter before all messages are added to group is very bad idea.
	 * @see #addMessage(Message)
	 * @return
	 */
	public String getHashKey() {
		return hashKey;
	}

	/**
	 * Adds message to the group.
	 * This method also generates hash key from them. For this it uses first message added which may be 
	 * overwritten only by English message later. So if there is English translation it will be used for 
	 * key generation, or if there is no English then any other language message will be used. 
	 * @param message
	 */
	public void addMessage(Message message){
		if (message==null || !message.getKey().equals(this.getKey())){
			throw new IllegalArgumentException("Cannot add null message or message with different key");
		}
		if (
				(getHashKey() == null) || 
				(getHashKey() != null && "en".equals(message.getLocale().toLowerCase()))
		){
			hashKey=TranslatorWorker.generateTrnKey(message.getMessage());
		}
		getMessages().put(message.getLocale(), message);
	}
	
	/**
	 * Patches all messages in the group with new hash key before returning them.
	 * @return
	 */
	public Collection<Message> getPatchedMessages(){
		Collection<Message> myMessages = getMessages().values();
		if (myMessages != null && myMessages.size() >0){
			for (Message message : myMessages) {
				message.setKey(getHashKey());
			}
		}
		return myMessages;
	}

}
