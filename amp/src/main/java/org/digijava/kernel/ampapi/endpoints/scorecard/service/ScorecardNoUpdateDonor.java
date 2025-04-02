package org.digijava.kernel.ampapi.endpoints.scorecard.service;

/***
 * Class to identify a donor. Used as model to retrieve/send data. Used just in Scorecard Manager (admin section)
 * AMP-20002
 * 
 * @author Viorel Chihai
 */

public class ScorecardNoUpdateDonor {

    private Long ampDonorId;
    private String name;

    public Long getAmpDonorId() {
        return ampDonorId;
    }

    public void setAmpDonorId(Long ampDonorId) {
        this.ampDonorId = ampDonorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
