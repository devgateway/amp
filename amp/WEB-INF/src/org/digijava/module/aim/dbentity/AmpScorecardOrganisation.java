package org.digijava.module.aim.dbentity;

import java.util.Date;


/***
 * Class to identify a donor that has explicitly declared that doesn't have project updates 
 * on a given quarter. 
 * Marking a donor as having no updates is only valid for a given quarter.
 * 
 * @author Emanuel Perez
 *
 */
public class AmpScorecardOrganisation {

    private Long ampDonorId;
    private Long id;
    private Date modifyDate;
    private boolean toExclude;

    
    /**
     * Returns the id of the donor that doesn't have activity/projects updates on a given quarter
     * 
     * @return the id of the donor 
     */
    public Long getAmpDonorId() {
        return ampDonorId;
    }
    
    /**
     * Sets the id of the donor that doesn't have activity/projects updates on a given quarter
     * 
     * @param ampDonorId, the donor id
     */
    public void setAmpDonorId(Long ampDonorId) {
        this.ampDonorId = ampDonorId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Returns the date where the donor was identified has having no updates. 
     * The date is used to calculate to which quarter the donor is marked as having no updates.
     * 
     * @return the date where the donor was set as having no updates
     */
    public Date getModifyDate() {
        return modifyDate;
    }
    
    /**
     * Sets the date where the donor was identified has having no updates. 
     * The date is used to calculate to which quarter the donor is marked as having no updates.
     * 
     * @param modifyDate the date where the donor was set as having no updates
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public boolean isToExclude() {
        return toExclude;
    }

    public void setToExclude(boolean toExclude) {
        this.toExclude = toExclude;
    }

}
