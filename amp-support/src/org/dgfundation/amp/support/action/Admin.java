package org.dgfundation.amp.support.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class Admin extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 5499126656626123250L;
	
	private HttpServletRequest request;

	public String execute() throws Exception {
		return "admin";
	}
	
	public void setServletRequest(HttpServletRequest arg) {
		this.request = arg;
		
	}

}
