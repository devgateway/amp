package org.dgfoundation.amp.test.parisindicator;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllParisIndicatorsManagerTest {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ParisIndicatorConfigDataTest.class);
		suite.addTestSuite(ParisIndicatorActionTest.class);
		return suite;
	}
}
