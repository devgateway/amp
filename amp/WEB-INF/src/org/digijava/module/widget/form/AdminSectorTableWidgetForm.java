package org.digijava.module.widget.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;

public class AdminSectorTableWidgetForm extends ActionForm {

    private List<AmpSectorTableWidget> sectorTables;
    private Long sectorTableId;
    private Long startYear;
    private String name;
    private String[] selectedTotalYears;
    private String[] selectedPercentYears;
    private List<AmpSectorOrder> sectors;
    private List<Long> years;
    private String[] selectedSectors;
    private Long sectorToReorderId;
    private Long[] selPlaces;
    private List<AmpDaWidgetPlace> places;

    public Long[] getSelPlaces() {
        return selPlaces;
    }

    public void setSelPlaces(Long[] selPlaces) {
        this.selPlaces = selPlaces;
    }

    public List<AmpDaWidgetPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<AmpDaWidgetPlace> places) {
        this.places = places;
    }

    public Long getSectorToReorderId() {
        return sectorToReorderId;
    }

    public void setSectorToReorderId(Long sectorToReorderId) {
        this.sectorToReorderId = sectorToReorderId;
    }

    public List<AmpSectorOrder> getSectors() {
        return sectors;
    }

    public void setSectors(List<AmpSectorOrder> sectors) {
        this.sectors = sectors;
    }

    public String[] getSelectedSectors() {
        return selectedSectors;
    }

    public void setSelectedSectors(String[] selectedSectors) {
        this.selectedSectors = selectedSectors;
    }

    public List<Long> getYears() {
        return years;
    }

    public void setYears(List<Long> years) {
        this.years = years;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSectorTableId() {
        return sectorTableId;
    }

    public void setSectorTableId(Long sectorTableId) {
        this.sectorTableId = sectorTableId;
    }

    public List<AmpSectorTableWidget> getSectorTables() {
        return sectorTables;
    }

    public void setSectorTables(List<AmpSectorTableWidget> sectorTables) {
        this.sectorTables = sectorTables;
    }

    public String[] getSelectedPercentYears() {
        return selectedPercentYears;
    }

    public void setSelectedPercentYears(String[] selectedPercentYears) {
        this.selectedPercentYears = selectedPercentYears;
    }

    public String[] getSelectedTotalYears() {
        return selectedTotalYears;
    }

    public void setSelectedTotalYears(String[] selectedTotalYears) {
        this.selectedTotalYears = selectedTotalYears;
    }

    public Long getStartYear() {
        return startYear;
    }

    public void setStartYear(Long startYear) {
        this.startYear = startYear;
    }
    
  
}
