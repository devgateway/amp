package org.digijava.kernel.ampapi.endpoints.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.dgfoundation.amp.visibility.data.FMSettingsMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FMSettingsConfig {
    
    @JsonProperty(EPConstants.FULL_ENABLED_PATHS)
    @ApiModelProperty(value = "if only fully enabled paths to detail", example = "true")
    private Boolean fullEnabledPaths = true;
    
    @JsonProperty(EPConstants.REPORTING_FIELDS)
    @ApiModelProperty(value = "to list or not reporting fields", example = "false")
    private Boolean reportingFields = false;
    
    @JsonProperty(EPConstants.ENABLED_MODULES)
    @ApiModelProperty(value = "to list or not enabled modules", example = "false")
    private Boolean enabledModules = false;
    
    @JsonIgnore
    private Boolean detailsFlat = false;
    
    @JsonProperty(EPConstants.DETAIL_MODULES)
    @ApiModelProperty(value = "Option1: [\"all-enabled\"] (if present, other entries are skipped)<br>"
            + "Option2: a list of valid module names like [\"GIS\"], or [\"GIS\", \"Dashboards\"]<br>"
            + "Option3: [] (i.e. None, which is the default if omitted))" + ". Example: [\"GIS\",\"Dashboards\"]")
    private List<String> detailModules = new ArrayList<>();
    
    @JsonProperty(EPConstants.FM_PATHS_FILTER)
    @ApiModelProperty(value = "an array of FM paths that are required, applies only to tree structures."
            + "Example: [\"/PROJECT MANAGEMENT/Funding/Funding Information/Delivery rate\"]")
    private List<String> requiredPaths = new ArrayList<>();
    
    public Boolean getFullEnabledPaths() {
        return fullEnabledPaths;
    }
    
    public void setFullEnabledPaths(Boolean fullEnabledPaths) {
        this.fullEnabledPaths = fullEnabledPaths;
    }
    
    public Boolean getReportingFields() {
        return reportingFields;
    }
    
    public void setReportingFields(Boolean reportingFields) {
        this.reportingFields = reportingFields;
    }
    
    public Boolean getEnabledModules() {
        return enabledModules;
    }
    
    public void setEnabledModules(Boolean enabledModules) {
        this.enabledModules = enabledModules;
    }
    
    public Boolean getDetailsFlat() {
        return detailsFlat;
    }
    
    public void setDetailsFlat(Boolean detailsFlat) {
        this.detailsFlat = detailsFlat;
    }
    
    public List<String> getDetailModules() {
        return detailModules;
    }
    
    public void setDetailModules(List<String> detailModules) {
        this.detailModules = detailModules;
    }
    
    public List<String> getRequiredPaths() {
        return requiredPaths;
    }
    
    public void setRequiredPaths(List<String> requiredPaths) {
        this.requiredPaths = requiredPaths;
    }
    
    @JsonIgnore
    public Set<String> getAllowedModules(Long templateId) {
        if (fullEnabledPaths) {
            return FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES, templateId);
        }
        
        return FMSettingsMediator.getSettings(FMSettingsMediator.FMGROUP_MODULES, templateId);
    }
    
    @JsonIgnore
    public boolean isValid(Long templateId) {
        if (detailModules != null && !getAllowedModules(templateId).containsAll(detailModules)) {
            return false;
        }
    
        return true;
    }
}
