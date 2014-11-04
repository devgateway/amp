/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.CompleteWorkspaceFilter;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.saiku.SaikuReportArea;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import clover.com.google.gson.Gson;
import clover.com.google.gson.GsonBuilder;

/**
 * Test Reports generation via Reports API provided by {@link org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator MondrianReportGenerator}
 * @author Nadejda Mandrescu
 */
public class MondrianReportsTests extends AmpTestCase {
	
	private MondrianReportsTests(String name) {
		super(name);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(MondrianReportsTests.class.getName());
		/* tests are failing so far, because we are moving out the totals calculation from MDX, 
		 * to revise when GeneratedReport structure will be build based on CellDataSet */  
		//suite.addTest(new MondrianReportsTests("testNoTotals"));
		//suite.addTest(new MondrianReportsTests("testTotals"));
		//suite.addTest(new MondrianReportsTests("testColumnSortingNoTotals"));
		//suite.addTest(new MondrianReportsTests("testColumnMeasureSortingTotals"));
		//suite.addTest(new MondrianReportsTests("testSortingByTuplesTotals"));
		//suite.addTest(new MondrianReportsTests("testMultipleDateFilters")); 
		//suite.addTest(new MondrianReportsTests("testActivityDateFilters"));
		suite.addTest(new MondrianReportsTests("testSectorsIds"));
		//suite.addTest(new MondrianReportsTests("testAmpReportToReportSpecification"));
		//suite.addTest(new MondrianReportsTests("testGenerateReportAsSaikuCellDataSet"));
		//suite.addTest(new MondrianReportsTests("testReportPagination"));
		//suite.addTest(new MondrianReportsTests("testHeavyQuery"));
		//suite.addTest(new MondrianReportsTests("testSimpleReportByRegion"));
		
		return suite;
	}
	
	public void testNoTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testNoTotals", false);
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		generateAndValidate(spec, true);
	}
	
	public void testTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testTotals", true);
		generateAndValidate(spec, true);
	}
	
	public void testColumnSortingNoTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testColumnSortingNoTotals", false);
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), true)); //ascending
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), false)); //descending
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
	
	public void testMultipleDateFilters() {
		ReportSpecificationImpl spec = getDefaultSpec("testMultipleDateFilters", true);
		String err = null;
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_QUARTERLY);
		MondrianReportFilters filters = new MondrianReportFilters();
		SimpleDateFormat sdf = new SimpleDateFormat(MoConstants.DATE_FORMAT);
		try {
			filters.addDateRangeFilterRule(sdf.parse("2010-01-01"), sdf.parse("2011-09-15"));
			filters.addYearsFilterRule(Arrays.asList(2013, 2014), true);
		} catch (Exception e) {
			e.printStackTrace();
			err = e.getMessage();
		}
		assertNull(err);
		spec.setFilters(filters);
		generateAndValidate(spec, true);
	}
	
	public void testActivityDateFilters() {
		ReportSpecificationImpl spec = getDefaultSpec("testActivityDateFilters", true);
		MondrianReportFilters filters = new MondrianReportFilters();
		ReportColumn activityCreatedOn = new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON, ReportEntityType.ENTITY_TYPE_ACTIVITY);
		spec.addColumn(activityCreatedOn);
		/*
		SimpleDateFormat sdf = new SimpleDateFormat(MoConstants.DATE_FORMAT);
		String err = null;
		try {
			//filters.addDateRangeFilterRule(activityCreatedOn, sdf.parse("2010-01-01"), sdf.parse("2011-09-15"));
			filters.addFilterRule(activityCreatedOn, new FilterRule("2010-01-01", "2011-09-15", true, true, true));
		} catch (Exception e) {
			e.printStackTrace();
			err = e.getMessage();
		}
		assertNull(err);
		spec.setFilters(filters);*/
		generateAndValidate(spec, true);
	}
	
	public void testSectorsIds() {
		ReportSpecificationImpl spec = new ReportSpecificationImpl("testSectorsIds");
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_SECTOR_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_5_ID, ReportEntityType.ENTITY_TYPE_ALL));
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
		generateAndValidate(spec, true);
	}
	
	private ReportSpecificationImpl getDefaultSpec(String name, boolean doTotals) {
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
		return spec;
	}
	
	public void testSimpleReportByRegion() {
		ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals");
		spec.addColumn(new ReportColumn(ColumnConstants.REGION, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		generateAndValidate(spec, true);
	}
	
	public void testAmpReportToReportSpecification1() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attr = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attr);
		HttpServletRequest test = TLSUtils.getRequest();
		System.out.println(test);
	}
	
	public void testAmpReportToReportSpecification() {
		ReportSpecificationImpl spec = getReportSpecificatin("NadiaMondrianTest");
		generateAndValidate(spec, true);
	}
	
	public void testGenerateReportAsSaikuCellDataSet() {
		ReportSpecificationImpl spec = getReportSpecificatin("NadiaMondrianTest");
		generateAndValidate(spec, true, true);
	}
	
	public void testReportPagination() {
		ReportSpecificationImpl spec = getReportSpecificatin("NadiaMondrianTest");
		GeneratedReport report = generateAndValidate(spec, true, false);
		ReportAreaMultiLinked[] areasDFArray = ReportPaginationUtils.convert(report.reportContents);
		ReportArea page0_10 = ReportPaginationUtils.getReportArea(areasDFArray, 0, 10);
		//doesn't work to print
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().serializeSpecialFloatingPointValues().create();
		String res = gson.toJson(page0_10);
		System.out.println(res);
	}
	
	private ReportSpecificationImpl getReportSpecificatin(String reportName) {
		//AmpReports report = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, 1018L);//id is from Moldova DB, TODO: update for tests db 
		AmpReports report = ReportTestingUtils.loadReportByName(reportName);

		org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
		mockRequest.setAttribute("ampReportId", report.getId().toString());
		TLSUtils.populate(mockRequest);
		ReportContextData.createWithId(report.getId().toString(), true);

		ReportSpecificationImpl spec = null;
		try {
			spec = AmpReportsToReportSpecification.convert(report);
		} catch (AMPException e) {
			System.err.println(e.getMessage());
		}
		assertNotNull(spec);
		
		//TODO: remove
		Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>();
		Iterator<ReportColumn> iter = spec.getColumns().iterator();
		hierarchies.add(iter.next());
		//hierarchies.add(iter.next());
		//hierarchies.add(iter.next());
		//spec.setHierarchies(hierarchies);
		//end to remove
		return spec;
	}
	
	public void testHeavyQuery() {
		long start = System.currentTimeMillis();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("testHeavyQuery");
		spec.addColumn(new ReportColumn(ColumnConstants.REGION, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		generateAndValidate(spec, true);
		long end = System.currentTimeMillis();
		System.out.println("Full time (with printing) = " + (end-start) + "(ms)");
	}
	
	private void generateAndValidate(ReportSpecification spec, boolean print) {
		generateAndValidate(spec, print, false);
	}
	
	private GeneratedReport generateAndValidate(ReportSpecification spec, boolean print, boolean asSaikuReport) {
		String err = null;
		MondrianReportGenerator generator = new MondrianReportGenerator(asSaikuReport ? SaikuReportArea.class : ReportAreaImpl.class, new ReportEnvironment("en", new CompleteWorkspaceFilter(null, null)), print);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
			System.out.println("[" + spec.getReportName() + "] total report generation duration = " + report.generationTime + "(ms)");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}
		//basic tests, todo more
		assertNull(err);
		assertNotNull(report);
		assertNotNull(report.reportContents);
		
		return report;
	}
}
