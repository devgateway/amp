package org.digijava.module.aim.helper ;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AllPrgIndicators implements Serializable
{
    private Long indicatorId;
    private String name;
    private String code;
    private String type;
    private String creationDate;
    private String description;
    private int valueType;
    private int category;
    private boolean npIndicator;
    private Collection themeIndValues;
    private Collection themes;
    private Collection sector;
    private Collection activity;
    private Long activityId;
    private String sectorName;
    
    private Float baseVal;  // BASE
    private Date baseValDate;
    private String baseValComments;
    private Float actualVal;  // ACTUAL
    private Date actualValDate;
    private String actualValComments;
    private Float targetVal; // TARGET
    private Date targetValDate;
    private String targetValComments;
    private Float revisedTargetVal; // Revised TARGET
    private String revisedTargetValComments;
    private Date revisedTargetValDate;
    private String comments;
    private String currentVal;
    private Date currentValDate;
    private String currentValComments;
    
    private AmpCategoryValue indicatorsCategory;

    private AmpIndicatorRiskRatings risk;
    
    
    
    /**
     * @return Returns the category.
     */
    public int getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(int category) {
        this.category = category;
    }
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return Returns the creationDate.
     */
    public String getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the indicatorId.
     */
    public Long getIndicatorId() {
        return indicatorId;
    }
    /**
     * @param indicatorId The indicatorId to set.
     */
    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the npIndicator.
     */
    public boolean isNpIndicator() {
        return npIndicator;
    }
    /**
     * @param npIndicator The npIndicator to set.
     */
    public void setNpIndicator(boolean npIndicator) {
        this.npIndicator = npIndicator;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
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
    /**
     * @return Returns the themeIndValues.
     */
    public Collection getThemeIndValues() {
        return themeIndValues;
    }
    public Collection getThemes() {
        return themes;
    }

    /**
     * @param themeIndValues The themeIndValues to set.
     */
    public void setThemeIndValues(Collection themeIndValues) {
        this.themeIndValues = themeIndValues;
    }
    public void setThemes(Collection themes) {
        this.themes = themes;
    }
    public Collection getSector() {
        return sector;
    }
    public void setSector(Collection sector) {
        this.sector = sector;
    }
    public String getSectorName() {
        return sectorName;
    }
    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }
    public Collection getActivity() {
        return activity;
    }
    public void setActivity(Collection activity) {
        this.activity = activity;
    }
    public Float getBaseVal() {
        return baseVal;
    }
    public void setBaseVal(Float baseVal) {
        this.baseVal = baseVal;
    }
    public Date getBaseValDate() {
        return baseValDate;
    }
    public void setBaseValDate(Date baseValDate) {
        this.baseValDate = baseValDate;
    }
    public String getBaseValComments() {
        return baseValComments;
    }
    public void setBaseValComments(String baseValComments) {
        this.baseValComments = baseValComments;
    }
    public Float getActualVal() {
        return actualVal;
    }
    public void setActualVal(Float actualVal) {
        this.actualVal = actualVal;
    }
    public Date getActualValDate() {
        return actualValDate;
    }
    public void setActualValDate(Date actualValDate) {
        this.actualValDate = actualValDate;
    }
    public String getActualValComments() {
        return actualValComments;
    }
    public void setActualValComments(String actualValComments) {
        this.actualValComments = actualValComments;
    }
    public Float getTargetVal() {
        return targetVal;
    }
    public void setTargetVal(Float targetVal) {
        this.targetVal = targetVal;
    }
    public Date getTargetValDate() {
        return targetValDate;
    }
    public void setTargetValDate(Date targetValDate) {
        this.targetValDate = targetValDate;
    }
    public String getTargetValComments() {
        return targetValComments;
    }
    public void setTargetValComments(String targetValComments) {
        this.targetValComments = targetValComments;
    }
    public Float getRevisedTargetVal() {
        return revisedTargetVal;
    }
    public void setRevisedTargetVal(Float revisedTargetVal) {
        this.revisedTargetVal = revisedTargetVal;
    }
    public String getRevisedTargetValComments() {
        return revisedTargetValComments;
    }
    public void setRevisedTargetValComments(String revisedTargetValComments) {
        this.revisedTargetValComments = revisedTargetValComments;
    }
    public Date getRevisedTargetValDate() {
        return revisedTargetValDate;
    }
    public void setRevisedTargetValDate(Date revisedTargetValDate) {
        this.revisedTargetValDate = revisedTargetValDate;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public AmpCategoryValue getIndicatorsCategory() {
        return indicatorsCategory;
    }
    public void setIndicatorsCategory(AmpCategoryValue indicatorsCategory) {
        this.indicatorsCategory = indicatorsCategory;
    }
    public AmpIndicatorRiskRatings getRisk() {
        return risk;
    }
    public void setRisk(AmpIndicatorRiskRatings risk) {
        this.risk = risk;
    }
    public String getCurrentVal() {
        return currentVal;
    }
    public void setCurrentVal(String currentVal) {
        this.currentVal = currentVal;
    }
    public Date getCurrentValDate() {
        return currentValDate;
    }
    public void setCurrentValDate(Date currentValDate) {
        this.currentValDate = currentValDate;
    }
    public String getCurrentValComments() {
        return currentValComments;
    }
    public void setCurrentValComments(String currentValComments) {
        this.currentValComments = currentValComments;
    }
    public Long getActivityId() {
        return activityId;
    }
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
