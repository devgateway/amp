package org.digijava.kernel.ampapi.endpoints.filetype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class ResourceManagerSettings {
    
    
    @ApiModelProperty(value = "A list of allowed file types")
    private List<String> allowedFileType = new ArrayList<>();
    
    @ApiModelProperty(value = "The settings of the resource manager")
    private Map<String, Object> resourceSettings = new HashMap<>();
    
    public List<String> getAllowedFileType() {
        return allowedFileType;
    }
    
    public void setAllowedFileType(List<String> allowedFileType) {
        this.allowedFileType = allowedFileType;
    }
    
    public Map<String, Object> getResourceSettings() {
        return resourceSettings;
    }
    
    public void setResourceSettings(Map<String, Object> resourceSettings) {
        this.resourceSettings = resourceSettings;
    }
}
