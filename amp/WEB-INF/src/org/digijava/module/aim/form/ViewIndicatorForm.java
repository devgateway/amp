package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.ActivityIndicator;

import java.util.Collection;

public class ViewIndicatorForm extends ActionForm {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Collection<ActivityIndicator> indicators;

    private Long ampActivityId;
    
    
    
    
    /**
     * @return Returns the indicators.
     */
    public Collection<ActivityIndicator> getIndicators() {
        return indicators;
    }

    /**
     * @param indicators The indicators to set.
     */
    public void setIndicators(Collection<ActivityIndicator> indicators) {
        this.indicators = indicators;
    }

    public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }
}
