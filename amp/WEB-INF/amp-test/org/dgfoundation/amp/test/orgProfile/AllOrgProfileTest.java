package org.dgfoundation.amp.test.orgProfile;

import junit.framework.Test;
import junit.framework.TestSuite;



public class AllOrgProfileTest {
    public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(AddNewOrgProfileWidget.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}
