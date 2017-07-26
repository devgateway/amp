package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * @author Viorel Chihai
 *
 */
public final class PerformanceRuleMatchers {

    public static final List<PerformanceRuleMatcher> RULE_TYPES = new ArrayList<PerformanceRuleMatcher>() {
        {
            add(new NoUpdatedStatusAfterSignatureDatesMatcher());
            add(new NoDisbursmentsAfterSignatureDateMatcher());
            add(new DisbursementsAfterClosingDateMatcher());
            add(new NoUpdatedDisbursmentsMatcher());
        }
    };
    
    public static final Map<String, PerformanceRuleMatcher> RULE_TYPES_BY_NAME = 
            PerformanceRuleMatchers.RULE_TYPES.stream()
            .collect(Collectors.toMap(r -> r.getName(), Function.identity()));

    private PerformanceRuleMatchers() {

    }

}
