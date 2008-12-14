package org.dgfoundation.amp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.test.PI.AllParisIndicatorsManagerTest;
import org.dgfoundation.amp.test.categorymanager.AllCategoryManagerTest;
import org.dgfoundation.amp.test.reports.AllReportTest;
import org.dgfoundation.amp.test.widget.AllWidgetTest;

public class AllTest {

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(AllReportTest.suite());
		suite.addTest(AllCategoryManagerTest.suite());
		suite.addTest(AllWidgetTest.suite());
		suite.addTest(AllParisIndicatorsManagerTest.suite());
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}