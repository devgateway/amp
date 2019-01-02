package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class FilteredDonors {

    @ApiModelProperty("the list of the filtered donors")
    private final List<Donor> allFilteredDonors;

    @ApiModelProperty("the list of noupdate donors")
    private final List<Donor> noUpdatesFilteredDonors;

    public FilteredDonors(
            List<Donor> allFilteredDonors,
            List<Donor> noUpdatesFilteredDonors) {
        this.allFilteredDonors = allFilteredDonors;
        this.noUpdatesFilteredDonors = noUpdatesFilteredDonors;
    }

    public List<Donor> getAllFilteredDonors() {
        return allFilteredDonors;
    }

    public List<Donor> getNoUpdatesFilteredDonors() {
        return noUpdatesFilteredDonors;
    }
}
