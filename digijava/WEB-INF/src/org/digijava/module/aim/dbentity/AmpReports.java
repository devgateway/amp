/*
 * AmpTeam.java
 * Created: 03-Sep-2004
 */

package org.digijava.module.aim.dbentity;

import java.util.Set;

public class AmpReports implements Comparable {

	private Long ampReportId;

	private String name;

	private String description;

	private Set members;

	private Set columns;
	private Set hierarchies;
	private Set measures;
	
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
}