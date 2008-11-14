package org.digijava.module.aim.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class UpdateRoleForm extends ValidatorForm {

		  private Long roleId = null;
		  private String action = null;
		  private String role = null;
		  private String description = null;
		  private String readPermission = null;
		  private String writePermission = null;
		  private String deletePermission = null;
		  private String teamHead = null;
		  private String flag = null;
		  private String teamHeadFlag = null; // to indicate whether any role is assigned the team lead position.

		  public Long getRoleId() {
					 return roleId;
		  }

		  public void setRoleId(Long roleId) {
					 this.roleId = roleId;
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
		  
		  public String getRole() {
					 return role;
		  }

		  public void setRole(String role) {
					 this.role = role;
		  }		  		  
		  
		  public String getDescription() {
					 return description;
		  }

		  public void setDescription(String description) {
					 this.description = description;
		  }

		  public String getReadPermission() {
					 return readPermission;
		  }

		  public void setReadPermission(String readPermission) {
					 this.readPermission = readPermission;
		  }

		  public String getWritePermission() {
					 return writePermission;
		  }

		  public void setWritePermission(String writePermission) {
					 this.writePermission = writePermission;
		  }

		  public String getDeletePermission() {
					 return deletePermission;
		  }

		  public void setDeletePermission(String deletePermission) {
					 this.deletePermission = deletePermission;
		  }

		  public String getTeamHead() {
					 return teamHead;
		  }

		  public void setTeamHead(String teamHead) {
					 this.teamHead = teamHead;
		  }

		  public String getTeamHeadFlag() {
					 return teamHeadFlag;
		  }

		  public void setTeamHeadFlag(String teamHeadFlag) {
					 this.teamHeadFlag = teamHeadFlag;
		  }

			public ActionErrors validate( ActionMapping mapping, HttpServletRequest request) {
				if (role != null || description != null) {
					return super.validate(mapping, request);
				} 
				else return null;
			}
	
}
