package org.digijava.module.aim.helper;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.Set;

public class TeamMember implements Comparable, Serializable{

	private Long memberId;

	private String memberName;

	private String email;

	private Long roleId;

	private String roleName;

	private Long teamId;

	private String teamName;

	@Deprecated
	private String teamType; // indicates whether the team is a DONOR team or MOFED team
	
	private String teamAccessType; // indicates whether MANAGEMENT or WORKING TEAM
	
	private Boolean computation;
	
	private Boolean useFilters;
	
	private Boolean addActivity;
	
	private boolean teamHead;

	private Boolean pledger;


	private ApplicationSettings appSettings; /*
											  * Application settings of the
											  * member
											  */

	private boolean translator;
	
	private Set activities;
	
	private Boolean publishDocuments; /*permissions to make docs public*/
	private boolean approver;
    private AmpCategoryValue workspacePrefix;

	public TeamMember()
	{
		
	}
        
        public TeamMember( String teamName,String teamMemberRole) {
            this.teamName = teamName;
            this.roleName = teamMemberRole;
        }

	/**
	 * @return Returns the appSettings.
	 */
	public ApplicationSettings getAppSettings() {
		return appSettings;
	}
	/**
	 * @param appSettings The appSettings to set.
	 */
	public void setAppSettings(ApplicationSettings appSettings) {
		this.appSettings = appSettings;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the memberId.
	 */
	public Long getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId The memberId to set.
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return Returns the memberName.
	 */
	public String getMemberName() {
		return memberName;
	}
	/**
	 * @param memberName The memberName to set.
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	/**
	 * @return Returns the roleId.
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return Returns the roleName.
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName The roleName to set.
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * @return Returns the teamHead.
	 */
	public boolean getTeamHead() {
		return teamHead;
	}
	/**
	 * @param teamHead The teamHead to set.
	 */
	public void setTeamHead(boolean teamHead) {
		this.teamHead = teamHead;
	}
	/**
	 * @return Returns the teamId.
	 */
	public Long getTeamId() {
		return teamId;
	}
	/**
	 * @param teamId The teamId to set.
	 */
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	/**
	 * @return Returns the teamName.
	 */
	public String getTeamName() {
		return teamName;
	}
	/**
	 * @param teamName The teamName to set.
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	/**
	 * @return Returns the translator.
	 */
	public boolean getTranslator() {
		return translator;
	}
	/**
	 * @param translator The translator to set.
	 */
	public void setTranslator(boolean translator) {
		this.translator = translator;
	}
	/**
	 * @return Returns the activities.
	 */
	public Set getActivities() {
		return activities;
	}
	/**
	 * @param activities The activities to set.
	 */
	public void setActivities(Set activities) {
		this.activities = activities;
	}

	/**
	 * @return Returns the teamType.
	 */
	@Deprecated
	public String getTeamType() {
		return teamType;
	}

	/**
	 * @param teamType The teamType to set.
	 */
	@Deprecated
	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	/**
	 * @return Returns the teamAccessType.
	 */
	public String getTeamAccessType() {
		return teamAccessType;
	}

	/**
	 * @param teamAccessType The teamAccessType to set.
	 */
	public void setTeamAccessType(String teamAccessType) {
		this.teamAccessType = teamAccessType;
	}
	
	public String toString() {
		return memberName;
	}

	public Boolean getComputation() {
		return computation;
	}

	public void setComputation(Boolean computation) {
		this.computation = computation;
	}

	public Boolean getAddActivity() {
		return addActivity;
	}

	public void setAddActivity(Boolean addActivity) {
		this.addActivity = addActivity;
	}

	/**
	 * @return the pledger
	 */
	public Boolean getPledger() {
		return pledger;
	}

	/**
	 * @param pledger the pledger to set
	 */
	public void setPledger(Boolean pledger) {
		this.pledger = pledger;
	}

	public Boolean getPublishDocuments() {
		return publishDocuments;
	}

	public void setPublishDocuments(Boolean publishDocuments) {
		this.publishDocuments = publishDocuments;
	}

	public void setApprover(boolean approver) {
		this.approver = approver;
	}

	public boolean isApprover() {
		return approver;
	}
	
	public void setUseFilters(Boolean useFilters)
	{
		this.useFilters = useFilters;
	}
	
	public Boolean getUseFilters()
	{
		return this.useFilters;
	}

    public AmpCategoryValue getWorkspacePrefix() {
        return workspacePrefix;
    }

    public void setWorkspacePrefix(AmpCategoryValue workspacePrefix) {
        this.workspacePrefix = workspacePrefix;
    }

    @Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0!=null || !(arg0 instanceof Long)) return -1;
		Long newId = (Long)arg0;
		return this.getMemberId().compareTo(newId); 
	}	
	
}
