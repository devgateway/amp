/*
* AMP GLOBAL SETTINGS 
*/
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGlobalSettings implements Serializable {
    private Long globalId;
    private String globalSettingsName;
    private String globalSettingsValue;
    private String globalSettingsPossibleValues; //the name of the database table/view containing the values
    private String globalSettingsDescription; //a description that will appear on mouseover
    private String section;
    private transient String[] listOfValues;
    private Boolean valueTranslatable;
    
    private Boolean internal;
    
    public Boolean getValueTranslatable() {
        return valueTranslatable;
    }
    public void setValueTranslatable(Boolean valueTranslatable) {
        this.valueTranslatable = valueTranslatable;
    }
    /**
     * @return Returns the globalSettingsName.
     */
    public String getGlobalSettingsName() {
        return globalSettingsName;
    }
    /**
     * @param globalSettingsName The globalSettingsName to set.
     */
    public void setGlobalSettingsName(String globalSettingsName) {
        this.globalSettingsName = globalSettingsName;
    }
    /**
     * @return Returns the globalSettingsValue.
     */
    public String getGlobalSettingsValue() {
        return globalSettingsValue;
    }
    /**
     * @param globalSettingsValue The globalSettingsValue to set.
     */
    public void setGlobalSettingsValue(String globalSettingsValue) {
        this.globalSettingsValue = globalSettingsValue;
    }
    /**
     * @return Returns the globalId.
     */
    public Long getGlobalId() {
        return globalId;
    }
    /**
     * @param globalId The globalId to set.
     */
    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }
    public String getGlobalSettingsPossibleValues() {
        return globalSettingsPossibleValues;
    }
    public void setGlobalSettingsPossibleValues(String globalSettingsPossibleValues) {
        this.globalSettingsPossibleValues = globalSettingsPossibleValues;
    }
    public String getGlobalSettingsDescription() {
        return globalSettingsDescription;
    }
    public void setGlobalSettingsDescription(String globalSettingsDescription) {
        this.globalSettingsDescription = globalSettingsDescription;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public String getSection() {
        return section;
    }
    @Override
    public String toString () {
        return this.globalSettingsName;
    }
    public String[] getListOfValues() {
        return listOfValues;
    }
    public void setListOfValues(String[] listOfValues) {
        this.listOfValues = listOfValues;
    }

    public Boolean isInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }
    
}
