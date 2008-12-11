package org.dgfoundation.amp.test.reports;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllReportTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestReportWizard.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
