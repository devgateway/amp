package org.digijava.module.mondrian.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SaveHtmlForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String reportname;
	private String action;
	private boolean reset;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.reportname = null;
		this.action = null;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

}
