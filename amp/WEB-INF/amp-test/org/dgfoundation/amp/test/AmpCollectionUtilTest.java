package org.dgfoundation.amp.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.util.TrnUtil;

import junit.framework.TestCase;

/**
 * test case for {@link AmpCollectionUtils}.
 * Testing tests itself.
 * @author Irakli Kobiashvili
 *
 */
public class AmpCollectionUtilTest extends TestCase {

	public AmpCollectionUtilTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateMap() {
		int expectedSize = 3;
		List<Message> messagesList = getMessages();
		Map<String, Message> messageMap = AmpCollectionUtils.createMap(messagesList, new TrnUtil.MessageShortKeyResolver());
		assertNotNull(messageMap);
		assertEquals(expectedSize, messageMap.size());
	}

	public void testGetIdsArray() {
		List<Message> mockMessages = getMessages();
		String ids[] = AmpCollectionUtils.getIdsArray(mockMessages, new TrnUtil.MessageShortKeyResolver());
		assertNotNull(ids);
		assertEquals(4, ids.length);
		int c=0;
		for (Message message : mockMessages) {
			assertEquals(ids[c],message.getKey());
		}
	}

	public void testSearchByKey() {
		String expectedKey="key3";
		List<Message> mockMessages = getMessages();
		Message foundMesage = AmpCollectionUtils.searchByKey(mockMessages, expectedKey, new TrnUtil.MessageShortKeyResolver());
		assertNotNull(foundMesage);
		assertEquals(expectedKey, foundMesage.getKey());
	}

	private List<Message> getMessages(){
		List<Message> result = new ArrayList<Message>();
		result.add(getMessage("key1", "en", "message1"));
		result.add(getMessage("key2", "sw", "message2"));
		result.add(getMessage("key3", "en", "message3"));
		result.add(getMessage("key2", "ge", "message4"));
		return result;
	}
	private Message getMessage(String key,String locale,String body){
		Message result = new Message();
		result.setCreated(new Timestamp(System.currentTimeMillis()));
		result.setKey(key);
		result.setLocale(locale);
		result.setMessage(body);
		return result;
	}
}
