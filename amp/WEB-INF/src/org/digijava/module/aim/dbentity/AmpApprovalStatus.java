/*
 * Created on 8/12/2005
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;
import javax.persistence.*;

@Entity
@Table(name = "AMP_APPROVAL_STATUS")
public class AmpApprovalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_approval_status_seq_generator")
    @SequenceGenerator(name = "amp_approval_status_seq_generator", sequenceName = "amp_approval_status_seq", allocationSize = 1)
    @Column(name = "amp_approval_status_id")
    private Long ampApprovalStatusId;

    @Column(name = "approval_status")
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
