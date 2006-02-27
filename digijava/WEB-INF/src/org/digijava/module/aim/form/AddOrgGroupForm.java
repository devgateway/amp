package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class AddOrgGroupForm extends ActionForm {
	
	private Long ampOrgGrpId;
	private String orgGrpName;
	private String orgGrpCode;
	private Long levelId;
	private String action = null;
	private String flag = null;
	private Collection level = null;
	private Long ampOrgId = null;

	public Long getAmpOrgGrpId() {
		return ampOrgGrpId;
	}
	
	public void setAmpOrgGrpId(Long ampOrgGrpId) {
		this.ampOrgGrpId = ampOrgGrpId;
	}
	
	public Long getLevelId() {
		return levelId;
	}
	
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	
	public String getOrgGrpCode() {
		return orgGrpCode;
	}
	
	public void setOrgGrpCode(String orgGrpCode) {
		this.orgGrpCode = orgGrpCode;
	}
	
	public String getOrgGrpName() {
		return orgGrpName;
	}
	
	public void setOrgGrpName(String orgGrpName) {
		this.orgGrpName = orgGrpName;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public Collection getLevel() {
		return level;
	}
	
	public void setLevel(Collection level) {
		this.level = level;
	}
	
	public Long getAmpOrgId() {
		return ampOrgId;
	}
	
	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}
}