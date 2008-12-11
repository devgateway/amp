package org.dgfoundation.amp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.test.example.MockStrutTest;
import org.dgfoundation.amp.test.example.MockTagLibTest;
import org.dgfoundation.amp.test.example.SimpleUnitTest;
import org.dgfoundation.amp.test.reports.AllReportTest;

public class AllTest {

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(AllReportTest.suite());
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}