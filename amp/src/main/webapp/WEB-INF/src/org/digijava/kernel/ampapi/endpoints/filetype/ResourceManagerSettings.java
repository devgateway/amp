package org.digijava.kernel.ampapi.endpoints.filetype;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ResourceManagerSettings {
    
    @ApiModelProperty(value = "A list of allowed file types", example = "[msword, msexcel, csv]")
    private List<String> allowedFileType = new ArrayList<>();
    
    @ApiModelProperty(value = "The settings of the resource manager")
    private ResourceSettings resourceSettings;
    
    public List<String> getAllowedFileType() {
        return allowedFileType;
    }
    
    public void setAllowedFileType(List<String> allowedFileType) {
        this.allowedFileType = allowedFileType;
    }
    
    public ResourceSettings getResourceSettings() {
        return resourceSettings;
    }
    
    public void setResourceSettings(ResourceSettings resourceSettings) {
        this.resourceSettings = resourceSettings;
    }
}
