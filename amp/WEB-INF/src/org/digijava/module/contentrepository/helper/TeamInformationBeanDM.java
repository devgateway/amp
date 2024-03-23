package org.digijava.module.contentrepository.helper;

import org.digijava.module.aim.helper.TeamMember;

import java.util.Collection;

public class TeamInformationBeanDM {
    private boolean isTeamLeader                    = false;
    private TeamMember meTeamMember             = null;
    private Collection<TeamMember> myTeamMembers    = null;
    
    public boolean getIsTeamLeader() {
        return isTeamLeader;
    }
    public void setIsTeamLeader(boolean isTeamLeader) {
        this.isTeamLeader = isTeamLeader;
    }
    public TeamMember getMeTeamMember() {
        return meTeamMember;
    }
    public void setMeTeamMember(TeamMember meTeamMember) {
        this.meTeamMember = meTeamMember;
    }
    public Collection<TeamMember> getMyTeamMembers() {
        return myTeamMembers;
    }
    public void setMyTeamMembers(Collection<TeamMember> myTeamMembers) {
        this.myTeamMembers = myTeamMembers;
    }
}
