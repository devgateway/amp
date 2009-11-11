/**
 * XmlPatcherTests.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.test.xmlpatcher;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
public class AllXmlPatcherTests {

	public static Test suite() {
			TestSuite suite = new TestSuite();
			
			suite.addTest(new XmlPatcherStructureTest("testPatchDiscovery"));
			suite.addTest(new XmlPatcherStructureTest("testDbNameCondition"));
			suite.addTest(new XmlPatcherStructureTest("testSQLCondition"));
			suite.addTest(new XmlPatcherStructureTest("testBSHCondition"));	
			suite.addTest(new XmlPatcherStructureTest("testPatchDependencyCondition"));
			return suite;
		}
	}


