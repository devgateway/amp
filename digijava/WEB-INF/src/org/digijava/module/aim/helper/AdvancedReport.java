package org.digijava.module.aim.helper ;
import java.util.Collection;

public class AdvancedReport
{
	private Long ampActivityId;
	private Long ampDonorId;
	private Long ampStatusId;
	
	private String title ;
	private String status ;
	private String level ;

	private String actualStartDate;
	private String actualCompletionDate;
	private String actualCommitment;

	private Collection donors ;
	private Collection regions ;
	private Collection sectors;
	private Collection projects;
	
	private String startDate ;
	private String closeDate ;
	
	private Collection modality;	
	private Collection assistance;
	
	private Collection fiscalYrs;
	private Collection ampFund;
	private int yearCount;

	public Collection getDonors() 
	{
		return donors;
	}

	public void setDonors(Collection c) 
	{
		donors = c;
	}

	public Long getAmpActivityId() 
	{
		return ampActivityId;
	}

	public void setAmpActivityId(Long l) 
	{
		ampActivityId = l;
	}

	public Long getAmpDonorId() 
	{
		return ampDonorId;
	}

	public void setAmpDonorId(Long l) 
	{
		ampDonorId = l;
	}

	public Long getAmpStatusId() 
	{
		return ampStatusId;
	}

	public void setAmpStatusId(Long l) 
	{
		ampStatusId = l;
	}

	public Collection getProjects() 
	{
		return projects;
	}

	public void setProjects(Collection c) 
	{
		projects = c;
	}

	public Collection getRegions() 
	{
		return regions;
	}

	public void setRegions(Collection c) 
	{
		regions = c;
	}

	public Collection getSectors() 
	{
		return sectors;
	}

	public void setSectors(Collection c) 
	{
		sectors = c;
	}
	
	
	public String getLevel() 
	{
		return level;
	}
	
	public void setLevel(String string) 
	{
		level = string;
	}

	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String string) 
	{
		title = string;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setStatus(String string) 
	{
		status = string;
	}

	public String getActualStartDate() 
	{
		return actualStartDate;
	}

	
	public void setActualStartDate(String string) 
	{
		actualStartDate = string;
	}

	
	public String getActualCompletionDate() 
	{
		return actualCompletionDate;
	}

	public void setActualCompletionDate(String string) 
	{
		actualCompletionDate = string;
	}

	public String getActualCommitment() 
	{
		return actualCommitment;
	}

	
	public void setActualCommitment(String string) 
	{
		actualCommitment = string;
	}

	
	

	/**
	 * @return
	 */
	public Collection getModality() {
		return modality;
	}

	/**
	 * @param string
	 */
	public void setModality(Collection c) {
		modality = c;
	}

	
	/**
	 * @return
	 */
	public Collection getFiscalYrs() {
		return fiscalYrs;
	}

	/**
	 * @param collection
	 */
	public void setFiscalYrs(Collection collection) {
		fiscalYrs = collection;
	}

	/**
	 * @return
	 */
	public Collection getAmpFund() {
		return ampFund;
	}


	public void setAmpFund(Collection collection) {
		ampFund = collection;
	}

	/**
	 * @return
	 */
	public Collection getAssistance() {
		return assistance;
	}

	/**
	 * @param string
	 */
	public void setAssistance(Collection c) {
		assistance = c;
	}

	/**
	 * @return
	 */
	public int getYearCount() {
		return yearCount;
	}

	/**
	 * @param i
	 */
	public void setYearCount(int i) {
		yearCount = i;
	}


}