package org.digijava.module.mondrian.form;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;

public class ShowReportForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String reportname;
	private String lastdate;
	private HashMap<String, String> errors = new HashMap<String, String>();
	private HashMap<String, String> messages = new HashMap<String, String>();
	private Collection<AmpCurrency> currencies;
	private String currency;
	private String reportid;
	private String jspname;
	
	public String getReportid() {
		return reportid;
	}

	public void setReportid(String reportid) {
		this.reportid = reportid;
	}

	public String getJspname() {
		return jspname;
	}

	public void setJspname(String jspname) {
		this.jspname = jspname;
	}

	public Collection<AmpCurrency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Collection<AmpCurrency> currencies) {
		this.currencies = currencies;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.clearMessages();
	}

	public void addMessage(String key, String value) {
		this.messages.put(key, value);
	}

	public void addError(String key, String value) {
		this.errors.put(key, value);
	}

	public void clearMessages() {
		this.errors.clear();
		this.messages.clear();
	}

	public HashMap<String, String> getErrors() {
		return errors;
	}

	public void setErrors(HashMap<String, String> errors) {
		this.errors = errors;
	}

	public HashMap<String, String> getMessages() {
		return messages;
	}

	public void setMessages(HashMap<String, String> messages) {
		this.messages = messages;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}
}
