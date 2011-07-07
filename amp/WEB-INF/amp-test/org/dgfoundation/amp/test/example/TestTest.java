package org.dgfoundation.amp.test.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestTest extends TestCase {

	public static Test suite() {
		junit.framework.TestSuite suite = new junit.framework.TestSuite();
		suite.addTest(new TestSuite(TestTest.class));
		return suite;

	}



	public void testTest() {
		assertEquals(true, true);

	}



}
