package org.dgfoundation.amp.mondrian;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.struts.mock.MockHttpServletRequest;
import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiReportsEngineForTesting;
import org.dgfoundation.amp.nireports.TestcasesReportsSchema;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.amp.NiReportsGenerator;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.dgfoundation.amp.nireports.testcases.ReportModelGenerator;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedReportsTestSchema;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.testutils.ActivityIdsFetcher;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;
import org.junit.BeforeClass;

public abstract class ReportingTestCase extends AmpTestCase {
    
    static protected int nrRunReports = 0;
    
    public static<K extends Cell> List<K> nicelySorted(Collection<K> in) {
        return AmpCollections.sorted(in, (a, b) -> {
            int delta = Long.compare(a.activityId, b.activityId);
            if (delta == 0)
                delta = a.getDisplayedValue().compareTo(b.getDisplayedValue());
            if (delta == 0)
                delta = Long.compare(a.entityId, b.entityId);
            return delta;
        });
    }
    
    public ReportSpecification getReportSpecification(String reportName) {
        MockHttpServletRequest mockRequest = StandaloneAMPInitializer.populateMockRequest();

        AmpReports report = ReportTestingUtils.loadReportByName(reportName);
        mockRequest.getSession().setAttribute(Constants.CURRENT_MEMBER, report.getOwnerId().toTeamMember());
        return ReportsUtil.getReport(report.getAmpReportId());
    }

    public ReportSpecificationImpl changeReportCurrency(ReportSpecificationImpl input, String currencyCode) {
        input.getOrCreateSettings().setCurrencyCode(currencyCode);
        return input;
    }
    
