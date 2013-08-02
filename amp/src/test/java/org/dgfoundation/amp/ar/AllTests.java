package org.dgfoundation.amp.ar;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$

		//suite.addTestSuite(DirectedDisbursementsTests.class);
		//suite.addTest(DirectedDisbursementsTests.suite());
		suite.addTest(new DirectedDisbursementsTests("testReports"));
		//$JUnit-END$
		return suite;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}
