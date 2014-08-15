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
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Nameable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Translates report filters from ARFilters to a configuration that is applicable for Mondrian Reports API
 * @author Nadejda Mandrescu
 */
public class AmpARFilterTranslator {
	//either transform filter by IDS, either by Names => if by IDS, then Level properties will be used
	private Map<ReportElement, FilterRule> filterRules;
	private static final boolean USE_IDS = false;
	private AmpARFilter arFilter;
	ReportEntityType entityType;

	public AmpARFilterTranslator(AmpARFilter arFilter) {
		this.setArFilter(arFilter);
	}
	
	public Map<ReportElement, FilterRule> buildFilters() {
		filterRules = new HashMap<ReportElement, FilterRule>();
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
		this.entityType = arFilter.isPledgeFilter() ? ReportEntityType.ENTITY_TYPE_PLEDGE : ReportEntityType.ENTITY_TYPE_ACTIVITY;

		addOrganizationsFilters();
		addSectorFilters();
		addProgramAndNationalObjectivesFilters();
		
		addLocationFilters();
		
		//financing
		addFinancingFilters();
		
		addFundingDatesFilters();
	}
	
	private void addFundingDatesFilters() {
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
	
	private void addOrganizationsFilters() {
		//Donor Agencies
		addFilter(arFilter.getDonorTypes(), ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(arFilter.getDonorGroups(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_DONOR_GROUP : ColumnConstants.DONOR_GROUP), entityType);
		addFilter(arFilter.getDonnorgAgency(), ColumnConstants.DONOR_AGENCY, ReportEntityType.ENTITY_TYPE_ALL);
		
		//Related Agencies
		addFilter(arFilter.getImplementingAgency(), ColumnConstants.IMPLEMENTING_AGENCY, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(arFilter.getExecutingAgency(), ColumnConstants.EXECUTING_AGENCY, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(arFilter.getBeneficiaryAgency(), ColumnConstants.BENEFICIARY_AGENCY, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(arFilter.getImplementingAgency(), ColumnConstants.RESPONSIBLE_ORGANIZATION, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(arFilter.getContractingAgency(), ColumnConstants.CONTRACTING_AGENCY, ReportEntityType.ENTITY_TYPE_ALL);
		//TODO: Secondary Beneficiary Agency Group	
	}
	
	/** adds primary, secondary and tertiary sectors to the filters if specified */
	private void addSectorFilters() { 
		addFilter(removeDescendents(arFilter.getSelectedSectors(), 0), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECTORS : ColumnConstants.PRIMARY_SECTOR), entityType);
		addFilter(removeDescendents(arFilter.getSelectedSecondarySectors(), 1), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECONDARY_SECTORS : ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), entityType);
		addFilter(arFilter.getSelectedTertiarySectors(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_TERTIARY_SECTORS : ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR), entityType);
	}
	
	private Set<AmpSector> removeDescendents(Set<AmpSector> parentsAndChildren, int parentsCount) {
		if (parentsAndChildren == null) return null;
		//create a new set to not alter the original
		//.getSelectedSecondarySectors() is null when getSelectedSectors() has secondary sectors, thus cannot just do sectors.removeAll(secondarySectors)
		Set<AmpSector> onlyParents = new HashSet<AmpSector>();
		for (AmpSector sector : parentsAndChildren) {
			int currParentsCount = 0;
			while (currParentsCount <= parentsCount && sector.getParentSectorId() != null) {
				sector = sector.getParentSectorId();
				currParentsCount ++;
			}
			if (currParentsCount == parentsCount) {
				onlyParents.add(sector);
			}
		}
		return onlyParents;
	}
	
	/** adds programs and national objectives filters */
	private void addProgramAndNationalObjectivesFilters() {
		addFilter(arFilter.getSelectedPrimaryPrograms(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_PROGRAMS : ColumnConstants.PRIMARY_PROGRAM), entityType);
		addFilter(arFilter.getSelectedSecondaryPrograms(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_SECONDARY_PROGRAMS : ColumnConstants.SECONDARY_PROGRAM), entityType);
		//TODO: how to detect tertiary programs
		//addFilter(arFilter.get(), 
		//		(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_TERTIARY_PROGRAMS : ColumnConstants.TERTIARY_PROGRAM), entityType);
		
		addFilter(arFilter.getSelectedNatPlanObj(), 
				(arFilter.isPledgeFilter() ? ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES : ColumnConstants.NATIONAL_PLANNING_OBJECTIVES), entityType);
		
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
		
		addFilter(countries, ColumnConstants.COUNTRY, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(regions, ColumnConstants.REGION, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(zones, ColumnConstants.ZONE, ReportEntityType.ENTITY_TYPE_ALL);
		addFilter(districts, ColumnConstants.DISTRICT, ReportEntityType.ENTITY_TYPE_ALL);
		//addIdsFilter(locations, ColumnConstants.??); //TODO:
	}
	
	private void addFilter(Set<? extends NameableOrIdentifiable> set, String columnName, 
			ReportEntityType type) {
		addFilter(set, columnName, type, null);
	}
	
	/**
	 * Adds values (ids/names) list to the filter rules
	 * @param filterRules - filter rules storage
	 * @param set - a collection of {@link Identifiable} objects to retrieve the values from
	 * @param columnName - column name of the to apply the rule over or null if this is a custom ElementType
	 * @param type - column type or null if elemType is provided
	 * @param elemType - element type or null if columnName is provided
	 */
	private void addFilter(Collection<? extends NameableOrIdentifiable> set, String columnName, 
			ReportEntityType type, ReportElement.ElementType elemType) {
		if (set == null || set.size() == 0) return;
		List<String> values = new ArrayList<String>(set.size());
		if (USE_IDS)
			for (Identifiable identifiable: set) { 
				values.add(identifiable.getIdentifier().toString());
			}
		else
			for (Nameable identifiable: set) { 
				values.add(identifiable.getName());
			}
		if (elemType ==null )
			filterRules.put(new ReportElement(new ReportColumn(columnName, type)), new FilterRule(values, true, USE_IDS));
		else
			filterRules.put(new ReportElement(elemType), new FilterRule(values, true, USE_IDS));
	}
	
	private void addFinancingFilters() {
		addCategoryValueNamesFilter(arFilter.getFinancingInstruments(), ColumnConstants.FINANCING_INSTRUMENT, ReportEntityType.ENTITY_TYPE_ALL);
		addCategoryValueNamesFilter(arFilter.getAidModalities(), ColumnConstants.MODALITIES, ReportEntityType.ENTITY_TYPE_ACTIVITY);
		addCategoryValueNamesFilter(arFilter.getTypeOfAssistance(), ColumnConstants.TYPE_OF_ASSISTANCE, ReportEntityType.ENTITY_TYPE_ACTIVITY);
		//TODO capital vs Recurrent
		//addCategoryValueNamesFilter(arFilter.get, ColumnConstants., ReportEntityType.ENTITY_TYPE_ACTIVITY);
		addCategoryValueNamesFilter(arFilter.getBudget(), ColumnConstants.ON_OFF_TREASURY_BUDGET, ReportEntityType.ENTITY_TYPE_ACTIVITY);
	}
	
	private void addCategoryValueNamesFilter(Set<AmpCategoryValue> set, String columnName, ReportEntityType type) {
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
