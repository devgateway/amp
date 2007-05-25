/**
 * 
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * @author mihai
 **/
public class ReportsFilterPickerForm extends ActionForm {
	private Collection currencies;
	private Collection calendars;
	private Collection sectors;
	private Collection statuses;
	private Collection donors;
	private Collection risks;
	private Collection fromYears;
	private Collection toYears;
	
	private Object[] selectedSectors;
	private Object[] selectedStatuses;
	private Object[] selectedDonors;
	private Object[] selectedRisks;
	private Long fromYear;
	private Long toYear;
	private Long currency;
	private Long calendar;

	public Long getFromYear() {
		return fromYear;
	}
	public void setFromYear(Long fromYear) {
		this.fromYear = fromYear;
	}
	public Collection getFromYears() {
		return fromYears;
	}
	public void setFromYears(Collection fromYears) {
		this.fromYears = fromYears;
	}
	public Long getToYear() {
		return toYear;
	}
	public void setToYear(Long toYear) {
		this.toYear = toYear;
	}
	public Collection getToYears() {
		return toYears;
	}
	public void setToYears(Collection toYears) {
		this.toYears = toYears;
	}
	public Collection getCalendars() {
		return calendars;
	}
	public void setCalendars(Collection calendars) {
		this.calendars = calendars;
	}
	public Collection getCurrencies() {
		return currencies;
	}
	public void setCurrencies(Collection currecies) {
		this.currencies = currecies;
	}
	public Collection getDonors() {
		return donors;
	}
	public void setDonors(Collection donors) {
		this.donors = donors;
	}
	public Collection getRisks() {
		return risks;
	}
	public void setRisks(Collection risks) {
		this.risks = risks;
	}
	public Collection getSectors() {
		return sectors;
	}
	public void setSectors(Collection sectors) {
		this.sectors = sectors;
	}
	public Collection getStatuses() {
		return statuses;
	}
	public void setStatuses(Collection statuses) {
		this.statuses = statuses;
	}

	public Object[] getSelectedDonors() {
		return selectedDonors;
	}
	public void setSelectedDonors(Object[] selectedDonors) {
		this.selectedDonors = selectedDonors;
	}
	public Object[] getSelectedRisks() {
		return selectedRisks;
	}
	public void setSelectedRisks(Object[] selectedRisks) {
		this.selectedRisks = selectedRisks;
	}
	public Object[] getSelectedSectors() {
		return selectedSectors;
	}
	public void setSelectedSectors(Object[] selectedSectors) {
		this.selectedSectors = selectedSectors;
	}
	public Object[] getSelectedStatuses() {
		return selectedStatuses;
	}
	public void setSelectedStatuses(Object[] selectedStatuses) {
		this.selectedStatuses = selectedStatuses;
	}
	public Long getCurrency() {
		return currency;
	}
	public void setCurrency(Long currency) {
		this.currency = currency;
	}
	public Long getCalendar() {
		return calendar;
	}
	public void setCalendar(Long calendar) {
		this.calendar = calendar;
	}
	
	
	
}
