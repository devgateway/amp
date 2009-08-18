package org.dgfoundation.amp.test.sectormanager;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllSectorManagerTests {

    public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(SectorManagerActionTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}
