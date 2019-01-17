package org.digijava.kernel.ampapi.endpoints.security.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemStructure {
    
    private String name;
    
    private String tooltip;
    
    private String url;
    
    private Boolean popup;
    
    private Boolean tab;
    
    private Boolean post;
    
    private List<MenuItemStructure> children;
    
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
