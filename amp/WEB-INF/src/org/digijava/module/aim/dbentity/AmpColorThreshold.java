/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.math.BigDecimal;

/**
 * Simple Heat Colors and Threshold storage
 * 
 * @author Nadejda Mandrescu
 */
public class AmpColorThreshold {
    
    private Long ampColorThresholdId;
    private String colorName;
    private String colorHash;
    private BigDecimal thresholdStart;
    private Integer index;
    
    /**
     * @return the ampColorThresholdId
     */
    public Long getAmpColorThresholdId() {
        return ampColorThresholdId;
    }
    
    /**
     * @param ampColorThresholdId the ampColorThresholdId to set
     */
    public void setAmpColorThresholdId(Long ampColorThresholdId) {
        this.ampColorThresholdId = ampColorThresholdId;
    }
    

    /**
     * @return the colorName
     */
    public String getColorName() {
        return colorName;
    }

    /**
     * @param colorName the colorName to set
     */
    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    /**
     * @return the colorHash
     */
    public String getColorHash() {
        return colorHash;
    }

    /**
     * @param colorHash the colorHash to set
     */
    public void setColorHash(String colorHash) {
        this.colorHash = colorHash;
    }

    /**
     * @return the thresholdStart
     */
    public BigDecimal getThresholdStart() {
        return thresholdStart;
    }

    /**
     * @param thresholdStart the thresholdStart to set
     */
    public void setThresholdStart(BigDecimal thresholdStart) {
        this.thresholdStart = thresholdStart;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }
    
    public String toString() {
        return String.format("[%d] %s (%s) from %s (idx = %d)", ampColorThresholdId, colorName, colorHash, 
                thresholdStart, index);
    }

}
