package org.dgfoundation.amp.newreports;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@Tag("databasetests")
public class AmpReportingTestCase extends ReportingTestCase {

    @BeforeAll
    public static void setUp() {
        StandaloneAMPInitializer.initialize();
    }

    @Override
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        return getDbExecutor(activityNames);
    }
}
