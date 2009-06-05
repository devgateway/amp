package org.dgfoundation.amp.test.customfields;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * All report test 
 * @author Mauricio
 *
 */
public class AllCustomFieldsTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(XMLCustomFieldParserTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
