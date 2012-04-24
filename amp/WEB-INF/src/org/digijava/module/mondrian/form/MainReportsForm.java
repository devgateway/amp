package org.digijava.module.mondrian.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.mondrian.dbentity.OffLineReports;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class MainReportsForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private Long currentMemberId;
	
	private Collection<OffLineReports> reports = null;

	public Collection<OffLineReports> getReports() {
		return reports;
	}

	public void setReports(Collection<OffLineReports> reports) {
		this.reports = reports;
	}

	public Long getCurrentMemberId() {
		return currentMemberId;
	}

	public void setCurrentMemberId(Long currentMemberId) {
		this.currentMemberId = currentMemberId;
	}

}
