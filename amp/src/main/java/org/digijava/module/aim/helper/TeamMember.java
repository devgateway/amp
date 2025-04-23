package org.digijava.module.aim.helper;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.Set;

public class TeamMember implements Comparable, Serializable{

    private Long memberId;
    private Long userId;
    private String memberName;
    private String email;
    private Long roleId;
    private String roleName;
    private Long teamId;
    private String teamName;
    private String teamAccessType; // indicates whether MANAGEMENT or WORKING TEAM
    private Boolean computation;
    private Boolean useFilters;
    private Boolean addActivity;
    private boolean teamHead;
    private Boolean pledger;
    private boolean teamIsolated;

    private ApplicationSettings appSettings; /*
                                              * Application settings of the
                                              * member
                                              */

    private Set activities;
    private Boolean publishDocuments; /*permissions to make docs public*/
    private boolean approver;
    private AmpCategoryValue workspacePrefix;
    private Integer allowAddTeamRes;
    private long visibilityTemplateId;
    private Boolean pledgeSuperUser;

    /**
     * use {@link #TeamMember(AmpTeamMember)} instead of this one
     */
    @Deprecated
    public TeamMember()
    {
        
    }
    /**
     * TeamMember utility wrapper over AmpTeamMember  
     * @param tm
     */
    public TeamMember(AmpTeamMember tm)
    {
        if( tm!=null ) {
            init(tm.getUser());
            init(tm.getAmpMemberRole());
            init(tm.getAmpTeam());
            this.memberId = tm.getAmpTeamMemId();
            this.publishDocuments = tm.getPublishDocPermission();
        }
    }
    /**
     * Use this constructor only if you cannot call {@link #TeamMember(AmpTeamMember)}
     * @param u
     */
    public TeamMember(User u) {
        init(u);
    }
    
    private void init(User u) {
        if( u!=null ) {
            this.memberId = u.getId();
            this.userId = u.getId();
            this.memberName = u.getName();
            this.email = u.getEmail();
            this.pledger = u.getPledger();
            this.pledgeSuperUser = u.getPledgeSuperUser();
        }
    }
    
    private void init(AmpTeamMemberRoles r) {
        if( r!=null ) {
            this.roleName = r.getRole();
            this.roleId = r.getAmpTeamMemRoleId();
            this.approver = r.isApprover();
            this.teamHead = TeamMemberUtil.isHeadRole(r);
        }
    }
    
    private void init(AmpTeam t) {
        if (t != null) {
            this.teamId = (Long)t.getIdentifier();
            this.teamName = t.getName();
            this.teamAccessType = t.getAccessType();
            this.computation = t.getComputation();
            this.useFilters = t.getUseFilter();
            this.addActivity = t.getAddActivity();
            this.workspacePrefix = t.getWorkspacePrefix();
            this.teamIsolated = t.getIsolated() == null ? false : t.getIsolated();
            AmpApplicationSettings sett = DbUtil.getTeamAppSettings(this.teamId);
            this.allowAddTeamRes = sett == null ? null : sett.getAllowAddTeamRes();
            this.visibilityTemplateId = t.getFmTemplate() == null ? 0 : t.getFmTemplate().getId();
        }
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
     * @return Returns the roleName.
     */
    public String getRoleName() {
        return roleName;
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
     * @return Returns the teamAccessType.
     */
    public String getTeamAccessType() {
        return teamAccessType;
    }

    public String toString() {
        return memberName;
    }

    public Boolean getComputation() {
        return computation;
    }

    public Boolean getAddActivity() {
        return addActivity;
    }

    /**
     * @return the pledger
     */
    public Boolean getPledger() {
        return pledger;
    }

    public Boolean getPledgeSuperUser() {
        return pledgeSuperUser;
    }
    public Boolean getPublishDocuments() {
        return publishDocuments;
    }

    public boolean isApprover() {
        return approver;
    }
    
    public Boolean getUseFilters()
    {
        return this.useFilters;
    }

    public AmpCategoryValue getWorkspacePrefix() {
        return workspacePrefix;
    }

    @Override
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        if(arg0!=null || !(arg0 instanceof Long)) return -1;
        Long newId = (Long)arg0;
        return this.getMemberId().compareTo(newId); 
    }
    
    public boolean getTeamIsolated() {
        return teamIsolated;
    }
    
    public Integer getAllowAddTeamRes() {
        return this.allowAddTeamRes;
    }
    
    public long getVisibilityTemplateId() {
        return this.visibilityTemplateId;
    }

}
