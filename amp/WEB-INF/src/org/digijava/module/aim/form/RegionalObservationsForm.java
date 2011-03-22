package org.digijava.module.aim.form;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.Components;

public class RegionalObservationsForm extends ActionForm {

	private ArrayList issues;
	private boolean validLogin;
	private long ampActivityId;

	public ArrayList getIssues() {
		return issues;
	}

	public void setIssues(ArrayList issues) {
		this.issues = issues;
	}

	public boolean isValidLogin() {
		return validLogin;
	}

	public void setValidLogin(boolean validLogin) {
		this.validLogin = validLogin;
	}

	public long getAmpActivityId() {
		return ampActivityId;
	}

	public void setAmpActivityId(long ampActivityId) {
		this.ampActivityId = ampActivityId;
	}
}