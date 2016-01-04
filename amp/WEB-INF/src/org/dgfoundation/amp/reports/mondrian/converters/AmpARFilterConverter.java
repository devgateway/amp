/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.ReportSettingsImpl;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**********************
The status for the Report Filters tab from: 
https://docs.google.com/a/developmentgateway.org/spreadsheets/d/14k8GFwiacYZ6su_TOSdPxNj2StxlSeixFbXbhumu32c/edit#gid=128056684
**********************
* Mapped:
* **********************
Donor Type
Donor Group
Donor Agency
Executing Agencies
Implementing Agencies
Responsible Agencies
Contracting Agencies
Primary Sectors
Secondary Sectors
Financing Instrument
Type of Assistance
Mode of Payment
On/Off Budget
Regions
Status
Workspace
Actual Start Date
Date Filter
Beneficiary Agency 
Contracting Agency Groups
Primary Programs
Secondary Programs
Approval Status
**********************
* pending schema / column constants clarifications:
* **********************
Pledges Titles
Archived
**********************
* need to detect where these filters are stored:
* **********************
Extension Date 2
Planned Completion Date
Date of Agreement
Municipality
* */

/**
 * Translates report filters from ARFilters to a configuration that is applicable for Mondrian Reports API.
 * Old AmpARFilter structure stores multiple information like the actual report filters, report settings, sorting info...  
 * @author Nadejda Mandrescu
 */
public class AmpARFilterConverter {
	protected static final Logger logger = Logger.getLogger(AmpARFilterConverter.class);
	
	//either transform filter by IDS, either by Names => if by IDS, then Level properties will be used
	private MondrianReportFilters filterRules;
	private ReportSettingsImpl settings;
	private AmpARFilter arFilter;
	private boolean isPledgeFilter;

	/**
	 * Translates report filters from ARFilters to a configuration that is applicable for Mondrian Reports API.
	 * Old AmpARFilter structure stores multiple information: the actual report filters, report settings, sorting info...
	 * @param arFilter - old configuration of the reports filters {@link AmpARFilter}
	 */
	public AmpARFilterConverter(AmpARFilter arFilter) {
		this.setArFilter(arFilter);
	}
	
	public MondrianReportFilters buildFilters() {
		filterRules = new MondrianReportFilters();
		filterRules.setCalendar(arFilter.getCalendarType());
		buildCurrentFilters();
		//TODO: to clarify how pledge specific filters should be applied, e.g. if "include pledges" is selected, then translate current filter into pledge filter?
		/*
		AmpARFilter tmpArFilter = arFilter;
		arFilter = arFilter.asPledgeFilter();
		buildCurrentFilters();
		arFilter = tmpArFilter;
		*/
		
		return filterRules;
	}
	
	private void buildCurrentFilters() {
		this.isPledgeFilter = arFilter.isPledgeFilter();

		addProjectFilters();
		
		addOrganizationsFilters();
		addSectorFilters();
		addProgramAndNationalObjectivesFilters();
		
		addLocationFilters();
		
		//financing
		addFinancingFilters();
		//dates
		addFundingDatesFilters();
		addActivityDatesFilters();
	}
	
	private void addProjectFilters() {
		if (arFilter.isPledgeFilter()) return;
		addCategoryValueNamesFilter(arFilter.getStatuses(), ColumnConstants.STATUS);
		addFilter(arFilter.getWorkspaces(), ColumnConstants.TEAM);
		addApprovalStatus();
		addBooleansFilter(arFilter.getHumanitarianAid(), ColumnConstants.HUMANITARIAN_AID);
		addBooleansFilter(arFilter.getDisasterResponse(), ColumnConstants.DISASTER_RESPONSE_MARKER);
		addBooleanFilter(arFilter.getGovernmentApprovalProcedures(), ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES);
		addBooleanFilter(arFilter.getJointCriteria(), ColumnConstants.JOINT_CRITERIA);
		addBooleanFilter(arFilter.getShowArchived(), ColumnConstants.ARCHIVED);
		addCategoryValueNamesFilter(arFilter.getProjectImplementingUnits(), ColumnConstants.PROJECT_IMPLEMENTING_UNIT);
		addCategoryValueNamesFilter(arFilter.getActivityPledgesTitle(), ColumnConstants.PLEDGES_TITLES);
	}
	
	/**
	 * builds a multiple-choices boolean filter
	 * @param vals
	 * @param columnName
	 */
	private void addBooleansFilter(Set<Integer> vals, String columnName) {
		if (arFilter.isPledgeFilter()) return;
		if (vals == null) return;
		List<String> values = new ArrayList<>();
		for(int v:vals) {
			values.add(Integer.toString(v));
		}
		filterRules.addFilterRule(new ReportColumn(columnName), new FilterRule(values, true));
	}
	
