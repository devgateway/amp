/*
 *  AmpTeamMember.java
 *  @Author Priyajith C
 *  Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.kernel.ampapi.endpoints.common.valueproviders.TeamMemberValueProvider;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.message.dbentity.AmpMessageState;

import java.io.Serializable;
import java.util.Set;

@InterchangeableValue(TeamMemberValueProvider.class)
public class AmpTeamMember implements Serializable, Identifiable/*, Versionable*/ {

    @PossibleValueId
    private Long ampTeamMemId;

    private User user;

    private AmpTeam ampTeam;
    private AmpTeamMemberRoles ampMemberRole;
    private Set<AmpActivityVersion> activities;
    private Set<AmpReports> reports;
    
    //private Set links;
    private Set logs;
    private Set<AmpMessageState> messages;
    private Boolean publishDocPermission; /*whether the team member has permissions to add document using templates*/

    // added for donor access
    private Set editableFundingOrgs;    // in case of donor - allowed organisations whose funding details this TM can edit

    private Set<AmpDesktopTabSelection> desktopTabSelections;

    public void setReports(Set<AmpReports> reports) {
        this.reports = reports;
    }

    public Set<AmpReports> getReports() {
        return this.reports;
    }

    private Boolean deleted;

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isSoftDeleted() {
        return Boolean.TRUE.equals(deleted);
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

    public TeamMember toTeamMember()
    {
        return new TeamMember(this);
    }

    public boolean isActivityValidatableByUser(Long ampActivityId) {
        String sql = WorkspaceFilter.generateWorkspaceFilterQuery(toTeamMember());
        Set<Long> ids = ActivityUtil.fetchLongs(sql);
        return ids.contains(ampActivityId);
    }

    @Override
    public Object getIdentifier() {
        return this.ampTeamMemId;
    }

}
