package org.digijava.module.aim.helper ;
import org.digijava.module.aim.dbentity.AmpReportSector;
import java.util.Collection;

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