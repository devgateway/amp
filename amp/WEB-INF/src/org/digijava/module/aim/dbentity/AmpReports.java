/*
 * AmpTeam.java
 * Created: 03-Sep-2004
 */

package org.digijava.module.aim.dbentity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.common.util.DateTimeUtil;

public class AmpReports implements Comparable, LoggerIdentifiable {

	private Long ampReportId;
	private Long id; //for logging

	private AmpARFilter defaultFilter;
	
	private String name;

	//private String description;
	private String reportDescription;

	private String options;

	private Boolean hideActivities;
	
	private Boolean drilldownTab;
	private Boolean publicReport;
	
	private Long type;

	//private AmpReportsOptions ampReportsOptions;
	private String description;
	
	private Set members;

	private Set columns;
	private List orderedColumns;
	private Set hierarchies;
	private Set measures;
	private AmpTeamMember ownerId;	// the member that created the report
	private Date updatedDate;		// last date when the report was modified
	
	//to be set in order to get information for translation purposes in pdf and excel reports 
	private String siteId;
	private String locale;
	
	//public static final String NOTE="NOTE: All shown funding items are in USD currency. All calendaristic date cells are shown using DD/MM/YYYY format. All amounts are in thousands.";
//	private static SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
	
	public String getFormatedUpdatedDate() {
		String result = null;
		if (this.updatedDate != null) {
			result = DateTimeUtil.formatDate(this.updatedDate);
		}
		return result;
	}

	public AmpTeamMember getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(AmpTeamMember owner) {
		this.ownerId = owner;
	}

	public static String getNote(HttpSession session)
	{ return "Amounts are in thousands (000)";}
	
	public Set getMeasures() {
		return measures;
	}
	public void setMeasures(Set measures) {
		this.measures = measures;
	}
	public Set getColumns() {
		return columns;
	}
	public void setColumns(Set columns) {
		this.columns = columns;
	}
	public Long getAmpReportId() {
		return ampReportId;
	}

	public String getName() {
		return name;
	}

	/*
	public String getDescription() {
		return description;
	}
	*/

	public String getReportDescription() {
		return reportDescription;
	}

	public String getOptions() {
		return options;
	}

	/*public AmpReportsOptions getAmpReportsOptions() {
		return ampReportsOptions;
	}*/
	
	public Set getMembers() {
		return members;
	}

	public void setAmpReportId(Long ampReportId) {
		this.ampReportId = ampReportId;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	public void setDescription(String description) {
		this.description = description;
	}
	*/

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public void setOptions(String options) {
		this.options = options;
	}
	
	/*public void setAmpReportsOptions(AmpReportsOptions ampReportsOptions) {
		this.ampReportsOptions = ampReportsOptions;
	}*/
	
	public void setMembers(Set members) {
		this.members = members;
	}
	
	public int compareTo(Object o) {
		  if (!(o instanceof AmpReports)) throw new ClassCastException();
		
		  AmpReports rep = (AmpReports) o;
		  return (this.name.trim().toLowerCase().
								compareTo(rep.name.trim().toLowerCase()));

	}
	
	public Set getHierarchies() {
		return hierarchies;
	}
	public void setHierarchies(Set hierarchies) {
		this.hierarchies = hierarchies;
	}

	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	/**
	 * @return Returns the orderedColumns.
	 */
	public List getOrderedColumns() {
		return orderedColumns;
	}
	/**
	 * @param orderedColumns The orderedColumns to set.
	 */
	public void setOrderedColumns(List orderedColumns) {
		this.orderedColumns = orderedColumns;
	}

	/**
	 * @return Returns the defaultFilter.
	 */
	public AmpARFilter getDefaultFilter() {
		return defaultFilter;
	}

	/**
	 * @param defaultFilter The defaultFilter to set.
	 */
	public void setDefaultFilter(AmpARFilter defaultFilter) {
		this.defaultFilter = defaultFilter;
	}

	/**
	 * @return Returns the hideActivities.
	 */
	public Boolean isHideActivities() {
		return hideActivities;
	}

	/**
	 * @param hideActivities The hideActivities to set.
	 */
	public void setHideActivities(Boolean hideActivities) {
		this.hideActivities = hideActivities;
	}

	/**
	 * @return Returns the hideActivities.
	 */
	public Boolean getHideActivities() {
		return hideActivities;
	}

	public Boolean getDrilldownTab() {
		return drilldownTab;
	}

	public void setDrilldownTab(Boolean drilldownTab) {
		this.drilldownTab = drilldownTab;
	}

	public Boolean getPublicReport() {
		return publicReport;
	}

	public void setPublicReport(Boolean publicReport) {
		this.publicReport = publicReport;
	}

    public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Object getObjectType() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}

	public Object getIdentifier() {
		// TODO Auto-generated method stub
		return this.getAmpReportId().toString();
	}

	public Long getId() {
		return this.getAmpReportId();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectName() {
		// TODO Auto-generated method stub
		return this.getName();
	}
}
