package org.dgfoundation.amp.test.widget;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllWidgetTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();		
		suite.addTestSuite(WidgetTest.class);
		return suite;
	}
}
