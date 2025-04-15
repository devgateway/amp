/*
 * DonorTeam.java
 * Created : 06-Feb-2006
 */

package org.digijava.module.aim.helper;

public class DonorTeam {
    private Long teamId;
    private String teamName;
    private Long teamMeberId;
    private String teamMemberName;
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
     * @return Returns the teamMeberId.
     */
    public Long getTeamMeberId() {
        return teamMeberId;
    }
    /**
     * @param teamMeberId The teamMeberId to set.
     */
    public void setTeamMeberId(Long teamMeberId) {
        this.teamMeberId = teamMeberId;
    }
    /**
     * @return Returns the teamMemberName.
     */
    public String getTeamMemberName() {
        return teamMemberName;
    }
    /**
     * @param teamMemberName The teamMemberName to set.
     */
    public void setTeamMemberName(String teamMemberName) {
        this.teamMemberName = teamMemberName;
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
}
