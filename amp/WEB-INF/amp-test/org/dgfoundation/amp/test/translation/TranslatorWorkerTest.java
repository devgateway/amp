package org.dgfoundation.amp.test.translation;

import junit.framework.TestCase;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;

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
		Message message = TrnTestUtil.getMessage(keyInitial, "ge", "some body");
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


}
