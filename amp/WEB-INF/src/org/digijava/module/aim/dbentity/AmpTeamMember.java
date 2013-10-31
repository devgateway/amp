/*
 *  AmpTeamMember.java
 *  @Author Priyajith C
 *  Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.Output;
import org.digijava.module.message.dbentity.AmpMessageState;

public class AmpTeamMember implements Serializable/*, Versionable*/ {

	private Long ampTeamMemId;
	private User user;
	private AmpTeam ampTeam;
	private AmpTeamMemberRoles ampMemberRole;
	private Set<AmpActivityVersion> activities;
	private Set reports;
	private Set links;
	private Set logs;
	private Set<AmpMessageState> messages;
	private Boolean publishDocPermission; /*whether the team member has permissions to add document using templates*/

	// added for donor access
	private Set editableFundingOrgs;	// in case of donor - allowed organisations whose funding details this TM can edit

	private Set<AmpDesktopTabSelection> desktopTabSelections;

	public void setReports(Set reports) {
		this.reports = reports;
	}

	public Set getReports() {
			  return this.reports;
	}

	/**
	 * @return ampTeam
	 */
	public AmpTeam getAmpTeam() {
		return ampTeam;
	}

	/**
	 * @return ampMemberRole
	 */
	public AmpTeamMemberRoles getAmpMemberRole() {
		return ampMemberRole;
	}

	/**
	 * @return ampTeamMemId
	 */
	public Long getAmpTeamMemId() {
		return ampTeamMemId;
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param ampTeam
	 */
	public void setAmpTeam(AmpTeam ampTeam) {
		this.ampTeam = ampTeam;
	}

	/**
	 * @param ampMemberRole
	 */
	public void setAmpMemberRole(AmpTeamMemberRoles ampMemberRole) {
		this.ampMemberRole = ampMemberRole;
	}

	/**
	 * @param ampTeamMemId
	 */
	public void setAmpTeamMemId(Long ampTeamMemId) {
		this.ampTeamMemId = ampTeamMemId;
	}

	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}



	public Set<AmpActivityVersion> getActivities() {
		return activities;
	}

	public void setActivities(Set<AmpActivityVersion> activities) {
		this.activities = activities;
	}



    /**
     * @return Returns the links.
     */
    public Set getLinks() {
        return links;
    }
    /**
     * @param links The links to set.
     */
    public void setLinks(Set links) {
        this.links = links;
    }
	/**
	 * @return Returns the editableFundingOrgs.
	 */
	public Set getEditableFundingOrgs() {
		return editableFundingOrgs;
	}
	/**
	 * @param editableFundingOrgs The editableFundingOrgs to set.
	 */
	public void setEditableFundingOrgs(Set editableFundingOrgs) {
		this.editableFundingOrgs = editableFundingOrgs;
	}

	public Set<AmpDesktopTabSelection> getDesktopTabSelections() {
		return desktopTabSelections;
	}

	public void setDesktopTabSelections(
			Set<AmpDesktopTabSelection> desktopTabSelections) {
		this.desktopTabSelections = desktopTabSelections;
	}

	public Set getLogs() {
		return logs;
	}

	public void setLogs(Set logs) {
		this.logs = logs;
	}

	public Set<AmpMessageState> getMessages() {
		return messages;
	}

	public void setMessages(Set<AmpMessageState> messages) {
		this.messages = messages;
	}

	public Boolean getPublishDocPermission() {
		return publishDocPermission;
	}

	public void setPublishDocPermission(Boolean publishDocPermission) {
		this.publishDocPermission = publishDocPermission;
	}

	@Override
	public boolean equals(Object oth)
	{
		if (!(oth instanceof AmpTeamMember))
			return false;
		
		AmpTeamMember other = (AmpTeamMember) oth;
		return this.getAmpTeamMemId().longValue() == other.getAmpTeamMemId().longValue();
	}
	
	@Override
	public String toString()
	{
		return String.format("User: %s, team %s", user.getName(), ampTeam.getName());
	}
	
	/*
	@Override
	public boolean equalsForVersioning(Object obj) {
		return this.equals(obj);
	}
    @Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}
    @Override
	public Output getOutput() {
		return new Output(null, new String[] { user.getLastName(), ", ", user.getFirstNames() }, new Object[] { "" });
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) {
		this.activities = new HashSet<AmpActivityVersion>();
		this.activities.add(newActivity);
		return this;
	}*/
	
	public TeamMember toTeamMember()
	{
		TeamMember tm = new TeamMember();
		
        tm.setMemberId(this.getAmpTeamMemId());
        tm.setMemberName(this.getUser().getName());
        tm.setRoleId(this.getAmpMemberRole().getAmpTeamMemRoleId());
        tm.setRoleName(this.getAmpMemberRole().getRole());
        tm.setTeamId(this.getAmpTeam().getAmpTeamId());
        tm.setTeamName(this.getAmpTeam().getName());
        tm.setTeamType(this.getAmpTeam().getTeamCategory());
        tm.setEmail(this.getUser().getEmail());
        tm.setTeamAccessType(this.getAmpTeam().getAccessType());
        tm.setComputation(this.getAmpTeam().getComputation());
        tm.setUseFilters(this.getAmpTeam().getUseFilter());
        tm.setAddActivity(this.getAmpTeam().getAddActivity());
        tm.setPledger(this.getUser().getPledger());
        tm.setPublishDocuments(this.getPublishDocPermission());
        tm.setApprover(this.getAmpMemberRole().isApprover());   
        
        return tm;
	}
}
