package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class DonorIdsWrapper {

    private List<Long> donorIds;

    public List<Long> getDonorIds() {
        return donorIds;
    }

    public void setDonorIds(List<Long> donorIds) {
        this.donorIds = donorIds;
    }
}
