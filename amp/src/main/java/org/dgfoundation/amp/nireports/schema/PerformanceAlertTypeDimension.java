package org.dgfoundation.amp.nireports.schema;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * NiReports' hardcoded boolean dimension, used for performance alert type columns
 * @author Viorel Chihai
 *
 */
public class PerformanceAlertTypeDimension extends ConstantNiDimension {
    
    public PerformanceAlertTypeDimension(String name) {
        super(name, 1, getPerformanceTypeList());
    }

    private static List<List<Long>> getPerformanceTypeList() {
        List<List<Long>> data = PerformanceRuleManager.PERF_ALERT_TYPE_TO_ID.values()
                .stream().map(id -> Arrays.asList(id))
                .collect(Collectors.toList());
        
        return data;
    }
}
