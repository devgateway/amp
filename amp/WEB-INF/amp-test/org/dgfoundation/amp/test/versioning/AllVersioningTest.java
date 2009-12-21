package org.dgfoundation.amp.test.versioning;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllVersioningTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestVersionActivity.class);
		suite.addTestSuite(TestCompareVersion.class);

		return suite;

	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
