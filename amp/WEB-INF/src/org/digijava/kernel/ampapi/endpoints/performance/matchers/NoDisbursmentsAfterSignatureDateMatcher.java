package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;

import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class NoDisbursmentsAfterSignatureDateMatcher extends PerformanceRuleMatcher {
    
    public NoDisbursmentsAfterSignatureDateMatcher() {
        super("noDisbAfterSignatureDate", "No Disbursments After Signature Date");
        
        this.attributes = new ArrayList<>();
        this.attributes.add(new PerformanceRuleMatcherAttribute("month", "Month after signature date", 
                AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.INTEGER));
    }
}
