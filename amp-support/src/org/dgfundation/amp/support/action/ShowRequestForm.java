package org.dgfundation.amp.support.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.dgfundation.amp.support.dbentity.AmpVersion;
import org.dgfundation.amp.support.dbentity.Browsers;
import org.dgfundation.amp.support.dbentity.Country;
import org.dgfundation.amp.support.dbentity.Login;
import org.dgfundation.amp.support.dbentity.OperatingSystem;
import org.dgfundation.amp.support.dbentity.RequestSupport;
import org.dgfundation.amp.support.dto.ModuleDto;
import org.dgfundation.amp.support.hibernate.EntityHelper;
import org.dgfundation.amp.support.mail.MailSender;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.xml.internal.bind.v2.TODO;

public class ShowRequestForm extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4576711197411563004L;

	private static Logger log = Logger.getLogger(ShowRequestForm.class
			.getName());

	HttpServletRequest request = null;

	private Date date;

	private Country country;

	private String fullusername;

	private String email;

	private String browser;

	private String browserversion;

	private String operatingsystem;

	private String subject;

	private String details;

	private String version;

	private String amplogin;

	private String amppassword;

	private String mailcc;

	private String[] modules;

	private String level;

	public String execute() throws Exception {
		request.getSession().setAttribute("request_locale", getLocale());
		return "succes";
	}

	public String saveRequest() {
		request.getSession().setAttribute("request_locale", getLocale());
		Login login = (Login) request.getSession().getAttribute("login");
		RequestSupport srequest = new RequestSupport();
		Calendar cal = Calendar.getInstance();
		srequest.setBrowser(this.getBrowser());
		srequest.setBrowserversion(this.getBrowserversion());
		srequest.setCountry(login.getCountry());
		srequest.setDate(cal.getTime());
		srequest.setDetails(this.getDetails());
		srequest.setEmail(this.getEmail());
		srequest.setFullusername(this.getFullusername());
		srequest.setLogin((Login) request.getSession().getAttribute("login"));
		srequest.setAmplogin(this.amplogin);
		srequest.setAmppassword(this.amppassword);
		srequest.setMailcc(this.mailcc);
		srequest.setLevel(this.level);
		String strmodules = "";
		for (int i = 0; i < modules.length; i++) {
			if ("".compareToIgnoreCase(strmodules) == 0) {
				strmodules = modules[i];
			} else {
				strmodules = strmodules + "," + modules[i];
			}
		}
		srequest.setModule(strmodules);
		srequest.setOperatingsystem(this.operatingsystem);
		srequest.setSubject(this.getSubject());
		srequest.setVersion(this.getVersion());

		try {
			srequest = EntityHelper.saveRequest(srequest);
		} catch (Exception e) {
			log.error(e);
			return "failed";
		}
		if (!MailSender.sendSupportMail(srequest)) {
			MailSender.sendSupportMail(srequest);
			addActionError(getText("error.sending_request"));
			return "failed";
		}
		MailSender.sendCustomerMail(srequest, getLocale().getLanguage());
		clearFields();
		addActionError(getText("succes.mail_sent"));
		return "succes";
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

	public String saveFailed() {
		request.getSession().setAttribute("request_locale", getLocale());
		clearFields();
		addActionError(getText("error.sending_request"));
		return "failed";
	}

	public Collection<Browsers> getBrowserlist() {
		List<Browsers> browsers = new ArrayList<Browsers>();
		browsers.add(new Browsers(getText("label.form.specify_browser"), "NB"));
		browsers.add(new Browsers("Internet Explorer", "IE"));
		browsers.add(new Browsers("Firefox", "FF"));
		browsers.add(new Browsers(getText("label.other"), "OT"));
		return browsers;
	}

	public Collection<OperatingSystem> getOs() {
		List<OperatingSystem> os = new ArrayList<OperatingSystem>();
		os = EntityHelper.getOs();
		return os;
	}

	public Collection<AmpVersion> getVLst() {
		List<AmpVersion> vl = new ArrayList<AmpVersion>();
		vl = EntityHelper.getAmpVersion();

		/*
		 * vl.add(getText("label.from.choose_version")); vl.add("1.09RC1");
		 * vl.add("1.09RC1.10"); vl.add("1.12"); vl.add("1.12i");
		 * vl.add("1.12k"); vl.add("1.12l"); vl.add("1.13");
		 */
		return vl;
	}

	public ArrayList<String> getLstLevel() {
		ArrayList<String> lv = new ArrayList<String>();
		lv.add(getText("label.level_low"));
		lv.add(getText("label.level_med"));
		lv.add(getText("label.level_high"));
		return lv;
	}

	public Collection<ModuleDto> getMlst() {
		List<ModuleDto> modules = new ArrayList<ModuleDto>();
		modules = EntityHelper.getMoudules(getLocale().getLanguage());
		return modules;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserversion() {
		return browserversion;
	}

	public void setBrowserversion(String browserversion) {
		this.browserversion = browserversion;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullusername() {
		return fullusername;
	}

	public void setFullusername(String fullusername) {
		this.fullusername = fullusername;
	}

	public String getOperatingsystem() {
		return operatingsystem;
	}

	public void setOperatingsystem(String operatingsystem) {
		this.operatingsystem = operatingsystem;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setServletRequest(HttpServletRequest httprequest) {
		this.request = httprequest;
	}

	@Override
	public void validate() {
		if ("".equalsIgnoreCase(this.fullusername)) {
			addActionError(getText("error.user_empty"));
		}
		if ("".equalsIgnoreCase(this.email)) {
			addActionError(getText("error.mail_empty"));
		}
		if (this.email != null) {
			if (this.email.indexOf('@') < 0 || this.email.indexOf('.') < 0) {
				addActionError(getText("error.mail_invalid"));
			}
		}
		if ("".equalsIgnoreCase(this.subject)) {
			addActionError(getText("error.subject_empty"));
		}
		if ("".equalsIgnoreCase(this.details)) {
			addActionError(getText("error.description_empty"));
		}
		if (getText("label.form.specify_browser")
				.equalsIgnoreCase(this.browser)) {
			addActionError(getText("error.browser_empty"));
		}
		if ("".equalsIgnoreCase(this.operatingsystem)) {
			addActionError(getText("error.os_empty"));
		}
		if ("".equalsIgnoreCase(this.amplogin)) {
			addActionError(getText("error.amp.user"));
		}
		if ("".equalsIgnoreCase(this.amppassword)) {
			addActionError(getText("error.amp.password"));
		}
		super.validate();
	}

	public void clearFields() {

		date = null;
		country = null;
		fullusername = null;
		email = null;
		browser = null;
		browserversion = null;
		operatingsystem = null;
		modules = null;
		subject = null;
		details = null;
		version = null;
	}

	public void setModules(String[] modules) {
		this.modules = modules;
	}

	public String[] getModules() {
		return modules;
	}

	public String getAmplogin() {
		return amplogin;
	}

	public void setAmplogin(String amplogin) {
		this.amplogin = amplogin;
	}

	public String getAmppassword() {
		return amppassword;
	}

	public void setAmppassword(String amppassword) {
		this.amppassword = amppassword;
	}

	public String getMailcc() {
		return mailcc;
	}

	public void setMailcc(String mailcc) {
		this.mailcc = mailcc;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
