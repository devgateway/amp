
package org.digijava.module.widget.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.helper.SectorTableHelper;

public class ShowSectorTableForm  extends ActionForm {
    private Long widgetId;
    private List<SectorTableHelper> sectorsInfo;
    private List<String> years;

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
