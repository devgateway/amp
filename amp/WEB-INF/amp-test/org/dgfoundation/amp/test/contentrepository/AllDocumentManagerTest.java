/**
 * 
 */
package org.dgfoundation.amp.test.contentrepository;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author MOUHAMAD
 *
 */
public class AllDocumentManagerTest {
	
	/**
	 * 
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(DocumentManagerBasicTest.class);
		return suite;
	}
}
