package org.dgfoundation.amp.test.usermanager;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * All user manager test 
 * @author Marcelo Sotero
 *
 */

public class AllUserManagerTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(AdminAddUserTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
