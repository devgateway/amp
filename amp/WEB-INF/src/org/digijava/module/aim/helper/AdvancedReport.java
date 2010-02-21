package org.digijava.module.aim.helper ;
import java.util.ArrayList;
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
	private String totalCommitment;
	private String totalDisbursement;
	private String actualCommitment;

	private Collection donors ;
	private Collection regions ;
	private Collection components;
	private Collection sectors;
	private Collection projects;
	
	private String startDate ;
	private String closeDate ;
	
	private Collection modality;	
	private Collection assistance;
	private Collection assistanceCopy;
	private Collection contacts;
	
	private Collection fiscalYrs;
	private Collection ampFund;
	private int yearCount;
	private String ampId;
	private String year;
	private String objective;
	private String objectivePDFXLS;
	private String description;
	private String descriptionPDFXLS;

	// added by Rahul for AMP-452
	// adding a comment line to test CVS STAGING tagControl on commit
	
	private Collection projId;
	
	public Collection getProjId() {
		return projId;
	}
	
	public void setProjId(Collection projId) {
		if(projId!=null) this.projId = projId; else this.projId=new ArrayList();
		
	}

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

	public Collection getContacts() 
	{
		return contacts;
	}

	public void setContacts(Collection c) 
	{
		contacts = c;
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

	public String getAmpId() 
	{
		return ampId;
	}
	
	public void setAmpId(String string) 
	{
		ampId = string;
	}

	public String getYear() 
	{
		return year;
	}
	
	public void setYear(String string) 
	{
		year = string;
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

	public String getTotalCommitment() 
	{
		return totalCommitment;
	}

	
	public void setTotalCommitment(String string) 
	{
		totalCommitment = string;
	}

	public String getTotalDisbursement() 
	{
		return totalDisbursement;
	}

	
	public void setTotalDisbursement(String string) 
	{
		totalDisbursement = string;
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

	public String getObjective() 
	{
		return objective;
	}

	
	public void setObjective(String string) 
	{
		objective = string;
	}

	public String getObjectivePDFXLS() 
	{
		return objectivePDFXLS;
	}
	public void setObjectivePDFXLS(String s) 
	{
		objectivePDFXLS = s;
	}
	
	public String getDescription() 
	{
		return description;
	}

	
	public void setDescription(String string) 
	{
		description = string;
	}

	public String getDescriptionPDFXLS() 
	{
		return descriptionPDFXLS;
	}
	public void setDescriptionPDFXLS(String s) 
	{
		descriptionPDFXLS = s;
	}

	/**
	 * @return Returns the components.
	 */
	public Collection getComponents() {
		return components;
	}

	/**
	 * @param components The components to set.
	 */
	public void setComponents(Collection components) {
		this.components = components;
	}

	/**
	 * @return Returns the assistanceCopy.
	 */
	public Collection getAssistanceCopy() {
		return assistanceCopy;
	}

	/**
	 * @param assistanceCopy The assistanceCopy to set.
	 */
	public void setAssistanceCopy(Collection assistanceCopy) {
		this.assistanceCopy = assistanceCopy;
	}
	
}
