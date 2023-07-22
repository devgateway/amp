package org.digijava.module.aim.dbentity;

import java.util.Date;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_ME_CURR_VAL_HISTORY")
@Deprecated
public class AmpMECurrValHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ME_CURR_VAL_HIST_seq")
    @SequenceGenerator(name = "AMP_ME_CURR_VAL_HIST_seq", sequenceName = "AMP_ME_CURR_VAL_HIST_seq", allocationSize = 1)
    @Column(name = "amp_me_curr_val_history_id")
    private Long ampMECurrValHistoryId;

    @Column(name = "curr_val")
    private Float currValue;

    @Column(name = "curr_val_date")
    private Date currValueDate;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "me_ind_value", referencedColumnName = "indicator_id")
    private AmpIndicator meIndValue;


    /**
     * @return Returns the ampMECurrValHistoryId.
     */
    public Long getAmpMECurrValHistoryId() {
        return ampMECurrValHistoryId;
    }
    /**
     * @param ampMECurrValHistoryId The ampMECurrValHistoryId to set.
     */
    public void setAmpMECurrValHistoryId(Long ampMECurrValHistoryId) {
        this.ampMECurrValHistoryId = ampMECurrValHistoryId;
    }
    /**
     * @return Returns the currValue.
     */
    public float getCurrValue() {
        return currValue;
    }
    /**
     * @param currValue The currValue to set.
     */
    public void setCurrValue(float currValue) {
        this.currValue = currValue;
    }
    /**
     * @return Returns the currValueDate.
     */
    public Date getCurrValueDate() {
        return currValueDate;
    }
    /**
     * @param currValueDate The currValueDate to set.
     */
    public void setCurrValueDate(Date currValueDate) {
        this.currValueDate = currValueDate;
    }
    /**
     * @return Returns the meIndValue.
     */
    public AmpIndicator getMeIndValue() {
        return meIndValue;
    }
    /**
     * @param meIndValue The meIndValue to set.
     */
    public void setMeIndValue(AmpIndicator meIndValue) {
        this.meIndValue = meIndValue;
    }
    /**
     * @return Returns the comments.
     */
    public String getComments() {
        return comments;
    }
    /**
     * @param comments The comments to set.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
}
