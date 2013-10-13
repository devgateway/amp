package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.dbentity.*;

/**
 * class used for "postprocessing" the settings of a report - like applying minor changes 
 * @author Dolghier Constantin
 *
 */
public interface AmpReportModifier 
{
	public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter);
}
