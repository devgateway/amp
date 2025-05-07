package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * class used for "postprocessing" the settings of a report - like applying minor changes 
 * @author Dolghier Constantin
 *
 */
public abstract class AmpReportModifier 
{
    public abstract void modifyAmpReportSettings(AmpReports report, AmpARFilter filter);
    public String getColumnFilter(String stockQuery, AmpColumns col, String extractorView){
        return null; //do nothing
    }
}
