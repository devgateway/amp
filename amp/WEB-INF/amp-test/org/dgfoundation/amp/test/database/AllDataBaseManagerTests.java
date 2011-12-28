package org.dgfoundation.amp.test.database;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDataBaseManagerTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(DataBaseDataTest.class);
		return suite;
	}
}