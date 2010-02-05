package org.dgfoundation.amp.test.search;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllSearchTest {

	/**
	 * @param args
	 */
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(SearchTest.class);
		suite.addTestSuite(ResourcesTest.class);
		suite.addTestSuite(ActivitySearch.class);
	    return suite;
		
	}
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}
