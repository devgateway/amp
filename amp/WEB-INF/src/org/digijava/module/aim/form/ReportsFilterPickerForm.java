/**
 * 
 */
package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 */
public class ReportsFilterPickerForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8336313984510706274L;
	private Collection currencies;
	private Collection calendars;
	private Collection sectors;
	private Collection secondarySectors;
	// private Collection donors;
	private Collection risks;
	private Collection<BeanWrapperImpl> fromYears;
	private Collection<BeanWrapperImpl> toYears;
	private Collection<BeanWrapperImpl> fromMonths;
	private Collection<BeanWrapperImpl> toMonths;
	private Collection<BeanWrapperImpl> countYears;
	private Collection actRankCollection;
	private Collection pageSizes; // A0,A1,A2,A3,A4
	private Collection donorTypes; // Ex: Multilateral, Bilateral, Regional
									// Governament
	private Collection donorGroups;
	private Collection executingAgency;
	private Collection<AmpOrganisation> donnorAgency;
	
	private Collection<AmpOrganisation> responsibleorg;
	private Collection implementingAgency;
	private Collection beneficiaryAgency;
	private Long regionSelected;
	private Object[] approvalStatusSelected;

	private String teamAccessType;
	
	private Object[] selectedSectors;
	private Object[] selectedSecondarySectors;
	private Object[] selectedStatuses;
	// private Object[] selectedDonors;
	private Object[] selectedRisks;
	private Object[] selectedFinancingInstruments;
	private Long[] selectedTypeOfAssistance;
	private Object[] selectedDonorTypes; // ids of AmpOrgType
	private Object[] selectedDonorGroups; //
	private Object[] selectedExecutingAgency;
	private Object[] selectedImplementingAgency;
	private Object[] selectedBeneficiaryAgency;
	private Object[] selectedDonnorAgency;
	private Object[] selectedProjectCategory;
	private Object[] selectedresponsibleorg;
	
	private Collection regionSelectedCollection;
	private Collection approvalStatusSelectedCollection;//AMP-3386

	private Long fromYear;
	private Long toYear;
	private Integer toMonth;
	private Integer fromMonth;
	private String fromDate;
	private String toDate;
	private Long countYear;
	private Long currency;
	private Long calendar;
	private Long ampReportId;
	private Integer lineMinRank;
	private Integer planMinRank;
	private String text;
	private String indexString;
	private String pageSize; // the specific page sizes

	private Boolean governmentApprovalProcedures;
	private Boolean jointCriteria;
	private Integer selectedBudget = null;
	private Boolean justSearch = null;

	// to keep the default currency after user changues
	private Long defaultCurrency;
	private boolean isnewreport;
	private Long countYearFrom;

	private List nationalPlanningObjectives;
	private List primaryPrograms;
	private List secondaryPrograms;

	private Object[] selectedNatPlanObj;
	private Object[] selectedPrimaryPrograms;
	private Object[] selectedSecondaryPrograms;

	private Integer renderStartYear; // the range of dates columns that has
										// to be render, years not in range will
										// be computables for totals but wont be
										// rederisables
	private Integer renderEndYear;

	
	
	private Integer resetRenderStartYear;
	private Integer resetRenderEndYear;

	
	private String decimalSymbol;
	private String customDecimalSymbol;
	private String customDecimalSymbolTxt;
	private Integer customDecimalPlaces;
	private Boolean customUseGrouping;
	private String customGroupCharacter;
	private Integer customDecimalPlacesTxt;
	private String customGroupCharacterTxt;
	private Integer customGroupSize;
	private String resetFormat;
	
	private Boolean sourceIsReportWizard;
	
	private String CRISNumber;
	private String budgetNumber;
	
	public String getResetFormat() {
		return resetFormat;
	}

	public void setResetFormat(String resetFormat) {
		this.resetFormat = resetFormat;
	}
	

	public String getDecimalSymbol() {
		return decimalSymbol;
	}

	public void setDecimalSymbol(String decimalSymbol) {
		this.decimalSymbol = decimalSymbol;
	}

	public String getCustomDecimalSymbolTxt() {
		return customDecimalSymbolTxt;
	}

	public void setCustomDecimalSymbolTxt(String customDecimalSymbolTxt) {
		this.customDecimalSymbolTxt = customDecimalSymbolTxt;
	}

	public Integer getCustomDecimalPlaces() {
		return customDecimalPlaces;
	}
	

	public void setCustomDecimalPlaces(Integer customDecimalPlaces) {
		this.customDecimalPlaces = customDecimalPlaces;
	}

	public Boolean getCustomUseGrouping() {
		return customUseGrouping;
	}

	public void setCustomUseGrouping(Boolean customUseGrouping) {
		this.customUseGrouping = customUseGrouping;
	}

	public String getCustomGroupCharacter() {
		return customGroupCharacter;
	}

	public void setCustomGroupCharacter(String customGroupCharacter) {
		this.customGroupCharacter = customGroupCharacter;
	}

	public Integer getCustomDecimalPlacesTxt() {
		return customDecimalPlacesTxt;
	}

	public void setCustomDecimalPlacesTxt(Integer customDecimalPlacesTxt) {
		this.customDecimalPlacesTxt = customDecimalPlacesTxt;
	}

	public String getCustomGroupCharacterTxt() {
		return customGroupCharacterTxt;
	}

	public void setCustomGroupCharacterTxt(String customGroupCharacterTxt) {
		this.customGroupCharacterTxt = customGroupCharacterTxt;
	}

	public Integer getRenderStartYear() {
		return renderStartYear;
	}

	public void setRenderStartYear(Integer renderStartYear) {
		this.renderStartYear = renderStartYear;
	}

	public Integer getRenderEndYear() {
		return renderEndYear;
	}

	public void setRenderEndYear(Integer renderEndYear) {
		this.renderEndYear = renderEndYear;
	}

	public Object[] getSelectedNatPlanObj() {
		return selectedNatPlanObj;
	}

	public void setSelectedNatPlanObj(Object[] selectedNatPlanObj) {
		this.selectedNatPlanObj = selectedNatPlanObj;
	}

	public Object[] getSelectedPrimaryPrograms() {
		return selectedPrimaryPrograms;
	}

	public void setSelectedPrimaryPrograms(Object[] selectedPrimaryPrograms) {
		this.selectedPrimaryPrograms = selectedPrimaryPrograms;
	}

	public Object[] getSelectedSecondaryPrograms() {
		return selectedSecondaryPrograms;
	}

	public void setSelectedSecondaryPrograms(Object[] selectedSecondaryPrograms) {
		this.selectedSecondaryPrograms = selectedSecondaryPrograms;
	}

	public List getNationalPlanningObjectives() {
		return nationalPlanningObjectives;
	}

	public void setNationalPlanningObjectives(List nationalPlanningObjectives) {
		this.nationalPlanningObjectives = nationalPlanningObjectives;
	}

	public List getPrimaryPrograms() {
		return primaryPrograms;
	}

	public void setPrimaryPrograms(List primaryPrograms) {
		this.primaryPrograms = primaryPrograms;
	}

	public List getSecondaryPrograms() {
		return secondaryPrograms;
	}

	public void setSecondaryPrograms(List secondaryPrograms) {
		this.secondaryPrograms = secondaryPrograms;
	}

	public Integer getSelectedBudget() {
		return selectedBudget;
	}

	public void setSelectedBudget(Integer selectedBudget) {
		this.selectedBudget = selectedBudget;
	}

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
		if (isnewreport) {
			this.ampReportId = ampReportId;
		}
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		/**
		 * Do not reset the filter when changing the range
		 */
		if ( request.getParameter("renderStartYear") != null )  
			return;
		
		if (request.getParameter("apply") != null && request.getAttribute("apply") == null || isnewreport) {
			// this.selectedDonors = null;
			// if applyFormat is clicked, the content of the filter was deleting not only the sectors...
			//AMP-5249
			if (request.getParameter("applyFormat")!=null)
				this.customUseGrouping=false;
			else{
				this.selectedDonnorAgency=null;
				this.selectedRisks = null;
				this.selectedSectors = null;
				this.selectedSecondarySectors = null;
				this.selectedStatuses = null;
				this.selectedFinancingInstruments = null;
				this.selectedTypeOfAssistance = null;
				this.selectedDonorTypes = null;
				this.selectedDonorGroups = null;
				this.selectedExecutingAgency = null;
				this.selectedBeneficiaryAgency = null;
				this.selectedImplementingAgency = null;
				this.selectedProjectCategory = null;
				this.selectedBudget = null;
				this.governmentApprovalProcedures = null;
				this.justSearch=false;
				this.selectedNatPlanObj = null;
				this.selectedPrimaryPrograms = null;
				this.selectedSecondaryPrograms = null;
				this.selectedresponsibleorg=null;
				this.approvalStatusSelected = null;
				this.lineMinRank=null;
				this.planMinRank=null;
				this.CRISNumber=null;
				this.budgetNumber=null;
			}
		}
		
			
		
	
	}

	public Collection<BeanWrapperImpl> getCountYears() {
		return countYears;
	}

	public void setCountYears(Collection<BeanWrapperImpl> countYears) {
		this.countYears = countYears;
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

	/*
	 * public Collection getDonors() { return donors; } public void
	 * setDonors(Collection donors) { this.donors = donors; }
	 */
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

	/*
	 * public Object[] getSelectedDonors() { return selectedDonors; } public
	 * void setSelectedDonors(Object[] selectedDonors) { this.selectedDonors =
	 * selectedDonors; }
	 */
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

	public Object[] getSelectedSecondarySectors() {
		return selectedSecondarySectors;
	}

	public void setSelectedSecondarySectors(Object[] selectedSecondarySectors) {
		this.selectedSecondarySectors = selectedSecondarySectors;
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

	public void setSelectedFinancingInstruments(Object[] selectedFinancingInstruments) {
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

	public Collection<AmpOrganisation> getResponsibleorg() {
		return responsibleorg;
	}

	public void setResponsibleorg(Collection<AmpOrganisation> responsibleorg) {
		this.responsibleorg = responsibleorg;
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

	public boolean isIsnewreport() {
		return isnewreport;
	}

	public void setIsnewreport(boolean isnewreport) {
		this.isnewreport = isnewreport;
	}

	public Long[] getSelectedTypeOfAssistance() {
		return selectedTypeOfAssistance;
	}

	public void setSelectedTypeOfAssistance(Long[] selectedTypeOfAssistance) {
		this.selectedTypeOfAssistance = selectedTypeOfAssistance;
	}

	public Long getCountYear() {
		return countYear;
	}

	public void setCountYear(Long countYear) {
		this.countYear = countYear;
	}

	public void setCountYearFrom(Long countYearFrom) {
		this.countYearFrom = countYearFrom;

	}

	public Long getCountYearFrom() {
		return countYearFrom;
	}

	public Collection getSecondarySectors() {
		return secondarySectors;
	}

	public void setSecondarySectors(Collection secondarySectors) {
		this.secondarySectors = secondarySectors;
	}

	public Integer getResetRenderStartYear() {
		return resetRenderStartYear;
	}

	public void setResetRenderStartYear(Integer resetRenderStartYear) {
		this.resetRenderStartYear = resetRenderStartYear;
	}

	public Integer getResetRenderEndYear() {
		return resetRenderEndYear;
	}
	

	public void setResetRenderEndYear(Integer resetRenderEndYear) {
		this.resetRenderEndYear = resetRenderEndYear;
	}

	public String getCustomDecimalSymbol() {
		return customDecimalSymbol;
	}

	public void setCustomDecimalSymbol(String customDecimalSymbol) {
		this.customDecimalSymbol = customDecimalSymbol;
	}

	public Integer getCustomGroupSize() {
		return customGroupSize;
	}

	public void setCustomGroupSize(Integer customGroupSize) {
		this.customGroupSize = customGroupSize;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Object[] getApprovalStatusSelected() {
		return approvalStatusSelected;
	}

	public void setApprovalStatusSelected(Object[] approvalStatusSelected) {
		this.approvalStatusSelected = approvalStatusSelected;
	}

	public Collection getApprovalStatusSelectedCollection() {
		return approvalStatusSelectedCollection;
	}

	public void setApprovalStatusSelectedCollection(
			Collection approvalStatusSelectedCollection) {
		this.approvalStatusSelectedCollection = approvalStatusSelectedCollection;
	}

	public String getTeamAccessType() {
		return teamAccessType;
	}

	public void setTeamAccessType(String teamAccessType) {
		this.teamAccessType = teamAccessType;
	}

	

	public Object[] getSelectedDonnorAgency() {
		return selectedDonnorAgency;
	}

	public void setSelectedDonnorAgency(Object[] selectedDonnorAgency) {
		this.selectedDonnorAgency = selectedDonnorAgency;
	}

	public Collection<AmpOrganisation> getDonnorAgency() {
		return donnorAgency;
	}

	public void setDonnorAgency(Collection<AmpOrganisation> donnorAgency) {
		this.donnorAgency = donnorAgency;
	}

	public Boolean getJustSearch() {
		return justSearch;
	}

	public void setJustSearch(Boolean justSearch) {
		this.justSearch = justSearch;
	}

	public void setSelectedProjectCategory(Object[] selectedProjectCategory) {
		this.selectedProjectCategory = selectedProjectCategory;
	}

	public Object[] getSelectedProjectCategory() {
		return selectedProjectCategory;
	}

	public Object[] getSelectedresponsibleorg() {
		return selectedresponsibleorg;
	}

	public void setSelectedresponsibleorg(Object[] selectedresponsibleorg) {
		this.selectedresponsibleorg = selectedresponsibleorg;
	}

	public Boolean getSourceIsReportWizard() {
		return sourceIsReportWizard;
	}

	public void setSourceIsReportWizard(Boolean sourceIsReportWizard) {
		this.sourceIsReportWizard = sourceIsReportWizard;
	}

	public String getCRISNumber() {
		return CRISNumber;
	}

	public void setCRISNumber(String number) {
		CRISNumber = number;
	}

	public String getBudgetNumber() {
		return budgetNumber;
	}

	public void setBudgetNumber(String budgetNumber) {
		this.budgetNumber = budgetNumber;
	}

	

	
}
