/**
 * AmpARFilter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.Set;

/**
 * Filtering bean. Holds info about filtering parameters and creates the filtering query
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 5, 2006
 *
 */
public class AmpARFilter implements Filter {
	
	private Long id;
	
	private Long ampStatusId=null;
	private Long ampOrgId=null;
	private Set sectors=null;
	private String region=null;
	private Long ampModalityId=null;
	private String ampCurrencyCode=null;
	private Set ampTeams=null;
	
	private Integer fromYear;
	private Integer toYear;
	
	
	private String generatedFilterQuery="SELECT amp_activity_id FROM amp_activity WHERE 1";
	private int initialQueryLength=generatedFilterQuery.length();
	
	private void queryAppend(String filter) {
		//generatedFilterQuery+= (initialQueryLength==generatedFilterQuery.length()?"":" AND ") + " amp_activity_id IN ("+filter+")";
		generatedFilterQuery+= " AND amp_activity_id IN ("+filter+")";
	}
	
	public AmpARFilter() {
		super();
		
	}
	
	public void generateFilterQuery() {
		String TEAM_FILTER="SELECT amp_activity_id FROM v_status WHERE amp_team_id IN ("+ARUtil.toSQLEnum(ampTeams)+")";
		String STATUS_FILTER="SELECT amp_activity_id FROM v_status WHERE amp_status_id="+ampStatusId;
		String ORG_FILTER = "SELECT amp_activity_id FROM v_donors WHERE amp_donor_org_id="+ampOrgId;
		String SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors WHERE amp_sector_id IN ("+ARUtil.toSQLEnum(sectors)+")";
		String REGION_FILTER="SELECT amp_activity_id FROM v_regions WHERE name='"+region+"'";
		String FINANCING_INSTR_FILTER="SELECT amp_activity_id FROM v_financing_instrument WHERE amp_modality_id='"+ampModalityId+"'";
		//currency is not a filter but a currency transformation
		//String START_YEAR_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_start_date,_latin1'%Y')>='"+startYear+"'";
		//String START_MONTH_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_start_date,_latin1'%m')>='"+startMonth+"'";
		//String START_DAY_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_start_date,_latin1'%d')>='"+startDay+"'";
	
		//String CLOSE_YEAR_FILTER="SELECT amp_activity_id FROM v_actual_completion_date WHERE date_format(actual_completion_date,_latin1'%Y')<='"+closeYear+"'";
		//String CLOSE_MONTH_FILTER="SELECT amp_activity_id FROM v_actual_completion_date WHERE date_format(actual_completion_date,_latin1'%m')<='"+closeMonth+"'";
		//String CLOSE_DAY_FILTER="SELECT amp_activity_id FROM v_actual_completion_date WHERE date_format(actual_completion_date,_latin1'%d')<='"+closeDay+"'";
	
		
		String FROM_YEAR_FILTER="SELECT f.amp_activity_id FROM amp_funding f, amp_funding_detail fd WHERE f.amp_funding_id=fd.AMP_FUNDING_ID and date_format(fd.transaction_date,_latin1'%Y')>='"+fromYear+"'";
		String TO_YEAR_FILTER="SELECT f.amp_activity_id FROM amp_funding f, amp_funding_detail fd WHERE f.amp_funding_id=fd.AMP_FUNDING_ID and date_format(fd.transaction_date,_latin1'%Y')<='"+toYear+"'";
		
		if(ampTeams!=null) queryAppend(TEAM_FILTER);
		if(ampStatusId!=null && ampStatusId.intValue()!=0) queryAppend(STATUS_FILTER);
		if(ampOrgId!=null && ampOrgId.intValue()!=0) queryAppend(ORG_FILTER);
		if(sectors!=null && sectors.size()!=0) queryAppend(SECTOR_FILTER);
		if(region!=null && !region.equals("All")) queryAppend(REGION_FILTER);
		if(ampModalityId!=null && ampModalityId.intValue()!=0) queryAppend(FINANCING_INSTR_FILTER);
		
		if(fromYear!=null) queryAppend(FROM_YEAR_FILTER);
		if(toYear!=null) queryAppend(TO_YEAR_FILTER);
		
		//if(startYear!=0) queryAppend(START_YEAR_FILTER);
		//if(startMonth!=0) queryAppend(START_MONTH_FILTER);
		//if(startDay!=0) queryAppend(START_DAY_FILTER);
		
		//if(closeYear!=0) queryAppend(CLOSE_YEAR_FILTER);
		//if(closeMonth!=0) queryAppend(CLOSE_MONTH_FILTER);
		//if(closeDay!=0) queryAppend(CLOSE_DAY_FILTER);	
	}
	
	
	/**
	 * @return Returns the ampCurrencyCode.
	 */
	public String getAmpCurrencyCode() {
		return ampCurrencyCode;
	}


	/**
	 * @param ampCurrencyCode The ampCurrencyCode to set.
	 */
	public void setAmpCurrencyCode(String ampCurrencyCode) {
		this.ampCurrencyCode = ampCurrencyCode;
	}


	/**
	 * @return Returns the ampModalityId.
	 */
	public Long getAmpModalityId() {
		return ampModalityId;
	}


	/**
	 * @param ampModalityId The ampModalityId to set.
	 */
	public void setAmpModalityId(Long ampModalityId) {
		this.ampModalityId = ampModalityId;
	}


	/**
	 * @return Returns the ampOrgId.
	 */
	public Long getAmpOrgId() {
		return ampOrgId;
	}


	/**
	 * @param ampOrgId The ampOrgId to set.
	 */
	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}

 

	/**
	 * @return Returns the sectors.
	 */
	public Set getSectors() {
		return sectors;
	}

	/**
	 * @param sectors The sectors to set.
	 */
	public void setSectors(Set sectors) {
		this.sectors = sectors;
	}

	/**
	 * @return Returns the ampStatusId.
	 */
	public Long getAmpStatusId() {
		return ampStatusId;
	}


	/**
	 * @param ampStatusId The ampStatusId to set.
	 */
	public void setAmpStatusId(Long ampStatusId) {
		this.ampStatusId = ampStatusId;
	}


	/**
	 * @return Returns the region.
	 */
	public String getRegion() {
		return region;
	}


	/**
	 * @param region The region to set.
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return Returns the generatedFilterQuery.
	 */
	public String getGeneratedFilterQuery() {
		return generatedFilterQuery;
	}

	/**
	 * @return Returns the initialQueryLength.
	 */
	public int getInitialQueryLength() {
		return initialQueryLength;
	}

	/**
	 * @return Returns the ampTeams.
	 */
	public Set getAmpTeams() {
		return ampTeams;
	}

	/**
	 * @param ampTeams The ampTeams to set.
	 */
	public void setAmpTeams(Set ampTeams) {
		this.ampTeams = ampTeams;
	}



	/**
	 * @return Returns the fromYear.
	 */
	public Integer getFromYear() {
		return fromYear;
	}

	/**
	 * @param fromYear The fromYear to set.
	 */
	public void setFromYear(Integer fromYear) {
		this.fromYear = fromYear;
	}

	/**
	 * @return Returns the toYear.
	 */
	public Integer getToYear() {
		return toYear;
	}

	/**
	 * @param toYear The toYear to set.
	 */
	public void setToYear(Integer toYear) {
		this.toYear = toYear;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
