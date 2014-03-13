/**
 * 
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import lombok.Data;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 */
@Data
public class ReportsFilterPickerForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8336313984510706274L;
	private Collection<AmpCurrency> currencies;
	private Collection calendars;
	
	private Collection<GroupingElement<HierarchyListableImplementation>> sectorElements;
	private Collection<GroupingElement<AmpTheme>> programElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> donorElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> relatedAgenciesElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> financingLocationElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> modeOfPayment;
	private Collection<GroupingElement<HierarchyListableImplementation>> otherCriteriaElements;
	
	private Object[] selectedHistory;


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

//	private String teamAccessType; unused
	
	private Object[] selectedSectors;
	private Object[] selectedSecondarySectors;
    private Object[] selectedTertiarySectors;
    private Object[] selectedTagSectors;
	private Object[] selectedStatuses;
	private Object[] selectedWorkspaces;
	// private Object[] selectedDonors;
	private Object[] selectedRisks;
	private Object[] selectedFinancingInstruments;
	private Long[] selectedTypeOfAssistance;
	private Long[] selectedModeOfPayment;
	private Object[] selectedDonorTypes; // ids of AmpOrgType
	private Object[] selectedDonorGroups; //
	private Object[] selectedContractingAgencyGroups; //
	private Object[] selectedExecutingAgency;
	private Object[] selectedContractingAgency;
	private Object[] selectedImplementingAgency;
	private Object[] selectedBeneficiaryAgency;
	private Object[] selectedDonnorAgency;
	private Object[] selectedProjectCategory;
	private Object[] selectedresponsibleorg;
	private Object[] selectedArchivedStatus	= new Object[]{"1"};
	private Object[] selectedActivitySettings;
	private Object[] selectedActivityPledgesTitle;
	
	//private Collection regionSelectedCollection;
	private Collection approvalStatusSelectedCollection;//AMP-3386

	private Long fromYear;
	private Long toYear;
	private Integer toMonth;
	private Integer fromMonth;
	
	private String fromDate;
	private String toDate;
	private DynamicDateFilter dynamicDateFilter = new DynamicDateFilter();
	
	private String fromActivityStartDate;
	private String toActivityStartDate;
	private DynamicDateFilter dynamicActivityStartFilter = new DynamicDateFilter(); 
	
	private String fromProposedApprovalDate;
	private String toProposedApprovalDate;
	private DynamicDateFilter dynamicProposedApprovalFilter = new DynamicDateFilter();
	
	private String fromActivityActualCompletionDate;
	private String toActivityActualCompletionDate;
	private DynamicDateFilter dynamicActivityActualCompletionFilter = new DynamicDateFilter(); 
	
	private String fromActivityFinalContractingDate; 
	private String toActivityFinalContractingDate;
	private DynamicDateFilter dynamicActivityFinalContractingFilter = new DynamicDateFilter();	

	private Long countYear;
	private Long currency;
	private Long calendar;
    private Long defaultCalendar;
	private String ampReportId;
	private Object[] lineMinRanks;
	private Object[] planMinRanks;
	private Object[] selectedMultiDonor;
	private String text;
	private String indexString;
	private String searchMode;
	private String pageSize; // the specific page sizes

	private Boolean governmentApprovalProcedures;
	private Boolean jointCriteria;
	private Long[] selectedBudgets = null;
	private Boolean justSearch = null;
	
	private Boolean pledged;
	private Boolean unallocatedLocation = null;

	// to keep the default currency after user changes
	private Long defaultCurrency;
	//private boolean isnewreport;
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
	
	private Collection<String> allgroupingseparators;
	private Collection<String> alldecimalSymbols;
	private String decimalSymbol;
	private String customDecimalSymbol;
	private String customDecimalSymbolTxt;
	private Integer customDecimalPlaces;
	private Boolean customUseGrouping;
	private String customGroupCharacter;
	private Integer customDecimalPlacesTxt;
	private String customGroupCharacterTxt;
	private Integer customGroupSize;
	private Integer amountinthousands;
	
	private String resetFormat;
	
	private Boolean sourceIsReportWizard;
	
	private Object[] disbursementOrders;
	
	private String CRISNumber;
	private String budgetNumber;
	
	private Integer computedYear=-1;
	private Integer actualAppYear=-1;
	
	private Long[] selectedProjectImplUnit;
	
	private Long reporttype;
	
	private Boolean workspaceonly;

    private boolean showWorkspaceFilter;

	public int getCalendarsSize() {
		if ( calendars == null )
			return 0;
		return calendars.size();
	}

	public String getCRISNumber() {
		return CRISNumber;
	}

	public void setCRISNumber(String number) {
		CRISNumber = number;
	}
   
}
