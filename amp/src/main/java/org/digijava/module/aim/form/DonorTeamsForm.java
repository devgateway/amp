/*
 * DonorTeamsForm.java
 * Created : 06-Feb-2006
 */

package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class DonorTeamsForm extends ActionForm {
    
    private Collection donorTeams;
    private Long teamId;

    /**
     * @return Returns the donorTeams.
     */
    public Collection getDonorTeams() {
        return donorTeams;
    }

    /**
     * @param donorTeams The donorTeams to set.
     */
    public void setDonorTeams(Collection donorTeams) {
        this.donorTeams = donorTeams;
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
}
