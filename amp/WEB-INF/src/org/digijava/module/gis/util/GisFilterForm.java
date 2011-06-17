package org.digijava.module.gis.util;

/**
  * User: flyer
 * Date: 6/9/11
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class GisFilterForm {

    private Long[] selectedDonorTypes;
    private Long[] selectedDonorGroups;
    private Long[] selectedDonnorAgency;
    private Long[] selectedSectors;
    private Long[] selectedSecondarySectors;
    private Long[] selectedTertiarySectors;
    private Long[] selectedNatPlanObj;
    private Long selectedCalendar;
    private String selectedCurrency;
    private String mapModeFin;
    private String fundingType;
    private int mapLevel;
    private String filterStartYear;
    private String filterEndYear;

    public String getFilterStartYear() {
        return filterStartYear;
    }

    public void setFilterStartYear(String filterStartYear) {
        this.filterStartYear = filterStartYear;
    }

    public String getFilterEndYear() {
        return filterEndYear;
    }

    public void setFilterEndYear(String filterEndYear) {
        this.filterEndYear = filterEndYear;
    }

    public int getMapLevel() {
        return mapLevel;
    }

    public void setMapLevel(int mapLevel) {
        this.mapLevel = mapLevel;
    }

    public Long[] getSelectedDonorTypes() {
        return selectedDonorTypes;
    }

    public void setSelectedDonorTypes(Long[] selectedDonorTypes) {
        this.selectedDonorTypes = selectedDonorTypes;
    }

    public Long[] getSelectedDonorGroups() {
        return selectedDonorGroups;
    }

    public void setSelectedDonorGroups(Long[] selectedDonorGroups) {
        this.selectedDonorGroups = selectedDonorGroups;
    }

    public Long[] getSelectedDonnorAgency() {
        return selectedDonnorAgency;
    }

    public void setSelectedDonnorAgency(Long[] selectedDonnorAgency) {
        this.selectedDonnorAgency = selectedDonnorAgency;
    }

    public Long[] getSelectedSectors() {
        return selectedSectors;
    }

    public void setSelectedSectors(Long[] selectedSectors) {
        this.selectedSectors = selectedSectors;
    }

    public Long[] getSelectedSecondarySectors() {
        return selectedSecondarySectors;
    }

    public void setSelectedSecondarySectors(Long[] selectedSecondarySectors) {
        this.selectedSecondarySectors = selectedSecondarySectors;
    }

    public Long[] getSelectedTertiarySectors() {
        return selectedTertiarySectors;
    }

    public void setSelectedTertiarySectors(Long[] selectedTertiarySectors) {
        this.selectedTertiarySectors = selectedTertiarySectors;
    }

    public Long getSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(Long selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public String getMapModeFin() {
        return mapModeFin;
    }

    public void setMapModeFin(String mapModeFin) {
        this.mapModeFin = mapModeFin;
    }

    public String getFundingType() {
        return fundingType;
    }

    public void setFundingType(String fundingType) {
        this.fundingType = fundingType;
    }

    public Long[] getSelectedNatPlanObj() {
        return selectedNatPlanObj;
    }

    public void setSelectedNatPlanObj(Long[] selectedNatPlanObj) {
        this.selectedNatPlanObj = selectedNatPlanObj;
    }
}
