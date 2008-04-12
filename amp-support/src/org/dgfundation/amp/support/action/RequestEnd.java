package org.dgfundation.amp.support.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class RequestEnd extends ActionSupport implements ServletRequestAware {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3122603414603319734L;
	HttpServletRequest request = null;

	public void setServletRequest(HttpServletRequest arg) {
		this.request = arg;

	}
	
	public String execute() throws Exception {
		request.getSession().setAttribute("request_locale", getLocale());
		return "viewend";
	}
	
	@Override
	public Locale getLocale() {
		if (request.getSession().getAttribute("request_locale") != null) {
			Locale myLocale = (Locale) request.getSession().getAttribute(
					"request_locale");
			return myLocale;
		}
		return super.getLocale();
	}
}
