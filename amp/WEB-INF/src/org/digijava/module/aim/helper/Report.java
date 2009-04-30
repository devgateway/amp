package org.digijava.module.aim.helper ;
import java.util.Collection;
import java.util.Iterator;

public class Report
{
	
	public int getReportRowSpan(boolean typeAssist) {
		if(!typeAssist) return 0;
		int fundId;
		//for all reports of type different than donor, we do not need rowspans because we do not show grant/loan!
		if (type!=1) return 0;
		if(records!=null) fundId=records.size()-1; else return 0;
		Iterator i=records.iterator();
		int c=0;
		while (i.hasNext()) {
			AdvancedReport ar = (AdvancedReport) i.next();
			if(c++ == fundId) {
				//last AdvancedReport holds funding info and assistanceCopy
				if (ar.getAssistanceCopy()!=null) return ar.getAssistanceCopy().size()+1; else return 0;
			}
			
		}
		return 0;
	}
	
	private Long ampActivityId;
	private String donor ;
	private String title ;
	private String status ;
	private String level ;

	private Collection donors ;
	private Collection regions ;
	private Collection sectors;
	private Collection projects;
	private Collection records;

	private String startDate ;
	private Collection commitmentDate ;
	private String closeDate ;
	private double plannedCommitment ;
	private double actualCommitment ;
	private double plannedDisbursement ;
	private double actualDisbursement ;
	private Double actualUnDisbursement ;
	
	private String plCommitment;
	private String acCommitment;
	private String plDisbursement;
	private String acDisbursement;
	private String acUnDisbursement;

	
	private double plannedCommitmentForcast1 ;
	private double plannedCommitmentForcast2 ;
	private double plannedCommitmentForcast3 ;

	private String plannedDisbursementForcast1 ;
	private String plannedDisbursementForcast2 ;
	private String plannedDisbursementForcast3 ;
	
	private StringBuffer sector ;
	private String sectorname;
	private String subSector ;
	private Collection modality;	
	private String country;
	private String region;
	private Collection assistance;
	
	private int type;
	
	private Collection fiscalYrs;
	private Collection ampFund;
	private Collection totDisbFund;
	private String totComm;
	private String totDisb;
	private String totUnDisb;

	private String objective;
		
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

		public Collection getProjects() 
		{
			return projects;
		}

		public void setProjects(Collection c) 
		{
			projects = c;
		}

		public Collection getRecords() 
		{
			return records;
		}

