package org.digijava.module.parisindicator.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

public class PIForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    /*
     * Reference to the current report.
     */
    private AmpAhsurveyIndicator piReport;

    /*
     * Selected values when the filters are submitted.
     */
    private int selectedStartYear;
    private int selectedEndYear;
    private String selectedCurrency;
    private String selectedCalendar;
    private int defaultStartYear;
    private int defaultEndYear;
    private String defaultCurrency;
    private String defaultCalendar;
    private String[] selectedStatuses;
    private String[] selectedDonorGroups;
    private String[] selectedSectors;
    private String[] selectedDonors;
    private String[] selectedFinancingIstruments;

    /*
     * Available values in filters.
     */
    private Collection<AmpCurrency> currencyTypes;
    private Collection<AmpCategoryValue> statuses;
    private Collection<AmpFiscalCalendar> calendars;
    private Collection<AmpOrganisation> donors;
    private Collection<AmpOrgGroup> donorGroups;
    private Collection<AmpSector> sectors;
    private Collection<AmpCategoryValue> financingInstruments;
    private int[] startYears;
    private int[] endYears;
    Collection<GroupingElement<HierarchyListableImplementation>> financingInstrumentsElements;
    Collection<GroupingElement<HierarchyListableImplementation>> donorElements;
    Collection<GroupingElement<HierarchyListableImplementation>> sectorStatusesElements;

    public Collection<GroupingElement<HierarchyListableImplementation>> getSectorStatusesElements() {
        return sectorStatusesElements;
    }

    public void setSectorStatusesElements(
            Collection<GroupingElement<HierarchyListableImplementation>> sectorStatusesElements) {
        this.sectorStatusesElements = sectorStatusesElements;
    }

    public Collection<GroupingElement<HierarchyListableImplementation>> getDonorElements() {
        return donorElements;
    }

    public void setDonorElements(
            Collection<GroupingElement<HierarchyListableImplementation>> donorElements) {
        this.donorElements = donorElements;
    }
    /*
     * Rows of the main table.
     */
    private Collection<PIReportAbstractRow> mainTableRows;

    /*
     * List of available reports from DB.
     */
    private Collection<AmpAhsurveyIndicator> availablePIReports;

    /*
     * Mini tables for reports 5a and 5b.
     */
    private int[][] miniTable;

    private boolean reset;
    private boolean printPreview;
    private boolean exportPDF;
    private boolean exportXLS;

    public boolean isExportPDF() {
        return exportPDF;
    }

    public void setExportPDF(boolean exportPDF) {
        this.exportPDF = exportPDF;
    }

    public boolean isExportXLS() {
        return exportXLS;
    }

    public void setExportXLS(boolean exportXLS) {
        this.exportXLS = exportXLS;
    }

    public int getSelectedStartYear() {
        return selectedStartYear;
    }

    public void setSelectedStartYear(int selectedStartYear) {
        this.selectedStartYear = selectedStartYear;
    }

    public int getSelectedEndYear() {
        return selectedEndYear;
    }

    public void setSelectedEndYear(int selectedEndYear) {
        this.selectedEndYear = selectedEndYear;
    }

    public int getStartYear() {
        return selectedStartYear;
    }

    public void setStartYear(int startYear) {
        this.selectedStartYear = startYear;
    }

    public int getEndYear() {
        return selectedEndYear;
    }

    public void setEndYear(int endYear) {
        this.selectedEndYear = endYear;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Collection<AmpCurrency> getCurrencyTypes() {
        return currencyTypes;
    }

    public void setCurrencyTypes(Collection<AmpCurrency> currencyTypes) {
        this.currencyTypes = currencyTypes;
    }

    public void setStatuses(Collection<AmpCategoryValue> statuses) {
        this.statuses = statuses;
    }

    public Collection<AmpCategoryValue> getStatuses() {
        return statuses;
    }

    public void setCalendars(Collection<AmpFiscalCalendar> calendars) {
        this.calendars = calendars;
    }

    public Collection<AmpFiscalCalendar> getCalendars() {
        return calendars;
    }

    public void setDonors(Collection<AmpOrganisation> donors) {
        this.donors = donors;
    }

    public Collection<AmpOrganisation> getDonors() {
        return donors;
    }

    public void setSectors(Collection<AmpSector> sectors) {
        this.sectors = sectors;
    }

    public Collection<AmpSector> getSectors() {
        return sectors;
    }

    public void setFinancingInstruments(Collection<AmpCategoryValue> financingInstruments) {
        this.financingInstruments = financingInstruments;
    }

    public Collection<AmpCategoryValue> getFinancingInstruments() {
        return financingInstruments;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCalendar(String selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

    public String getSelectedCalendar() {
        return selectedCalendar;
    }

    public Collection<PIReportAbstractRow> getMainTableRows() {
        return mainTableRows;
    }

    public void setMainTableRows(Collection<PIReportAbstractRow> mainTableRows) {
        this.mainTableRows = mainTableRows;
    }

    public Collection<AmpAhsurveyIndicator> getAvailablePIReports() {
        return availablePIReports;
    }

    public void setAvailablePIReports(Collection<AmpAhsurveyIndicator> availablePIReports) {
        this.availablePIReports = availablePIReports;
    }

    public AmpAhsurveyIndicator getPiReport() {
        return piReport;
    }

    public void setPiReport(AmpAhsurveyIndicator piReport) {
        this.piReport = piReport;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public Collection<AmpOrgGroup> getDonorGroups() {
        return donorGroups;
    }

    public void setDonorGroups(Collection<AmpOrgGroup> donorGroups) {
        this.donorGroups = donorGroups;
    }

    public int[][] getMiniTable() {
        return miniTable;
    }

    public void setMiniTable(int[][] miniTable) {
        this.miniTable = miniTable;
    }

    public String[] getSelectedStatuses() {
        return selectedStatuses;
    }

    public void setSelectedStatuses(String[] selectedStatuses) {
        this.selectedStatuses = selectedStatuses;
    }

    public String[] getSelectedDonorGroups() {
        return selectedDonorGroups;
    }

    public void setSelectedDonorGroups(String[] selectedDonorGroups) {
        this.selectedDonorGroups = selectedDonorGroups;
    }

    public String[] getSelectedSectors() {
        return selectedSectors;
    }

    public void setSelectedSectors(String[] selectedSectors) {
        this.selectedSectors = selectedSectors;
    }

    public String[] getSelectedDonors() {
        return selectedDonors;
    }

    public void setSelectedDonors(String[] selectedDonors) {
        this.selectedDonors = selectedDonors;
    }

    public String[] getSelectedFinancingIstruments() {
        return selectedFinancingIstruments;
    }

    public void setSelectedFinancingIstruments(String[] selectedFinancingIstruments) {
        this.selectedFinancingIstruments = selectedFinancingIstruments;
    }

    public int[] getStartYears() {
        return startYears;
    }

    public void setStartYears(int[] startYears) {
        this.startYears = startYears;
    }

    public int[] getEndYears() {
        return endYears;
    }

    public void setEndYears(int[] endYears) {
        this.endYears = endYears;
    }

    public boolean isPrintPreview() {
        return printPreview;
    }

    public void setPrintPreview(boolean print) {
        this.printPreview = print;
    }

    public Collection<GroupingElement<HierarchyListableImplementation>> getFinancingInstrumentsElements() {
        return financingInstrumentsElements;
    }

    public void setFinancingInstrumentsElements(
            Collection<GroupingElement<HierarchyListableImplementation>> financingInstrumentsElements) {
        this.financingInstrumentsElements = financingInstrumentsElements;
    }
    
    public int getDefaultStartYear() {
        return defaultStartYear;
    }

    public void setDefaultStartYear(int defaultStartYear) {
        this.defaultStartYear = defaultStartYear;
    }

    public int getDefaultEndYear() {
        return defaultEndYear;
    }

    public void setDefaultEndYear(int defaultEndYear) {
        this.defaultEndYear = defaultEndYear;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getDefaultCalendar() {
        return defaultCalendar;
    }

    public void setDefaultCalendar(String defaultCalendar) {
        this.defaultCalendar = defaultCalendar;
    }
    

}
