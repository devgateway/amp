package org.digijava.module.aim.helper ;

public class AmpProjectDonor
{
	private Long ampDonorId;
	private String donorName;
	
	public String getDonorName() 
	{
		return donorName;
	}

	public Long getAmpDonorId() 
	{
		return ampDonorId;
	}
		
	public void setDonorName(String name) 
	{
		this.donorName = name ;
	}

	public void setAmpDonorId(Long donor) 
	{
		this.ampDonorId = donor ;
	}
}		