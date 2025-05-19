/**
 * 
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.menu.AmpView;
import org.digijava.kernel.user.Group;
import org.hibernate.annotations.DiscriminatorOptions;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores one menu entry details
 * 
 * @author Nadejda Mandrescu
 */
@DiscriminatorOptions(force=true)
public class AmpMenuEntry implements Serializable {
    private Long id;
    private AmpMenuEntry parent;
    private String name;
    private String title;
    private String tooltip;
    private String url;
    private String flags;
    private int position = 0;
    private String requestUrl;
    private Set<AmpMenuEntry> items;
    private Set<Group> groups= new HashSet<>();
    private Map<AmpView, AmpVisibilityRule> viewVisibilityMap;
    private Map<AmpView, String> viewURLMap;
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the parent
     */
    public AmpMenuEntry getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(AmpMenuEntry parent) {
        this.parent = parent;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip the tooltip to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    
    /**
     * @return the flags
     */
    public String getFlags() {
        return flags;
    }
    
    /**
     * @param flags the flags to set
     */
    public void setFlags(String flags) {
        this.flags = flags;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the requestUrl
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * @param requestUrl the requestUrl to set
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * @return the items
     */
    public Set<AmpMenuEntry> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Set<AmpMenuEntry> items) {
        this.items = items;
    }

    /**
     * @return the groups
     */
    public Set<Group> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    /**
     * @return the viewVisibilityMap
     */
    public Map<AmpView, AmpVisibilityRule> getViewVisibilityMap() {
        return viewVisibilityMap;
    }

    /**
     * @param viewVisibilityMap the viewVisibilityMap to set
     */
    public void setViewVisibilityMap(
            Map<AmpView, AmpVisibilityRule> viewVisibilityMap) {
        this.viewVisibilityMap = viewVisibilityMap;
    }

    /**
     * @return the viewURLMap
     */
    public Map<AmpView, String> getViewURLMap() {
        return viewURLMap;
    }

    /**
     * @param viewURLMap the viewURLMap to set
     */
    public void setViewURLMap(Map<AmpView, String> viewURLMap) {
        this.viewURLMap = viewURLMap;
    }
    
}
