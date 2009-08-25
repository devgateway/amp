package org.dgfoundation.amp.ecs.webservice;

import java.io.Serializable;
import java.util.Date;

public class ErrorScene implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Date date;
	public String browser;

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
}
