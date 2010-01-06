package org.dgfoundation.ecs.keeper;

import java.io.Serializable;
import java.util.Calendar;

import org.dgfoundation.ecs.core.ECS;

public class ErrorScene implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Calendar date;
	public String browser;
	public String sessionId;

	public ErrorScene() {
		super();
	}
	
	public ErrorScene(String browser, Calendar date, String sessionId) {
		super();
		this.date = date;
		this.browser = browser;
		this.sessionId = sessionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public String toJson(){
		String result = 
			"{" +
			ECS.t("date", date.get(Calendar.YEAR) +"-"+ (date.get(Calendar.MONTH)+1) +"-"+ date.get(Calendar.DATE) +" "+ date.get(Calendar.HOUR_OF_DAY) +":"+ date.get(Calendar.MINUTE)+":"+ date.get(Calendar.SECOND)) +
			ECS.t("browser", browser) +
			ECS.to("sessionId", sessionId) +
			"}";
		return result;
	}
	
}
