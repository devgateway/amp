/**
 * 
 */
package org.dgfoundation.amp.reports.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * Translates report filters from ARFilters to a configuration that is applicable for Reports API.
 * Old AmpARFilter structure stores multiple information like the actual report filters, report settings,
 * sorting info...
 *
 * TODO AMP-26970 activity columns are converted to pledge columns here and in AmpFiltersConverter as well.
 *
 * @author Nadejda Mandrescu
 */
public class AmpARFilterConverter {
    protected static final Logger logger = Logger.getLogger(AmpARFilterConverter.class);
    
    //either transform filter by IDS, either by Names => if by IDS, then Level properties will be used
    private AmpReportFilters filterRules;
    private ReportSettingsImpl settings;
    private AmpARFilter arFilter;

    /**
     * Translates report filters from ARFilters to a configuration that is applicable for Reports API.
     * Old AmpARFilter structure stores multiple information: the actual report filters, report settings, sorting info...
     * @param arFilter - old configuration of the reports filters {@link AmpARFilter}
     */
    public AmpARFilterConverter(AmpARFilter arFilter) {
        this.setArFilter(arFilter);
    }
    
    public AmpReportFilters buildFilters() {
        filterRules = new AmpReportFilters(arFilter.getCalendarType());
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
        addCategoryValueNamesFilter(arFilter.getPerformanceAlertLevel(), ColumnConstants.PERFORMANCE_ALERT_LEVEL);
        addPerformanceAlertTypeFilter();
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
    
    private void addPerformanceAlertTypeFilter() {
        if (arFilter.getPerformanceAlertType() != null && !arFilter.getPerformanceAlertType().isEmpty()) {
            List<String> values = new ArrayList<String>(arFilter.getPerformanceAlertType());
            filterRules.addFilterRule(new ReportColumn(ColumnConstants.PERFORMANCE_ALERT_TYPE),
                    new FilterRule(values, values, true));
        }
    }
    
    private void addFundingDatesFilters() {
        try {
            if (arFilter.getYearFrom() != null || arFilter.getYearTo() != null) {
                if (arFilter.getYearFrom().equals(arFilter.getYearTo())) {
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
        addActualAppYear();
        addActivityDateFilter(arFilter.buildFromAndToActivityStartDateAsDate(), ColumnConstants.ACTUAL_START_DATE);
        addActivityDateFilter(arFilter.buildFromAndToProposedApprovalDateAsDate(), ColumnConstants.PROPOSED_APPROVAL_DATE);
        addActivityDateFilter(arFilter.buildFromAndToProposedStartDateAsDate(), ColumnConstants.PROPOSED_START_DATE);
        addActivityDateFilter(arFilter.buildFromAndToActivityActualCompletionDateAsDate(), ColumnConstants.ACTUAL_COMPLETION_DATE);
        addActivityDateFilter(arFilter.buildFromAndToActivityFinalContractingDateAsDate(), ColumnConstants.FINAL_DATE_FOR_CONTRACTING);
        addActivityDateFilter(arFilter.buildFromAndToEffectiveFundingDateAsDate(), ColumnConstants.EFFECTIVE_FUNDING_DATE);
        addActivityDateFilter(arFilter.buildFromAndToFundingClosingDateAsDate(), ColumnConstants.FUNDING_CLOSING_DATE);
        addActivityDateFilter(arFilter.buildFromAndToIssueDateAsDate(), ColumnConstants.ISSUE_DATE);
    }

    /**
     * old filter widget uses year for actual approval
     * new filter widget uses date for actual approval
     */
    private void addActualAppYear() {
        try {
            if (arFilter.getActualAppYear() != null) {
                Date from = DateTimeUtil.firstDayOfYear(arFilter.getActualAppYear());
                Date to = DateTimeUtil.lastDayOfYear(arFilter.getActualAppYear());
                filterRules.addDateRangeFilterRule(new ReportColumn(ColumnConstants.ACTUAL_APPROVAL_DATE), from, to);
            }
        } catch (AmpApiException ex) {
            logger.error(ex.getMessage(), ex);
        }
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
        addFilter(arFilter.getDonorGroups(), ColumnConstants.DONOR_GROUP);
        addFilter(arFilter.getDonnorgAgency(), ColumnConstants.DONOR_AGENCY);
        
        //Related Agencies
        addFilter(arFilter.getImplementingAgency(), ColumnConstants.IMPLEMENTING_AGENCY);
        addFilter(arFilter.getExecutingAgency(), ColumnConstants.EXECUTING_AGENCY);
        addFilter(arFilter.getBeneficiaryAgency(), ColumnConstants.BENEFICIARY_AGENCY);
        addFilter(arFilter.getResponsibleorg(), ColumnConstants.RESPONSIBLE_ORGANIZATION);
        addFilter(arFilter.getComponentFunding(), ColumnConstants.COMPONENT_FUNDING_ORGANIZATION);
        addFilter(arFilter.getComponentSecondResponsible(), ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION);
        addFilter(arFilter.getContractingAgency(), ColumnConstants.CONTRACTING_AGENCY);
        //related agencies groups
        addFilter(arFilter.getContractingAgencyGroups(), ColumnConstants.CONTRACTING_AGENCY_GROUPS);
    }
    
    /** adds primary, secondary and tertiary sectors to the filters if specified */
    private void addSectorFilters() {
        addSectorSchemeFilters(arFilter.getSelectedSectors(), "Primary", ColumnConstants.PRIMARY_SECTOR);
        addSectorSchemeFilters(arFilter.getSelectedSecondarySectors(), "Secondary", ColumnConstants.SECONDARY_SECTOR);
        addSectorSchemeFilters(arFilter.getSelectedTertiarySectors(), "Tertiary", ColumnConstants.TERTIARY_SECTOR);
        addSectorSchemeFilters(arFilter.getSelectedQuaternarySectors(), "Quaternary",
                ColumnConstants.QUATERNARY_SECTOR);
        addSectorSchemeFilters(arFilter.getSelectedQuinarySectors(), "Quinary", ColumnConstants.QUINARY_SECTOR);

        if (!arFilter.isPledgeFilter())
            addSectorSchemeFilters(arFilter.getSelectedTagSectors(), "Tag", ColumnConstants.SECTOR_TAG);
    }

    private void addSectorSchemeFilters(Set<AmpSector> selectedEntries, String scheme, String columnName) {
        if (selectedEntries == null || selectedEntries.isEmpty())
            return;

        Map<Long, AmpSector> sectorsByIds = selectedEntries.stream().collect(Collectors.toMap(z -> z.getAmpSectorId(), z -> z));
        Map<String, List<AmpSector>> sectorsByScheme = distributeEntities(SectorUtil.distributeSectorsByScheme(selectedEntries), sectorsByIds);


        List<AmpSector> ampSectors = sectorsByScheme.get(scheme);
        if (ampSectors != null) {
            ampSectors.stream()
                    .collect(Collectors.groupingBy(s -> findSubSectorColumnName(columnName, s)))
                    .forEach((levelColumn, levelSectors) -> addFilter(levelSectors, levelColumn));
        }
    }

    private String findSubSectorColumnName(String columnName, AmpSector sector) {
        AmpSector current = sector;
        int depth = 0;
        while (current.getParentSectorId() != null) {
            current = current.getParentSectorId();
            depth++;
        }
        String levelColumn;
        if (depth == 0) {
            levelColumn = columnName;
        } else {
            levelColumn = columnName + " " + StringUtils.repeat("Sub-", depth) + "Sector";
        }
        return levelColumn;
    }

    private <T> Map<String, List<T>> distributeEntities(Map<String, List<Long>> distributedIds, Map<Long, T> input) {
        Map<String, List<T>> res = new HashMap<>();
        
        for(String scheme:distributedIds.keySet()) {
            res.put(scheme, new ArrayList<>());
            for(Long id:distributedIds.get(scheme)) {
                T entity = input.get(id);
                if (entity == null)
                    throw new RuntimeException("bug while restoring backmap for id: " + id + ", scheme: " + scheme);
                res.get(scheme).add(entity);
            }
        }           
        return res;
    }

    /** adds programs and national objectives filters */
    private void addProgramAndNationalObjectivesFilters() {
        addMultiLevelFilter(arFilter.getSelectedPrimaryPrograms(), ColumnConstants.PRIMARY_PROGRAM);

        addMultiLevelFilter(arFilter.getSelectedSecondaryPrograms(), ColumnConstants.SECONDARY_PROGRAM);

        //TODO: how to detect tertiary programs
        //addFilter(arFilter.get(), ColumnConstants.TERTIARY_PROGRAM, entityType);

        addMultiLevelFilter(arFilter.getSelectedNatPlanObj(), ColumnConstants.NATIONAL_PLANNING_OBJECTIVES);
        
        if (!arFilter.isPledgeFilter()) {
            //TBD national plan objectives levels 1-8?
        }
    }

    private void addMultiLevelFilter(Collection<AmpTheme> themes, String columnName) {
        if (themes != null) {
            themes.stream()
                    .collect(Collectors.groupingBy(t -> findLevelColumnName(columnName, t)))
                    .forEach((levelColumnName, levelThemes) -> addFilter(levelThemes, levelColumnName));
        }
    }

    private String findLevelColumnName(String columnName, AmpTheme ampTheme) {
        AmpTheme current = ampTheme;
        int depth = 0;
        while (current.getParentThemeId() != null) {
            current = current.getParentThemeId();
            depth++;
        }
        return columnName + " Level " + depth;
    }
    
    private void addLocationFilters() {
        Collection<AmpCategoryValueLocations> filterLocations = arFilter.getLocationSelected();
        if (filterLocations == null || filterLocations.isEmpty())
            return;
        
        Set<AmpCategoryValueLocations> countries = new HashSet<>();
        Set<AmpCategoryValueLocations> regions = new HashSet<>();
        Set<AmpCategoryValueLocations> zones = new HashSet<>();
        Set<AmpCategoryValueLocations> districts = new HashSet<>();
        Set<AmpCategoryValueLocations> communalSections = new HashSet<>();

        for(AmpCategoryValueLocations loc : filterLocations) {
            AmpCategoryValue parentCatVal = loc.getParentCategoryValue();
            if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(parentCatVal)) {
                countries.add(loc);
            } else if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1.equalsCategoryValue(parentCatVal)) {
                regions.add(loc);
            } else if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_2.equalsCategoryValue(parentCatVal)) {
                zones.add(loc);
            } else if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_3.equalsCategoryValue(parentCatVal)) {
                districts.add(loc);
            } else if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_4.equalsCategoryValue(parentCatVal)) {
                communalSections.add(loc);
            }
        }

