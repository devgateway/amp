package org.digijava.kernel.ampapi.endpoints.util;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.digijava.module.aim.helper.TeamMember;
import org.joda.time.DateTime;

public class AmpApiToken implements Serializable,HttpSessionBindingListener{
	 
	
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

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * When the token is removed from session expired the asociated token
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		//not using TLSUtils since at this point is ThreadLocal is no loger valid
		SecurityUtil.removeTokenFromContext(event.getSession().getServletContext(),this.getToken());
	}


}
