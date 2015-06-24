package org.digijava.kernel.ampapi.endpoints.util;

import java.io.Serializable;

import org.digijava.module.aim.helper.TeamMember;
import org.joda.time.DateTime;

public class AmpApiToken implements Serializable{
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8372878787005618060L;
	private TeamMember teamMember;
	private DateTime expirationTime;
	private String token;
	
	
	public AmpApiToken (){
		
	}
	
	public TeamMember getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}

	public DateTime getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(DateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
