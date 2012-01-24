package org.digijava.module.aim.helper ;
import java.util.Collection;

import org.digijava.module.aim.dbentity.AmpReportSector;

public class AmpProjectBySector
{
	private AmpReportSector sector;
	private Collection ampActivityId ;
	
	public AmpReportSector getSector() 
	{
		return sector;
	}

	public Collection getAmpActivityId() 
	{
		return ampActivityId;
	}

	
	public void setSector(AmpReportSector sector) 
	{
		this.sector = sector ;
	}

	public void setAmpActivityId(Collection ampActivityId)
	{
		this.ampActivityId = ampActivityId;
	}
}