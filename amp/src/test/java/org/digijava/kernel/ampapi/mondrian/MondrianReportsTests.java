/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.MondrianReportGenerator;
import org.dgfoundation.amp.testutils.AmpTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test Reports generation via Reports API provided by {@link org.dgfoundation.amp.reports.MondrianReportGenerator MondrianReportGenerator}
 * @author Nadejda Mandrescu
 */
public class MondrianReportsTests extends AmpTestCase {

	private MondrianReportsTests(String name) {
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MondrianReportsTests.class.getName());
		suite.addTest(new MondrianReportsTests("testNoTotals"));
		return suite;
	}
	
	public void testNoTotals() {
		String err = null;
		ReportSpecificationImpl spec = new ReportSpecificationImpl("test");
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (AMPException e) {
			System.err.println(e.getMessage());
			err = e.getMessage();
		}
		//basic tests, todo more
		assertNull(err);
		assertNotNull(report);
		assertNotNull(report.reportContents);
	}
}
