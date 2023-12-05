package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.ar.StringGenerator;

public class NonFilteringTeamFilter implements StringGenerator {
    
    @Override
    public String getString() {
        // do not filter out any activity
        return "SELECT amp_activity_id FROM amp_activity";
    }
}
