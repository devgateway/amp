package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;

public class AuditLoggerManagerForm extends ActionForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2614366966871314200L;
	/**
	 * @author dan
	 */
	private Collection logs;
	
	public Collection getLogs() {
		return logs;
	}
	public void setLogs(Collection logs) {
		this.logs = logs;
	}
}
