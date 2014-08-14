/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * Translates report filters from ARFilters to a configuration that is applicable for Mondrian Reports API
 * @author Nadejda Mandrescu
 */
public class MondrianReportFilters implements ReportFilters {
	private AmpARFilter arFilter;
	private GroupingCriteria groupingCriteria;
	ReportEntityType entityType;

	public MondrianReportFilters(AmpARFilter arFilter, GroupingCriteria groupingCriteria) {
		this.setArFilter(arFilter);
	}
	
	@Override
	public Map<ReportElement, FilterRule> getFilterRules() {
		Map<ReportElement, FilterRule> filterRules = new HashMap<ReportElement, FilterRule>();

		this.entityType = arFilter.isPledgeFilter() ? ReportEntityType.ENTITY_TYPE_PLEDGE : ReportEntityType.ENTITY_TYPE_ACTIVITY;
		//TODO: to clarify how pledge specific filters should be applied, e.g. if "include pledges" is selected, then translate current filter into pledge filter?
		
		addFilters(filterRules);
		
		return filterRules;
	}
	
	private void addFilters(Map<ReportElement, FilterRule> filterRules) {
		addOrganizationsFilters(filterRules);
		addSectorFilters(filterRules);
		addProgramAndNationalObjectivesFilters(filterRules);
		
		addLocationFilters(filterRules);
		
		//financing
		addFinancingFilters(filterRules);
		
		addFundingDatesFilters(filterRules);
	}
	
	private void addFundingDatesFilters(Map<ReportElement, FilterRule> filterRules) {
		//if (!arFilter.hasDateFilter()) return;
		String min = arFilter.getYearFrom() == null ? null : arFilter.getYearFrom().toString();
		String max = arFilter.getYearTo() == null ? null : arFilter.getYearTo().toString();
		if (min !=null || max !=null) 
			filterRules.put(new ReportElement(ElementType.YEAR), new FilterRule(min, max, true, true, false));

		min = null;
		max = null;
				
		if (arFilter.buildFromDateAsDate() != null) {
			//int minDate  = DateTimeUtil.toJulianDayNumber(arFilter.buildFromDateAsDate());
			String minDate = (new SimpleDateFormat(MoConstants.DATE_FORMAT)).format(arFilter.buildFromDateAsDate()); 
			min = String.valueOf(minDate);
		}
		if (arFilter.buildToDateAsDate() != null) {
			//int maxDate = DateTimeUtil.toJulianDayNumber(arFilter.buildToDateAsDate());
			String maxDate = (new SimpleDateFormat(MoConstants.DATE_FORMAT)).format(arFilter.buildToDateAsDate());
			max = String.valueOf(maxDate);
		}
		if (min !=null || max !=null) 
			filterRules.put(new ReportElement(ElementType.DATE), new FilterRule(min, max, true, true, false));
	}
	
	private void addActivityDatesFilters(Map<ReportElement, FilterRule> filterRules) {
		
	}
	
	private void addOrganizationsFilters(Map<ReportElement, FilterRule> filterRules) {
		//Donor Agencies
		addIdsFilter(filterRules, arFilter.getDonorTypes(), ColumnConstants.DONOR_TYPE);
		addIdsFilter(filterRules, arFilter.getDonorGroups(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_DONOR_GROUP : ColumnConstants.DONOR_GROUP), entityType);
		addIdsFilter(filterRules, arFilter.getDonnorgAgency(), ColumnConstants.DONOR_AGENCY);
		
		//Related Agencies
		addIdsFilter(filterRules, arFilter.getImplementingAgency(), ColumnConstants.IMPLEMENTING_AGENCY);
		addIdsFilter(filterRules, arFilter.getExecutingAgency(), ColumnConstants.EXECUTING_AGENCY);
		addIdsFilter(filterRules, arFilter.getBeneficiaryAgency(), ColumnConstants.BENEFICIARY_AGENCY);
		addIdsFilter(filterRules, arFilter.getImplementingAgency(), ColumnConstants.RESPONSIBLE_ORGANIZATION);
		addIdsFilter(filterRules, arFilter.getContractingAgency(), ColumnConstants.CONTRACTING_AGENCY);
	}
	
	/** adds primary, secondary and tertiary sectors to the filters if specified */
	private void addSectorFilters(Map<ReportElement, FilterRule> filterRules) { 
		addIdsFilter(filterRules, arFilter.getSelectedSectors(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECTORS : ColumnConstants.PRIMARY_SECTOR), entityType);
		addIdsFilter(filterRules, arFilter.getSelectedSecondarySectors(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECONDARY_SECTORS : ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), entityType);
		addIdsFilter(filterRules, arFilter.getSelectedTertiarySectors(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_TERTIARY_SECTORS : ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR), entityType);
	}
	
