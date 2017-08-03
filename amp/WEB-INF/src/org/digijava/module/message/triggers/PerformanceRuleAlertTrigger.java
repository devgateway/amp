package org.digijava.module.message.triggers;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.message.helper.Event;
import org.digijava.module.message.helper.MessageConstants;

public class PerformanceRuleAlertTrigger extends Trigger {

    public static final String PARAM_TRIGGER_SENDER = "sender";
    public static final String PARAM_DATA_PERFORMANCE_ISSUES = "performanceIssues";

    private static final String[] parameterNames = new String[] { PARAM_TRIGGER_SENDER };

    public PerformanceRuleAlertTrigger(Object source) {
        if (!(source instanceof List))
            throw new RuntimeException("Incompatible object. Source must be a ! " + List.class);
        
        this.source = source;
        forwardEvent();
    }

    @Override
    protected Event generateEvent() {
        Event e = new Event(PerformanceRuleAlertTrigger.class);
        List<AmpActivityVersion> activities = (ArrayList<AmpActivityVersion>) source;
        
        PerfomanceRuleManager manager = PerfomanceRuleManager.getInstance();
        String performanceIssues = manager.buildPerformanceIssuesMessage(activities);
        
        e.getParameters().put(PARAM_TRIGGER_SENDER, MessageConstants.SENDER_TYPE_SYSTEM);
        e.getParameters().put(PARAM_DATA_PERFORMANCE_ISSUES, performanceIssues);
        
        return e;
    }

    @Override
    public String[] getParameterNames() {
        return parameterNames;
    }
}
