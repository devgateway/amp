/**
 * AmpNewFilter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.List;

/**
 * Filtering bean. Holds info about filtering parameters and creates the filtering query
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 5, 2006
 *
 */
public class AmpNewFilter implements Filter {
	
	
	private Long ampStatusId=null;
	private Long ampOrgId=null;
	private Long ampSectorId=null;
	private String region=null;
	private Long ampModalityId=null;
	private String ampCurrencyCode=null;
	private int startYear = 0;
	private int startMonth = 0;
	private int startDay = 0;
	private List ampTeams=null;
	
	private int fromYear = 0;
	private int toYear= 0;
	

	private int closeYear = 0;
	private int closeMonth = 0;
	private int closeDay = 0;
	
	private String generatedFilterQuery="SELECT amp_activity_id FROM amp_activity WHERE 1";
	private int initialQueryLength=generatedFilterQuery.length();
	
	private void queryAppend(String filter) {
		//generatedFilterQuery+= (initialQueryLength==generatedFilterQuery.length()?"":" AND ") + " amp_activity_id IN ("+filter+")";
		generatedFilterQuery+= " AND amp_activity_id IN ("+filter+")";
	}
	
	public AmpNewFilter() {
		super();
		
	}
	
	public void generateFilterQuery() {
		String TEAM_FILTER="SELECT amp_activity_id FROM v_status WHERE amp_team_id IN ("+ARUtil.toSQLEnum(ampTeams)+")";
		String STATUS_FILTER="SELECT amp_activity_id FROM v_status WHERE amp_status_id="+ampStatusId;
		String ORG_FILTER = "SELECT amp_activity_id FROM v_donors WHERE amp_donor_org_id="+ampOrgId;
		String SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors WHERE amp_sector_id="+ampSectorId;
		String REGION_FILTER="SELECT amp_activity_id FROM v_regions WHERE name='"+region+"'";
		String FINANCING_INSTR_FILTER="SELECT amp_activity_id FROM v_financing_instrument WHERE modality_code='"+ampModalityId+"'";
		//currency is not a filter but a currency transformation
		//String START_YEAR_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_start_date,_latin1'%Y')>='"+startYear+"'";
		//String START_MONTH_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_start_date,_latin1'%m')>='"+startMonth+"'";
		//String START_DAY_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_start_date,_latin1'%d')>='"+startDay+"'";
	
		//String CLOSE_YEAR_FILTER="SELECT amp_activity_id FROM v_actual_completion_date WHERE date_format(actual_completion_date,_latin1'%Y')<='"+closeYear+"'";
		//String CLOSE_MONTH_FILTER="SELECT amp_activity_id FROM v_actual_completion_date WHERE date_format(actual_completion_date,_latin1'%m')<='"+closeMonth+"'";
		//String CLOSE_DAY_FILTER="SELECT amp_activity_id FROM v_actual_completion_date WHERE date_format(actual_completion_date,_latin1'%d')<='"+closeDay+"'";
	
		
		String FROM_YEAR_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_start_date,_latin1'%Y')>='"+fromYear+"'";
		String TO_YEAR_FILTER="SELECT amp_activity_id FROM v_actual_start_date WHERE date_format(actual_completion_date,_latin1'%Y')<='"+toYear+"'";
		
		if(ampTeams!=null) queryAppend(TEAM_FILTER);
		if(ampStatusId!=null && ampStatusId.intValue()!=0) queryAppend(STATUS_FILTER);
		if(ampOrgId!=null && ampOrgId.intValue()!=0) queryAppend(ORG_FILTER);
		if(ampSectorId!=null && ampSectorId.intValue()!=0) queryAppend(SECTOR_FILTER);
		if(region!=null && !region.equals("All")) queryAppend(REGION_FILTER);
		if(ampModalityId!=null && ampModalityId.intValue()!=0) queryAppend(FINANCING_INSTR_FILTER);
		
		if(fromYear!=0) queryAppend(FROM_YEAR_FILTER);
		if(toYear!=0) queryAppend(TO_YEAR_FILTER);
		
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
	 * @return Returns the ampSectorId.
	 */
	public Long getAmpSectorId() {
		return ampSectorId;
	}


	/**
	 * @param ampSectorId The ampSectorId to set.
	 */
	public void setAmpSectorId(Long ampSectorId) {
		this.ampSectorId = ampSectorId;
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
	 * @return Returns the closeDay.
	 */
	public int getCloseDay() {
		return closeDay;
	}


	/**
	 * @param closeDay The closeDay to set.
	 */
	public void setCloseDay(int closeDay) {
		this.closeDay = closeDay;
	}


	/**
	 * @return Returns the closeMonth.
	 */
	public int getCloseMonth() {
		return closeMonth;
	}


	/**
	 * @param closeMonth The closeMonth to set.
	 */
	public void setCloseMonth(int closeMonth) {
		this.closeMonth = closeMonth;
	}


	/**
	 * @return Returns the closeYear.
	 */
	public int getCloseYear() {
		return closeYear;
	}


	/**
	 * @param closeYear The closeYear to set.
	 */
	public void setCloseYear(int closeYear) {
		this.closeYear = closeYear;
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
	 * @return Returns the startDay.
	 */
	public int getStartDay() {
		return startDay;
	}


	/**
	 * @param startDay The startDay to set.
	 */
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}


	/**
	 * @return Returns the startMonth.
	 */
	public int getStartMonth() {
		return startMonth;
	}


	/**
	 * @param startMonth The startMonth to set.
	 */
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}


	/**
	 * @return Returns the startYear.
	 */
	public int getStartYear() {
		return startYear;
	}


	/**
	 * @param startYear The startYear to set.
	 */
	public void setStartYear(int startYear) {
		this.startYear = startYear;
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
	public List getAmpTeams() {
		return ampTeams;
	}

	/**
	 * @param ampTeams The ampTeams to set.
	 */
	public void setAmpTeams(List ampTeams) {
		this.ampTeams = ampTeams;
	}

	/**
	 * @return Returns the fromYear.
	 */
	public int getFromYear() {
		return fromYear;
	}

	/**
	 * @param fromYear The fromYear to set.
	 */
	public void setFromYear(int fromYear) {
		this.fromYear = fromYear;
	}

	/**
	 * @return Returns the toYear.
	 */
	public int getToYear() {
		return toYear;
	}

	/**
	 * @param toYear The toYear to set.
	 */
	public void setToYear(int toYear) {
		this.toYear = toYear;
	}

}
