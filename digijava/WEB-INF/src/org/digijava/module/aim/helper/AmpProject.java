package org.digijava.module.aim.helper ;

import java.util.Collection;

public class AmpProject
{
	private Long ampActivityId;
	private String ampId;
	private String name ;
	private Collection donor ;
	private Collection sector ;
	private String currency;
	private String totalCommited;
	
	private String objective;
	private String description;
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the objective.
	 */
	public String getObjective() {
		return objective;
	}
	/**
	 * @param objective The objective to set.
	 */
	public void setObjective(String objective) {
		this.objective = objective;
	}
	// added by Akash
	private String approvalStatus;
	
	public AmpProject() {
		totalCommited = "0.00";
		approvalStatus = "";
	}

	public String getName() 
	{
		return name;
	}

	public Collection getDonor() 
	{
		return donor;
	}

	public Collection getSector() 
	{
		return sector;
	}

	public String getCurrency() 
	{
		return currency;
	}

	public String getTotalCommited() 
	{
		return totalCommited;
	}

	public Long getAmpActivityId() 
	{
		return ampActivityId;
	}

	public String getAmpId() 
	{
		return ampId;
	}

	public void setName(String name) 
	{
		this.name = name ;
	}

	public void setDonor(Collection donor) 
	{
		this.donor = donor ;
	}

	public void setSector(Collection sector) 
	{
		this.sector = sector ;
	}

	public void setAmpActivityId(Long ampActivityId)
	{
		this.ampActivityId = ampActivityId;
	}

	public void setAmpId(String ampId)
	{
		this.ampId = ampId;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public void setTotalCommited(String totalCommited)
	{
		this.totalCommited = totalCommited;
	}

	/**
	 * @return Returns the approvalStatus.
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approvalStatus The approvalStatus to set.
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
}