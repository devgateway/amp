package org.dgfoundation.amp.test.example;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllSampleTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(MockTagLibTest.class);
		suite.addTestSuite(MockStrutTest.class);
		suite.addTestSuite(SimpleUnitTest.class);
		suite.addTestSuite(UserRegistrationTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}
