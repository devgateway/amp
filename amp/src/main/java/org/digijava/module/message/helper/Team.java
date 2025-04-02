package org.digijava.module.message.helper;

/**
 * Transient storage for AmpTeam
 * 
 * @author jdeanquin
 *
 */
public class Team {
    private Long teamId;
    private String teamName;

    public Team(Long teamId, String teamName) {
        super();
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}
