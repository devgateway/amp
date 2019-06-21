package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FMSettingsResult<T> {
    
    @JsonProperty(EPConstants.REPORTING_FIELDS)
    @ApiModelProperty(value = "list of reporting fields", example = "[\"Project Title\", \"Primary Sector\", ...]")
    private Set<String> reportingFields;
    
    @JsonProperty(EPConstants.ENABLED_MODULES)
    @ApiModelProperty(value = "list of enable modules", example = "[\"GIS\", \"Dashboards\", ...]")
    private Set<String> enabledModules;
    
    @JsonProperty("fm-settings")
    @ApiModelProperty(value = "list of FM settings")
    private T fmSettings;
    
    @ApiModelProperty(value = "Message if an error occurs")
    private String error;
    
    public Set<String> getReportingFields() {
        return reportingFields;
    }
    
    public void setReportingFields(Set<String> reportingFields) {
        this.reportingFields = reportingFields;
    }
    
    public Set<String> getEnabledModules() {
        return enabledModules;
    }
    
    public void setEnabledModules(Set<String> enabledModules) {
        this.enabledModules = enabledModules;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public T getFmSettings() {
        return fmSettings;
    }
    
    public void setFmSettings(T fmSettings) {
        this.fmSettings = fmSettings;
    }
    
}
