package org.dgfoundation.amp.test.messages;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllMessagesTest {
    public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(MessageSettingsTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}
