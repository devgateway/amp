package org.digijava.module.aim.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SectorClassConfigForm extends ActionForm {

    private List classifications;
    private Long id;
    private String event;
    private String configName;
    private String configDescription;
    private Long multiSectorSelecting;
    private Long sectorClassId;
    private List classificationConfigs;

    public List getClassificationConfigs() {
        return classificationConfigs;
    }

    public void setClassificationConfigs(List classificationConfigs) {
        this.classificationConfigs = classificationConfigs;
    }

    public Long getSectorClassId() {
        return sectorClassId;
    }

    public void setSectorClassId(Long sectorClassId) {
        this.sectorClassId = sectorClassId;
    }

    public Long getMultiSectorSelecting() {
        return multiSectorSelecting;
    }

    public void setMultiSectorSelecting(Long multiSectorSelecting) {
        this.multiSectorSelecting = multiSectorSelecting;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String config) {
        this.configName = config;
    }
    
    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public List getClassifications() {
        return classifications;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClassifications(List classifications) {
        this.classifications = classifications;
    }


    public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
        classificationConfigs = null;
        event = null;
        id = null;
        classifications = null;
        sectorClassId=null;
        multiSectorSelecting=null;
        configName=null;
    }
}
