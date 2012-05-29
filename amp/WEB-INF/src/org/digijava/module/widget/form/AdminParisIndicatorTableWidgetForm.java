

package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpParisIndicatorBaseTargetValues;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;


public class AdminParisIndicatorTableWidgetForm  extends ActionForm{
	private static final long serialVersionUID = 1L;
	private List<AmpParisIndicatorTableWidget> piTableWidgets;
    private Long piTableWidgetId;
    private List<AmpParisIndicatorBaseTargetValues> parisIndicators;
    private Long donorGroupYearColumn;
    private Long[] selPlaces;
    private List<Long> years;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPiTableWidgetId() {
        return piTableWidgetId;
    }

    public void setPiTableWidgetId(Long piTableWidgetId) {
        this.piTableWidgetId = piTableWidgetId;
    }

    public List<Long> getYears() {
        return years;
    }

    public void setYears(List<Long> years) {
        this.years = years;
    }

    public Long getDonorGroupYearColumn() {
        return donorGroupYearColumn;
    }

    public void setDonorGroupYearColumn(Long donorGroupYearColumn) {
        this.donorGroupYearColumn = donorGroupYearColumn;
    }

    public List<AmpDaWidgetPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<AmpDaWidgetPlace> places) {
        this.places = places;
    }

    public Long[] getSelPlaces() {
        return selPlaces;
    }

    public void setSelPlaces(Long[] selPlaces) {
        this.selPlaces = selPlaces;
    }
    private List<AmpDaWidgetPlace> places;


    public List<AmpParisIndicatorBaseTargetValues> getParisIndicators() {
        return parisIndicators;
    }

    public void setParisIndicators(List<AmpParisIndicatorBaseTargetValues> parisIndicators) {
        this.parisIndicators = parisIndicators;
    }

    public List<AmpParisIndicatorTableWidget> getPiTableWidgets() {
        return piTableWidgets;
    }

    public void setPiTableWidgets(List<AmpParisIndicatorTableWidget> piTableWidgets) {
        this.piTableWidgets = piTableWidgets;
    }

}
