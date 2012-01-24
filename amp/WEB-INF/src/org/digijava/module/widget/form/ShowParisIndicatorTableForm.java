

package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.widget.dbentity.AmpParisIndicatorBaseTargetValues;


public class ShowParisIndicatorTableForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private Long widgetId;
    private List<AmpParisIndicatorBaseTargetValues> parisIndicators;
    private List<AmpOrgGroup> donorGroups;
  
    public List<AmpParisIndicatorBaseTargetValues> getParisIndicators() {
        return parisIndicators;
    }

    public void setParisIndicators(List<AmpParisIndicatorBaseTargetValues> parisIndicators) {
        this.parisIndicators = parisIndicators;
    }

    public Long getWidgetId() {
        return widgetId;
    }

    public List<AmpOrgGroup> getDonorGroups() {
        return donorGroups;
    }

    public void setDonorGroups(List<AmpOrgGroup> donorGroups) {
        this.donorGroups = donorGroups;
    }

  

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

}
