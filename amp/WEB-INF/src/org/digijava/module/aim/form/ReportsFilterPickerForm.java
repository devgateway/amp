/**
 * 
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
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
	
	private Collection<GroupingElement<HierarchyListableImplementation>> sectorElements;
	private Collection<GroupingElement<AmpTheme>> programElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> donorElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> relatedAgenciesElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> financingLocationElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> modeOfPayment;
	private Collection<GroupingElement<HierarchyListableImplementation>> otherCriteriaElements;
	
	
	private Object[] selectedHistory;
	
	public Object[] getSelectedHistory() {
		return selectedHistory;
	}

	public void setSelectedHistory(Object[] selectedHistory) {
		this.selectedHistory = selectedHistory;
	}

	// private Collection donors;
	private Collection risks;
	private Collection<BeanWrapperImpl> fromYears;
	private Collection<BeanWrapperImpl> toYears;
	private Collection<BeanWrapperImpl> fromMonths;
	private Collection<BeanWrapperImpl> toMonths;
	private Collection<BeanWrapperImpl> countYears;
	private Collection<BeanWrapperImpl> computedYearsRange;
	private Collection<BeanWrapperImpl> actualAppYearsRange;
	
	//private Collection actRankCollection;
	private Collection pageSizes; // A0,A1,A2,A3,A4
	//private Collection donorTypes; // Ex: Multilateral, Bilateral, Regional
									// Governament
	//private Collection donorGroups;
	//private Collection executingAgency;
	//private Collection<AmpOrganisation> donnorAgency;
	
	//private Collection<AmpOrganisation> responsibleorg;
	//private Collection implementingAgency;
	//private Collection beneficiaryAgency;
	private Object[] regionSelected;
	private Object[] approvalStatusSelected;

	private String teamAccessType;
	
	private Object[] selectedSectors;
	private Object[] selectedSecondarySectors;
    private Object[] selectedTertiarySectors;
	private Object[] selectedStatuses;
	// private Object[] selectedDonors;
	private Object[] selectedRisks;
	private Object[] selectedFinancingInstruments;
	private Long[] selectedTypeOfAssistance;
	private Long[] selectedModeOfPayment;
	private Object[] selectedDonorTypes; // ids of AmpOrgType
	private Object[] selectedDonorGroups; //
	private Object[] selectedExecutingAgency;
	private Object[] selectedImplementingAgency;
	private Object[] selectedBeneficiaryAgency;
	private Object[] selectedDonnorAgency;
	private Object[] selectedProjectCategory;
	private Object[] selectedresponsibleorg;
	private Object[] selectedArchivedStatus	= new Object[]{"1"};
	
	//private Collection regionSelectedCollection;
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
	private Object[] lineMinRanks;
	private Object[] planMinRanks;
	private String text;
	private String indexString;
	private String pageSize; // the specific page sizes

	private Boolean governmentApprovalProcedures;
	private Boolean jointCriteria;
	private Long[] selectedBudgets = null;
	private Boolean justSearch = null;
	
	private Boolean unallocatedLocation = null;

	// to keep the default currency after user changues
	private Long defaultCurrency;
	private boolean isnewreport;
	private Long countYearFrom;

//	private List nationalPlanningObjectives;
//	private List primaryPrograms;
//	private List secondaryPrograms;

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
	private Boolean amountinthousands;
	private Boolean amountinmillions;
	
	public Boolean getAmountinmillions() {
		return amountinmillions;
	}

	public void setAmountinmillions(Boolean amountsinmillions) {
		this.amountinmillions = amountsinmillions;
	}

	private String resetFormat;
	
	private Boolean sourceIsReportWizard;
	
	private Object[] disbursementOrders;
	
	private String CRISNumber;
	private String budgetNumber;
	
	private Integer computedYear=-1;
	private Integer actualAppYear=-1;
	
	private Long[] selectedProjectImplUnit;


	public Integer getComputedYear() {
		return computedYear;
	}

	public void setComputedYear(Integer computedYear) {
		this.computedYear = computedYear;
	}
	
	public Integer getActualAppYear() {
		return actualAppYear;
	}

	public void setActualAppYear(Integer actualAppYear) {
		this.actualAppYear = actualAppYear;
	}
	
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

	/**
	 * @return the selectedBudgets
	 */
	public Long[] getSelectedBudgets() {
		return selectedBudgets;
	}

	/**
	 * @param selectedBudgets the selectedBudgets to set
	 */
	public void setSelectedBudgets(Long[] selectedBudgets) {
		this.selectedBudgets = selectedBudgets;
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



	/**
	 * @return the lineMinRanks
	 */
	public Object[] getLineMinRanks() {
		return lineMinRanks;
	}

	/**
	 * @param lineMinRanks the lineMinRanks to set
	 */
	public void setLineMinRanks(Object[] lineMinRanks) {
		this.lineMinRanks = lineMinRanks;
	}

	/**
	 * @return the planMinRanks
	 */
	public Object[] getPlanMinRanks() {
		return planMinRanks;
	}

	/**
	 * @param planMinRanks the planMinRanks to set
	 */
	public void setPlanMinRanks(Object[] planMinRanks) {
		this.planMinRanks = planMinRanks;
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
		 * 
		 * [this fix seems old. if still needed check that the checkboxes in the filter still work after]
		 * 
		 *if ( request.getParameter("renderStartYear") != null && !request.getParameter("renderStartYear").equals("-1"))  
		 *	return;
		 */
		if (request.getParameter("apply") != null && request.getAttribute("apply") == null || isnewreport) {
			// this.selectedDonors = null;
			// if applyFormat is clicked, the content of the filter was deleting not only the sectors...
			//AMP-5249
			if (request.getParameter("applyFormat")!=null){
				this.customUseGrouping=false;
				this.amountinthousands=false;
				this.amountinmillions=false;
			}else{
				this.selectedDonnorAgency=null;
				this.selectedRisks = null;
				this.selectedSectors = null;
				this.selectedSecondarySectors = null;
                this.selectedTertiarySectors=null;
				this.selectedStatuses = null;
				this.selectedFinancingInstruments = null;
				this.selectedTypeOfAssistance = null;
				this.selectedModeOfPayment = null;
				this.selectedDonorTypes = null;
				this.regionSelected = null;
				this.selectedDonorGroups = null;
				this.selectedExecutingAgency = null;
				this.selectedBeneficiaryAgency = null;
				this.selectedImplementingAgency = null;
				this.selectedProjectCategory = null;
				this.selectedBudgets = null;
				this.governmentApprovalProcedures = null;
				this.unallocatedLocation = null;
				this.justSearch=false;
				this.selectedNatPlanObj = null;
				this.selectedPrimaryPrograms = null;
				this.selectedSecondaryPrograms = null;
				this.selectedresponsibleorg=null;
				this.approvalStatusSelected = null;
				this.lineMinRanks=null;
				this.planMinRanks=null;
				this.CRISNumber=null;
				this.budgetNumber=null;
				this.selectedArchivedStatus=new Object[]{"1"};
				this.selectedProjectImplUnit=null;
			}
		}
		
			
		
	
	}

	
	public Collection<BeanWrapperImpl> getComputedYearsRange() {
		return computedYearsRange;
	}
	
	public void setComputedYearsRange(Collection<BeanWrapperImpl> computedYearsRange) {
		this.computedYearsRange = computedYearsRange;
	}

	public Collection<BeanWrapperImpl> getActualAppYearsRange() {
		return actualAppYearsRange;
	}
	
	public void setActualAppYearsRange(Collection<BeanWrapperImpl> actualAppYearsRange) {
		this.actualAppYearsRange = actualAppYearsRange;
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

	public int getCalendarsSize() {
		if ( calendars == null )
			return 0;
		return calendars.size();
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

    public Object[] getSelectedTertiarySectors() {
        return selectedTertiarySectors;
    }

    public void setSelectedTertiarySectors(Object[] selectedTertiarySectors) {
        this.selectedTertiarySectors = selectedTertiarySectors;
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

	public Object[] getRegionSelected() {
		return regionSelected;
	}

	public void setRegionSelected(Object[] regionSelected) {
		this.regionSelected = regionSelected;
	}

	public String getIndexString() {
		return indexString;
	}

	public void setIndexString(String indexString) {
		this.indexString = indexString;
	}

	public Object[] getSelectedDonorTypes() {
		return selectedDonorTypes;
	}

	public void setSelectedDonorTypes(Object[] selectedDonorTypes) {
		this.selectedDonorTypes = selectedDonorTypes;
	}
	
	public Object[] getSelectedDonorGroups() {
		return selectedDonorGroups;
	}

	public void setSelectedDonorGroups(Object[] selectedDonorGroups) {
		this.selectedDonorGroups = selectedDonorGroups;
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

	public Long[] getSelectedModeOfPayment() {
		return selectedModeOfPayment;
	}

	public void setSelectedModeOfPayment(Long[] selectedModeOfPayment) {
		this.selectedModeOfPayment = selectedModeOfPayment;
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

	

	/**
	 * @return the disbursementOrders
	 */
	public Object[] getDisbursementOrders() {
		return disbursementOrders;
	}

	/**
	 * @param disbursementOrders the disbursementOrders to set
	 */
	public void setDisbursementOrders(Object[] disbursementOrders) {
		this.disbursementOrders = disbursementOrders;
	}

	public Boolean getUnallocatedLocation() {
		return unallocatedLocation;
	}

	public void setUnallocatedLocation(Boolean unallocatedLocation) {
		this.unallocatedLocation = unallocatedLocation;
	}

	public Boolean getAmountinthousands() {
		return amountinthousands;
	}

	public void setAmountinthousands(Boolean amountinthousands) {
		this.amountinthousands = amountinthousands;
	}

	/**
	 * @return the sectorElements
	 */
	public Collection<GroupingElement<HierarchyListableImplementation>> getSectorElements() {
		return sectorElements;
	}

	/**
	 * @param sectorElements the sectorElements to set
	 */
	public void setSectorElements(
			Collection<GroupingElement<HierarchyListableImplementation>> sectorElements) {
		this.sectorElements = sectorElements;
	}

	/**
	 * @return the programElements
	 */
	public Collection<GroupingElement<AmpTheme>> getProgramElements() {
		return programElements;
	}

	/**
	 * @param programElements the programElements to set
	 */
	public void setProgramElements(
			Collection<GroupingElement<AmpTheme>> programElements) {
		this.programElements = programElements;
	}

	/**
	 * @return the donorElements
	 */
	public Collection<GroupingElement<HierarchyListableImplementation>> getDonorElements() {
		return donorElements;
	}

	/**
	 * @param donorElements the donorElements to set
	 */
	public void setDonorElements(
			Collection<GroupingElement<HierarchyListableImplementation>> donorElements) {
		this.donorElements = donorElements;
	}

	/**
	 * @return the relatedAgenciesElements
	 */
	public Collection<GroupingElement<HierarchyListableImplementation>> getRelatedAgenciesElements() {
		return relatedAgenciesElements;
	}

	/**
	 * @param relatedAgenciesElements the relatedAgenciesElements to set
	 */
	public void setRelatedAgenciesElements(
			Collection<GroupingElement<HierarchyListableImplementation>> relatedAgenciesElements) {
		this.relatedAgenciesElements = relatedAgenciesElements;
	}

	/**
	 * @return the financingLocationElements
	 */
	public Collection<GroupingElement<HierarchyListableImplementation>> getFinancingLocationElements() {
		return financingLocationElements;
	}

	/**
	 * @param financingLocationElements the financingLocationElements to set
	 */
	public void setFinancingLocationElements(
			Collection<GroupingElement<HierarchyListableImplementation>> financingLocationElements) {
		this.financingLocationElements = financingLocationElements;
	}

	/**
	 * @return the modeOfPayment
	 */
	public Collection<GroupingElement<HierarchyListableImplementation>> getModeOfPayment() {
		return modeOfPayment;
	}

	/**
	 * @param modeOfPayment the modeOfPayment to set
	 */
	public void setModeOfPayment(
			Collection<GroupingElement<HierarchyListableImplementation>> modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	/**
	 * @return the otherCriteriaElements
	 */
	public Collection<GroupingElement<HierarchyListableImplementation>> getOtherCriteriaElements() {
		return otherCriteriaElements;
	}

	/**
	 * @param otherCriteriaElements the otherCriteriaElements to set
	 */
	public void setOtherCriteriaElements(
			Collection<GroupingElement<HierarchyListableImplementation>> otherCriteriaElements) {
		this.otherCriteriaElements = otherCriteriaElements;
	}

	/**
	 * @return the selectedArchivedStatus
	 */
	public Object[] getSelectedArchivedStatus() {
		return selectedArchivedStatus;
	}

	/**
	 * @param selectedArchivedStatus the selectedArchivedStatus to set
	 */
	public void setSelectedArchivedStatus(Object[] selectedArchivedStatus) {
		this.selectedArchivedStatus = selectedArchivedStatus;
	}

	public Long[] getSelectedProjectImplUnit() {
		return selectedProjectImplUnit;
	}

	public void setSelectedProjectImplUnit(Long[] selectedProjectImplUnit) {
		this.selectedProjectImplUnit = selectedProjectImplUnit;
	}	
	
}
