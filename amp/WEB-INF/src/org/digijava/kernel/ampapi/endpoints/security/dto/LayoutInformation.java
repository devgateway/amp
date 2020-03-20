package org.digijava.kernel.ampapi.endpoints.security.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayoutInformation {
    
    @ApiModelProperty(example = "23.10.2018")
    private String buildDate;
    
    @ApiModelProperty(example = "3.4")
    private String ampVersion;
    
    private Boolean trackingEnabled;
    
    @ApiModelProperty("AMP site id")
    private String siteId;
    
    @ApiModelProperty(example = "https://stats.ampsite.net/")
    private String trackingUrl;
    
    private String footerText;
    
    private List<LayoutAdminLink> adminLinks;
    
    @ApiModelProperty(example = "atl@amp.org")
    private String email;
    
    private Long userId;
    
    @ApiModelProperty(example = "ATL")
    private String firstName;
    
    @ApiModelProperty(example = "ATL")
    private String lastName;
    
    @ApiModelProperty(example = "false")
    private Boolean administratorMode;
    
    @ApiModelProperty(example = "Ministry of Finance")
    private String workspace;
    
    private Long workspaceId;
    
    private Boolean logged;
    
    public String getBuildDate() {
        return buildDate;
    }
    
    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }
    
    public String getAmpVersion() {
        return ampVersion;
    }
    
    public void setAmpVersion(String ampVersion) {
        this.ampVersion = ampVersion;
    }
    
    public Boolean getTrackingEnabled() {
        return trackingEnabled;
    }
    
    public void setTrackingEnabled(Boolean trackingEnabled) {
        this.trackingEnabled = trackingEnabled;
    }
    
    public String getSiteId() {
        return siteId;
    }
    
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    
    public String getTrackingUrl() {
        return trackingUrl;
    }
    
    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }
    
    public String getFooterText() {
        return footerText;
    }
    
    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }
    
    public List<LayoutAdminLink> getAdminLinks() {
        return adminLinks;
    }
    
    public void setAdminLinks(List<LayoutAdminLink> adminLinks) {
        this.adminLinks = adminLinks;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Boolean getAdministratorMode() {
        return administratorMode;
    }
    
    public void setAdministratorMode(Boolean administratorMode) {
        this.administratorMode = administratorMode;
    }
    
    public String getWorkspace() {
        return workspace;
    }
    
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }
    
    public Long getWorkspaceId() {
        return workspaceId;
    }
    
    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
    
    public Boolean getLogged() {
        return logged;
    }
    
    public void setLogged(Boolean logged) {
        this.logged = logged;
    }
    
    public static class LayoutAdminLink {
        
        private String name;
        
        private String url;
    
        public LayoutAdminLink(String name, String url) {
            this.name = name;
            this.url = url;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getUrl() {
            return url;
        }
    
        public void setUrl(String url) {
            this.url = url;
        }
    }
}
