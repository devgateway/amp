package org.digijava.module.translation.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.translation.jaxb.Language;
import org.digijava.module.translation.jaxb.ObjectFactory;
import org.digijava.module.translation.jaxb.Trn;
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
	
	public MessageGroup(Trn trn){
		Message message=new Message();
		//generate key, if it's null
		String key=trn.getKey();
		if(key==null || key.length()==0){
			Language lang=hasEnglishTranslation(trn.getLang());
			String text;
			if(lang==null){
				//if there is no translation, then key(hashcode) should be created from any translation text
				lang=trn.getLang().get(0);
			}
			text=lang.getValue();
			key=TranslatorWorker.generateTrnKey(text);
		}
		message.setKey(key);
		message.setKeyWords(trn.getKeywords());
		message.setSiteId(trn.getSiteId());
		for (Language lang : trn.getLang()) {
			message.setLocale(lang.getCode());
			message.setMessage(lang.getValue());
			message.setCreated(new Timestamp(lang.getUpdated().toGregorianCalendar().getTime().getTime()));
			if(lang.getLastAccessed()!=null){
				message.setLastAccessed(new Timestamp(lang.getLastAccessed().toGregorianCalendar().getTime().getTime()));
			}
			
			if(this.key==null){
				this.key=key;
			}
			if(this.messages==null){
				this.messages = new HashMap<String, Message>();
			}
			
			addMessage(message);
		}		
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
	
	private Language hasEnglishTranslation(List<Language> languages){
		Language retValue=null;
		for (Language language : languages) {
			if(language.getCode().equalsIgnoreCase("en")){
				retValue=language;
				break;
			}
		}
		return retValue;
	}
	
	public Trn createTrn() throws Exception{
		ObjectFactory of=new ObjectFactory();
		Trn trn=of.createTrn();
		trn.setKey(this.getKey());
		for(Map.Entry<String ,Message> entry : messages.entrySet()){
			Message msg=entry.getValue();
			if(trn.getKeywords()==null){
				trn.setKeywords(msg.getKeyWords());
			}
			if(trn.getSiteId()==null){
				trn.setSiteId(msg.getSiteId());
			}
			//creating Language
			Language lang=of.createLanguage();
			lang.setCode(msg.getLocale());
			lang.setValue(msg.getMessage());
			//created
			Calendar cal_u = Calendar.getInstance();
			cal_u.setTime(msg.getCreated());
			lang.setUpdated(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(cal_u.get(Calendar.YEAR),cal_u.get(Calendar.MONTH),cal_u.get(Calendar.DAY_OF_MONTH),cal_u.get(Calendar.HOUR),cal_u.get(Calendar.MINUTE),cal_u.get(Calendar.SECOND))));
			//last accessed			
			if(msg.getLastAccessed()!=null){
				Calendar lastAccessed = Calendar.getInstance();
				lastAccessed.setTime(msg.getLastAccessed());						
				lang.setLastAccessed(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(lastAccessed.get(Calendar.YEAR),lastAccessed.get(Calendar.MONTH),lastAccessed.get(Calendar.DAY_OF_MONTH),lastAccessed.get(Calendar.HOUR),lastAccessed.get(Calendar.MINUTE),lastAccessed.get(Calendar.SECOND))));						
			}
			trn.getLang().add(lang);
		}
		return trn;
	}
}
