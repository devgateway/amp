package org.dgfoundation.amp.test.location;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllSelectLocationTest {
	   public static Test suite() {
			TestSuite suite = new TestSuite();
			suite.addTestSuite(SelectDynLocationTest.class);
			return suite;
		}

		public static void main(String[] args) {
			junit.textui.TestRunner.run(suite());
		}

}
