package org.digijava.kernel.ampapi.endpoints.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemStructure {
    
    @ApiModelProperty("Translated menu entry title")
    private String name;
    
    @ApiModelProperty("Translated menu entry tooltip")
    private String tooltip;
    
    @ApiModelProperty("Menu entry action URL")
    private String url;
    
    @ApiModelProperty("Open action URL within a popup")
    private Boolean popup;
    
    @ApiModelProperty("Open action URL in a new browser tab")
    private Boolean tab;
    
    @ApiModelProperty("Use POST instead of GET for the action URL")
    private Boolean post;
    
    private List<MenuItemStructure> children;
    
    @ApiModelProperty("Flags if this is a language switch menu entry for any custom handling")
    private Boolean language;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTooltip() {
        return tooltip;
    }
    
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Boolean getPopup() {
        return popup;
    }
    
    public void setPopup(Boolean popup) {
        this.popup = popup;
    }
    
    public Boolean getTab() {
        return tab;
    }
    
    public void setTab(Boolean tab) {
        this.tab = tab;
    }
    
    public Boolean getPost() {
        return post;
    }
    
    public void setPost(Boolean post) {
        this.post = post;
    }
    
    public List<MenuItemStructure> getChildren() {
        return children;
    }
    
    public void setChildren(List<MenuItemStructure> children) {
        this.children = children;
    }
    
    public Boolean getLanguage() {
        return language;
    }
    
    public void setLanguage(Boolean language) {
        this.language = language;
    }
}
