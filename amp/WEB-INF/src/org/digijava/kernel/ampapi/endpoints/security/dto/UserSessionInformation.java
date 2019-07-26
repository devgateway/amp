package org.digijava.kernel.ampapi.endpoints.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class UserSessionInformation {
    
    @JsonProperty("token")
    @ApiModelProperty(example = "34bf1c55-f2d1-43bb-bef4-e98b6077f66f")
    private String token;
    
    @JsonProperty("token-expiration")
    @ApiModelProperty(value = "The expiration time in ms", example = "1546859700327")
    private Long tokenExpiration;
    
    @JsonProperty("url")
    @ApiModelProperty(value = "The login URL", example = "http://localhost:8080/showLayout.do?layout=login")
    private String url;
    
    @JsonProperty("team")
    @ApiModelProperty(value = "Selected workspace name", example = "Espace de Travail Cellule Technique du COMOREX")
    private String teamName;
    
    @JsonProperty("user-id")
    private Long userId;
    
    @JsonProperty("user-name")
    @ApiModelProperty(example = "atl@amp.org")
    private String userName;
    
    @JsonProperty("is-admin")
    private Boolean admin;
    
    @JsonProperty("add-activity")
    private Boolean addActivity;
    
    @JsonProperty("view-activity")
    private Boolean viewActivity;
    
    @JsonProperty("national-coordinator")
    @ApiModelProperty("Clarifies if user is part of the national coordinator group")
    private Boolean nationalCoordinator;
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Long getTokenExpiration() {
        return tokenExpiration;
    }
    
    public void setTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Boolean getAdmin() {
        return admin;
    }
    
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    
    public Boolean getAddActivity() {
        return addActivity;
    }
    
    public void setAddActivity(Boolean addActivity) {
        this.addActivity = addActivity;
    }
    
    public Boolean getViewActivity() {
        return viewActivity;
    }
    
    public void setViewActivity(Boolean viewActivity) {
        this.viewActivity = viewActivity;
    }
    
    public Boolean getNationalCoordinator() {
        return nationalCoordinator;
    }
    
    public void setNationalCoordinator(Boolean nationalCoordinator) {
        this.nationalCoordinator = nationalCoordinator;
    }
}
