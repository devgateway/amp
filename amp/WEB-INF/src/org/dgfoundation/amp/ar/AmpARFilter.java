/**
 * AmpARFilter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.Set;

import org.dgfoundation.amp.Util;

/**
 * Filtering bean. Holds info about filtering parameters and creates the filtering query
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 5, 2006
 *
 */
public class AmpARFilter implements Filter {	
	private Long id;
	private Set statuses=null;
	private Set donors=null;
	private Set sectors=null;
	private Set regions=null;
	private Long ampModalityId=null;
	private String ampCurrencyCode=null;
	private Set ampTeams=null;
	private Integer calendarType=null;
	private String perspectiveCode;
	private boolean widget=false;
	private boolean publicView=false;
	private Boolean budget=null;

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
		String BUDGET_FILTER="SELECT amp_activity_id FROM amp_activity WHERE budget="+(budget!=null?budget.toString():"null")+(budget!=null && budget.booleanValue()==false?" OR budget is null":"");
		String TEAM_FILTER="SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IN ("+Util.toCSString(ampTeams)+") OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("+Util.toCSString(ampTeams)+") )";
		String STATUS_FILTER="SELECT amp_activity_id FROM v_status WHERE amp_status_id IN ("+Util.toCSString(statuses)+")";
		String ORG_FILTER = "SELECT amp_activity_id FROM v_donors WHERE amp_donor_org_id IN ("+Util.toCSString(donors)+")";
		String SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors WHERE amp_sector_id IN ("+Util.toCSString(sectors)+")";
		String REGION_FILTER="SELECT amp_activity_id FROM v_regions WHERE name IN ("+Util.toCSString(regions)+")";
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
		
		if(budget!=null) queryAppend(BUDGET_FILTER);
		if(ampTeams!=null && ampTeams.size()>0) queryAppend(TEAM_FILTER);
		if(statuses!=null && statuses.size()>0) queryAppend(STATUS_FILTER);
		if(donors!=null && donors.size()>0) queryAppend(ORG_FILTER);
		if(sectors!=null && sectors.size()!=0) queryAppend(SECTOR_FILTER);
		if(regions!=null && regions.size()>0) queryAppend(REGION_FILTER);
		if(ampModalityId!=null && ampModalityId.intValue()!=0) queryAppend(FINANCING_INSTR_FILTER);
		
		//if(fromYear!=null) queryAppend(FROM_YEAR_FILTER);
		//if(toYear!=null) queryAppend(TO_YEAR_FILTER);
		
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

	/**
	 * @return Returns the calendarType.
	 */
	public Integer getCalendarType() {
		return calendarType;
	}

	/**
	 * @param calendarType The calendarType to set.
	 */
	public void setCalendarType(Integer calendarType) {
		this.calendarType = calendarType;
	}

	/**
	 * @return Returns the donors.
	 */
	public Set getDonors() {
		return donors;
	}

	/**
	 * @param donors The donors to set.
	 */
	public void setDonors(Set donors) {
		this.donors = donors;
	}

	/**
	 * @return Returns the regions.
	 */
	public Set getRegions() {
		return regions;
	}

	/**
	 * @param regions The regions to set.
	 */
	public void setRegions(Set regions) {
		this.regions = regions;
	}

	/**
	 * @return Returns the statuses.
	 */
	public Set getStatuses() {
		return statuses;
	}

	/**
	 * @param statuses The statuses to set.
	 */
	public void setStatuses(Set statuses) {
		this.statuses = statuses;
	}

	/**
	 * @return Returns the perspectiveCode.
	 */
	public String getPerspectiveCode() {
		return perspectiveCode;
	}

	/**
	 * @param perspectiveCode The perspectiveCode to set.
	 */
	public void setPerspectiveCode(String perspective) {
		this.perspectiveCode = perspective;
	}

	public boolean isPublicView() {
		return publicView;
	}

	public void setPublicView(boolean publicView) {
		this.publicView = publicView;
	}

	public boolean isWidget() {
		return widget;
	}

	public void setWidget(boolean widget) {
		this.widget = widget;
	}

	public Boolean getBudget() {
		return budget;
	}

	public void setBudget(Boolean budget) {
		this.budget = budget;
	}

	/**
	 * @return Returns the approvalStatus.
	 */
	

}
