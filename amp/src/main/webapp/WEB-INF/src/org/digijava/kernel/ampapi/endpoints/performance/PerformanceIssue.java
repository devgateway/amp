package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.List;

import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class PerformanceIssue {

    private PerformanceRuleMatcher matcher;
    private List<AmpOrganisation> donors;
    
    public PerformanceIssue(PerformanceRuleMatcher matcher, List<AmpOrganisation> donors) {
        super();
        this.matcher = matcher;
        this.donors = donors;
    }

    public List<AmpOrganisation> getDonors() {
        return donors;
    }

    public void setDonors(List<AmpOrganisation> donors) {
        this.donors = donors;
    }

    public PerformanceRuleMatcher getMatcher() {
        return matcher;
    }

    public void setMatcher(PerformanceRuleMatcher matcher) {
        this.matcher = matcher;
    }

}
