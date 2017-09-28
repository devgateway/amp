package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;

@Deprecated
public class AmpThemeIndicatorValue implements Comparable, Serializable 
{
    private Long ampThemeIndValId;
    private int valueType;
    private Double valueAmount;
    private Date creationDate;
    private AmpThemeIndicators themeIndicatorId;
    private AmpIndicator indicatorId;
    /**
     * @return Returns the ampThemeIndValId.
     */
    public Long getAmpThemeIndValId() {
        return ampThemeIndValId;
    }
    /**
     * @param ampThemeIndValId The ampThemeIndValId to set.
     */
    public void setAmpThemeIndValId(Long ampThemeIndValId) {
        this.ampThemeIndValId = ampThemeIndValId;
    }
    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * @return Returns the themeIndicatorId.
     */
    public AmpThemeIndicators getThemeIndicatorId() {
        return themeIndicatorId;
    }
    /**
     * @param themeIndicatorId The themeIndicatorId to set.
     */
    public void setThemeIndicatorId(AmpThemeIndicators themeIndicatorId) {
        this.themeIndicatorId = themeIndicatorId;
    }
    /**
     * @return Returns the valueAmount.
     */
    public Double getValueAmount() {
        return FeaturesUtil.applyThousandsForVisibility(valueAmount);
    }
    /**
     * @param valueAmount The valueAmount to set.
     */
    public void setValueAmount(Double valueAmount) {
        this.valueAmount = FeaturesUtil.applyThousandsForEntry(valueAmount);
    }
    /**
     * @return Returns the valueType.
     */
    public int getValueType() {
        return valueType;
    }
    /**
     * @param valueType The valueType to set.
     */
    public void setValueType(int valueType) {
        this.valueType = valueType;
    }
    public int compareTo(Object obj) {
        AmpThemeIndicatorValue other = (AmpThemeIndicatorValue) obj;
        return this.getCreationDate().compareTo(other.getCreationDate());
    }
    public AmpIndicator getIndicatorId() {
        return indicatorId;
    }
    public void setIndicatorId(AmpIndicator indicatorId) {
        this.indicatorId = indicatorId;
    }
}






