package org.digijava.module.aim.dbentity;

import java.util.Date;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_ME_INDICATOR_VALUE")
@Deprecated
public class AmpMEIndicatorValue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ME_INDICATOR_VALUE_seq")
    @SequenceGenerator(name = "AMP_ME_INDICATOR_VALUE_seq", sequenceName = "AMP_ME_INDICATOR_VALUE_seq", allocationSize = 1)
    @Column(name = "amp_me_indicator_val_id")
    private Long ampMeIndValId;

    @Column(name = "base_val")
    private Float baseVal;

    @Column(name = "actual_val")
    private Float actualVal;

    @Column(name = "target_val")
    private Float targetVal;

    @Column(name = "revised_target_val")
    private Float revisedTargetVal;

    @Column(name = "base_val_date")
    private Date baseValDate;

    @Column(name = "actual_val_date")
    private Date actualValDate;

    @Column(name = "target_val_date")
    private Date targetValDate;

    @Column(name = "revised_target_val_date")
    private Date revisedTargetValDate;

    @Column(name = "base_val_comments")
    private String baseValComments;

    @Column(name = "actual_val_comments")
    private String actualValComments;

    @Column(name = "target_val_comments")
    private String targetValComments;

    @Column(name = "revised_target_val_comments")
    private String revisedTargetValComments;

    @ManyToOne
    @JoinColumn(name = "indicators_category")
    private AmpCategoryValue indicatorsCategory;

    @Column(name = "Comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "amp_activity_id")
    private AmpActivityVersion activityId;

    @ManyToOne
    @JoinColumn(name = "indicator_id")
    private AmpIndicator indicator;

    @ManyToOne
    @JoinColumn(name = "risk")
    private AmpIndicatorRiskRatings risk;

    private AmpMEIndicators meIndicatorId;



    
    /**
     * @return Returns the activityId.
     */
    public AmpActivityVersion getActivityId() {
        return activityId;
    }
    /**
     * @param activityId The activityId to set.
     */
    public void setActivityId(AmpActivityVersion activityId) {
        this.activityId = activityId;
    }
    /**
     * @return Returns the ampMeIndValId.
     */
    public Long getAmpMeIndValId() {
        return ampMeIndValId;
    }
    /**
     * @param ampMeIndValId The ampMeIndValId to set.
     */
    public void setAmpMeIndValId(Long ampMeIndValId) {
        this.ampMeIndValId = ampMeIndValId;
    }
    /**
     * @return Returns the baseValDate.
     */
    public Date getBaseValDate() {
        return baseValDate;
    }
    /**
     * @param baseValDate The baseValDate to set.
     */
    public void setBaseValDate(Date baseValDate) {
        this.baseValDate = baseValDate;
    }
    /**
     * @return Returns the meIndicatorId.
     */
    public AmpMEIndicators getMeIndicatorId() {
        return meIndicatorId;
    }
    /**
     * @param meIndicatorId The meIndicatorId to set.
     */
    public void setMeIndicatorId(AmpMEIndicators meIndicatorId) {
        this.meIndicatorId = meIndicatorId;
    }
    /**
     * @return Returns the risk.
     */
    public AmpIndicatorRiskRatings getRisk() {
        return risk;
    }
    /**
     * @param risk The risk to set.
     */
    public void setRisk(AmpIndicatorRiskRatings risk) {
        this.risk = risk;
    }
    /**
     * @return Returns the targetValDate.
     */
    public Date getTargetValDate() {
        return targetValDate;
    }
    /**
     * @param targetValDate The targetValDate to set.
     */
    public void setTargetValDate(Date targetValDate) {
        this.targetValDate = targetValDate;
    }
    /**
     * @return Returns the baseVal.
     */
    public Float getBaseVal() {
        return baseVal;
    }
    /**
     * @param baseVal The baseVal to set.
     */
    public void setBaseVal(Float baseVal) {
        this.baseVal = baseVal;
    }
    /**
     * @return Returns the targetVal.
     */
    public Float getTargetVal() {
        return targetVal;
    }
    /**
     * @param targetVal The targetVal to set.
     */
    public void setTargetVal(Float targetVal) {
        this.targetVal = targetVal;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    /**
     * @return Returns the actualVal.
     */
    public Float getActualVal() {
        return actualVal;
    }
    /**
     * @param actualVal The actualVal to set.
     */
    public void setActualVal(Float actualVal) {
        this.actualVal = actualVal;
    }
    /**
     * @return Returns the actualValDate.
     */
    public Date getActualValDate() {
        return actualValDate;
    }
    /**
     * @param actualValDate The actualValDate to set.
     */
    public void setActualValDate(Date actualValDate) {
        this.actualValDate = actualValDate;
    }
    /**
     * @return Returns the revisedTargetVal.
     */
    public Float getRevisedTargetVal() {
        return revisedTargetVal;
    }
    /**
     * @param revisedTargetVal The revisedTargetVal to set.
     */
    public void setRevisedTargetVal(Float revisedTargetVal) {
        this.revisedTargetVal = revisedTargetVal;
    }
    /**
     * @return Returns the revisedTargetValDate.
     */
    public Date getRevisedTargetValDate() {
        return revisedTargetValDate;
    }
    /**
     * @param revisedTargetValDate The revisedTargetValDate to set.
     */
    public void setRevisedTargetValDate(Date revisedTargetValDate) {
        this.revisedTargetValDate = revisedTargetValDate;
    }
    /**
     * @return Returns the actualValComments.
     */
    public String getActualValComments() {
        return actualValComments;
    }
    /**
     * @param actualValComments The actualValComments to set.
     */
    public void setActualValComments(String actualValComments) {
        this.actualValComments = actualValComments;
    }
    /**
     * @return Returns the baseValComments.
     */
    public String getBaseValComments() {
        return baseValComments;
    }
    /**
     * @param baseValComments The baseValComments to set.
     */
    public void setBaseValComments(String baseValComments) {
        this.baseValComments = baseValComments;
    }
    /**
     * @return Returns the revisedTargetValComments.
     */
    public String getRevisedTargetValComments() {
        return revisedTargetValComments;
    }
    /**
     * @param revisedTargetValComments The revisedTargetValComments to set.
     */
    public void setRevisedTargetValComments(String revisedTargetValComments) {
        this.revisedTargetValComments = revisedTargetValComments;
    }
    /**
     * @return Returns the targetValComments.
     */
    public String getTargetValComments() {
        return targetValComments;
    }
    /**
     * @param targetValComments The targetValComments to set.
     */
    public void setTargetValComments(String targetValComments) {
        this.targetValComments = targetValComments;
    }
/*  public Long getLogframeValueId() {
        return logframeValueId;
    }
    public void setLogframeValueId(Long logframeValueId) {
        this.logframeValueId = logframeValueId;
    }*/
    public AmpCategoryValue getIndicatorsCategory() {
        return indicatorsCategory;
    }
    public void setIndicatorsCategory(AmpCategoryValue indicatorsCategory) {
        this.indicatorsCategory = indicatorsCategory;
    }
    public AmpIndicator getIndicator() {
        return indicator;
    }
    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }
}
