package org.digijava.module.message.triggers;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceIssue;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.message.helper.Event;
import org.digijava.module.message.helper.MessageConstants;

import java.util.List;
import java.util.Map;

public class PerformanceRuleAlertTrigger extends Trigger {

    public static final String PARAM_TRIGGER_SENDER = "sender";
    public static final String PARAM_DATA_PERFORMANCE_ISSUES = "performanceIssues";

    private static final String[] PARAMETER_STRINGS = new String[] { PARAM_TRIGGER_SENDER };

    public PerformanceRuleAlertTrigger(Object source) {
        if (!(source instanceof Map)) {
            throw new RuntimeException("Incompatible object. Source must be a ! " + Map.class);
        }
        
        this.source = source;
        forwardEvent();
    }

    @Override
    protected Event generateEvent() {
        Event e = new Event(PerformanceRuleAlertTrigger.class);
        Map<AmpActivityVersion, List<PerformanceIssue>> activitiesWithPerfIssues = 
                (Map<AmpActivityVersion, List<PerformanceIssue>>) source;
        
        PerformanceRuleManager manager = PerformanceRuleManager.getInstance();
        String performanceIssues = manager.buildPerformanceIssuesMessage(activitiesWithPerfIssues);
        
        e.getParameters().put(PARAM_TRIGGER_SENDER, MessageConstants.SENDER_TYPE_SYSTEM);
        e.getParameters().put(PARAM_DATA_PERFORMANCE_ISSUES, performanceIssues);
        
        return e;
    }

    @Override
    public String[] getParameterNames() {
        return PARAMETER_STRINGS;
    }
}
