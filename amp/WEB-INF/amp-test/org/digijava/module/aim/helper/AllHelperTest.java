package org.digijava.module.aim.helper;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * All report test 
 * @author Mauricio
 *
 */
public class AllHelperTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(XMLCustomFieldParserTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
