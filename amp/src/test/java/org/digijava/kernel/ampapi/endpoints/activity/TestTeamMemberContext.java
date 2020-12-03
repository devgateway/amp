package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;

public class TestTeamMemberContext {
    
    private boolean addActivity;
    
    private boolean editActivity;
    
    private User user;
    
    private AmpTeam team;
    
    private AmpTeamMember ampTeamMember;
    
    private TeamMember teamMember;
    
    public TestTeamMemberContext(boolean addActivity, boolean editActivity, User user, AmpTeam team,
                                 AmpTeamMember ampTeamMember, TeamMember teamMember) {
        this.addActivity = addActivity;
        this.editActivity = editActivity;
        this.user = user;
        this.team = team;
        this.ampTeamMember = ampTeamMember;
        this.teamMember = teamMember;
    }
    
    public boolean isAddActivity() {
        return addActivity;
    }
    
    public boolean isEditActivity() {
        return editActivity;
    }
    
    public User getUser() {
        return user;
    }
    
    public AmpTeam getTeam() {
        return team;
    }
    
    public AmpTeamMember getAmpTeamMember() {
        return ampTeamMember;
    }
    
    public TeamMember getTeamMember() {
        return teamMember;
    }
}
