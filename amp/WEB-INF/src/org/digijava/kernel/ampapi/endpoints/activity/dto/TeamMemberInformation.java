package org.digijava.kernel.ampapi.endpoints.activity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;

public class TeamMemberInformation {
    @JsonProperty(ActivityEPConstants.TEAM_MEMBER_ROLE)
    private Long teamMemberRole;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AmpTeam workspace;



    public TeamMemberInformation(AmpTeamMember ampTeamMember) {
        teamMemberRole = ampTeamMember.getAmpMemberRole().getAmpTeamMemRoleId();
        workspace = ampTeamMember.getAmpTeam();
    }



    public Long getTeamMemberRole() {
        return teamMemberRole;
    }

    public void setTeamMemberRole(Long teamMemberRole) {
        this.teamMemberRole = teamMemberRole;
    }

    public AmpTeam getWorkspace() {
        return workspace;
    }

    public void setWorkspace(AmpTeam workspace) {
        this.workspace = workspace;
    }

}

