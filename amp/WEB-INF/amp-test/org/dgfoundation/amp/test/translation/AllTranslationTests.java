package org.dgfoundation.amp.test.translation;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTranslationTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	private static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TranslatorWorkerTest.class);
		suite.addTestSuite(TrnUtilTest.class);
		return suite;
	}

}
