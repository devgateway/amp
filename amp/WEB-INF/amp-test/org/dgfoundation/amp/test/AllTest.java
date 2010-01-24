package org.dgfoundation.amp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.test.categorymanager.AllCategoryManagerTest;
import org.dgfoundation.amp.test.contacts.AllContactsTest;
import org.dgfoundation.amp.test.customfields.AllCustomFieldsTest;
import org.dgfoundation.amp.test.database.AllDataBaseManagerTests;
import org.dgfoundation.amp.test.featureManager.AllFmCheckTest;
import org.dgfoundation.amp.test.jobs.AllJobsTest;
import org.dgfoundation.amp.test.location.AllSelectLocationTest;
import org.dgfoundation.amp.test.messages.AllMessagesTest;
import org.dgfoundation.amp.test.ngomodule.AllNGOModuleTest;
import org.dgfoundation.amp.test.orgProfile.AllOrgProfileTest;
import org.dgfoundation.amp.test.parisindicator.AllParisIndicatorsManagerTest;
import org.dgfoundation.amp.test.reports.AllReportTest;
import org.dgfoundation.amp.test.sectormanager.AllSectorManagerTests;
import org.dgfoundation.amp.test.widget.AllWidgetTest;
import org.dgfoundation.amp.test.workspacemanager.AllWorkspaceManagerTests;
import org.dgfoundation.amp.test.xmlpatcher.AllXmlPatcherTests;
/**
 * This suite include all test suite modules 
 * @author Sebas
 *
 */
public class AllTest {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AllXmlPatcherTests.suite());
		suite.addTest(AllReportTest.suite());
		suite.addTest(AllCategoryManagerTest.suite());
		suite.addTest(AllWidgetTest.suite());
		suite.addTest(AllParisIndicatorsManagerTest.suite());
        suite.addTest(AllOrgProfileTest.suite());  
        suite.addTest(AllCustomFieldsTest.suite());
        suite.addTest(AllJobsTest.suite());
        suite.addTest(AllMessagesTest.suite());
        suite.addTest(AllDataBaseManagerTests.suite());
		suite.addTest(AllWorkspaceManagerTests.suite());
        suite.addTest(AllNGOModuleTest.suite()); 
        suite.addTest(AllSectorManagerTests.suite());
        suite.addTest(AllSelectLocationTest.suite());
        suite.addTest(AllFmCheckTest.suite());
        suite.addTest(AllContactsTest.suite());
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}