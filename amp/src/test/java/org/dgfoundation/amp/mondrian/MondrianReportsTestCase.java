package org.dgfoundation.amp.mondrian;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.PartialReportArea;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.testutils.ActivityIdsFetcher;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.PledgeIdsFetcher;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

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
		
		runMondrianTestCase(getReportSpecification(reportName), locale, activities, correctResult);
	}
	
	protected ReportSpecification getReportSpecification(String reportName) {
		org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
		TLSUtils.populate(mockRequest);

		AmpReports report = ReportTestingUtils.loadReportByName(reportName);
		mockRequest.getSession().setAttribute(Constants.CURRENT_MEMBER, report.getOwnerId().toTeamMember());
		return ReportsUtil.getReport(report.getAmpReportId());
	}
	
	protected void runMondrianTestCase(String reportName, List<String> activities, ReportAreaForTests correctResult, String locale,
			Class<? extends ReportAreaImpl> areaType, Integer page, Integer pageSize) {
			runMondrianTestCase(getReportSpecification(reportName), locale, activities, correctResult, areaType, page, pageSize);
	}
	
	protected void runMondrianTestCase(String reportName, List<String> activities, ReportAreaForTests correctResult, String locale) {
		runMondrianTestCase(reportName, reportName, activities, correctResult, locale);
	}
	
