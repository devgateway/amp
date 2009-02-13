package org.digijava.module.mondrian.dbentity;

import java.sql.Date;

import org.digijava.module.aim.dbentity.AmpTeamMember;

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

	public AmpTeamMember getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(AmpTeamMember ownerId) {
		this.ownerId = ownerId;
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

	
}