    public ReportSpecificationImpl buildSpecification(String reportName, List<String> columns, List<String> measures, List<String> hierarchies, GroupingCriteria groupingCriteria) {
        return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria);
    }
        
    protected GeneratedReport runReportOnNiReports(ReportSpecification spec, String locale, List<String> entities,
            Class<? extends ReportAreaImpl> areaType) {
        try {
            org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
            if (TLSUtils.getRequest() == null)
                TLSUtils.getThreadLocalInstance().request = mockRequest;
            
            TLSUtils.getRequest().setAttribute(ReportEnvironment.OVERRIDDEN_WORKSPACE_FILTER, new ActivityIdsFetcher(entities));
            ReportExecutor generator = new NiReportsGenerator(AmpReportsSchema.getInstance(), false, null);
            GeneratedReport res = generator.executeReport(spec);
            return res;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
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
    
    protected ReportFiltersImpl buildSimpleFilter(String column, List<String> ids, boolean inclusive) {
        ReportFiltersImpl mrf = new ReportFiltersImpl();
        mrf.addFilterRule(new ReportColumn(column), new FilterRule(ids, inclusive));
        return mrf;

    }
    
    protected ReportFiltersImpl buildSimpleFilter(String column, String value, boolean inclusive) {
        ReportFiltersImpl mrf = new ReportFiltersImpl();
        mrf.addFilterRule(new ReportColumn(column), new FilterRule(value, inclusive));
        return mrf;
    }
    
    /**
     * runs a given lambda in the context of a fully initialized NiReports engine, which will have its activity filters overridden to generate ids corresponding to a given list of names in English
     * @param activityNames
     * @param runnable
     */
    public void runInEngineContext(List<String> activityNames, ExceptionConsumer<NiReportsEngine> runnable) {
        TestcasesReportsSchema.workspaceFilter = new ActivityIdsFetcher(activityNames);
        NiReportsEngineForTesting engine = new NiReportsEngineForTesting(TestcasesReportsSchema.instance, runnable);
        engine.execute(); // will run runnable in the engine's context
    }
    
    /**
     * runs a given lambda in the context of a fully initialized NiReports engine, which will have its activity filters overridden to generate ids corresponding to a given list of names in English
     * @param activityNames
     * @param runnable
     */
    public void runInEngineContext(List<String> activityNames, Function<ReportSpecificationImpl, ReportSpecification> reportSpecSupplier, ExceptionConsumer<NiReportsEngine> runnable) {
        TestcasesReportsSchema.workspaceFilter = new ActivityIdsFetcher(activityNames);
        NiReportsEngineForTesting engine = new NiReportsEngineForTesting(TestcasesReportsSchema.instance, 
            reportSpecSupplier,
            runnable);
        engine.execute(); // will run runnable in the engine's context
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
    
    public static String describeReportOutputInCode(ReportArea gr) {
        return new ReportAreaDescriber(null).describeInCode(gr);
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
    
    public String digestTransactionAmounts(CategAmountCell cell) {
        return String.format("(actId: %d, %s, adjustment_type: %s, transaction_type: %s)", cell.activityId, cell.amount, cell.metaInfo.getMetaInfo(MetaCategory.ADJUSTMENT_TYPE.category).v, cell.metaInfo.getMetaInfo(MetaCategory.TRANSACTION_TYPE.category).v);
    }
    
    public static NiReportExecutor getDbExecutor(List<String> activityNames) {
        NiReportExecutor res = new NiReportExecutor(TestcasesReportsSchema.instance);
        TestcasesReportsSchema.workspaceFilter = new ActivityIdsFetcher(activityNames);
        return res;
    }

    public static NiReportExecutor getOfflineExecutor(List<String> activityNames) {
        NiReportExecutor res = new NiReportExecutor(HardcodedReportsTestSchema.getInstance());
        HardcodedReportsTestSchema.workspaceFilter = new HashSet<>(activityNames);
        return res;
    }

    /**
     * override this one in your child classes
     * @param activityNames
     * @return
     */
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        return getOfflineExecutor(activityNames);
    }
    
    protected <K> K buildNiReportDigest(ReportSpecification spec, List<String> activityNames, NiReportOutputBuilder<K> outputBuilder) {
        return getNiExecutor(activityNames).executeReport(spec, outputBuilder);
    }
    
    protected ReportSpecification spec(String reportName) {
        ReportSpecification spec = AmpReportsToReportSpecification.convert(ReportTestingUtils.loadReportByName(reportName));
        return spec;
    }
    
    protected<K> K buildDigest(ReportSpecification spec, List<String> activityNames, NiReportOutputBuilder<K> outputBuilder) {
        nrRunReports ++;
        return buildNiReportDigest(spec, activityNames, outputBuilder);
    }
    
    protected void runNiTestCase(ReportSpecification spec, String locale, List<String> activityNames, NiReportModel cor) {
        runNiTestCase(cor, spec, locale, activityNames);
    }
    
    protected void runNiTestCase(NiReportModel cor, ReportSpecification spec, List<String> activityNames) {
        runNiTestCase(cor, spec, "en", activityNames);
    }
        
    protected void runNiTestCase(NiReportModel cor, ReportSpecification spec, String locale, List<String> activityNames) {
        setLocale(locale);
        NiReportModel out = buildDigest(spec, activityNames, new ReportModelGenerator());
        String delta = null;
        AssertionError assertionError = null;
        try {delta = cor.compare(out);}
        catch(Exception e) {
            delta = e.getMessage();
            if (delta == null || cor == null)
                delta = "(null)";
        }
        catch(AssertionError ass) {
            assertionError = ass;
        }
        if (delta != null || assertionError != null) {
            if (delta != null)
                System.err.format("error for test %s: %s\n", spec.getReportName(), delta);
            System.err.println("this is output for test " + spec.getReportName() + ": " + out.describeInCode());
            //System.err.println("this is output for test " + spec.getReportName() + new ReportAreaDescriber().describeInCode(out.body, 2));
        }
        assertNull(delta);
        if (assertionError != null)
            throw assertionError;
    }
    
    protected void compareBodies(String name, PaginatedReportAreaForTests cor, ReportArea out) {
        String delta = null;
        AssertionError assertionError = null;
        try {
            delta = cor.getDifferenceAgainst(out);
        }
        catch(Exception e) {
            delta = e.getMessage();
            if (delta == null || cor == null)
                delta = "(null)";
        }
        catch(AssertionError ass) {
            assertionError = ass;
        }
        if (delta != null || assertionError != null) {
            System.err.println("this is output for test " + name + ": " + new ReportAreaDescriber(null).describeInCode(out, 2));
        }
        assertNull(delta);
        if (assertionError != null)
            throw assertionError;
    }
    
    protected ReportSpecificationImpl withUnits(ReportSpecificationImpl spec, AmountsUnits unitsOption) {
        spec.getOrCreateSettings().setUnitsOption(unitsOption);
        return spec;
    }
    
//  public<K> K buildNiReportDigest(ReportSpecification spec, NiReportExecutor executor, NiReportOutputBuilder<K> outputBuilder) {
//      return executor.executeReport(spec, outputBuilder);
//  }
    
//  public static <K> K buildNiReportDigest(ReportSpecification spec, List<String> activityNames, NiReportOutputBuilder<K> outputBuilder) {
//      NiReportExecutor executor = getExecutor(activityNames);
//      return executor.executeReport(spec, outputBuilder);
//  }

    @BeforeClass
    public static void setUp() {
        StandaloneAMPInitializer.initialize();
    }
}

