package org.digijava.module.aim.helper ;
import java.util.Collection;

public class AmpSectors
{
	private String sectorName;
	private Collection donors;
	private Collection totalSectorTermAssistFund;
	private Collection totalSectorFund;
	private String sectorCommAmount;
	private String sectorDisbAmount;
	private String sectorUnDisbAmount;
	private String sectorPlannedDisbAmount;
	private String sectorExpAmount;
	
	public String getSectorName() 
	{
		return sectorName;
	}

	public String getSectorCommAmount() 
	{
		return sectorCommAmount;
	}

	public String getSectorDisbAmount() 
	{
		return sectorDisbAmount;
	}

	public String getSectorUnDisbAmount() 
	{
		return sectorUnDisbAmount;
	}

	public String getSectorPlannedDisbAmount() 
	{
		return sectorPlannedDisbAmount;
	}

	public String getSectorExpAmount() 
	{
		return sectorExpAmount;
	}

	public Collection getTotalSectorFund() 
	{
		return totalSectorFund;
	}

	public Collection getTotalSectorTermAssistFund() 
	{
		return totalSectorTermAssistFund;
	}

	public Collection getDonors() 
	{
		return donors;
	}

	public void setSectorName(String string) 
	{
		sectorName = string;
	}
	public void setDonors(Collection c) 
	{
		donors = c;
	}

	public void setTotalSectorFund(Collection c) 
	{
		totalSectorFund = c;
	}

	public void setTotalSectorTermAssistFund(Collection c) 
	{
		totalSectorTermAssistFund = c;
	}

	public void setSectorCommAmount(String s) 
	{
		sectorCommAmount = s;
	}

	public void setSectorDisbAmount(String s) 
	{
		sectorDisbAmount = s;
	}

	public void setSectorUnDisbAmount(String s) 
	{
		sectorUnDisbAmount = s;
	}

	public void setSectorPlannedDisbAmount(String s) 
	{
		sectorPlannedDisbAmount = s;
	}

	public void setSectorExpAmount(String s) 
	{
		sectorExpAmount = s;
	}
}	
