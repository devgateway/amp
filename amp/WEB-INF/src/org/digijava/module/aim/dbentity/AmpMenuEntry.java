/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.menu.AmpView;
import org.digijava.kernel.user.Group;
import org.hibernate.annotations.DiscriminatorOptions;

/**
 * Stores one menu entry details
 * 
 * @author Nadejda Mandrescu
 */

import javax.persistence.*;

@Entity
@Table(name = "AMP_MENU_ENTRY")
@DiscriminatorOptions(force=true)
public class AmpMenuEntry implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_MENU_ENTRY_seq")
    @SequenceGenerator(name = "AMP_MENU_ENTRY_seq", sequenceName = "AMP_MENU_ENTRY_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private AmpMenuEntry parent;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TOOLTIP")
    private String tooltip;

    @Column(name = "URL")
    private String url;

    @Column(name = "FLAGS")
    private String flags;

    @Column(name = "POSITION")
    private int position;

    @Column(name = "REQUEST_URL", columnDefinition = "text")
    private String requestUrl;

    @ManyToMany
    @JoinTable(name = "AMP_MENU_ENTRY_DG_GROUP", joinColumns = @JoinColumn(name = "MENU_ID"), inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    private Set<Group> groups;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AmpMenuEntry> items;

    @ElementCollection
    @CollectionTable(name = "AMP_MENU_ENTRY_VIEW", joinColumns = @JoinColumn(name = "MENU_ID"))
    @MapKeyColumn(name = "VIEW_TYPE", columnDefinition = "integer")
    @Column(name = "RULE_ID")
    private Map<Integer, Long> viewVisibilityMap;

    @ElementCollection
    @CollectionTable(name = "AMP_MENU_ENTRY_VIEW", joinColumns = @JoinColumn(name = "MENU_ID"))
    @MapKeyColumn(name = "VIEW_TYPE", columnDefinition = "integer")
    @Column(name = "URL")
    private Map<Integer, String> viewURLMap;


    
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
    public Map<Integer, Long> getViewVisibilityMap() {
        return viewVisibilityMap;
    }

    /**
     * @param viewVisibilityMap the viewVisibilityMap to set
     */
    public void setViewVisibilityMap(
            Map<Integer, Long> viewVisibilityMap) {
        this.viewVisibilityMap = viewVisibilityMap;
    }

    /**
     * @return the viewURLMap
     */
    public Map<Integer, String> getViewURLMap() {
        return viewURLMap;
    }

    /**
     * @param viewURLMap the viewURLMap to set
     */
    public void setViewURLMap(Map<Integer, String> viewURLMap) {
        this.viewURLMap = viewURLMap;
    }
    
}
