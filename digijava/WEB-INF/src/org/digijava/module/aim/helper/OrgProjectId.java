package org.digijava.module.aim.helper;

import java.io.Serializable;

public class OrgProjectId implements Serializable 
{

	private Long ampOrgId;
	private String name;
	private String projectId;

	public OrgProjectId() {
		ampOrgId = null;
		name = null;
		projectId = null;
	}

	/**
	 * @return Returns the ampOrgId.
	 */
	public Long getAmpOrgId() {
		return ampOrgId;
	}
	/**
	 * @param ampOrgId The ampOrgId to set.
	 */
	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
