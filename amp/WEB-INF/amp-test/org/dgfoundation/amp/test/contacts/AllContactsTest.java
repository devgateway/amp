package org.dgfoundation.amp.test.contacts;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllContactsTest {
	 public static Test suite() {
			TestSuite suite = new TestSuite();
			suite.addTestSuite(PrimaryContactCheckTest.class);
			return suite;
		}

		public static void main(String[] args) {
			junit.textui.TestRunner.run(suite());
		}
}
