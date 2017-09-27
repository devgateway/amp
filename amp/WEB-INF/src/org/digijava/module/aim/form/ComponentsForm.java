package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class ComponentsForm extends ActionForm implements Serializable{

    private Collection components ;
    private Collection compIndicators;
    private String compIndicatorName;
    private String compIndicatorCode;
    private String compIndicatorDesc;
    private Long indicatorId;
    private String duplicate;
        
    /**
     * @return the duplicate
     */
    public String getDuplicate() {
        return duplicate;
    }

    /**
     * @param duplicate the duplicate to set
     */
    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    /**
     * @return Returns the indicatorId.
     */
    public Long getIndicatorId() {
        return indicatorId;
    }

    /**
     * @param components The components to set.
     */
    public void setIndicatorId(Long  id) {
        this.indicatorId = id;
    }
    
    /**
     * @return Returns the components.
     */
    public Collection getComponents() {
        return components;
    }

    /**
     * @param components The components to set.
     */
    public void setComponents(Collection components) {
        this.components = components;
    }

    /**
     * @return Returns the compIndicatorCode.
     */
    public String getCompIndicatorCode() {
        return compIndicatorCode;
    }

    /**
     * @param compIndicatorCode The compIndicatorCode to set.
     */
    public void setCompIndicatorCode(String compIndicatorCode) {
        this.compIndicatorCode = compIndicatorCode;
    }

    /**
     * @return Returns the compIndicatorDesc.
     */
    public String getCompIndicatorDesc() {
        return compIndicatorDesc;
    }

    /**
     * @param compIndicatorDesc The compIndicatorDesc to set.
     */
    public void setCompIndicatorDesc(String compIndicatorDesc) {
        this.compIndicatorDesc = compIndicatorDesc;
    }

    /**
     * @return Returns the compIndicatorName.
     */
    public String getCompIndicatorName() {
        return compIndicatorName;
    }

    /**
     * @param compIndicatorName The compIndicatorName to set.
     */
    public void setCompIndicatorName(String compIndicatorName) {
        this.compIndicatorName = compIndicatorName;
    }

    /**
     * @return Returns the compIndicators.
     */
    public Collection getCompIndicators() {
        return compIndicators;
    }

    /**
     * @param compIndicators The compIndicators to set.
     */
    public void setCompIndicators(Collection compIndicators) {
        this.compIndicators = compIndicators;
    }
    
}
