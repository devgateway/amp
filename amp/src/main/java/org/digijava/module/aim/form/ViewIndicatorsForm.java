package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.IndicatorsBean;

import java.util.Collection;

public class ViewIndicatorsForm  extends ActionForm {

    private int category = -1;
    private int type = -1;
    private Long sectorId = new Long(-1);
    private String sortBy;
    private String keyword;
    private Long indid[]; // list of ind selected from
    private Long themeid;
    private String event;
    private Collection  Sectors;
    private String themeName;
    private String flag; 
    

    private Collection<IndicatorsBean> allIndicators;

    public void setAllIndicators(Collection allIndicators) {
        this.allIndicators = allIndicators;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Collection getAllIndicators() {
        return allIndicators;
    }

    public int getCategory() {
        return category;
    }

    public int getType() {
        return type;
    }

    public String getKeyword() {
        return keyword;
    }

    public ViewIndicatorsForm() {
    }

    public Long[] getIndid() {
        return indid;
    }

    public void setIndid(Long[] indid) {
        this.indid = indid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Long getThemeid() {
        return themeid;
    }

    public void setThemeid(Long themeid) {
        this.themeid = themeid;
    }

    public Collection getSectors() {
        return Sectors;
    }

    public void setSectors(Collection sectors) {
        Sectors = sectors;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
