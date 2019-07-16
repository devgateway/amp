package org.dgfoundation.amp.ar.amp212;

import java.util.List;

import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedReportsTestSchema;
import org.junit.BeforeClass;

/**
 * 
 * sanity checks for NiReports filtering offdb
 * 
 * @author Dolghier Constantin
 *
 */
public class OffDbNiReportFilteringTests extends FilteringSanityChecks {
    
    HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();
    public OffDbNiReportFilteringTests() {
        nrRunReports = 0;
    }
    
    
    @Override
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        return getOfflineExecutor(activityNames);
    }

    @BeforeClass
    public static void setUp() {
        // this empty method is used as a shadow for org.dgfoundation.amp.mondrian.ReportingTestCase.setUp()
    }
}