		public void setRecords(Collection c) 
		{
			records = c;
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
	
		public String getDonor() 
		{
			return donor;
		}
	
		public void setDonor(String string) 
		{
			donor = string;
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

	public String getStartDate() 
	{
		return startDate;
	}

	public Collection getCommitmentDate() 
	{
		return commitmentDate;
	}

	public void setStartDate(String string) 
	{
		startDate = string;
	}

	public void setCommitmentDate(Collection c) 
	{
		commitmentDate = c;
	}

	public String getCloseDate() 
	{
		return closeDate;
	}

	public void setCloseDate(String string) 
	{
		closeDate = string;
	}

	
	public double getPlannedCommitment()
	{
		return plannedCommitment ;
	}
	
	public void setPlannedCommitment(double d)
	{
		plannedCommitment = d;
	}
	
	public double getActualCommitment()
	{
		return actualCommitment ;
	}
	
	public void setActualCommitment(double d)
	{
		actualCommitment = d;
	}

	public double getPlannedDisbursement()
	{
		return plannedDisbursement ;
	}
	
	public void setPlannedDisbursement(double d)
	{
		plannedDisbursement = d;
	}
	
	public double getActualDisbursement()
	{
		return actualDisbursement ;
	}
	
	public void setActualDisbursement(double d)
	{
		actualDisbursement = d;
	}
	
	public double getPlannedCommitmentForcast1()
	{
		return plannedCommitmentForcast1 ;
	}
	
	public void setPlannedCommitmentForcast1(double d)
	{
		plannedCommitmentForcast1 = d;
	}

	public double getPlannedCommitmentForcast2()
	{
		return plannedCommitmentForcast2 ;
	}
	
	public void setPlannedCommitmentForcast2(double d)
	{
		plannedCommitmentForcast2 = d;
	}
	public double getPlannedCommitmentForcast3()
	{
		return plannedCommitmentForcast3 ;
	}
	
	public void setPlannedCommitmentForcast3(double d)
	{
		plannedCommitmentForcast3 = d;
	}

	
	public String getSubSector() 
	{
		return subSector;
	}
	
	public void setSubSector(String string) 
	{
		subSector = string;
	}


	/**
	 * @return
	 */
	public StringBuffer getSector() {
		return sector;
	}

	/**
	 * @param buffer
	 */
	public void setSector(StringBuffer buffer) {
		sector = buffer;
	}

	/**
	 * @return
	 */
	public String getSectorname() {
		return sectorname;
	}

	/**
	 * @param string
	 */
	public void setSectorname(String string) {
		sectorname = string;
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
	public String getCountry() {
		return country;
	}

	/**
	 * @return
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param string
	 */
	public void setCountry(String string) {
		country = string;
	}

	/**
	 * @param string
	 */
	public void setRegion(String string) {
		region = string;
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

	public Collection getTotDisbFund() {
		return totDisbFund;
	}

	/**
	 * @param collection
	 */
	public void setTotDisbFund(Collection collection) {
		totDisbFund = collection;
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
	public Double getActualUnDisbursement() {
		return actualUnDisbursement;
	}

	/**
	 * @param d
	 */
	public void setActualUnDisbursement(Double d) {
		actualUnDisbursement = d;
	}

	/**
	 * @return
	 */
	public String getTotComm() {
		return totComm;
	}

	/**
	 * @return
	 */
	public String getTotDisb() {
		return totDisb;
	}

	/**
	 * @return
	 */
	public String getTotUnDisb() {
		return totUnDisb;
	}

	/**
	 * @param string
	 */
	public void setTotComm(String string) {
		totComm = string;
	}

	/**
	 * @param string
	 */
	public void setTotDisb(String string) {
		totDisb = string;
	}

	/**
	 * @param string
	 */
	public void setTotUnDisb(String string) {
		totUnDisb = string;
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

	/**
	 * @return
	 */
	public String getAcCommitment() {
		return acCommitment;
	}

	/**
	 * @return
	 */
	public String getAcDisbursement() {
		return acDisbursement;
	}

	/**
	 * @return
	 */
	public String getAcUnDisbursement() {
		return acUnDisbursement;
	}

	/**
	 * @return
	 */
	public String getPlCommitment() {
		return plCommitment;
	}

	/**
	 * @return
	 */
	public String getPlDisbursement() {
		return plDisbursement;
	}

	 public String getObjective() {
					 return objective;
		  }

	/**
	 * @param string
	 */
	public void setAcCommitment(String string) {
		acCommitment = string;
	}

	/**
	 * @param string
	 */
	public void setAcDisbursement(String string) {
		acDisbursement = string;
	}

	/**
	 * @param string
	 */
	public void setAcUnDisbursement(String string) {
		acUnDisbursement = string;
	}

	/**
	 * @param string
	 */
	public void setPlCommitment(String string) {
		plCommitment = string;
	}

	/**
	 * @param string
	 */
	public void setPlDisbursement(String string) {
		plDisbursement = string;
	}

	/**
	 * @return
	 */
	public String getPlannedDisbursementForcast1() {
		return plannedDisbursementForcast1;
	}

	/**
	 * @return
	 */
	public String getPlannedDisbursementForcast2() {
		return plannedDisbursementForcast2;
	}

	/**
	 * @return
	 */
	public String getPlannedDisbursementForcast3() {
		return plannedDisbursementForcast3;
	}

	/**
	 * @param string
	 */
	public void setPlannedDisbursementForcast1(String string) {
		plannedDisbursementForcast1 = string;
	}

	/**
	 * @param string
	 */
	public void setPlannedDisbursementForcast2(String string) {
		plannedDisbursementForcast2 = string;
	}

	/**
	 * @param string
	 */
	public void setPlannedDisbursementForcast3(String string) {
		plannedDisbursementForcast3 = string;
	}
	
	 public void setObjective(String objective) {
					 this.objective = objective;
		  }

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
}