	/** adds programs and national objectives filters */
	private void addProgramAndNationalObjectivesFilters(Map<ReportElement, FilterRule> filterRules) {
		addIdsFilter(filterRules, arFilter.getSelectedPrimaryPrograms(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_PROGRAMS : ColumnConstants.PRIMARY_PROGRAM), entityType);
		addIdsFilter(filterRules, arFilter.getSelectedSecondaryPrograms(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECONDARY_PROGRAMS : ColumnConstants.SECONDARY_PROGRAM), entityType);
		addIdsFilter(filterRules, arFilter.getSelectedTertiarySectors(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_TERTIARY_PROGRAMS : ColumnConstants.TERTIARY_PROGRAM), entityType);
		
		addIdsFilter(filterRules, arFilter.getSelectedNatPlanObj(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES : ColumnConstants.NATIONAL_PLANNING_OBJECTIVES), entityType);
		
		if (!arFilter.isPledgeFilter()) {
			//TBD national plan objectives levels 1-8?
		}
	}
	
	private void addLocationFilters(Map<ReportElement, FilterRule> filterRules) {
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
		
		addIdsFilter(filterRules, countries, ColumnConstants.COUNTRY);
		addIdsFilter(filterRules, regions, ColumnConstants.REGION);
		addIdsFilter(filterRules, zones, ColumnConstants.ZONE);
		addIdsFilter(filterRules, districts, ColumnConstants.DISTRICT);
		//addIdsFilter(filterRules, locations, ColumnConstants.??); //TODO:
	}
	
	private void addIdsFilter(Map<ReportElement, FilterRule> filterRules, Set<? extends Identifiable> set, String columnName) {
		addIdsFilter(filterRules, set, columnName, ReportEntityType.ENTITY_TYPE_ALL, null);
	}
	
	private void addIdsFilter(Map<ReportElement, FilterRule> filterRules, Set<? extends Identifiable> set, String columnName, 
			ReportEntityType type) {
		addIdsFilter(filterRules, set, columnName, type, null);
	}
	
	/**
	 * Adds ids list to the filter rules
	 * @param filterRules - filter rules storage
	 * @param set - a collection of {@link Identifiable} objects to retrive the ids from
	 * @param columnName - column name of the to apply the rule over or null if this is a custom ElementType
	 * @param type - column type or null if elemType is provided
	 * @param elemType - element type or null if columnName is provided
	 */
	private void addIdsFilter(Map<ReportElement, FilterRule> filterRules, Collection<? extends Identifiable> set, String columnName, 
			ReportEntityType type, ReportElement.ElementType elemType) {
		if (set == null || set.size() == 0) return;
		List<String> ids = new ArrayList<String>(set.size());
		for (Identifiable identifiable: set) { 
			ids.add(identifiable.getIdentifier().toString());
		}
		if (elemType ==null )
			filterRules.put(new ReportElement(new ReportColumn(columnName, type)), new FilterRule(ids, true, true));
		else
			filterRules.put(new ReportElement(elemType), new FilterRule(ids, true, true));
	}
	
	private void addFinancingFilters(Map<ReportElement, FilterRule> filterRules) {
		addCategoryValueNamesFilter(filterRules, arFilter.getFinancingInstruments(), ColumnConstants.FINANCING_INSTRUMENT, ReportEntityType.ENTITY_TYPE_ALL);
		addCategoryValueNamesFilter(filterRules, arFilter.getAidModalities(), ColumnConstants.MODALITIES, ReportEntityType.ENTITY_TYPE_ACTIVITY);
		addCategoryValueNamesFilter(filterRules, arFilter.getTypeOfAssistance(), ColumnConstants.TYPE_OF_ASSISTANCE, ReportEntityType.ENTITY_TYPE_ACTIVITY);
		//TODO capital vs Recurrent
		//addCategoryValueNamesFilter(filterRules, arFilter.get, ColumnConstants., ReportEntityType.ENTITY_TYPE_ACTIVITY);
		addCategoryValueNamesFilter(filterRules, arFilter.getBudget(), ColumnConstants.ON_OFF_TREASURY_BUDGET, ReportEntityType.ENTITY_TYPE_ACTIVITY);
	}
	
	private void addCategoryValueNamesFilter(Map<ReportElement, FilterRule> filterRules, Set<AmpCategoryValue> set, String columnName, ReportEntityType type) {
		if (set == null || set.size() == 0) return;
		//TODO: can we filter by ids? no schema definition is available yet 
		List<String> names = new ArrayList<String>(set.size());
		for (AmpCategoryValue categValue: set) { 
			names.add(categValue.getValue());
		}
		filterRules.put(new ReportElement(new ReportColumn(columnName, type)), new FilterRule(names, true, false));
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
