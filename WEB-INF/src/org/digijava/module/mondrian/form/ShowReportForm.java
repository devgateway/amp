package org.digijava.module.mondrian.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

public class ShowReportForm extends ActionForm{

	
	private static final long serialVersionUID = 1L;
	private String lastdate;
	public String getLastdate() {
		return lastdate;
	}
	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}
	
}
