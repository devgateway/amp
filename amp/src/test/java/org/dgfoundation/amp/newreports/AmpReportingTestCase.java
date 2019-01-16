package org.dgfoundation.amp.newreports;

import java.util.List;

import org.dgfoundation.amp.newreports.ReportingTestCase;
import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

/**
 * @author Octavian Ciubotaru
 */
@Category(DatabaseTests.class)
public class AmpReportingTestCase extends ReportingTestCase {

    @BeforeClass
    public static void setUp() {
        StandaloneAMPInitializer.initialize();
    }

    @Override
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        return getDbExecutor(activityNames);
    }
}
