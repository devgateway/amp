package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class SectorsForm extends ActionForm {

    private Long parentSectorId;      
    private String parentSector;
    private String prevViewedSectorId;
    private Collection sectors;

    public Long getParentSectorId() {
        return (this.parentSectorId);
    }

    public void setParentSectorId(Long parentSectorId) {
        this.parentSectorId = parentSectorId;     
    }
    
    public String getParentSector() {
        return (this.parentSector);
    }
    
    public void setParentSector(String parentSector) {
        this.parentSector = parentSector;
    }
    
    public Collection getSectors() {
        return (this.sectors); 
    }
    
    public void setSectors(Collection sectors) {
        this.sectors = sectors;
    }

    public String getPrevViewedSectorId() {
        return (this.prevViewedSectorId);     
    }

    public void setPrevViewedSectorId(String prevViewedSectorId) {
        this.prevViewedSectorId = prevViewedSectorId;
    }
}
