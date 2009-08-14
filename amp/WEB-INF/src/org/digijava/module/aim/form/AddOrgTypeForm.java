package org.digijava.module.aim.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class AddOrgTypeForm extends ActionForm {
	
	private Long ampOrgTypeId = null;
	private String orgType = null;
	private Boolean orgTypeIsGovernmental = Boolean.FALSE;
	private String orgTypeCode = null;
	private String action = null;
	private Boolean reset = Boolean.FALSE;
	private String deleteFlag = "delete";
	
	public void reset(ActionMapping mapping, HttpServletRequest req) {
		if (reset.booleanValue()) {
			////System.out.println("inside reset: clearing properties");
			ampOrgTypeId = null;
			orgType = null;
			orgTypeCode = null;
			orgTypeIsGovernmental = Boolean.FALSE;
			deleteFlag = "delete";
			reset = Boolean.FALSE;
		}
	}
	
	public Long getAmpOrgTypeId() {
		return ampOrgTypeId;
	}
	public void setAmpOrgTypeId(Long ampOrgTypeId) {
		this.ampOrgTypeId = ampOrgTypeId;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getOrgTypeCode() {
		return orgTypeCode;
	}
	public void setOrgTypeCode(String orgTypeCode) {
		this.orgTypeCode = orgTypeCode;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Boolean getReset() {
		return reset;
	}
	public void setReset(Boolean reset) {
		this.reset = reset;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public boolean getOrgTypeIsGovernmental() {
		return orgTypeIsGovernmental;
	}

	public void setOrgTypeIsGovernmental(boolean orgTypeIsGovernmental) {
		this.orgTypeIsGovernmental = orgTypeIsGovernmental;
	}
	
}