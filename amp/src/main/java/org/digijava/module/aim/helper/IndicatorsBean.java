package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;

public class IndicatorsBean {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String date;
    private Integer category;
    private String type;
    private Collection sector;
    private Collection sectorNames = new ArrayList();
    private String sectorName;
    
    private Long ampThemeIndId;
    private Date creationDate;
    private boolean npIndicator;
    private Set themes;
    private Set indicatorValues;
    private Set sectors;
    private boolean flag;
    
    
    

    public IndicatorsBean() {
    }

    
    @Deprecated
    public IndicatorsBean(AllMEIndicators ind) {
        this.id=ind.getAmpMEIndId();
        this.name=ind.getName();
        this.code=ind.getCode();
    }

    @Deprecated
    public IndicatorsBean(AllPrgIndicators ind) {
        this.id=ind.getIndicatorId();
        this.name=ind.getName();
        this.category=Integer.valueOf(ind.getCategory());
        this.code=ind.getCode();
        this.date=ind.getCreationDate();
        this.description=ind.getDescription();
        this.sector= ind.getSector();
        this.sectorName = ind.getSectorName();
    }

    @Deprecated
    public IndicatorsBean(AmpThemeIndicators ind) {
        this.id=ind.getAmpThemeIndId();
        this.name=ind.getName();
        this.category=Integer.valueOf(ind.getCategory());
        this.code=ind.getCode();
        this.sector = ind.getSectors();
        this.description=ind.getDescription();
    }

    public IndicatorsBean(AmpIndicator ind) {
        this.id=ind.getIndicatorId();
        this.name=ind.getName();
        this.category=Integer.valueOf(ind.getCategory());
        this.code=ind.getCode();
        this.sector = ind.getSectors();
        this.description=ind.getDescription();
    }
    
    public IndicatorsBean(AmpMEIndicatorList ind) {
        this.id=ind.getAmpMEIndId();
        this.name=ind.getName();
        this.code=ind.getCode();
        this.description=ind.getDescription();
       
    }


    public Integer getCategory() {
        return category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection getSector() {
        return sector;
    }

    public void setSector(Collection sector) {
        this.sector = sector;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public Long getAmpThemeIndId() {
        return ampThemeIndId;
    }

    public void setAmpThemeIndId(Long ampThemeIndId) {
        this.ampThemeIndId = ampThemeIndId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isNpIndicator() {
        return npIndicator;
    }

    public void setNpIndicator(boolean npIndicator) {
        this.npIndicator = npIndicator;
    }

    public Set getThemes() {
        return themes;
    }

    public void setThemes(Set themes) {
        this.themes = themes;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public Collection getSectorNames() {
        return sectorNames;
    }

    public void setSectorNames(Collection sectorNames) {
        this.sectorNames = sectorNames;
    }


}