        addFilter(countries, ColumnConstants.LOCATION_ADM_LEVEL_0);
        addFilter(regions, ColumnConstants.LOCATION_ADM_LEVEL_1);
        addFilter(zones, ColumnConstants.LOCATION_ADM_LEVEL_2);
        addFilter(districts, ColumnConstants.LOCATION_ADM_LEVEL_3);
        addFilter(communalSections, ColumnConstants.LOCATION_ADM_LEVEL_4);
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
        Set<String> values = new LinkedHashSet<>(set.size());
        List<String> names = new ArrayList<String>(set.size());
        for (NameableOrIdentifiable identifiable: set) {
            final String value = identifiable.getIdentifier().toString();
            if (values.contains(value))
                continue;
            values.add(value);
            names.add(identifiable.getName());
        }
        
        addFilterRule(columnName, new FilterRule(names, new ArrayList<>(values), true));
    }
    
    /**
     * adds a single-choice boolean filter
     * @param flag
     * @param columnName
     */
    private void addBooleanFilter(Boolean flag, String columnName) {
        if(flag == null) return;
        addFilterRule(columnName, 
                new FilterRule(flag ? ArConstants.BOOLEAN_TRUE_KEY : ArConstants.BOOLEAN_FALSE_KEY, 
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
        addCategoryValueNamesFilter(arFilter.getExpenditureClass(), ColumnConstants.EXPENDITURE_CLASS);
        addCategoryValueNamesFilter(arFilter.getConcessionalityLevel(), ColumnConstants.CONCESSIONALITY_LEVEL);
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
                settings.setYearsRangeFilterRule(
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
