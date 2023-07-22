package org.digijava.module.aim.dbentity ;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 
 * @deprecated use {@link AmpIndicator}
 *
 */
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "AMP_THEME_INDICATORS")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Deprecated
public class AmpThemeIndicators implements Serializable, Comparable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amp_theme_ind_id")
    private Long ampThemeIndId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "type")
    private String type;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "category")
    private Integer category;

    @Column(name = "np_indicator")
    private Boolean npIndicator;

    @Column(name = "description", length = 10000)
    private String description;

    @OneToMany(mappedBy = "ampThemeIndicators", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpIndicatorSector> sectors = new HashSet<>();

    @OneToMany(mappedBy = "themeIndicator", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpThemeIndicatorValue> indicatorValues = new HashSet<>();

    private Set themes;

    
    public AmpThemeIndicators() {}

    /**
     * @return Returns the ampThemeIndId.
     */
    public Long getAmpThemeIndId() {
        return ampThemeIndId;
    }

    /**
     * @param ampThemeIndId The ampThemeIndId to set.
     */
    public void setAmpThemeIndId(Long ampThemeIndId) {
        this.ampThemeIndId = ampThemeIndId;
    }

    /**
     * @return Returns the category.
     */
    public int getCategory() {
        return category;
    }

    /**
     * @param category The category to set.
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the npIndicator.
     */
    public boolean isNpIndicator() {
        return npIndicator;
    }

    /**
     * @param npIndicator The npIndicator to set.
     */
    public void setNpIndicator(boolean npIndicator) {
        this.npIndicator = npIndicator;
    }

    /**
     * @return Returns the themes.
     */
    public Set getThemes() {
        return themes;
    }

    /**
     * @param themes The themes to set.
     */
    public void setThemes(Set themes) {
        this.themes = themes;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int compareTo(Object obj) {
        AmpThemeIndicators other=(AmpThemeIndicators)obj;
        return this.getAmpThemeIndId().compareTo(other.getAmpThemeIndId());
    }

    public Set getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(Set indicatorValues) {
        this.indicatorValues = indicatorValues;
    }
    public Set getSectors() {
        return sectors;
    }

    public void setSectors(Set sectors) {
        this.sectors = sectors;
    }

}
