
package org.dgfoundation.amp.test.ngomodule;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllNGOModuleTest {
    public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(NgoModuleTest.class);
                suite.addTestSuite(TypeNGOTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}

