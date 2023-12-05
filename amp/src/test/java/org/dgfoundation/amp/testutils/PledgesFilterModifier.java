package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReports;

import java.util.Arrays;
import java.util.List;

public class PledgesFilterModifier extends AmpReportModifier {
    List<String> pledgeNames;
    public PledgesFilterModifier(String... pledgeNames){
        this.pledgeNames = Arrays.asList(pledgeNames);
    }
    
    @Override
    public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter) {        
    }
    
    @Override
    public String getColumnFilter(String stockQuery, AmpColumns col, String extractorView){     
        String res = stockQuery + " AND (pledge_id = -1 OR pledge_id IN (SELECT pledge_id FROM v_pledges_titles WHERE title IN (" + Util.toCSStringForIN(pledgeNames) + ")))";
        return res;
    }

}
