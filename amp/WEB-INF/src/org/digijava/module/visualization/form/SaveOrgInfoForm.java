package org.digijava.module.visualization.form;

import org.apache.struts.action.ActionForm;

public class SaveOrgInfoForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private Long orgId;
	private String orgBackground;
	private String orgDescription;
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgBackground() {
		return orgBackground;
	}
	public void setOrgBackground(String orgBackground) {
		this.orgBackground = orgBackground;
	}
	public String getOrgDescription() {
		return orgDescription;
	}
	public void setOrgDescription(String orgDescription) {
		this.orgDescription = orgDescription;
	}

}
