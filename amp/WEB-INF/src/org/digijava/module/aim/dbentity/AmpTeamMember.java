/*
 *  AmpTeamMember.java
 *  @Author Priyajith C
 *  Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.kernel.ampapi.endpoints.common.valueproviders.TeamMemberValueProvider;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.message.dbentity.AmpMessageState;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_TEAM_MEMBER")
@InterchangeableValue(TeamMemberValueProvider.class)
public class AmpTeamMember implements Serializable, Identifiable/*, Versionable*/ {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_team_member_seq_generator")
    @SequenceGenerator(name = "amp_team_member_seq_generator", sequenceName = "amp_team_member_seq", allocationSize = 1)
    @Column(name = "amp_team_mem_id")   
    @PossibleValueId

    private Long ampTeamMemId;

    @Column(name = "publish_doc_permission")
    private Boolean publishDocPermission;

    @ManyToOne
    @JoinColumn(name = "user_")
    private User user;

    @ManyToOne
    @JoinColumn(name = "amp_team_id")
    private AmpTeam ampTeam;

    @ManyToOne
    @JoinColumn(name = "amp_member_role_id")
    private AmpTeamMemberRoles ampMemberRole;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_MEMBER_ACTIVITIES",
            joinColumns = @JoinColumn(name = "amp_member_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_activity_id"))
    private Set<AmpActivityVersion> activities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_MEMBER_REPORTS",
            joinColumns = @JoinColumn(name = "amp_member_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_report_id"))
    private Set<AmpReports> reports;

    @OneToMany(mappedBy = "ampMemberId", cascade = CascadeType.ALL)
    private Set<AmpReportLog> logs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_EDIT_DONOR_ORG",
            joinColumns = @JoinColumn(name = "amp_team_mem_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_org_id"))
    private Set<AmpOrganisation> editableFundingOrgs;

    @OneToMany(mappedBy = "ownerId", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OrderBy(value = "index")
    private Set<AmpDesktopTabSelection> desktopTabSelections;

    @OneToMany(mappedBy = "receiverId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpMessageState> messages;

    @Column(name = "deleted")
    private Boolean deleted;
   

    public void setReports(Set<AmpReports> reports) {
        this.reports = reports;
    }

    public Set<AmpReports> getReports() {
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
