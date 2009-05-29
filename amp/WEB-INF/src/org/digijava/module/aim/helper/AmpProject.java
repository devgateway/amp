package org.digijava.module.aim.helper ;

import java.util.Collection;

/**
 * @deprecated
 * @author mihai
 *
 */
public class AmpProject implements Comparable
{
	private Long ampActivityId;
	private String ampId;
	private String name ;
	private Collection donor ;
	private Collection sector ;
	private String currency;
	private String totalCommited;
	private Collection commitmentList;
	private Long statusId;
	private int activityRisk;
	
	private Integer lineMinRank;
	private Integer planMinRank;
	
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

	/**
	 * @return Returns the commitmentList.
	 */
	public Collection getCommitmentList() {
		return commitmentList;
	}

	/**
	 * @param commitmentList The commitmentList to set.
	 */
	public void setCommitmentList(Collection commitmentList) {
		this.commitmentList = commitmentList;
	}

	/**
	 * @return Returns the statusId.
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId The statusId to set.
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof AmpProject) {
			AmpProject p = (AmpProject) obj;
			return p.getAmpActivityId().equals(ampActivityId);
		} else throw new ClassCastException();
	}

	/**
	 * @return Returns the activityRisk.
	 */
	public int getActivityRisk() {
		return activityRisk;
	}

	/**
	 * @param activityRisk The activityRisk to set.
	 */
	public void setActivityRisk(int activityRisk) {
		this.activityRisk = activityRisk;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj) {
		if (obj instanceof AmpProject) {
			AmpProject p = (AmpProject) obj;
			String t1 = "";
			String t2 = "";
			if (name != null) {
				t1 = name.toLowerCase();
			}
			if (p.getName() != null) {
				t2 = p.getName().toLowerCase();
			}
			
			return t1.compareTo(t2);
		} else throw new ClassCastException();		
	}

	public Integer getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Integer lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Integer getPlanMinRank() {
		return planMinRank;
	}

	public void setPlanMinRank(Integer planMinRank) {
		this.planMinRank = planMinRank;
	}
}
