package org.dgfoundation.amp.reports.converters;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Session;
import org.hibernate.ObjectNotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * an AmpReportFilters -> AmpARFilter converter (e.g. the opposite of {@link AmpARFilterConverter})
 * @author ginchauspe, nmandrescu
 *
 */
public class AmpReportFiltersConverter {

    protected static final Logger logger = Logger.getLogger(AmpReportFiltersConverter.class);
    private AmpReportFilters filters;
    private AmpARFilter ampARFilter;
    private static final String TRANSACTION_DATE = "DATE";
    
    public AmpReportFiltersConverter(AmpReportFilters filters) {
        if (filters == null) {
            filters = new AmpReportFilters();
        }
        this.filters = filters;
    }

    public AmpReportFiltersConverter(AmpReportFilters filters, AmpARFilter ampARFilter) {
        this.filters = filters;
        this.ampARFilter = ampARFilter;
    }

    public void setReportFilters(AmpReportFilters filters) {
        this.filters = filters;
    }

    public AmpARFilter buildFilters() {
        if (this.ampARFilter == null) {
            this.ampARFilter = new AmpARFilter();
        }

        // Donors section.
        addFilter(ColumnConstants.DONOR_AGENCY, AmpOrganisation.class, "donnorgAgency", true);
        addFilter(ColumnConstants.DONOR_TYPE, AmpOrgType.class, "donorTypes", true);
        addFilter(ColumnConstants.DONOR_GROUP, AmpOrgGroup.class, "donorGroups", true);

        addFilter(ColumnConstants.PLEDGES_DONOR_TYPE, AmpOrgType.class, "donorTypes", true);
        addFilter(ColumnConstants.PLEDGES_DONOR_GROUP, AmpOrgGroup.class, "donorGroups", true);

        // Related organizations section.
        addFilter(ColumnConstants.BENEFICIARY_AGENCY_TYPE, AmpOrgType.class, "beneficiaryAgencyTypes", true);
        addFilter(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, AmpOrgGroup.class, "beneficiaryAgencyGroups", true);
        addFilter(ColumnConstants.BENEFICIARY_AGENCY, AmpOrganisation.class, "beneficiaryAgency", true);
        addFilter(ColumnConstants.EXECUTING_AGENCY_TYPE, AmpOrgType.class, "executingAgencyTypes", true);
        addFilter(ColumnConstants.EXECUTING_AGENCY_GROUPS, AmpOrgGroup.class, "executingAgencyGroups", true);
        addFilter(ColumnConstants.EXECUTING_AGENCY, AmpOrganisation.class, "executingAgency", true);
        addFilter(ColumnConstants.CONTRACTING_AGENCY_TYPE, AmpOrgType.class, "contractingAgencyTypes", true);
        addFilter(ColumnConstants.CONTRACTING_AGENCY_GROUPS, AmpOrgGroup.class, "contractingAgencyGroups", true);
        addFilter(ColumnConstants.CONTRACTING_AGENCY, AmpOrganisation.class, "contractingAgency", true);
        addFilter(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, AmpOrgType.class, "implementingAgencyTypes", true);
        addFilter(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, AmpOrgGroup.class, "implementingAgencyGroups", true);
        addFilter(ColumnConstants.IMPLEMENTING_AGENCY, AmpOrganisation.class, "implementingAgency", true);
        //addFilter(ColumnConstants.REGIONAL_GROUP, AmpOrganisation.class, "", true);
        addFilter(ColumnConstants.RESPONSIBLE_ORGANIZATION_TYPE, AmpOrgType.class, "responsibleAgencyTypes", true);
        addFilter(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, AmpOrgGroup.class, "responsibleAgencyGroups", true);
        addFilter(ColumnConstants.RESPONSIBLE_ORGANIZATION, AmpOrganisation.class, "responsibleorg", true);
        addFilter(ColumnConstants.COMPONENT_FUNDING_ORGANIZATION, AmpOrganisation.class, "componentFunding", true);
        addFilter(ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, AmpOrganisation.class,
                "componentSecondResponsible", true);
        //addFilter(ColumnConstants.SECTOR_GROUP, AmpOrganisation.class, "", true);

        // Sector´s section.
        addFilter(ColumnConstants.PRIMARY_SECTOR, AmpSector.class, "selectedSectors", true);
        addFilter(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, AmpSector.class, "selectedSectors", false);
        addFilter(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, AmpSector.class, "selectedSectors", false);
        addFilter(ColumnConstants.SECONDARY_SECTOR, AmpSector.class, "selectedSecondarySectors", true);
        addFilter(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, AmpSector.class, "selectedSecondarySectors", false);
        addFilter(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, AmpSector.class, "selectedSecondarySectors", false);
        addFilter(ColumnConstants.TERTIARY_SECTOR, AmpSector.class, "selectedTertiarySectors", true);
        addFilter(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, AmpSector.class, "selectedTertiarySectors", false);
        addFilter(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, AmpSector.class, "selectedTertiarySectors", false);

        addFilter(ColumnConstants.PLEDGES_SECTORS, AmpSector.class, "selectedSectors", true);
        addFilter(ColumnConstants.PLEDGES_SECTORS_SUBSECTORS, AmpSector.class, "selectedSectors", false);
        addFilter(ColumnConstants.PLEDGES_SECTORS_SUBSUBSECTORS, AmpSector.class, "selectedSectors", false);
        addFilter(ColumnConstants.PLEDGES_SECONDARY_SECTORS, AmpSector.class, "selectedSecondarySectors", true);
        addFilter(ColumnConstants.PLEDGES_SECONDARY_SUBSECTORS, AmpSector.class, "selectedSecondarySectors", false);
        addFilter(ColumnConstants.PLEDGES_SECONDARY_SUBSUBSECTORS, AmpSector.class, "selectedSecondarySectors", false);

        // Programs and national objectives section.
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_0, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_4, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_5, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_6, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_7, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PRIMARY_PROGRAM_LEVEL_8, AmpTheme.class, "selectedPrimaryPrograms", false);

