package org.dgfoundation.amp.mondrian;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.testutils.ActivityIdsFetcher;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.dgfoundation.amp.newreports.ReportAreaImpl;

public abstract class MondrianReportsTestCase extends AmpTestCase
{
	public MondrianReportsTestCase(String name) {
		super(name);
	}
	
	/**
	 * runs a single report test and compares the result with the expected cor 
	 * @param testName - test name to be displayed in case of error
	 * @param reportName - the name of the report (AmpReports entry in the tests database) which should be run
	 * @param activities - the names of the activities which should be presented via a dummy WorkspaceFilter to the report. Put ReportTestingUtils.NULL_PLACEHOLDER if NO WorkspaceFilter should be put (e.g. let the report see all the activities) 
	 * @param correctResult - a model (sketch) of the expected result
	 * @param modifier - the modifier (might be null) to postprocess AmpReports and AmpARFilter after being loaded from the DB
	 */
	protected void runMondrianTestCase(String testName, String reportName, List<String> activities, ReportAreaForTests correctResult, String locale) {
		
		org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
		TLSUtils.populate(mockRequest);

		AmpReports report = ReportTestingUtils.loadReportByName(reportName);
		ReportSpecification spec = ReportsUtil.getReport(report.getAmpReportId());
		runMondrianTestCase(spec, locale, activities, correctResult);
	}
	
//	protected void runReportTest(String testName, ReportSpecification reportSpec, List<String> activities, GeneratedReport correctResult, String locale) {
//		GeneratedReport report = runReportOn(reportSpec, locale, activities);
//		String error = compareOutputs(correctResult, report);
//		assertNull(String.format("test %s, report %s: %s", testName, reportSpec, error), error);
//	}
	
	protected GeneratedReport runReportOn(String reportName, String locale, List<String> activities) {
		AmpReports report = ReportTestingUtils.loadReportByName(reportName);
		ReportSpecification spec = ReportsUtil.getReport(report.getAmpReportId());
		return runReportOn(spec, locale, activities);
	}
	
	protected GeneratedReport runReportOn(ReportSpecification spec, String locale, List<String> activities) {
		try {
			ReportEnvironment env = new ReportEnvironment(locale, new ActivityIdsFetcher(activities), FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY));
			MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, env);
			GeneratedReport res = generator.executeReport(spec);
			return res;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ReportSpecification buildSpecification(String reportName, List<String> columns, List<String> measures, List<String> hierarchies, GroupingCriteria groupingCriteria) {
		ReportSpecificationImpl spec = new ReportSpecificationImpl(reportName);
		
		for(String columnName:columns)
			spec.addColumn(MondrianReportUtils.getColumn(columnName, ReportEntityType.ENTITY_TYPE_ALL));
		
		for(String measureName:measures)
			spec.addMeasure(new ReportMeasure(measureName, ReportEntityType.ENTITY_TYPE_ALL));
		
		if (hierarchies != null) {
			for(String hierarchyName:hierarchies) {
				if (!columns.contains(hierarchyName))
					throw new RuntimeException("hierarchy should be present in column list: " + hierarchyName);
				spec.getHierarchies().add(MondrianReportUtils.getColumn(hierarchyName, ReportEntityType.ENTITY_TYPE_ALL));
			}
		}

		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		spec.setGroupingCriteria(groupingCriteria);

		return spec;
	}
	
	protected void runMondrianTestCase(ReportSpecification spec, String locale, List<String> activities, ReportAreaForTests cor) {
		GeneratedReport rep = this.runReportOn(spec, locale, activities);
		//Iterator<ReportOutputColumn> bla = rep.reportContents.getChildren().get(0).getContents().keySet().iterator();
		//ReportOutputColumn first = bla.next(), second = bla.next(), third = bla.next(), fourth = bla.next();
		
		//if (cor == null) return;
		String delta = cor == null ? null : cor.getDifferenceAgainst(rep.reportContents);
		
		if (cor == null || delta != null)
			System.err.println("this is output for test " + spec.getReportName() + describeInCode(rep.reportContents, 1));
		
		if (delta != null)
			fail("test " + spec.getReportName() + " failed: " + delta);			
	}
	
	public static String describeReportOutputInCode(GeneratedReport gr) {
		return describeInCode(gr.reportContents, 1);
	}
	
	public static String describeInCode(ReportArea area, int depth) {
		if (area.getOwner() != null)
			throw new RuntimeException("describing owned reports not implemented!");
		
		return String.format("%snew ReportAreaForTests()\n%s%s%s", prefixString(depth),
				prefixString(depth), describeContents(area, depth),
				describeChildren(area, depth));
	}
	
	public static String describeContents(ReportArea area, int depth) {
		if (area.getContents() == null) return "";
		StringBuffer res = new StringBuffer(prefixString(depth) + ".withContents(");	
		boolean first = true;
		for (ReportOutputColumn colKey:area.getContents().keySet()) {
			ReportCell colContents = area.getContents().get(colKey);
			res.append(String.format("%s\"%s\", \"%s\"", first ? "" : ", ", generateDisplayedName(colKey), colContents.displayedValue));
			first = false;
		}
		res.append(")");
		return res.toString();
	}
	
	public static String generateDisplayedName(ReportOutputColumn colKey) {
		//return colKey.getHierarchicalName();
		if (colKey.parentColumn == null)
			return colKey.originalColumnName;
		return String.format("%s-%s", generateDisplayedName(colKey.parentColumn), colKey.originalColumnName);
	}
	
	public static String describeChildren(ReportArea area, int depth) {
		if (area.getChildren() == null) return "";
		StringBuffer res = new StringBuffer("\n" + prefixString(depth) + ".withChildren(");
		for(int i = 0; i < area.getChildren().size(); i++) {
			ReportArea child = area.getChildren().get(i);
			res.append("\n");
			res.append(describeInCode(child, depth + 1));
			if (i != area.getChildren().size() - 1)
				res.append(",");
		}
		res.append(prefixString(depth) + ")");
		return res.toString();
	}
	
	/**
	 * returns the prefix of a string shifted right
	 * @param depth
	 * @return
	 */
	public static String prefixString(int depth)
	{
		String res = "";
		for(int i = 0; i < depth; i++)
			res = res + "  ";
		return res;
	}
}

