/*
 * AmpTeam.java @Author Priyajith C Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpTeam  implements Serializable {

	private Long ampTeamId;

	private String name;

	private String description;

	private AmpTeamMember teamLead; // Denotes the Team Leader

	private String type; // Whether Bilateral or Multilateral

	private AmpTeam parentTeamId;

	private String accessType;  // Management or Team

	//private Set activityList;

	/**
	 * @return ampTeamId
	 */
	public Long getAmpTeamId() {
		return ampTeamId;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return teamLeadId
	 */
	public AmpTeamMember getTeamLead() {
		return teamLead;
	}

	/**
	 * @param ampTeamId
	 */
	public void setAmpTeamId(Long ampTeamId) {
		this.ampTeamId = ampTeamId;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param teamLeadId
	 */
	public void setTeamLead(AmpTeamMember teamLead) {
		this.teamLead = teamLead;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the parentTeamId.
	 */
	public AmpTeam getParentTeamId() {
		return parentTeamId;
	}

	/**
	 * @param parentTeamId
	 *            The parentTeamId to set.
	 */
	public void setParentTeamId(AmpTeam parentTeamId) {
		this.parentTeamId = parentTeamId;
	}

	/**
	 * @return Returns the accessType.
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * @param accessType
	 *            The accessType to set.
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof AmpTeam)) throw new ClassCastException();
		
		AmpTeam temp = (AmpTeam) obj;
		return (this.ampTeamId.equals(temp.getAmpTeamId()));
	}
}