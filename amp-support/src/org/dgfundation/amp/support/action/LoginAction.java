package org.dgfundation.amp.support.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.dgfundation.amp.support.dbentity.Country;
import org.dgfundation.amp.support.dbentity.Languages;
import org.dgfundation.amp.support.dbentity.Login;
import org.dgfundation.amp.support.hibernate.EntityHelper;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 8739024626239922575L;

	HttpServletRequest request = null;

	private String password;

	private String username;

	private String countrycode;

	private String rlocale;

	public String execute() throws Exception {
		Login login = null;
		if (request.getParameter("code") != null) {
			String code = (String) request.getParameter("code").toUpperCase();
			login = EntityHelper.getLoginbyCountry(EntityHelper.getCountry(code));
			if (login != null) {
				request.getSession().setAttribute("login", login);
				request.getSession().setAttribute("request_locale", getLocale());
				return "succes";
			}
		}
		return "login";
	}

	public String postLoging() throws Exception {
		Login login = EntityHelper.getLogin(this.username, this.password,
				EntityHelper.getCountry(this.countrycode));
		if (login != null) {
			if (login.getRole() != 2) {
				request.getSession().setAttribute("login", login);
				request.getSession().setAttribute("request_locale", getLocale());
				return "succes";
			} else {
				request.getSession().setAttribute("login", login);
				request.getSession()
						.setAttribute("request_locale", getLocale());
				return "isadmin";
			}
		} else {
			return "failed";
		}
	}

	@Override
	public Locale getLocale() {
		if (this.rlocale != null) {
			Locale myLocale = new Locale(this.rlocale);
			return myLocale;
		}
		return super.getLocale();
	}

	public String loginfailed() throws Exception {
		addActionError(getText("error.password_wrong"));
		return "failed";
	}

	public void setServletRequest(HttpServletRequest arg) {
		this.request = arg;

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Country> getCountries() {
		return EntityHelper.getAllCountries();
	}

	public List<Languages> getLanguages() {
		List<Languages> languageslist = new ArrayList<Languages>();
		languageslist.add(new Languages(getText("lang.english"), "en"));
		languageslist.add(new Languages(getText("lang.french"), "fr"));
		languageslist.add(new Languages(getText("lang.spanish"), "es"));
		return languageslist;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	@Override
	public void validate() {
		if ("".equalsIgnoreCase(this.username)) {
			addActionError(getText("error.username_empty"));
		}
		if ("".equalsIgnoreCase(this.password)) {
			addActionError(getText("error.password_empty"));
		}
		super.validate();
	}

	public String getRlocale() {
		return rlocale;
	}

	public void setRlocale(String rlocale) {
		this.rlocale = rlocale;
	}

}