//	protected void runReportTest(String testName, ReportSpecification reportSpec, List<String> activities, GeneratedReport correctResult, String locale) {
//		GeneratedReport report = runReportOn(reportSpec, locale, activities);
//		String error = compareOutputs(correctResult, report);
//		assertNull(String.format("test %s, report %s: %s", testName, reportSpec, error), error);
//	}
	
	protected GeneratedReport runReportOn(String reportName, String locale, List<String> entities, 
			Class<? extends ReportAreaImpl> areaType) {
		AmpReports report = ReportTestingUtils.loadReportByName(reportName);
		ReportSpecification spec = ReportsUtil.getReport(report.getAmpReportId());
		return runReportOn(spec, locale, entities, ReportAreaImpl.class);
	}
	
	protected GeneratedReport runReportOn(ReportSpecification spec, String locale, List<String> entities,
			Class<? extends ReportAreaImpl> areaType) {
		try {
			IdsGeneratorSource activitiesSrc = null;
			IdsGeneratorSource pledgesSrc = null;
			if (spec.getReportType() == ArConstants.PLEDGES_TYPE)
				pledgesSrc = new PledgeIdsFetcher(entities);
			else
				activitiesSrc = new ActivityIdsFetcher(entities);
			ReportEnvironment env = new ReportEnvironment(locale, activitiesSrc, pledgesSrc, FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY));
			ReportExecutor generator = new MondrianReportGenerator(areaType, env);
			GeneratedReport res = generator.executeReport(spec);
			return res;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ReportSpecificationImpl buildSpecification(String reportName, List<String> columns, List<String> measures, List<String> hierarchies, GroupingCriteria groupingCriteria) {
		return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria);
	}
	
	/**
	 * 
	 * @param spec
	 * @param locale
	 * @param entities
	 * @param cor
	 */
	protected void runMondrianTestCase(ReportSpecification spec, String locale, List<String> entities, ReportAreaForTests cor) {
		runMondrianTestCase(spec, locale, entities, cor, ReportAreaImpl.class, null, null);
	}
	
	/**
	 * Runs Mondrian Test case with pagination option
	 * 
	 * @param spec
	 * @param locale 
	 * @param entities
	 * @param cor
	 * @param areaType normally for Pagination you would use {@link PartialReportArea}
	 * @param page page number starting from 1 (as if it is selected by the user in the UI)
	 * @param pageSize number of leaf records per page or null to use the default config (not recommended)
	 */
	protected void runMondrianTestCase(ReportSpecification spec, String locale, List<String> entities, 
			ReportAreaForTests cor, Class<? extends ReportAreaImpl> areaType, Integer page, Integer pageSize) {
		GeneratedReport rep = this.runReportOn(spec, locale, entities, areaType);
		//Iterator<ReportOutputColumn> bla = rep.reportContents.getChildren().get(0).getContents().keySet().iterator();
		//ReportOutputColumn first = bla.next(), second = bla.next(), third = bla.next(), fourth = bla.next();
		
		ReportArea reportContents = ReportPaginationUtils.getSinglePage(rep.reportContents, page, pageSize);
		
		//if (cor == null) return;
		String delta = cor == null ? null : cor.getDifferenceAgainst(reportContents);
		
		if (cor == null || delta != null) {
			System.err.println("\n------------------------------------------------------------------------------------------");
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (int i = 0; i < stackTrace.length; i++) {
				StackTraceElement stackTraceEl = stackTrace[i];
				if (stackTraceEl.getClassName().startsWith("org.dgfoundation.amp")
						&& !stackTraceEl.getClassName().contains(MondrianReportsTestCase.class.getName())) {
					System.err.println(stackTraceEl.toString() + ":");
					break;
				}
			}
			System.err.println("this is output for test " + spec.getReportName() + describeInCode(reportContents, 1));
		}

        checkReportHeaders(rep, spec);

		if (delta != null) {
            fail("test " + spec.getReportName() + " failed: " + delta);
        }
	}
	
	protected ReportSpecificationImpl buildActivityListingReportSpec(String name) {
		ReportSpecificationImpl spec = buildSpecification(name, 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		spec.setDisplayEmptyFundingColumns(true);
		spec.setDisplayEmptyFundingRows(true);
		return spec;
	}
	
	protected MondrianReportFilters buildSimpleFilter(String column, List<String> ids, boolean inclusive) {
		MondrianReportFilters mrf = new MondrianReportFilters();
		mrf.addFilterRule(new ReportColumn(column), new FilterRule(ids, inclusive));
		return mrf;

	}
	
	protected MondrianReportFilters buildSimpleFilter(String column, String value, boolean inclusive) {
		MondrianReportFilters mrf = new MondrianReportFilters();
		mrf.addFilterRule(new ReportColumn(column), new FilterRule(value, inclusive));
		return mrf;
	}

    /**
     * if the report specification asks for headers to be generated regardless of the report being empty, checks that all of the columns specified in the spec are present in the output headers
     * @param rep
     * @param spec
     */
    public static void checkReportHeaders(GeneratedReport rep, ReportSpecification spec) {
        if (spec.getColumns() == null || !spec.isPopulateReportHeadersIfEmpty()) {
            return;
        }

       	assertFalse("Report headers cannot be empty", rep.leafHeaders == null || rep.leafHeaders.isEmpty());

        // convert leafHeaders to Map for easier access
        Map<String, ReportOutputColumn> leafHeadersMap = new HashMap<String, ReportOutputColumn>();
        for (ReportOutputColumn roc : rep.leafHeaders) {
            leafHeadersMap.put(roc.originalColumnName, roc);
        }

        for (ReportColumn rc : spec.getColumns()) {
            ReportOutputColumn outputColumn = leafHeadersMap.get(rc.getColumnName());
            assertNotNull("Column '" + outputColumn + "' Does not exist in the headers", rep);
        }
    }
	
	public static String describeReportOutputInCode(GeneratedReport gr) {
		return describeInCode(gr.reportContents, 1);
	}
	
	public static String describeInCode(ReportArea area, int depth) {
		if (area.getOwner() != null)
			throw new RuntimeException("describing owned reports not implemented!");
		
		boolean isPartialReportArea = PartialReportArea.class.isAssignableFrom(area.getClass());
		String testAreaType = isPartialReportArea ? "PaginatedReportAreaForTests" : "ReportAreaForTests"; 
		
		return String.format("%snew %s()%s%s%s", prefixString(depth), testAreaType,
				(area.getChildren() != null && area.getChildren().size() > 1) ? "\n" + prefixString(depth) : "",
				describeContents(area, depth, isPartialReportArea),
				describeChildren(area, depth));
	}
	
	public static String describeContents(ReportArea area, int depth, boolean isPartialReportArea) {
		if (area.getContents() == null) return "";
		StringBuffer res = new StringBuffer(prefixString(depth) + ".withContents(");	
		boolean first = true;
		for (ReportOutputColumn colKey:area.getContents().keySet()) {
			ReportCell colContents = area.getContents().get(colKey);
			res.append(String.format("%s\"%s\", \"%s\"", first ? "" : ", ", generateDisplayedName(colKey), colContents.displayedValue));
			first = false;
		}
		res.append(")");
		if (isPartialReportArea) {
			PartialReportArea pArea = (PartialReportArea) area;
			res.append(".withCounts(").append(pArea.getCurrentLeafActivitiesCount()).append(", ")
			.append(pArea.getTotalLeafActivitiesCount()).append(")");
		}
		return res.toString();
	}
	
	public static String generateDisplayedName(ReportOutputColumn colKey) {
		//return colKey.getHierarchicalName();
		if (colKey.parentColumn == null)
			return colKey.originalColumnName == null ? "<null>" : colKey.originalColumnName;
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
	
	public static AmpReportColumn ampReportColumnForColName(String colName, long order) {
		AmpColumns col = (AmpColumns) PersistenceManager.getSession().createQuery("FROM " + AmpColumns.class.getName() + " c WHERE c.columnName=:colName").setString("colName", colName).uniqueResult();
		if (col == null)
			throw new RuntimeException("column with name <" + colName + "> not found!");
		
		AmpReportColumn res = new AmpReportColumn();
		res.setColumn(col);
		res.setOrderId(order);
		return res;
	}
}

