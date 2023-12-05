package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;

import java.util.Set;

public class TestcasesReportsSchema extends AmpReportsSchema {
    public static IdsGeneratorSource workspaceFilter;
    public static boolean disableToAMoPSplitting = true;

    public final static TestcasesReportsSchema instance = new TestcasesReportsSchema(); 
    
    private TestcasesReportsSchema() {
    }

    @Override
    public Set<Long> _getWorkspaceActivities(NiReportsEngine engine) {
        return workspaceFilter.getIds();
    }
    
    @Override
    public boolean disableSubmeasureSplittingByColumn(NiReportsEngine engine) {
        return disableToAMoPSplitting;
    }
}
