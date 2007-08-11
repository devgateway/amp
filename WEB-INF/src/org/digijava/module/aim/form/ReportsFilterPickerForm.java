/**
 * 
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author mihai
 **/
public class ReportsFilterPickerForm extends ActionForm {
	private Collection currencies;
	private Collection calendars;
	private Collection sectors;
	private Collection donors;
	private Collection risks;
	private Collection fromYears;
	private Collection toYears;
	private Collection actRankCollection;
	private Collection pageSizes; //A0,A1,A2,A3,A4 
	
	private Object[] selectedSectors;
	private Object[] selectedStatuses;
	private Object[] selectedDonors;
	private Object[] selectedRisks;
	
	private Long fromYear;
	private Long toYear;
	private Long currency;
	private Long calendar;
	private Long ampReportId;
	private Integer lineMinRank;
	private Integer planMinRank;
	private String text;
	private String pageSize; //the specific page sizes

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Integer lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Integer getPlanMinRank() {
		return planMinRank;
	}

	public void setPlanMinRank(Integer planMinRank) {
		this.planMinRank = planMinRank;
	}

	public Long getAmpReportId() {
		return ampReportId;
	}

	public void setAmpReportId(Long ampReportId) {
		this.ampReportId = ampReportId;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if(request.getParameter("apply")!=null && request.getAttribute("apply")==null){
			this.selectedDonors = null;
			this.selectedRisks = null;
			this.selectedSectors = null;
			this.selectedStatuses = null;
		}
	}
	
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

	public Collection getActRankCollection() {
		return actRankCollection;
	}

	public void setActRankCollection(Collection actRankCollection) {
		this.actRankCollection = actRankCollection;
	}

	public Collection getPageSizes() {
		return pageSizes;
	}

	public void setPageSizes(Collection pageSizes) {
		this.pageSizes = pageSizes;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
}
