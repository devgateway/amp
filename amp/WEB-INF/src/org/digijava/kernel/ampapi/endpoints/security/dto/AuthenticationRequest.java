package org.digijava.kernel.ampapi.endpoints.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class AuthenticationRequest {
    
    @JsonProperty("username")
    @ApiModelProperty(required = true, example = "atltest@amp.org")
    private String userName;
    
    @JsonProperty("password")
    @ApiModelProperty(required = true, example = "a7848b4c1b75cb7bb7449069fe0e114b730c0423")
    private String password;
    
    @JsonProperty("workspaceId")
    private Integer workspaceId;
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Integer getWorkspaceId() {
        return workspaceId;
    }
    
    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }
}
