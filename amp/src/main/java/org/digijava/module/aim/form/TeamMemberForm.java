package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpTeamMember;

import java.util.Collection;

public class TeamMemberForm extends ActionForm {

    private Collection allUser;
    private Collection teamMembers;
    private Long teamMemberId;
    private String action;
    private String teamName;
    private Long teamId;
    private Long[] selMembers;
    private String addMember;
    private String removeMember;
    private String email;
    private String name;
    private Long role;
    private Collection ampRoles;
    private String permissions;
    private Long userId;
    private int fromPage;
    private Long headId;
    private Long workspaceManId;
    private boolean someError;
    
    private AmpTeamMember teamHead; //hold's workspace manager
    private String teamLeaderExists; //TL exists or not
    private Long workspaceManagerRoleId;  // Holds the "Team Head" role's id (amp_team_member_roles)
    private String[] userIdsWithRoles;    // userId_RoleId

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }
        
        private Integer selectedRow;
        private String fullname;
      
        public Integer getSelectedRow() {
            return selectedRow;
        }

        public void setSelectedRow(Integer selectedRow) {
            this.selectedRow = selectedRow;
        }
    
    /**
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }
    /**
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }
    /**
     * @return Returns the addMember.
     */
    public String getAddMember() {
        return addMember;
    }
    /**
     * @param addMember The addMember to set.
     */
    public void setAddMember(String addMember) {
        this.addMember = addMember;
    }
    /**
     * @return Returns the ampRoles.
     */
    public Collection getAmpRoles() {
        return ampRoles;
    }
    /**
     * @param ampRoles The ampRoles to set.
     */
    public void setAmpRoles(Collection ampRoles) {
        this.ampRoles = ampRoles;
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
     * @return Returns the fromPage.
     */
    public int getFromPage() {
        return fromPage;
    }
    /**
     * @param fromPage The fromPage to set.
     */
    public void setFromPage(int fromPage) {
        this.fromPage = fromPage;
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
     * @return Returns the permissions.
     */
    public String getPermissions() {
        return permissions;
    }
    /**
     * @param permissions The permissions to set.
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
    /**
     * @return Returns the removeMember.
     */
    public String getRemoveMember() {
        return removeMember;
    }
    /**
     * @param removeMember The removeMember to set.
     */
    public void setRemoveMember(String removeMember) {
        this.removeMember = removeMember;
    }
    /**
     * @return Returns the role.
     */
    public Long getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
    public void setRole(Long role) {
        this.role = role;
    }
    /**
     * @return Returns the selMembers.
     */
    public Long[] getSelMembers() {
        return selMembers;
    }
    /**
     * @param selMembers The selMembers to set.
     */
    public void setSelMembers(Long[] selMembers) {
        this.selMembers = selMembers;
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
     * @return Returns the teamMemberId.
     */
    public Long getTeamMemberId() {
        return teamMemberId;
    }
    /**
     * @param teamMemberId The teamMemberId to set.
     */
    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
    }
    /**
     * @return Returns the teamMembers.
     */
    public Collection getTeamMembers() {
        return teamMembers;
    }
    /**
     * @param teamMembers The teamMembers to set.
     */
    public void setTeamMembers(Collection teamMembers) {
        this.teamMembers = teamMembers;
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
     * @return Returns the userId.
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Collection getallUser() {
        return allUser;
    }
    public void setallUser(Collection allMembers) {
        this.allUser = allMembers;
    }
    public void setHeadId(Long headId) {
        // TODO Auto-generated method stub
        this.headId = headId;
    }
    public Long getHeadId() {
        return headId;
    }
    public Long getWorkspaceManId() {
        return workspaceManId;
    }
    public void setWokspaceManId(Long wmId) {
        this.workspaceManId = wmId;
    }
    public void setSomeError(boolean b) {
        this.someError = b;
        
    }
    public boolean getSomeError() {
        return this.someError;
        
    }
    public String[] getUserIdsWithRoles() {
        return userIdsWithRoles;
    }
    public void setUserIdsWithRoles(String[] userIdsWithRoles) {
        this.userIdsWithRoles = userIdsWithRoles;
    }
    public Long getWorkspaceManagerRoleId() {
        return workspaceManagerRoleId;
    }
    public void setWorkspaceManagerRoleId(Long workspaceManagerRoleId) {
        this.workspaceManagerRoleId = workspaceManagerRoleId;
    }
    public String getTeamLeaderExists() {
        return teamLeaderExists;
    }
    public void setTeamLeaderExists(String teamLeaderExists) {
        this.teamLeaderExists = teamLeaderExists;
    }
    public AmpTeamMember getTeamHead() {
        return teamHead;
    }
    public void setTeamHead(AmpTeamMember teamHead) {
        this.teamHead = teamHead;
    }   
}
