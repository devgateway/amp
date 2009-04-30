
package org.digijava.module.widget.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.widget.helper.SectorTableHelper;

public class ShowSectorTableForm  extends ActionForm {
    private Long widgetId;
    private List<SectorTableHelper> sectorsInfo;
    private List<String> years;
    private List<AmpOrganisation> donors;
    private boolean donorColumnAdded;
    
    public boolean isDonorColumnAdded() {
        return donorColumnAdded;
    }

    public void setDonorColumnAdded(boolean donorColumnAdded) {
        this.donorColumnAdded = donorColumnAdded;
    }

    public List<AmpOrganisation> getDonors() {
        return donors;
    }

    public void setDonors(List<AmpOrganisation> donors) {
        this.donors = donors;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public List<SectorTableHelper> getSectorsInfo() {
        return sectorsInfo;
    }

    public void setSectorsInfo(List<SectorTableHelper> sectorsInfo) {
        this.sectorsInfo = sectorsInfo;
    }

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

}
