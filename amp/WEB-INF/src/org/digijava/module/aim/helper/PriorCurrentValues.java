package org.digijava.module.aim.helper;

public class PriorCurrentValues
{
    private Long currHistoryId;
    private float currValue;
    private String currValDate;
    private String comments;
    
    /**
     * @return Returns the currHistoryId.
     */
    public Long getCurrHistoryId() {
        return currHistoryId;
    }
    /**
     * @param currHistoryId The currHistoryId to set.
     */
    public void setCurrHistoryId(Long currHistoryId) {
        this.currHistoryId = currHistoryId;
    }
    /**
     * @return Returns the currValDate.
     */
    public String getCurrValDate() {
        return currValDate;
    }
    /**
     * @param currValDate The currValDate to set.
     */
    public void setCurrValDate(String currValDate) {
        this.currValDate = currValDate;
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
