package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.legacy.ActivityPreviewTests;
import org.dgfoundation.amp.ar.legacy.CategoryManagerTests;
import org.dgfoundation.amp.ar.legacy.DirectedDisbursementsTests;
import org.dgfoundation.amp.ar.legacy.DirectedDisbursementsTests_amp27;
import org.dgfoundation.amp.ar.legacy.FiltersTests;
import org.dgfoundation.amp.ar.legacy.HierarchyTests27;
import org.dgfoundation.amp.ar.legacy.MiscColumnsTests;
import org.dgfoundation.amp.ar.legacy.MiscReportsTests;
import org.dgfoundation.amp.ar.legacy.MiscTests28;
import org.dgfoundation.amp.ar.legacy.MtefTests;
import org.dgfoundation.amp.ar.legacy.MultilingualTests;
import org.dgfoundation.amp.ar.legacy.MultilingualTests28;
//import org.dgfoundation.amp.testutils.LiberiaFiller;
import org.dgfoundation.amp.ar.legacy.MultilingualThroughTrnTests;
import org.dgfoundation.amp.ar.legacy.OldReportsNewFeaturesTests;
import org.dgfoundation.amp.ar.legacy.PledgesFormTests;
import org.dgfoundation.amp.ar.legacy.ProgramsTests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * entry point for AMP 2.6 tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
public class AllTests_LegacyReports {

	public static Test suite() {
		
		setUp();
//		LiberiaFiller.fillInDatabase();
		TestSuite suite = new TestSuite(AllTests_LegacyReports.class.getName());
		//$JUnit-BEGIN$

		//suite.addTestSuite(DirectedDisbursementsTests.class);
		suite.addTest(DirectedDisbursementsTests.suite());
		suite.addTest(MtefTests.suite());
		suite.addTest(ActivityPreviewTests.suite());
		suite.addTest(MiscColumnsTests.suite());
		//suite.addTest(EsriTestCases.suite());
		suite.addTest(FiltersTests.suite());
		suite.addTest(MiscReportsTests.suite());
		suite.addTest(new JUnit4TestAdapter(OldReportsNewFeaturesTests.class));	
		suite.addTest(HierarchyTests27.suite());
		suite.addTest(DirectedDisbursementsTests_amp27.suite());
		suite.addTest(MultilingualTests.suite());
		suite.addTest(MultilingualThroughTrnTests.suite());
		suite.addTest(CategoryManagerTests.suite());
		
		suite.addTest(MultilingualTests28.suite());
		suite.addTest(PledgesFormTests.suite());
		suite.addTest(MiscTests28.suite());
		suite.addTest(ProgramsTests.suite());
		
		//suite.addTest(new DirectedDisbursementsTests("testReports"));
		//$JUnit-END$
		return suite;
	}
	
	public static void main(String[] args) {		
		junit.textui.TestRunner.run(suite());
	}

	public static void setUp() {
		AllTests_amp212.setUp();
	}
	
}

