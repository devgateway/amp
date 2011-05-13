/*
 *  AmpTeamMemberRoles.java
 *  @Author Priyajith C
 *  Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AmpTeamMemberRoles implements Serializable {
	
	private Long ampTeamMemRoleId;
	private String role;
	private String description;
	private Boolean readPermission;
	private Boolean writePermission;
	private Boolean deletePermission;
	private Boolean teamHead;     /* whether this role is the team lead role */
	
	
	/**
	 * @return ampTeamMemRoleId
	 */
	public Long getAmpTeamMemRoleId() {
		return ampTeamMemRoleId;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param ampTeamMemRoleId
	 */
	public void setAmpTeamMemRoleId(Long ampTeamMemRoleId) {
		this.ampTeamMemRoleId = ampTeamMemRoleId;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getReadPermission() {
		return readPermission;
	}

	public void setReadPermission(Boolean readPermission) {
		this.readPermission = readPermission;
	}

	public Boolean getWritePermission() {
		return writePermission;
	}

	public void setWritePermission(Boolean writePermission) {
		this.writePermission = writePermission;
	}
	
	public Boolean getDeletePermission() {
		return deletePermission;
	}

	public void setDeletePermission(Boolean deletePermission) {
		this.deletePermission = deletePermission;
	}	

	public Boolean getTeamHead() {
		return teamHead;
	}

	public void setTeamHead(Boolean teamHead) {
		this.teamHead = teamHead;
	}
	
	/**
	 * @return the translation key used
	 */
	public String getAmpTeamMemberKey () {
		return 
			getAmpTeamMemberKey( this.role );
	}
	
	public static String getAmpTeamMemberKey ( String roleName ) {
		String asciiName	= CategoryManagerUtil.asciiStringFilter(roleName);
		return 
			"aim:AmpTeamMemeberRoleTrnKey:" + asciiName;
	}

}
