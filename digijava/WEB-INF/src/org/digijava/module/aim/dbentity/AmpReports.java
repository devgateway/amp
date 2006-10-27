/*
 * AmpTeam.java
 * Created: 03-Sep-2004
 */

package org.digijava.module.aim.dbentity;


import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.AmpARFilter;

public class AmpReports implements Comparable {

	private Long ampReportId;

	private AmpARFilter defaultFilter;
	
	private String name;

	private String description;
	private String reportDescription;

	private String options;

	private Boolean hideActivities;
	
	private Long type;

	//private AmpReportsOptions ampReportsOptions;
	
	private Set members;

	private Set columns;
	private List orderedColumns;
	private Set hierarchies;
	private Set measures;
	
	public static final String NOTE="NOTE: All shown funding items are in USD currency. All calendaristic date cells are shown using DD/MM/YYYY format. All amounts are in thousands.";
	
	public String getNote(HttpSession session)
	{ return "NOTE: All shown funding items are in USD currency. All calendaristic date cells are shown using DD/MM/YYYY format. All amounts are in thousands."+session.getAttribute("reportCurrencyCode");}
	
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

	public String getDescription() {
		return description;
	}

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

	public void setDescription(String description) {
		this.description = description;
	}

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


}
