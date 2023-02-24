package org.digijava.kernel.geocoding.service;

import org.digijava.module.aim.dbentity.AmpTeamMember;

/**
 * @author Octavian Ciubotaru
 */
public class GeoCodingNotAvailableException extends RuntimeException {

    /**
     * The other team member that is currently in geo coding process.
     */
    private final AmpTeamMember teamMember;

    public GeoCodingNotAvailableException(AmpTeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public AmpTeamMember getTeamMember() {
        return teamMember;
    }
}
