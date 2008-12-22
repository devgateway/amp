package org.dgfoundation.amp.test.currency;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllCurrencyRateTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(WSCurrencyClientTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
