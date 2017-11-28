package org.dgfoundation.amp.ar.amp212;

import java.util.List;

import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedReportsTestSchema;
import org.junit.BeforeClass;

/**
 * 
 * sanity checks for NiReports running offdb
 * 
 * @author Alexandru Cartaleanu
 *
 */
public class OffDbNiReportSortingTests extends SortingSanityChecks {
    
    HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();
    public OffDbNiReportSortingTests() {
        nrRunReports = 0;
    }
    
//  @Override
//  public<K> K buildNiReportDigest(ReportSpecification spec, List<String> activityNames, NiReportOutputBuilder<K> outputBuilder) {
//      NiReportExecutor executor = getExecutor(activityNames);
//      return executor.executeReport(spec, outputBuilder);
//  }   
    
    @Override
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        return getOfflineExecutor(activityNames);
    }
    
    @BeforeClass
    public static void setUp() {
        // this empty method is used as a shadow for org.dgfoundation.amp.mondrian.ReportingTestCase.setUp()
    }
}
