package org.digijava.kernel.ampapi.endpoints.security.dto;

import io.swagger.annotations.ApiModelProperty;

public class WorkspaceInfo {
    
    private Long id;
    
    @ApiModelProperty(example = "Test workspace")
    private String name;
    
    public WorkspaceInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
