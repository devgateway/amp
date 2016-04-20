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

import org.dgfoundation.amp.ar.ArConstants;
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
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
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
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
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
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		generateAndValidate(spec, true);
	}
	
	public void testTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testTotals", true);
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
		ReportColumn activityCreatedOn = new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON);
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
		ReportSpecificationImpl spec = new ReportSpecificationImpl("testSectorsIds", ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_SECTOR_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_5_ID));
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
		generateAndValidate(spec, true);
	}
	
	private ReportSpecificationImpl getDefaultSpec(String name, boolean doTotals) {
		if (!doTotals)
			throw new RuntimeException("doTotals==false not supported anymore");
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_TYPE));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
		return spec;
	}
	
	public void testSimpleReportByRegion() {
		ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals", ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(ColumnConstants.REGION));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		generateAndValidate(spec, true);
	}
	
	public void testAmpReportToReportSpecification1() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attr = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attr);
		HttpServletRequest test = TLSUtils.getRequest();
		System.out.println(test);
	}
		
	public void testHeavyQuery() {
		long start = System.currentTimeMillis();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("testHeavyQuery", ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(ColumnConstants.REGION));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
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
		ReportExecutor generator = new MondrianReportGenerator(
				asSaikuReport ? SaikuReportArea.class : ReportAreaImpl.class, 
				new ReportEnvironment("en", new CompleteWorkspaceFilter(null, null), FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY)), 
				print);
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