	private void addApprovalStatus() {
		if (arFilter.getApprovalStatusSelected() == null || arFilter.getApprovalStatusSelected().size() == 0) 
			return;
		List<String> values = new ArrayList<String>(arFilter.getApprovalStatusSelected());
		filterRules.addFilterRule(new ReportColumn(ColumnConstants.APPROVAL_STATUS), new FilterRule(arFilter.getApprovalStatusSelectedStrings(), values, true));
	}
	
	private void addFundingDatesFilters() {
		try {
			if (arFilter.getYearFrom() != null || arFilter.getYearTo() != null) {
				if (arFilter.getYearFrom() == arFilter.getYearTo()) {
					filterRules.addSingleYearFilterRule(arFilter.getYearFrom(), true);
				} else {
					filterRules.addYearsRangeFilterRule(arFilter.getYearFrom(), arFilter.getYearTo());
				}
			}
	
			Date from = arFilter.buildFromDateAsDate();
			Date to = arFilter.buildToDateAsDate();
			if (from != null || to != null) {
				if (from != null && from.equals(to)) {
					filterRules.addSingleDateFilterRule(from, true);
				} else { 
					filterRules.addDateRangeFilterRule(from, to);
				}
			}
			filterRules.setComputedYear(arFilter.getComputedYear());
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		}
	}
	
	private void addActivityDatesFilters() {
		addActivityDateFilter(arFilter.buildFromAndToActivityStartDateAsDate(), ColumnConstants.ACTUAL_START_DATE);
		addActivityDateFilter(arFilter.buildFromAndToProposedApprovalDateAsDate(), ColumnConstants.PROPOSED_APPROVAL_DATE);
		addActivityDateFilter(arFilter.buildFromAndToActivityActualCompletionDateAsDate(), ColumnConstants.ACTUAL_COMPLETION_DATE);
		addActivityDateFilter(arFilter.buildFromAndToActivityFinalContractingDateAsDate(), ColumnConstants.FINAL_DATE_FOR_CONTRACTING);
	}
	
