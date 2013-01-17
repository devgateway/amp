package org.dgfoundation.amp.test.reports;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * All report test
 * 
 * @author Sebas
 * 
 */
public class AllReportTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestReportWizard.class);
		suite.addTestSuite(TestDonorReportbyDonorAgency.class);
		suite.addTestSuite(TestDonorReportbyNPO.class);
		suite.addTestSuite(TestDonorReportbyRegion.class);
		suite.addTestSuite(TestDonorReportbyRegionAndDonor.class);
		suite.addTestSuite(TestDonorReportbyRegionSectorandNPO.class);
		suite.addTestSuite(TestDonorReportbySector.class);
		suite.addTestSuite(TestDonorReportbySectorAndDonorAgency.class);
		suite.addTestSuite(TestRegionalReportbyRegion.class);
		suite.addTestSuite(TestRegionalReportbySector.class);
		suite.addTestSuite(TestRegionalReportbyRegionandSector.class);
		suite.addTestSuite(TestRegionalReportbyRegionAndNPO.class);
		suite.addTestSuite(TestRegionalReportyRegionSectorandNPO.class);
		suite.addTestSuite(TestComponentReportByRegion.class);
		// suite.addTestSuite(TestComponentReportByComponent.class);
		suite.addTestSuite(TestComponentReportBySector.class);
		suite.addTestSuite(TesComputedDatesColumns.class);
		suite.addTestSuite(TestComputedColumnReport.class);
		suite.addTestSuite(TestComputedMeasures.class);

		// suite.addTestSuite(TestComponentReportbyComponentandSector.class);
		// suite.addTestSuite(TestComponentReportbyComponentandRegion.class);
		// suite.addTestSuite(TestComponentReportbyComponentAndNPO.class);
		// suite.addTestSuite(TestComponentReportbyComponentSectorAndNPO.class);
		return suite;

	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