        addFilter(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_0, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_1, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_2, AmpTheme.class, "selectedPrimaryPrograms", false);
        addFilter(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_3, AmpTheme.class, "selectedPrimaryPrograms", false);
    
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_0, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_4, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_5, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_6, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_7, AmpTheme.class, "selectedSecondaryPrograms", false);
        addFilter(ColumnConstants.SECONDARY_PROGRAM_LEVEL_8, AmpTheme.class, "selectedSecondaryPrograms", false);

        addFilter(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_0, AmpTheme.class, "selectedSecondaryPrograms",
                false);
        addFilter(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_1, AmpTheme.class, "selectedSecondaryPrograms",
                false);
        addFilter(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_2, AmpTheme.class, "selectedSecondaryPrograms",
                false);
        addFilter(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_3, AmpTheme.class, "selectedSecondaryPrograms",
                false);
    
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_0, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_4, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_5, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_6, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_7, AmpTheme.class, "selectedTertiaryPrograms", false);
        addFilter(ColumnConstants.TERTIARY_PROGRAM_LEVEL_8, AmpTheme.class, "selectedTertiaryPrograms", false);
    
        addFilter(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_0, AmpTheme.class, "selectedTertiaryPrograms",
                false);
        addFilter(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_1, AmpTheme.class, "selectedTertiaryPrograms",
                false);
        addFilter(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_2, AmpTheme.class, "selectedTertiaryPrograms",
                false);
        addFilter(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_3, AmpTheme.class, "selectedTertiaryPrograms",
                false);
    
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_0, AmpTheme.class, "selectedNatPlanObj", false);
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, AmpTheme.class, "selectedNatPlanObj", false);       
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2, AmpTheme.class, "selectedNatPlanObj", false);
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3, AmpTheme.class, "selectedNatPlanObj", false);
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_4, AmpTheme.class, "selectedNatPlanObj", false);
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_5, AmpTheme.class, "selectedNatPlanObj", false);
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_6, AmpTheme.class, "selectedNatPlanObj", false);
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_7, AmpTheme.class, "selectedNatPlanObj", false);
        addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_8, AmpTheme.class, "selectedNatPlanObj", false);

        addFilter(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_0, AmpTheme.class, "selectedNatPlanObj",
                false);
        addFilter(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_1, AmpTheme.class, "selectedNatPlanObj",
                false);
        addFilter(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_2, AmpTheme.class, "selectedNatPlanObj",
                false);
        addFilter(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_3, AmpTheme.class, "selectedNatPlanObj",
                false);
        
        // Activity section.
        addFilter(ColumnConstants.STATUS, AmpCategoryValue.class, "statuses", true);
        addFilter(ColumnConstants.APPROVAL_STATUS, String.class, "approvalStatusSelected", true);

        addFilter(ColumnConstants.PLEDGE_STATUS, AmpCategoryValue.class, "statuses", true);

        // Location section.
        addFilter(ColumnConstants.LOCATION_ADM_LEVEL_0, AmpCategoryValueLocations.class, "locationSelected", true);
        addFilter(ColumnConstants.LOCATION_ADM_LEVEL_1, AmpCategoryValueLocations.class, "locationSelected", false);
        addFilter(ColumnConstants.LOCATION_ADM_LEVEL_2, AmpCategoryValueLocations.class, "locationSelected", false);
        addFilter(ColumnConstants.LOCATION_ADM_LEVEL_3, AmpCategoryValueLocations.class, "locationSelected", false);
        addFilter(ColumnConstants.LOCATION_ADM_LEVEL_4, AmpCategoryValueLocations.class, "locationSelected", false);

        addFilter(ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_0, AmpCategoryValueLocations.class,
                "locationSelected", true);
        addFilter(ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_1, AmpCategoryValueLocations.class,
                "locationSelected", false);
        addFilter(ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_2, AmpCategoryValueLocations.class,
                "locationSelected", false);
        addFilter(ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_3, AmpCategoryValueLocations.class,
                "locationSelected", false);
        addFilter(ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_4, AmpCategoryValueLocations.class,
                "locationSelected", false);
        
        // Financial section.
        addFilter(ColumnConstants.FINANCING_INSTRUMENT, AmpCategoryValue.class, "financingInstruments", true);
        addFilter(ColumnConstants.TYPE_OF_ASSISTANCE, AmpCategoryValue.class, "typeOfAssistance", true);
        addFilter(ColumnConstants.ACTIVITY_BUDGET, AmpCategoryValue.class, "budget", true);
        addFilter(ColumnConstants.WORKSPACES, AmpTeam.class, "workspaces", true);
        addFilter(ColumnConstants.FUNDING_STATUS, AmpCategoryValue.class, "fundingStatus", true);
        addFilter(ColumnConstants.MODE_OF_PAYMENT, AmpCategoryValue.class, "modeOfPayment", true);
        addFilter(ColumnConstants.EXPENDITURE_CLASS, AmpCategoryValue.class, "expenditureClass", true);
        addFilter(ColumnConstants.CONCESSIONALITY_LEVEL, AmpCategoryValue.class, "concessionalityLevel", true);

        addFilter(ColumnConstants.PLEDGES_AID_MODALITY, AmpCategoryValue.class, "financingInstruments", true);
        addFilter(ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE, AmpCategoryValue.class, "typeOfAssistance", true);

        // Other section.
        addFilter(ColumnConstants.HUMANITARIAN_AID, Integer.class, "humanitarianAid", true);
        addFilter(ColumnConstants.DISASTER_RESPONSE_MARKER, Integer.class, "disasterResponse", true);
        addDateRangeFilter(ColumnConstants.ACTUAL_START_DATE, "fromActivityStartDate", "toActivityStartDate");
        addDateRangeFilter(ColumnConstants.ISSUE_DATE, "fromIssueDate", "toIssueDate");
        addDateRangeFilter(ColumnConstants.PROPOSED_APPROVAL_DATE, "fromProposedApprovalDate",
                "toProposedApprovalDate");
        addDateRangeFilter(ColumnConstants.PROPOSED_START_DATE, "fromProposedStartDate", "toProposedStartDate");
        addDateRangeFilter(ColumnConstants.ACTUAL_APPROVAL_DATE,
                "fromActualApprovalDate", "toActualApprovalDate");
        addDateRangeFilter(ColumnConstants.PROPOSED_COMPLETION_DATE,
                "fromProposedCompletionDate", "toProposedCompletionDate");
        addDateRangeFilter(ColumnConstants.ACTUAL_COMPLETION_DATE, "fromActivityActualCompletionDate",
                "toActivityActualCompletionDate");
        addDateRangeFilter(ColumnConstants.FINAL_DATE_FOR_CONTRACTING, "fromActivityFinalContractingDate",
                "toActivityFinalContractingDate");
        addDateRangeFilter(ColumnConstants.EFFECTIVE_FUNDING_DATE, "fromEffectiveFundingDate",
                "toEffectiveFundingDate");
        addDateRangeFilter(ColumnConstants.FUNDING_CLOSING_DATE, "fromFundingClosingDate", "toFundingClosingDate");
        addDateRangeFilter(TRANSACTION_DATE, "fromDate", "toDate");
        this.ampARFilter.setComputedYear(this.filters.getComputedYear());

        addDateRangeFilter(ColumnConstants.PLEDGES_DETAIL_START_DATE, "fromPledgeDetailStartDate",
                "toPledgeDetailStartDate");
        addDateRangeFilter(ColumnConstants.PLEDGES_DETAIL_END_DATE, "fromPledgeDetailEndDate",
                "toPledgeDetailEndDate");

        return this.ampARFilter;
    }

    /**
     * Convert one of the filters to one of the fields in AmpARFilters and add it to the new filter.
     * 
     * @param filterColumnName
     *          is the constant name in the new filters.
     * @param ampARFilterFieldClass
     *          is the Class of the field in AmpARFilters.
     * @param ampARFilterFieldName
     *          is the field name in AmpARFilters.
     * @param cleanup
     *          if true then old values will be replaced.
     */
    private void addFilter(String filterColumnName, Class ampARFilterFieldClass, String ampARFilterFieldName,
            boolean cleanup) {
        try {
            Session session = PersistenceManager.getSession();
            // Use reflection to dynamically call the setter method on AmpARFilter class, that includes generating the setter
            // name and inspecting the type of data it uses (Set, Collection, Wrapper, etc).
            Method getterMethod = AmpARFilter.class.getDeclaredMethod(getGetterName(ampARFilterFieldName));
            Class paramClass = getterMethod.getReturnType();
            Class[] param = new Class[1];
            param[0] = paramClass;
            Method setterMethod = AmpARFilter.class.getDeclaredMethod(getSetterName(ampARFilterFieldName), param);

            // Get values from Reports API filters.
            FilterRule filterRule = this.filters.getAllFilterRules().get(
                    new ReportElement(new ReportColumn(filterColumnName)));

            if (filterRule != null) {
                if (paramClass.getName().equals("java.util.Set") || paramClass.getName().equals("java.util.Collection")) {
                    Set<Object> values = new HashSet();
                    if (filterRule.values != null) {
                        Iterator<String> iValues = filterRule.values.iterator();
                        while (iValues.hasNext()) {
                            String auxValue = iValues.next();
                            if (auxValue.equals(Long.toString(ColumnReportData.UNALLOCATED_ID))) {
                                ampARFilter.getUndefinedOptions().add(ampARFilterFieldName);
                            } else if (ampARFilterFieldClass.toString().equals("class java.lang.String")) {
                                values.add(auxValue);
                            } else if (ampARFilterFieldClass.toString().equals("class java.lang.Integer")) {
                                values.add(Integer.valueOf(auxValue));
                            } else if (ampARFilterFieldClass.toString().equals("class java.lang.Double")) {
                                values.add(Double.valueOf(auxValue));
                            } else {
                                Object auxEntity = session.load(ampARFilterFieldClass, new Long(auxValue));
                                values.add(auxEntity);
                            }
                        }
                    }

                    if (cleanup == false) {
                        // Preserve old values.
                        Set<Object> previousValues = (Set) getterMethod.invoke(this.ampARFilter);
                        if (previousValues != null) {
                            values.addAll(previousValues);
                        }
                    }
                    // Use reflection to call the setter.
                    setterMethod.invoke(this.ampARFilter, values);
                    logger.info("Found filter: " + filterColumnName + " with values: " + values.toString());
                } else if (paramClass.getName().equals("java.lang.String")) {
                    setterMethod.invoke(this.ampARFilter, filterRule.toString());
                    logger.info("Found filter: " + filterColumnName + " with values: " + filterRule.toString());
                } else if (paramClass.getName().equals("java.lang.Integer")) {
                    setterMethod.invoke(this.ampARFilter, Integer.valueOf(filterRule.toString()));
                    logger.info("Found filter: " + filterColumnName + " with values: " + filterRule.toString());
                } else if (paramClass.getName().equals("java.lang.Double")) {
                    setterMethod.invoke(this.ampARFilter, Double.valueOf(filterRule.toString()));
                    logger.info("Found filter: " + filterColumnName + " with values: " + filterRule.toString());
                } else {
                    throw new RuntimeException(paramClass.getName());
                }
            } else {
                logger.info("Not found filter: " + filterColumnName);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | ObjectNotFoundException e) {
            logger.error(e, e);
        }
    }

    private void addDateRangeFilter(String filterColumnName, String fromMethod, String toMethod) {
        try {
            Method setterFromMethod = AmpARFilter.class.getDeclaredMethod(getSetterName(fromMethod), java.lang.String.class);
            Method setterToMethod = AmpARFilter.class.getDeclaredMethod(getSetterName(toMethod), java.lang.String.class);           
            ReportElement filterElement = null;
            if (filterColumnName.equalsIgnoreCase(TRANSACTION_DATE)) {
                filterElement = new ReportElement(ElementType.DATE);
            } else {
                filterElement = new ReportElement(new ReportColumn(filterColumnName));
            }
            FilterRule filterRule = this.filters.getAllFilterRules().get(filterElement);
            if (filterRule != null) {
                String fromDate = DateTimeUtil.convertJulianNrToDefaultDateFormat(filterRule.min);
                String toDate = DateTimeUtil.convertJulianNrToDefaultDateFormat(filterRule.max);

                // Use reflection to call the setter.
                setterFromMethod.invoke(this.ampARFilter, fromDate);
                setterToMethod.invoke(this.ampARFilter, toDate);
                logger.info("Found filter: " + filterColumnName + " with values: " + fromDate + " / " + toDate);
            }
        } catch (Exception e) {
            logger.error("Failed to add date range filter.", e);
        }
    }

    /**
     * Merge all fields that have not been populated in the build process.
     * 
     * @param oldFilters
     */
    public void mergeWithOldFilters(AmpARFilter oldFilters) {
        Field[] declaredFields = AmpARFilter.class.getDeclaredFields();
        for (Field auxField : declaredFields) {
            try {
                // Obtain getter and setter for each field.
                Method getter = AmpARFilter.class.getDeclaredMethod(getGetterName(auxField.getName()), null);
                Class paramClass = getter.getReturnType();
                Class[] param = new Class[1];
                param[0] = paramClass;
                Method setter = AmpARFilter.class.getDeclaredMethod(getSetterName(auxField.getName()), param);
                Object oldFieldValue = getter.invoke(oldFilters, null);
                Object newFieldValue = getter.invoke(this.ampARFilter, null);
                if (oldFieldValue != null && newFieldValue == null) {
                    logger.info(auxField.toGenericString() + ": " + oldFieldValue);

                    if (oldFieldValue instanceof Collection) {
                        if (((Collection) oldFieldValue).size() > 0) {
                            setter.invoke(this.ampARFilter, oldFieldValue);
                        }
                    } else if (oldFieldValue instanceof Map) {
                        if (((Map) oldFieldValue).size() > 0) {
                            setter.invoke(this.ampARFilter, oldFieldValue);
                        }
                    } else {
                        setter.invoke(this.ampARFilter, oldFieldValue);
                    }
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    private String getSetterName(String field) {
        String get = null;
        get = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
        return get;
    }

    private String getGetterName(String field) {
        String get = null;
        get = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
        return get;
    }

}
