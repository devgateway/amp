/**
 * 
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 **/
public class ReportsFilterPickerForm extends ActionForm {
	/**
     * 
     */
    private static final long serialVersionUID = -8336313984510706274L;
	private Collection currencies;
	private Collection calendars;
	private Collection sectors;
	//private Collection donors;
	private Collection risks;
	private Collection<BeanWrapperImpl> fromYears;
	private Collection<BeanWrapperImpl> toYears;
	private Collection<BeanWrapperImpl> fromMonths;
	private Collection<BeanWrapperImpl> toMonths;
	private Collection actRankCollection;
	private Collection pageSizes; //A0,A1,A2,A3,A4
	private Collection donorTypes; //Ex: Multilateral, Bilateral, Regional Governament
	private Collection donorGroups;
	private Collection executingAgency;
	private Collection implementingAgency;
	private Collection beneficiaryAgency;
	private Long regionSelected;
	
	private Object[] selectedSectors;
	private Object[] selectedStatuses;
	//private Object[] selectedDonors;
	private Object[] selectedRisks;
	private Object[] selectedFinancingInstruments;
	private Object[] selectedDonorTypes; // ids of AmpOrgType
	private Object[] selectedDonorGroups; //
	private Object[] selectedExecutingAgency;
	private Object[] selectedImplementingAgency;
	private Object[] selectedBeneficiaryAgency;
	
	private Collection regionSelectedCollection;
	
	private Long fromYear;
	private Long toYear;
	private Integer toMonth;
	private Integer fromMonth;
	private Long currency;
	private Long calendar;
	private Long ampReportId;
	private Integer lineMinRank;
	private Integer planMinRank;
	private String text;
	private String indexString;
	private String pageSize; //the specific page sizes

	private Boolean governmentApprovalProcedures;
	private Boolean jointCriteria;
	
	//to keep the default currency after user changues 
	private Long defaultCurrency;
	
	public Long getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(Long defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

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
			//this.selectedDonors 				= null;
			this.selectedRisks	 				= null;
			this.selectedSectors 				= null;
			this.selectedStatuses				= null;
			this.selectedFinancingInstruments 	= null;
			this.selectedDonorTypes				= null;
			this.selectedDonorGroups			= null;
			this.selectedExecutingAgency		= null;
			this.selectedBeneficiaryAgency		= null;
			this.selectedImplementingAgency		= null;
		}
	}
	
	public Long getFromYear() {
		return fromYear;
	}
	public void setFromYear(Long fromYear) {
		this.fromYear = fromYear;
	}
	public Collection<BeanWrapperImpl> getFromYears() {
		return fromYears;
	}
	public Collection<BeanWrapperImpl> getFromMonths() {
		return fromMonths;
	}
	public void setFromMonths(Collection<BeanWrapperImpl> fromMonths) {
		this.fromMonths = fromMonths;
	}
	public Collection<BeanWrapperImpl> getToMonths() {
		return toMonths;
	}
	public void setToMonths(Collection<BeanWrapperImpl> toMonths) {
		this.toMonths = toMonths;
	}
	public void setFromYears(Collection<BeanWrapperImpl> fromYears) {
		this.fromYears = fromYears;
	}
	public Long getToYear() {
		return toYear;
	}
	public void setToYear(Long toYear) {
		this.toYear = toYear;
	}
	public Collection<BeanWrapperImpl> getToYears() {
		return toYears;
	}
	public void setToYears(Collection<BeanWrapperImpl> toYears) {
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
	/*public Collection getDonors() {
		return donors;
	}
	public void setDonors(Collection donors) {
		this.donors = donors;
	}*/
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
	
	/*public Object[] getSelectedDonors() {
		return selectedDonors;
	}
	public void setSelectedDonors(Object[] selectedDonors) {
		this.selectedDonors = selectedDonors;
	}*/
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
	
	
	
	public Object[] getSelectedFinancingInstruments() {
		return selectedFinancingInstruments;
	}

	public void setSelectedFinancingInstruments(
			Object[] selectedFinancingInstruments) {
		this.selectedFinancingInstruments = selectedFinancingInstruments;
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
	
	public Integer getToMonth() {
		return toMonth;
	}
	
	public void setToMonth(Integer toMonth) {
		this.toMonth = toMonth;
	}
	
	public Integer getFromMonth() {
		return fromMonth;
	}
	
	public void setFromMonth(Integer fromMonth) {
		this.fromMonth = fromMonth;
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

	public Boolean getGovernmentApprovalProcedures() {
		return governmentApprovalProcedures;
	}

	public void setGovernmentApprovalProcedures(Boolean governmentApprovalProcedures) {
		this.governmentApprovalProcedures = governmentApprovalProcedures;
	}

	public Boolean getJointCriteria() {
		return jointCriteria;
	}

	public void setJointCriteria(Boolean jointCriteria) {
		this.jointCriteria = jointCriteria;
	}

	public Collection getRegionSelectedCollection() {
		return regionSelectedCollection;
	}

	public void setRegionSelectedCollection(Collection regionSelectedCollection) {
		this.regionSelectedCollection = regionSelectedCollection;
	}

	public Long getRegionSelected() {
		return regionSelected;
	}

	public void setRegionSelected(Long regionSelected) {
		this.regionSelected = regionSelected;
	}

	public String getIndexString() {
		return indexString;
	}

	public void setIndexString(String indexString) {
		this.indexString = indexString;
	}
	
	
	public Collection getDonorTypes() {
		return donorTypes;
	}

	public void setDonorTypes(Collection donorTypes) {
		this.donorTypes = donorTypes;
	}

	public Object[] getSelectedDonorTypes() {
		return selectedDonorTypes;
	}

	public void setSelectedDonorTypes(Object[] selectedDonorTypes) {
		this.selectedDonorTypes = selectedDonorTypes;
	}

	public Collection getDonorGroups() {
		return donorGroups;
	}

	public void setDonorGroups(Collection donorGroups) {
		this.donorGroups = donorGroups;
	}

	public Object[] getSelectedDonorGroups() {
		return selectedDonorGroups;
	}

	public void setSelectedDonorGroups(Object[] selectedDonorGroups) {
		this.selectedDonorGroups = selectedDonorGroups;
	}

	public Collection getBeneficiaryAgency() {
		return beneficiaryAgency;
	}

	public void setBeneficiaryAgency(Collection beneficiaryAgency) {
		this.beneficiaryAgency = beneficiaryAgency;
	}

	public Collection getExecutingAgency() {
		return executingAgency;
	}

	public void setExecutingAgency(Collection executingAgency) {
		this.executingAgency = executingAgency;
	}

	public Collection getImplementingAgency() {
		return implementingAgency;
	}

	public void setImplementingAgency(Collection implementingAgency) {
		this.implementingAgency = implementingAgency;
	}

	public Object[] getSelectedBeneficiaryAgency() {
		return selectedBeneficiaryAgency;
	}

	public void setSelectedBeneficiaryAgency(Object[] selectedBeneficiaryAgency) {
		this.selectedBeneficiaryAgency = selectedBeneficiaryAgency;
	}

	public Object[] getSelectedExecutingAgency() {
		return selectedExecutingAgency;
	}

	public void setSelectedExecutingAgency(Object[] selectedExecutingAgency) {
		this.selectedExecutingAgency = selectedExecutingAgency;
	}

	public Object[] getSelectedImplementingAgency() {
		return selectedImplementingAgency;
	}

	public void setSelectedImplementingAgency(Object[] selectedImplementingAgency) {
		this.selectedImplementingAgency = selectedImplementingAgency;
	}
	
}
