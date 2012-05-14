package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableYear;

public class AdminSectorTableWidgetForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private List<AmpSectorTableWidget> sectorTables;
    private Long sectorTableId;
    private String name;
    private String[] selectedTotalYears;
    private String[] selectedPercentYears;
    private List<AmpSectorOrder> sectors;
    private List<Long> years;
    private String[] selectedSectors;
    private Long sectorToReorderId;
    private Long[] selPlaces;
    private List<AmpDaWidgetPlace> places;
    private List<AmpSectorTableYear> sectorTableYears;
    private Long sectorTableYearId;
    private boolean donorColumn;
    private Long donorColumnYear;

    public Long getDonorColumnYear() {
        return donorColumnYear;
    }

    public void setDonorColumnYear(Long donorColumnYear) {
        this.donorColumnYear = donorColumnYear;
    }

    public boolean isDonorColumn() {
        return donorColumn;
    }

    public void setDonorColumn(boolean donorColumn) {
        this.donorColumn = donorColumn;
    }

    public Long getSectorTableYearId() {
        return sectorTableYearId;
    }

    public void setSectorTableYearId(Long sectorTableYearId) {
        this.sectorTableYearId = sectorTableYearId;
    }

    public List<AmpSectorTableYear> getSectorTableYears() {
        return sectorTableYears;
    }

    public void setSectorTableYears(List<AmpSectorTableYear> sectorTableYears) {
        this.sectorTableYears = sectorTableYears;
    }

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
}
