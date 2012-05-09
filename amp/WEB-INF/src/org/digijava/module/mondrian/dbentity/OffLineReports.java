package org.digijava.module.mondrian.dbentity;

import java.sql.Date;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class OffLineReports {
	private long id;
	private String name;
	private String query;
	private AmpTeamMember ownerId;
	private Long teamid;
	private Boolean publicreport;
	private String measures;
	private String columns;
	private java.sql.Timestamp creationdate;
	private java.lang.Integer type;
	public java.lang.Integer getType() {
		return type;
	}

	public void setType(java.lang.Integer type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Long getTeamid() {
		return teamid;
	}

	public void setTeamid(Long teamid) {
		this.teamid = teamid;
	}

	public Boolean getPublicreport() {
		return publicreport;
	}

	public void setPublicreport(Boolean publicreport) {
		this.publicreport = publicreport;
	}

	public String getMeasures() {
		return measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public java.sql.Timestamp getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(java.sql.Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	public AmpTeamMember getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(AmpTeamMember ownerId) {
		this.ownerId = ownerId;
	}

	
}
