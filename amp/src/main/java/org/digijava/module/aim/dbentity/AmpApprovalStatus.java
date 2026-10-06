/*
 * Created on 8/12/2005
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

public class AmpApprovalStatus {

    private Long ampApprovalStatusId;
    private String approvalStatus;
    
    /**
     * @return Returns the ampApprovalStatusId.
     */
    public Long getAmpApprovalStatusId() {
        return ampApprovalStatusId;
    }
    /**
     * @param ampApprovalStatusId The ampApprovalStatusId to set.
     */
    public void setAmpApprovalStatusId(Long ampApprovalStatusId) {
        this.ampApprovalStatusId = ampApprovalStatusId;
    }
    /**
     * @return Returns the approvalStatus.
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }
    /**
     * @param approvalStatus The approvalStatus to set.
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
