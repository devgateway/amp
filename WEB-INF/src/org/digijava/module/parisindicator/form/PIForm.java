package org.digijava.module.parisindicator.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.parisindicator.helper.PIReportAbstractRow;

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
	private AmpCurrency selectedCurrency;
	private AmpFiscalCalendar selectedCalendar;
	private Collection<AmpStatus> selectedStatuses;
	private Collection<AmpOrgGroup> selectedDonorGroups;
	private Collection<AmpSector> selectedSectors;
	private Collection<AmpOrganisation> selectedDonors;

	/*
	 * Available values in filters.
	 */
	private Collection<AmpCurrency> currencyTypes;
	private Collection<AmpStatus> statuses;
	private Collection<AmpFiscalCalendar> calendars;
	private Collection<AmpOrganisation> donors;
	private Collection<AmpOrgGroup> donorGroups;
	private Collection<AmpSector> sectors;
	private Collection financingInstruments;

	/*
	 * Rows of the main table.
	 */
	private Collection<PIReportAbstractRow> mainTableRows;

	/*
	 * List of available reports from DB.
	 */
	private Collection<AmpAhsurveyIndicator> availablePIReports;

	private boolean reset;

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

	public void setStatuses(Collection<AmpStatus> statuses) {
		this.statuses = statuses;
	}

	public Collection<AmpStatus> getStatuses() {
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

	public void setFinancingInstruments(Collection financingInstruments) {
		this.financingInstruments = financingInstruments;
	}

	public Collection getFinancingInstruments() {
		return financingInstruments;
	}

	public void setSelectedCurrency(AmpCurrency selectedCurrency) {
		this.selectedCurrency = selectedCurrency;
	}

	public AmpCurrency getSelectedCurrency() {
		return selectedCurrency;
	}

	public void setSelectedCalendar(AmpFiscalCalendar selectedCalendar) {
		this.selectedCalendar = selectedCalendar;
	}

	public AmpFiscalCalendar getSelectedCalendar() {
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

	public void setAvailablePIReports(
			Collection<AmpAhsurveyIndicator> availablePIReports) {
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

	public Collection<AmpStatus> getSelectedStatuses() {
		return selectedStatuses;
	}

	public void setSelectedStatuses(Collection<AmpStatus> selectedStatuses) {
		this.selectedStatuses = selectedStatuses;
	}

	public Collection<AmpSector> getSelectedSectors() {
		return selectedSectors;
	}

	public void setSelectedSectors(Collection<AmpSector> selectedSectors) {
		this.selectedSectors = selectedSectors;
	}

	public Collection<AmpOrganisation> getSelectedDonors() {
		return selectedDonors;
	}

	public void setSelectedDonors(Collection<AmpOrganisation> selectedDonors) {
		this.selectedDonors = selectedDonors;
	}

	public Collection<AmpOrgGroup> getSelectedDonorGroups() {
		return selectedDonorGroups;
	}

	public void setSelectedDonorGroups(
			Collection<AmpOrgGroup> selectedDonorGroups) {
		this.selectedDonorGroups = selectedDonorGroups;
	}

	public Collection<AmpOrgGroup> getDonorGroups() {
		return donorGroups;
	}

	public void setDonorGroups(Collection<AmpOrgGroup> donorGroups) {
		this.donorGroups = donorGroups;
	}
}
