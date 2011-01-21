package org.dgfoundation.amp.test.workspacemanager;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllWorkspaceManagerTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(WorkspaceDataTest.class);
		return suite;
	}
}