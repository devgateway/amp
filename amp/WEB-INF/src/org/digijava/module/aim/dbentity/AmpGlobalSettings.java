/*
* AMP GLOBAL SETTINGS 
*/
package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.config.utils.ConfigHelper;
import org.digijava.module.aim.helper.KeyValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmpGlobalSettings implements Serializable {
    
    @JsonIgnore
    private Long globalId;
    
    @JsonProperty("settingName")
    @ApiModelProperty(value = "the name of the global settings", example = "Link Mode of Payment to Funding Status")
    private String globalSettingsName;
    
    @JsonProperty("settingValue")
    @ApiModelProperty(value = "the current value of the settings", example = "false")
    private String globalSettingsValue;
    
    @JsonProperty("possibleValues")
    @ApiModelProperty(value = "the type of possible values",
            allowableValues = "t_Boolean, t_Integer, t_Double, t_year_default_start, t_year_default_end, "
                    + "t_static_range, t_static_year, t_audit_trial_clenaup, t_components_sort, "
                    + "t_daily_currency_update_hour, t_timeout_currency_update, v_g_settings_activity_statusess",
            example = "t_Boolean")
    private String globalSettingsPossibleValues;
    
    @JsonProperty("description")
    @ApiModelProperty(example = "Link Mode of Payment to Funding Status Description")
    private String globalSettingsDescription; //a description that will appear on mouseover
    
    @JsonProperty("section")
    @ApiModelProperty(example = "funding")
    private String section;
    
    @JsonIgnore
    private transient String[] listOfValues;
    
    @JsonProperty("valueTranslatable")
    @ApiModelProperty("if the global settings has translations")
    private Boolean valueTranslatable;
    
    @JsonProperty("possibleValuesIds")
    @ApiModelProperty("possible values of the current setting in a { \"name1\" : \"value1\", ...} format")
    private Map<String, String> possibleValuesIds = new HashMap<>();
    
    @JsonIgnore
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
    
    public Map<String, String> getPossibleValuesIds() {
        List<KeyValue> possibleValues = ConfigHelper.getPossibleValues(globalSettingsPossibleValues);
        if (possibleValues != null) {
            for (KeyValue value : possibleValues) {
                possibleValuesIds.put(value.getValue(), value.getKey());
            }
        }
        
        return possibleValuesIds;
    }

    public Boolean isInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }
    
    public void updateValuesFromSetting(AmpGlobalSettings setting) {
        this.globalSettingsName = setting.getGlobalSettingsName();
        this.globalSettingsValue = setting.getGlobalSettingsValue();
        this.globalSettingsDescription = setting.getGlobalSettingsDescription();
        this.section = setting.getSection();
        this.valueTranslatable = setting.getValueTranslatable();
        
        if ("null".equalsIgnoreCase(setting.getGlobalSettingsPossibleValues())) {
            this.globalSettingsPossibleValues = "";
        } else {
            this.globalSettingsPossibleValues = setting.getGlobalSettingsPossibleValues();
        }
    }
}
