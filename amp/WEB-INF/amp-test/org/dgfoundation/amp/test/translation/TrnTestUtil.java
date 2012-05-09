package org.dgfoundation.amp.test.translation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.entity.Message;

/**
 * Some utilities used in translations testing.
 * @author Irakli Kobiashvili
 *
 */
public class TrnTestUtil {

	public static Message getMessage(String key,String locale,String body){
		Message result = new Message();
		result.setCreated(new Timestamp(System.currentTimeMillis()));
		result.setKey(key);
		result.setLocale(locale);
		result.setMessage(body);
		return result;
	}

	public static List<Message> getMessagesForCaseTest(){
		List<Message> result = new ArrayList<Message>();
		result.add(getMessage("key1", "en", "message1"));
		result.add(getMessage("key2", "sw", "message2"));
		result.add(getMessage("key3", "en", "message3"));
		result.add(getMessage("key2", "ge", "message4"));
		return result;
	}
	
	public static List<Message> getMessagesForHashPatch(){
		List<Message> result = new ArrayList<Message>();
		result.add(getMessage("key1", "en", "message1en"));
		result.add(getMessage("key1", "sw", "message1sw"));
		result.add(getMessage("key1", "fr", "message1fr"));
		result.add(getMessage("key2", "ge", "message2ge"));
		result.add(getMessage("key2", "en", "message2en"));
		result.add(getMessage("key3", "en", "message1en"));
		result.add(getMessage("key3", "fr", "message3fr"));
		result.add(getMessage("key4", "ge", "message4ge"));
		return result;
	}

}
