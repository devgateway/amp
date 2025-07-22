package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.digijava.module.translation.util.MultilingualInputFieldValues;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Form class is used to edit/add new indicators
 */
public class AidEffectivenessIndicatorForm extends ActionForm {

    private Long ampIndicatorId;

    private String ampIndicatorName;
    private String tooltipText;

    // default is true
    private boolean active;

    // deafult is true
    private boolean mandatory;

    // 0 - dropdown list, 1 - selectbox list
    private int indicatorType = -1;

    private String oldAmpIndicatorName;

    private List<AmpAidEffectivenessIndicatorOption> options = new ArrayList<AmpAidEffectivenessIndicatorOption>();

    public Long getAmpIndicatorId() {
        return ampIndicatorId;
    }

    public void setAmpIndicatorId(Long ampIndicatorId) {
        this.ampIndicatorId = ampIndicatorId;
    }

    public String getAmpIndicatorName() {
        return ampIndicatorName;
    }

    public void setAmpIndicatorName(String ampIndicatorName) {
        this.ampIndicatorName = ampIndicatorName;
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public int getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(int indicatorType) {
        this.indicatorType = indicatorType;
    }

    public List<AmpAidEffectivenessIndicatorOption> getOptions() {
        return options;
    }

    public void setOptions(List<AmpAidEffectivenessIndicatorOption> options) {
        this.options = options;
    }

    public String getOldAmpIndicatorName() {
        return oldAmpIndicatorName;
    }

    public void setOldAmpIndicatorName(String oldAmpIndicatorName) {
        this.oldAmpIndicatorName = oldAmpIndicatorName;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setMandatory(false);
        setActive(false);
    }
    
    public MultilingualInputFieldValues buildMultilingualInputInstance(String attributeName) {
        return new MultilingualInputFieldValues(AmpAidEffectivenessIndicator.class, this.getAmpIndicatorId(), attributeName, null, null);
    }

}
