package org.dgfoundation.amp.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;

import junit.framework.TestCase;

public class TranslatorWorkerTest extends TestCase {

	private TranslatorWorker worker = null;
	
	public TranslatorWorkerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		worker = TranslatorWorker.getInstance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testProcessKeyCaseMessage() {
		String keyInitial = " testKey1 ";
		String keyExpected = " testkey1 ";
		Message message = getMessage(keyInitial, "ge", "some body");
		worker.processKeyCase(message);
		assertNotNull(message.getKey());
		assertEquals(keyExpected, message.getKey());
	}

	public void testProcessKeyCaseString() {
		String keyInitial = " testKey1 ";
		String keyExpected = " testkey 1";
		String result = worker.processKeyCase(keyInitial);
		assertNotNull(result);
		assertEquals(keyExpected, result);
	}

	public void testProcessBodyCharsMessage() {
		fail("Not yet implemented");
	}

	public void testProcessBodyCharsString() {
		fail("Not yet implemented");
	}

	public void testSetHash() {
		fail("Not yet implemented");
	}

	public void testGenerateHash() {
		String body = "Something that should be translated";
		String hash = "1240420793";
		String result = TranslatorWorker.generateTrnKey(body);
		assertNotNull(result);
		assertEquals(hash, result);
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
