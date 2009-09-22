package org.dgfoundation.amp.test.featureManager;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllFmCheckTest {
	
	public static Test suite() {
		System.out.println("Test Start");
		TestSuite suite = new TestSuite();
		suite.addTestSuite(FmCheckTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}	
}
