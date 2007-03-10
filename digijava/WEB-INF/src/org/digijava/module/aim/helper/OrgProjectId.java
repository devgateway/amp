package org.digijava.module.aim.helper;

import java.io.Serializable;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class OrgProjectId implements Serializable, Comparable
{

	private Long ampOrgId;
	private String name;
	private String projectId;
	private AmpOrganisation organisation;

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

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		OrgProjectId oPId=(OrgProjectId)arg0;
		return this.name.compareTo(oPId.getName());
	}

	public AmpOrganisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(AmpOrganisation organisation) {
		this.organisation = organisation;
	}
}
