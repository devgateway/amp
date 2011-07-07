package org.dgfoundation.amp.test.jobs;

import junit.framework.Test;
import junit.framework.TestSuite;



public class AllJobsTest {
     public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(VerifyUserDateJobTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}
