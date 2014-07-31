/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;

/**
 * Test Reports generation via Reports API provided by {@link org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator MondrianReportGenerator}
 * @author Nadejda Mandrescu
 */
public class MondrianReportsTests extends AmpTestCase {
	private static String PRINT_PATH = null;

	private MondrianReportsTests(String name) {
		super(name);
	}
	
	public static Test suite()
	{
		MondrianUtils.PRINT_PATH = PRINT_PATH;
		TestSuite suite = new TestSuite(MondrianReportsTests.class.getName());
		suite.addTest(new MondrianReportsTests("testNoTotals"));
		suite.addTest(new MondrianReportsTests("testTotals"));
		suite.addTest(new MondrianReportsTests("testColumnSortingNoTotals"));
		suite.addTest(new MondrianReportsTests("testColumnMeasureSortingTotals"));
		suite.addTest(new MondrianReportsTests("testSortingByTuplesTotals"));
		return suite;
	}
	
	public void testNoTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testNoTotals", false);
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		generateAndValidate(spec, false);
	}
	
	public void testTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testTotals", true);
		generateAndValidate(spec, true);
	}
	
	public void testColumnSortingNoTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testColumnSortingNoTotals", false);
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), true)); //ascending
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ALL), false)); //descending
		generateAndValidate(spec, true);
	}
	
	public void testColumnMeasureSortingTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testColumnMeasureSortingTotals", true);
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), true)); //ascending
		spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL), false)); //descending
		generateAndValidate(spec, true);
	}

	public void testSortingByTuplesTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testSortingByTuplesTotals", true);
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_QUARTERLY);
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), true)); //ascending
		spec.addSorter(new SortingInfo("2013", "Q2", new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL), false)); //descending
		generateAndValidate(spec, true);
	}

	private ReportSpecificationImpl getDefaultSpec(String name, boolean doTotals) {
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
		return spec;
	}
	
	private void generateAndValidate(ReportSpecification spec, boolean print) {
		String err = null;
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, print);
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