	private void addActivityDateFilter(Date[] fromTo, String columnName) {
		if (fromTo == null || fromTo.length != 2 || (fromTo[0] == null && fromTo[1] == null)) return;
		try {
			filterRules.addDateRangeFilterRule(new ReportColumn(columnName), fromTo[0], fromTo[1]);
		} catch (AmpApiException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	private void addOrganizationsFilters() {
		//Donor Agencies
		addFilter(arFilter.getDonorTypes(), ColumnConstants.DONOR_TYPE);
		addFilter(arFilter.getDonorGroups(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_DONOR_GROUP : ColumnConstants.DONOR_GROUP));
		addFilter(arFilter.getDonnorgAgency(), ColumnConstants.DONOR_AGENCY);
		
		//Related Agencies
		addFilter(arFilter.getImplementingAgency(), ColumnConstants.IMPLEMENTING_AGENCY);
		addFilter(arFilter.getExecutingAgency(), ColumnConstants.EXECUTING_AGENCY);
		addFilter(arFilter.getBeneficiaryAgency(), ColumnConstants.BENEFICIARY_AGENCY);
		addFilter(arFilter.getImplementingAgency(), ColumnConstants.RESPONSIBLE_ORGANIZATION);
		addFilter(arFilter.getContractingAgency(), ColumnConstants.CONTRACTING_AGENCY);
		//related agencies groups
		addFilter(arFilter.getContractingAgencyGroups(), ColumnConstants.CONTRACTING_AGENCY_GROUPS);
	}
	
	/** adds primary, secondary and tertiary sectors to the filters if specified */
	private void addSectorFilters() {
		if (arFilter.getSelectedSectors() != null && !arFilter.getSelectedSectors().isEmpty()) {
			Map<Long, AmpSector> sectorsByIds = new HashMap<>();
			for (AmpSector sec : arFilter.getSelectedSectors()) {
				sectorsByIds.put(sec.getAmpSectorId(), sec);
			}
			Map<String, List<NameableOrIdentifiable>> sectorsByScheme = distributeEntities(
					SectorUtil.distributeSectorsByScheme(arFilter.getSelectedSectors()), sectorsByIds);
			addFilter(sectorsByScheme.get("Primary"), (arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECTORS
					: ColumnConstants.PRIMARY_SECTOR));
		}
		if (arFilter.getSelectedSecondarySectors() != null && !arFilter.getSelectedSecondarySectors().isEmpty()) {
			Map<Long, AmpSector> sectorsByIds = new HashMap<>();
			for (AmpSector sec : arFilter.getSelectedSecondarySectors()) {
				sectorsByIds.put(sec.getAmpSectorId(), sec);
			}
			Map<String, List<NameableOrIdentifiable>> sectorsByScheme = distributeEntities(
					SectorUtil.distributeSectorsByScheme(arFilter.getSelectedSecondarySectors()), sectorsByIds);
			addFilter(sectorsByScheme.get("Secondary"), (arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECONDARY_SECTORS
					: ColumnConstants.SECONDARY_SECTOR));
		}
		if (arFilter.getSelectedTertiarySectors() != null && !arFilter.getSelectedTertiarySectors().isEmpty()) {
			Map<Long, AmpSector> sectorsByIds = new HashMap<>();
			for (AmpSector sec : arFilter.getSelectedTertiarySectors()) {
				sectorsByIds.put(sec.getAmpSectorId(), sec);
			}
			Map<String, List<NameableOrIdentifiable>> sectorsByScheme = distributeEntities(
					SectorUtil.distributeSectorsByScheme(arFilter.getSelectedTertiarySectors()), sectorsByIds);
			addFilter(sectorsByScheme.get("Tertiary"), (arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_TERTIARY_SECTORS
					: ColumnConstants.TERTIARY_SECTOR));
		}
	}
	
	protected Map<String, List<NameableOrIdentifiable>> distributeEntities(Map<String, List<Long>> distributedIds, Map<Long, ? extends NameableOrIdentifiable> input) {
		Map<String, List<NameableOrIdentifiable>> res = new HashMap<>();
		
		for(String scheme:distributedIds.keySet()) {
			res.put(scheme, new ArrayList<NameableOrIdentifiable>());
			for(Long id:distributedIds.get(scheme)) {
				NameableOrIdentifiable entity = input.get(id);
				if (entity == null)
					throw new RuntimeException("bug while restoring backmap for id: " + id + ", scheme: " + scheme);
				res.get(scheme).add(entity);
			}
		}			
		return res;
	};
	
	/** adds programs and national objectives filters */
	private void addProgramAndNationalObjectivesFilters() {
		addFilter(arFilter.getSelectedPrimaryPrograms(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_PROGRAMS : ColumnConstants.PRIMARY_PROGRAM));
		addFilter(arFilter.getSelectedSecondaryPrograms(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECONDARY_PROGRAMS : ColumnConstants.SECONDARY_PROGRAM));
		
		//TODO: how to detect tertiary programs
		//addFilter(arFilter.get(), 
		//		(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_TERTIARY_PROGRAMS : ColumnConstants.TERTIARY_PROGRAM), entityType);
		
		addFilter(arFilter.getSelectedNatPlanObj(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES : ColumnConstants.NATIONAL_PLANNING_OBJECTIVES));
		
		if (!arFilter.isPledgeFilter()) {
			//TBD national plan objectives levels 1-8?
		}
	}
	
	private void addLocationFilters() {
		if (arFilter.isPledgeFilter()) { 
			if (arFilter.getPledgesLocations() == null || arFilter.getPledgesLocations().size() == 0) return;
		} else if (arFilter.getLocationSelected() == null || arFilter.getLocationSelected().size() == 0) return;
		
		Set<AmpCategoryValueLocations> countries = new HashSet<AmpCategoryValueLocations>();
		Set<AmpCategoryValueLocations> regions = new HashSet<AmpCategoryValueLocations>();
		Set<AmpCategoryValueLocations> zones = new HashSet<AmpCategoryValueLocations>();
		Set<AmpCategoryValueLocations> districts = new HashSet<AmpCategoryValueLocations>();
		Set<AmpCategoryValueLocations> locations = new HashSet<AmpCategoryValueLocations>();
		
		Collection<AmpCategoryValueLocations> filterLocations = arFilter.isPledgeFilter() ? 
				arFilter.getPledgesLocations() : arFilter.getLocationSelected();
		
		for(AmpCategoryValueLocations loc : filterLocations) {
			if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(loc.getParentCategoryValue()))
				countries.add(loc);
			else if (CategoryConstants.IMPLEMENTATION_LOCATION_REGION.equalsCategoryValue(loc.getParentCategoryValue()))
				regions.add(loc);
			else if (CategoryConstants.IMPLEMENTATION_LOCATION_ZONE.equalsCategoryValue(loc.getParentCategoryValue()))
				zones.add(loc);
			else if (CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT.equalsCategoryValue(loc.getParentCategoryValue()))
				districts.add(loc);
			else
				locations.add(loc);
		}
		
		addFilter(countries, ColumnConstants.COUNTRY);
		addFilter(regions, ColumnConstants.REGION);
		addFilter(zones, ColumnConstants.ZONE);
		addFilter(districts, ColumnConstants.DISTRICT);
		//addIdsFilter(locations, ColumnConstants.??); //TODO:
	}
	
	/**
	 * Adds values (ids/names) list to the filter rules
	 * @param filterRules - filter rules storage
	 * @param set - a collection of {@link Identifiable} objects to retrieve the values from
	 * @param columnName - column name of the to apply the rule over or null if this is a custom ElementType
	 * @param type - column type or null if elemType is provided
	 */
	private void addFilter(Collection<? extends NameableOrIdentifiable> set, String columnName) {
		if (set == null || set.size() == 0) return;
		List<String> values = new ArrayList<String>(set.size());
		List<String> names = new ArrayList<String>(set.size());
		for (NameableOrIdentifiable identifiable: set) { 
			names.add(identifiable.getName());
			final String value = identifiable.getIdentifier().toString();  
			values.add(value);
		}
		
		addFilterRule(columnName, new FilterRule(names, values, true));
	}
	
	/**
	 * adds a single-choice boolean filter
	 * @param flag
	 * @param columnName
	 */
	private void addBooleanFilter(Boolean flag, String columnName) {
		if(flag == null) return;
		addFilterRule(columnName, 
				new FilterRule(flag ? MoConstants.BOOLEAN_TRUE_KEY : MoConstants.BOOLEAN_FALSE_KEY, 
						flag.toString(), true));
	}
	
	private void addFilterRule(String columnName, FilterRule rule) {
		filterRules.addFilterRule(new ReportColumn(columnName), rule);
	}
	
	private void addFinancingFilters() {
		addCategoryValueNamesFilter(arFilter.getFinancingInstruments(), ColumnConstants.FINANCING_INSTRUMENT);
		addCategoryValueNamesFilter(arFilter.getFundingStatus(), ColumnConstants.FUNDING_STATUS);
		addCategoryValueNamesFilter(arFilter.getAidModalities(), ColumnConstants.PLEDGES_AID_MODALITY);
		addCategoryValueNamesFilter(arFilter.getTypeOfAssistance(), ColumnConstants.TYPE_OF_ASSISTANCE);
		addCategoryValueNamesFilter(arFilter.getModeOfPayment(), ColumnConstants.MODE_OF_PAYMENT);
		//TODO capital vs Recurrent
		//addCategoryValueNamesFilter(arFilter.get, ColumnConstants., ReportEntityType.ENTITY_TYPE_ACTIVITY);
		addCategoryValueNamesFilter(arFilter.getBudget(), ColumnConstants.ON_OFF_TREASURY_BUDGET);
	}
	
	private void addCategoryValueNamesFilter(Set<AmpCategoryValue> set, String columnName) {
		if (set == null || set.size() == 0) return;
		List<String> names = new ArrayList<String>(set.size());
		List<String> values = new ArrayList<String>(set.size());
		for (AmpCategoryValue categValue: set) {
			names.add(categValue.getValue());
			final String value = String.valueOf(categValue.getId());
			values.add(value);
		}
		addFilterRule(columnName, new FilterRule(names, values, true));
	}
	
	public ReportSettingsImpl buildSettings() {
		settings = new ReportSettingsImpl();
		settings.setUnitsOption(arFilter.getUnitsOptions()); // might be null and we are ok with that - the getter will never return a null
		addCurrencySettings();
		addDateSettings();
		
		return settings;
	}
	
	private void addCurrencySettings() {
		if (arFilter.getCurrency() != null)
			settings.setCurrencyCode(arFilter.getCurrency().getCurrencyCode());
		if (arFilter.getCurrentFormat() != null)
			settings.setCurrencyFormat(arFilter.getCurrentFormat());
	}
	
	/**
	 * Adds any display filter settings and calendar settings
	 */
	private void addDateSettings() {
		settings.setCalendar(arFilter.getCalendarType());
		try {
			if (arFilter.getRenderStartYear() != null || arFilter.getRenderEndYear() != null) {
				settings.addYearsRangeFilterRule(
						(arFilter.getRenderStartYear() == -1 ? 1950 : arFilter.getRenderStartYear()), // 1950 / 2150 are ugly hacks because the API does not support both NULLs (e.g. unlimited) 
						(arFilter.getRenderEndYear() == -1 ? 2150 : arFilter.getRenderEndYear())
						);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}


	/**
	 * @return the arFilter
	 */
	public AmpARFilter getArFilter() {
		return arFilter;
	}

	/**
	 * @param arFilter the arFilter to set
	 */
	public void setArFilter(AmpARFilter arFilter) {
		this.arFilter = arFilter;
	}
}
