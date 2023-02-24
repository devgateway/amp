/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.utils.ConstantsUtil;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Detects which columns are visible in Activity Form and can be further used, 
 * e.g. in report generation & filtering
 * 
 * @author Nadejda Mandrescu
 */
public class ColumnsVisibility extends DataVisibility implements FMSettings {
    protected static final Logger logger = Logger.getLogger(ColumnsVisibility.class);
    
    public static final int PROGRAM_LEVEL_COUNT = 8;

    private static final Set<String> columnsSet = ConstantsUtil.getConstantsSet(ColumnConstants.class);

    /*
     * Expenditure Class is not an actual column, but has column-type of filtering 
     * so it needs to be 'visible' for the filters, but not for any other purpose
     */
    private static final Set<String> fakeColumns = new HashSet<>(Arrays.asList(ColumnConstants.EXPENDITURE_CLASS,
            ColumnConstants.COMPUTED_YEAR));
    
    /**
     * @return the current set of visible columns
     */
    synchronized
    public static Set<String> getVisibleColumnsWithFakeOnes() {
        return FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_COLUMNS, null);
    }

    /**
     * @return the current set of visible columns 
     */
    synchronized
    public static Set<String> getVisibleColumns() {
        return getVisibleColumns(null);
    }
    
    public static Set<String> getVisibleColumns(Long templateId) {
        Set<String> set = new HashSet<>();
        for (String z : FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_COLUMNS, templateId)) {
            if (!fakeColumns.contains(z)) {
                set.add(z);
            }
        }
        return set;
    }
    
    public static Set<String> getConfigurableColumns() {
        return getConfigurableColumns(null);
    }
    
    public static Set<String> getConfigurableColumns(Long templateId) {
        Set<String> configurableColumns = new HashSet<String>(getVisibleColumns(templateId));
        configurableColumns.retainAll(AmpReportsSchema.getInstance().getColumns().keySet());
        return configurableColumns;
    }
    
    protected ColumnsVisibility() {
    }
    
    @Override
    public Set<String> getEnabledSettings(Long templateId) {
        return getVisibleData(templateId);
    }
    
    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAny() {
        return anyDependencyMap;
    }
    
    @Override
    protected List<String> getVisibleByDefault() {
        List<String> currentlyVisible = new ArrayList<String>(visibleByDefault);
        for (Entry<String, HardCodedCategoryValue> entry : categoryValueDependency.entrySet()) {
            AmpCategoryValue acv = entry.getValue().getAmpCategoryValueFromDB();
            if (acv != null && acv.isVisible())
                currentlyVisible.add(entry.getKey());   
        }
        return currentlyVisible;
    }
    
    @Override
    protected Set<String> getAllData() {
        return columnsSet;
    }
    
    @Override
    protected Map<String, String> getDataMap(DataMapType dataMapType) {
        switch(dataMapType) {
        case MODULES: return modulesToColumnsMap; 
        case FEATURES: return featuresToColumnsMap;
        case FIELDS: return FIELDS_TO_COLUMNS_MAP;
        case DEPENDENCY: return dependencyMap;
        default: return null; //shouldn't come here
        }
    }
    
    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAll() {
        return dependencyTypeAll;
    }
    
    /****************
     * Visibility related constants
     ***************/
    private static final String DONOR_ORAGNIZATION = "Donor Organization";
    private static final String SEARCH_DONOR_ORAGNIZATION = "Search Donor Organization";
    private static final String IMPLEMENTATION_LOCATION_ADM_LEVEL_0 = "IMPLEMENTATION_LOCATION_ADM_LEVEL_0";
    private static final String IMPLEMENTATION_LOCATION_ADM_LEVEL_1 = "IMPLEMENTATION_LOCATION_ADM_LEVEL_1";
    private static final String IMPLEMENTATION_LOCATION_ADM_LEVEL_2 = "IMPLEMENTATION_LOCATION_ADM_LEVEL_2";
    private static final String IMPLEMENTATION_LOCATION_ADM_LEVEL_3 = "IMPLEMENTATION_LOCATION_ADM_LEVEL_3";
    
    //Note: mappings are manually retrieved, because no certain way exists to map them 
    @SuppressWarnings("serial")
    protected static final Map<String, String> modulesToColumnsMap = new HashMap<String, String>() {{
        put("Activity Approval Process", ColumnConstants.APPROVAL_STATUS);
        put("/Activity Form/Contacts/Donor Contact Information", ColumnConstants.DONOR_CONTACT_ORGANIZATION);
        put("/Activity Form/Contacts/Sector Ministry Contact Information", ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION);
        put("/Activity Form/Contacts/Project Coordinator Contact Information", ColumnConstants.PROJECT_COORDINATOR_CONTACT_ORGANIZATION);
        put("/Activity Form/Contacts/Implementing Executing Agency Contact Information", ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION);
        put("/Activity Form/Contacts/Ministry Of Finance Contact Organization", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION);
        put("/Activity Form/Budget Structure", ColumnConstants.BUDGET_STRUCTURE);
        put("/Activity Form/Cross Cutting Issues/Environment", ColumnConstants.ENVIRONMENT);
        put("/Activity Form/Cross Cutting Issues/Equal Opportunity", ColumnConstants.EQUAL_OPPORTUNITY);
        put("/Activity Form/Cross Cutting Issues/Minorities", ColumnConstants.MINORITIES);
        put("/Activity Form/Identification/A.C. Chapter", ColumnConstants.AC_CHAPTER);
        put("/Activity Form/Identification/Accession Instrument", ColumnConstants.ACCESSION_INSTRUMENT);
        put("/Activity Form/Identification/Activity Budget", ColumnConstants.ACTIVITY_BUDGET);
        put("/Activity Form/Identification/Activity Status", ColumnConstants.STATUS);
        put("/Activity Form/Identification/Audit System", ColumnConstants.AUDIT_SYSTEM);
        put("/Activity Form/Identification/Budget Code Project ID", ColumnConstants.BUDGET_CODE_PROJECT_ID); 
        put("/Activity Form/Identification/Budget Extras/FY", ColumnConstants.FY);
        put("/Activity Form/Identification/Budget Extras/Indirect On Budget", ColumnConstants.INDIRECT_ON_BUDGET); 
        put("/Activity Form/Identification/Budget Extras/Ministry Code", ColumnConstants.MINISTRY_CODE);
        put("/Activity Form/Identification/Budget Extras/Project Code", ColumnConstants.PROJECT_CODE);
        put("/Activity Form/Identification/Budget Extras/Sub-Vote", ColumnConstants.SUB_VOTE);
        put("/Activity Form/Identification/Budget Extras/Sub-Program", ColumnConstants.SUB_PROGRAM);
        put("/Activity Form/Identification/Budget Extras/Vote", ColumnConstants.VOTE);
        put("/Activity Form/Identification/Cris Number", ColumnConstants.CRIS_NUMBER);
        put("/Activity Form/Identification/IATI Identifier", ColumnConstants.IATI_IDENTIFIER);
        put("/Activity Form/Identification/Financial Instrument", ColumnConstants.FINANCIAL_INSTRUMENT);
        put("/Activity Form/Identification/Government Agreement Number", ColumnConstants.GOVERNMENT_AGREEMENT_NUMBER);
        put("/Activity Form/Identification/Government Approval Procedures", ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES);
        put("/Activity Form/Identification/Joint Criteria", ColumnConstants.JOINT_CRITERIA);
        put("/Activity Form/Identification/Institutions", ColumnConstants.INSTITUTIONS);
        put("/Activity Form/Identification/Procurement System", ColumnConstants.PROCUREMENT_SYSTEM);
        put("/Activity Form/Identification/Project Category", ColumnConstants.PROJECT_CATEGORY);
        put("/Activity Form/Identification/Project Comments", ColumnConstants.PROJECT_COMMENTS);
        put("/Activity Form/Identification/Project Impact", ColumnConstants.PROJECT_IMPACT);
        put("/Activity Form/Identification/Project Implementing Unit", ColumnConstants.PROJECT_IMPLEMENTING_UNIT);
        put("/Activity Form/Identification/Project Title", ColumnConstants.PROJECT_TITLE);
        put("/Activity Form/Identification/Purpose", ColumnConstants.PURPOSE);
        put("/Activity Form/Identification/Humanitarian Aid", ColumnConstants.HUMANITARIAN_AID);
        put("/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Disaster Response", ColumnConstants.DISASTER_RESPONSE_MARKER);
        put("/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Disbursement Id",
                ColumnConstants.DISBURSEMENT_ID);
        put("/Activity Form/Identification/Objective", ColumnConstants.OBJECTIVE);
        put("/Activity Form/Identification/Reporting System", ColumnConstants.REPORTING_SYSTEM);
        put("/Activity Form/Identification/Results", ColumnConstants.RESULTS);
        put("/Activity Form/Funding/Overview Section/Type of Cooperation", ColumnConstants.TYPE_OF_COOPERATION);
        put("/Activity Form/Funding/Overview Section/Type of Implementation", ColumnConstants.TYPE_OF_IMPLEMENTATION);
        put("/Activity Form/Issues Section", ColumnConstants.ISSUES);
        put("/Activity Form/Issues Section/Issue/Date", ColumnConstants.ISSUE_DATE);
        put("/Activity Form/Issues Section/Issue/Measure/Actor", ColumnConstants.ISSUES___MEASURES___ACTORS);
        put("/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Expenditure Class", ColumnConstants.EXPENDITURE_CLASS);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement", ColumnConstants.AGREEMENT_CODE);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Financing Instrument", ColumnConstants.FINANCING_INSTRUMENT);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Classification Date", ColumnConstants.FUNDING_CLASSIFICATION_DATE);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Organization Id", ColumnConstants.FUNDING_ORGANIZATION_ID);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Status", ColumnConstants.FUNDING_STATUS);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Mode of Payment", ColumnConstants.MODE_OF_PAYMENT);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Type of Assistence", ColumnConstants.TYPE_OF_ASSISTANCE);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Effective Funding Date", ColumnConstants.EFFECTIVE_FUNDING_DATE);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Closing Date", ColumnConstants.FUNDING_CLOSING_DATE);
        put("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Concessionality Level", ColumnConstants.CONCESSIONALITY_LEVEL);
        put("/Activity Form/Funding/Modalities", ColumnConstants.PLEDGES_AID_MODALITY);
        put("/Activity Form/Funding/Overview Section/Proposed Project Cost", ColumnConstants.PROPOSED_PROJECT_AMOUNT);
        put("/Activity Form/Funding/Overview Section/Revised Project Cost", ColumnConstants.REVISED_PROJECT_AMOUNT);
        put("/Activity Form/Location/Implementation Level", ColumnConstants.IMPLEMENTATION_LEVEL);
        put("/Activity Form/Location/Locations", ColumnConstants.LOCATION);
        put("/Activity Form/Line Ministry Observations", ColumnConstants.LINE_MINISTRY_OBSERVATIONS);
        put("/Activity Form/Planning/Actual Approval Date", ColumnConstants.ACTUAL_APPROVAL_DATE);
        put("/Activity Form/Planning/Actual Completion Date", ColumnConstants.ACTUAL_COMPLETION_DATE);
        put("/Activity Form/Planning/Actual Start Date", ColumnConstants.ACTUAL_START_DATE);
        put("/Activity Form/Planning/Final Date for Contracting", ColumnConstants.FINAL_DATE_FOR_CONTRACTING);
        put("/Activity Form/Planning/Final Date for Disbursements", ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS);
        put("/Activity Form/Planning/Proposed Approval Date", ColumnConstants.PROPOSED_APPROVAL_DATE);
        put("/Activity Form/Planning/Proposed Completion Date", ColumnConstants.PROPOSED_COMPLETION_DATE);
        put("/Activity Form/Planning/Proposed Project Life", ColumnConstants.PROPOSED_PROJECT_LIFE);
        put("/Activity Form/Planning/Proposed Start Date", ColumnConstants.PROPOSED_START_DATE);
        put("/Activity Form/Planning/Original Completion Date", ColumnConstants.ORIGINAL_COMPLETION_DATE);
        put("/Activity Form/Planning/Project Implementation Delay", ColumnConstants.PROJECT_IMPLEMENTATION_DELAY);
        put("/Activity Form/Program/Program Description", ColumnConstants.PROGRAM_DESCRIPTION);
        put("/Activity Form/Regional Observations", ColumnConstants.REGIONAL_OBSERVATIONS);
        put("/Activity Form/Organizations/Beneficiary Agency", ColumnConstants.BENEFICIARY_AGENCY);
        put("/Activity Form/Organizations/Contracting Agency", ColumnConstants.CONTRACTING_AGENCY);
        put("/Activity Form/Organizations/Donor Organization", DONOR_ORAGNIZATION);
        put("/Activity Form/Funding/Search Funding Organizations", SEARCH_DONOR_ORAGNIZATION);
        put("/Activity Form/Organizations/Executing Agency", ColumnConstants.EXECUTING_AGENCY);
        put("/Activity Form/Organizations/Implementing Agency", ColumnConstants.IMPLEMENTING_AGENCY);
        put("/Activity Form/Organizations/Regional Group", ColumnConstants.REGIONAL_GROUP);
        put("/Activity Form/Organizations/Responsible Organization", ColumnConstants.RESPONSIBLE_ORGANIZATION);
        put("/Activity Form/Organizations/Sector Group", ColumnConstants.SECTOR_GROUP);
        put("/Activity Form/Sectors/Primary Sectors", ColumnConstants.PRIMARY_SECTOR);
        put("/Activity Form/Sectors/Secondary Sectors", ColumnConstants.SECONDARY_SECTOR);
        put("/Activity Form/Sectors/Tertiary Sectors", ColumnConstants.TERTIARY_SECTOR);
        put("/Activity Form/Sectors/Quaternary Sectors", ColumnConstants.QUATERNARY_SECTOR);
        put("/Activity Form/Sectors/Quinary Sectors", ColumnConstants.QUINARY_SECTOR);
        put("/Activity Form/Structures", ColumnConstants.STRUCTURES_COLUMN);
    }};
    
    @SuppressWarnings("serial")
    public static final Map<String, String> dependencyMap= new HashMap<String, String>() {{
        put(ColumnConstants.ACTIVITY_ID, ColumnConstants.PROJECT_TITLE);
        put(ColumnConstants.INTERNAL_USE_ID, ColumnConstants.PROJECT_TITLE);
        put(ColumnConstants.AGREEMENT_CLOSE_DATE, ColumnConstants.AGREEMENT_CODE);
        put(ColumnConstants.AGREEMENT_EFFECTIVE_DATE, ColumnConstants.AGREEMENT_CODE);
        put(ColumnConstants.AGREEMENT_SIGNATURE_DATE, ColumnConstants.AGREEMENT_CODE);
        put(ColumnConstants.AGREEMENT_TITLE_CODE, ColumnConstants.AGREEMENT_CODE);
        
        put(ColumnConstants.DONOR_ID, ColumnConstants.DONOR_AGENCY);
        put(ColumnConstants.DONOR_GROUP, ColumnConstants.DONOR_AGENCY);
        put(ColumnConstants.DONOR_TYPE, ColumnConstants.DONOR_AGENCY);
        put(ColumnConstants.DONOR_BUDGET_CODE, ColumnConstants.DONOR_AGENCY);
        put(ColumnConstants.DONOR_COUNTRY, ColumnConstants.DONOR_AGENCY);
        put(ColumnConstants.DONOR_COMMITMENT_DATE, ColumnConstants.DONOR_AGENCY);
        put(ColumnConstants.RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION, ColumnConstants.RESPONSIBLE_ORGANIZATION);
        put(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, ColumnConstants.RESPONSIBLE_ORGANIZATION);
        put(ColumnConstants.RESPONSIBLE_ORGANIZATION_TYPE, ColumnConstants.RESPONSIBLE_ORGANIZATION);
        put(ColumnConstants.EXECUTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.EXECUTING_AGENCY);
        put(ColumnConstants.EXECUTING_AGENCY_GROUPS, ColumnConstants.EXECUTING_AGENCY);
        put(ColumnConstants.EXECUTING_AGENCY_TYPE, ColumnConstants.EXECUTING_AGENCY);
        put(ColumnConstants.EXECUTING_AGENCY_COUNTRY, ColumnConstants.EXECUTING_AGENCY);
        put(ColumnConstants.IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.IMPLEMENTING_AGENCY);
        put(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, ColumnConstants.IMPLEMENTING_AGENCY);
        put(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, ColumnConstants.IMPLEMENTING_AGENCY);
        put(ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION, ColumnConstants.BENEFICIARY_AGENCY);
        put(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, ColumnConstants.BENEFICIARY_AGENCY);
        put(ColumnConstants.BENEFICIARY_AGENCY_TYPE, ColumnConstants.BENEFICIARY_AGENCY);
        put(ColumnConstants.BENEFICIARY_AGENCY_COUNTRY, ColumnConstants.BENEFICIARY_AGENCY);
        put(ColumnConstants.CONTRACTING_AGENCY_ACRONYM, ColumnConstants.CONTRACTING_AGENCY);
        put(ColumnConstants.CONTRACTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.CONTRACTING_AGENCY);
        put(ColumnConstants.CONTRACTING_AGENCY_GROUPS, ColumnConstants.CONTRACTING_AGENCY);
        put(ColumnConstants.CONTRACTING_AGENCY_TYPE, ColumnConstants.CONTRACTING_AGENCY);
        put(ColumnConstants.SECTOR_GROUP_DEPARTMENT_DIVISION, ColumnConstants.SECTOR_GROUP);
        put(ColumnConstants.REGIONAL_GROUP_DEPARTMENT_DIVISION, ColumnConstants.REGIONAL_GROUP);
        
        String[][] contacts = new String[][] {
            {"Donor Contact", ColumnConstants.DONOR_CONTACT_ORGANIZATION}, 
            {"Sector Ministry Contact", ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION},
            {"Project Coordinator Contact", ColumnConstants.PROJECT_COORDINATOR_CONTACT_ORGANIZATION},
            {"Implementing/Executing Agency Contact", ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION},
            {"Ministry Of Finance Contact", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION},
            {"Pledge Contact 1 -", ColumnConstants.PLEDGE_CONTACT_1___MINISTRY},
            {"Pledge Contact 2 -", ColumnConstants.PLEDGE_CONTACT_2___MINISTRY}
        };
        String[] suffixList = new String[] {"Email", "Fax", "Name", "Phone", "Title",
                //and pledges specific
                "Address", "Alternate Contact", "Telephone", "Alternate Phone", "Alternate Email"};
        for (String[] con : contacts)
            for (String suffix : suffixList)
                put(con[0] + " " + suffix, con[1]);
        
        put(ColumnConstants.GEOCODE, ColumnConstants.LOCATION);
    }};
    
    @SuppressWarnings("serial")
    protected static final Map<String, Collection<String>> anyDependencyMap= new HashMap<String, Collection<String>>() {{
        /* moving DONOR_AGENCY to always visible list per confirmation from Diego that there won't be any 
         * country that may not want donors to be listed and everything related to it to be disabled in FM 
        put(ColumnConstants.DONOR_AGENCY, Arrays.asList(DONOR_ORAGNIZATION, SEARCH_DONOR_ORAGNIZATION));
        */
    }};
    
    @SuppressWarnings("serial")
    protected static final Map<String, String> featuresToColumnsMap = new HashMap<String, String>() {{
        put("Computed Year", ColumnConstants.COMPUTED_YEAR);
    }};
    
    @SuppressWarnings("serial")
    protected static final Map<String, String> FIELDS_TO_COLUMNS_MAP = new HashMap<String, String>() {{
            put("AMP ID", ColumnConstants.AMP_ID);
            put("Activity Approved By", ColumnConstants.ACTIVITY_APPROVED_BY);
            put("Activity Created By", ColumnConstants.ACTIVITY_CREATED_BY);
            put("Activity Created On", ColumnConstants.ACTIVITY_CREATED_ON);
            put("Activity Pledges Title", ColumnConstants.ACTIVITY_PLEDGES_TITLE);
            put("Activity Updated By", ColumnConstants.ACTIVITY_UPDATED_BY);
            put("Activity Updated On", ColumnConstants.ACTIVITY_UPDATED_ON);
            put("Actors", ColumnConstants.ACTORS);
            put("Age of Project (Months)", ColumnConstants.AGE_OF_PROJECT_MONTHS);
            put("Average Size of Disbursements", ColumnConstants.AVERAGE_SIZE_OF_DISBURSEMENTS);
            put("Average Size of Projects", ColumnConstants.AVERAGE_SIZE_OF_PROJECTS);
            put("Budget Department", ColumnConstants.BUDGET_DEPARTMENT);
            put("Budget Organization", ColumnConstants.BUDGET_ORGANIZATION);
            put("Budget Program", ColumnConstants.BUDGET_PROGRAM);
            put("Budget Sector", ColumnConstants.BUDGET_SECTOR);
            put("Calculated Project Life", ColumnConstants.CALCULATED_PROJECT_LIFE);
            put("Capital Expenditure", ColumnConstants.CAPITAL_EXPENDITURE);
            put("Component Name", ColumnConstants.COMPONENT_NAME);
            put("Component description", ColumnConstants.COMPONENT_DESCRIPTION);
            put("Component Funding Organization", ColumnConstants.COMPONENT_FUNDING_ORGANIZATION);
            put("Component Second Responsible Organization", ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION);
            put("Component Type", ColumnConstants.COMPONENT_TYPE);
            put("Credit/Donation", ColumnConstants.CREDIT_DONATION);
            put("Current Completion Date Comments", ColumnConstants.CURRENT_COMPLETION_DATE_COMMENTS);
            put("Description of Component Funding", ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING);
            put("Draft", ColumnConstants.DRAFT);
            put("Execution Rate", ColumnConstants.EXECUTION_RATE);
            put("Final Date for Disbursements Comments", ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS_COMMENTS);
            put("Funding end date", ColumnConstants.FUNDING_END_DATE);
            put("Funding start date", ColumnConstants.FUNDING_START_DATE);
            put("Loan Interest Rate", ColumnConstants.INTEREST_RATE);
            put("Loan Grace Period", ColumnConstants.GRACE_PERIOD);
            put("Loan Maturity Date", ColumnConstants.MATURITY);
            put("Loan Ratification Date", ColumnConstants.RATIFICATION_DATE);
            put("Line Ministry Observations Actors", ColumnConstants.LINE_MINISTRY_OBSERVATIONS_ACTORS);
            put("Line Ministry Observations Date", ColumnConstants.LINE_MINISTRY_OBSERVATIONS_DATE);
            put("Line Ministry Observations Measures", ColumnConstants.LINE_MINISTRY_OBSERVATIONS_MEASURES);
            put("Measures Taken", ColumnConstants.MEASURES_TAKEN);
            put("Ministry Of Finance Contact Organization", ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION);
            put("Multi Donor", ColumnConstants.MULTI_DONOR);
            put("Organizations and Project ID", ColumnConstants.ORGANIZATIONS_AND_PROJECT_ID);
            put("Overage Project", ColumnConstants.OVERAGE_PROJECT);
            put("Payment Capital - Recurrent", ColumnConstants.PAYMENT_CAPITAL___RECURRENT);
            put("Performance Alert Level", ColumnConstants.PERFORMANCE_ALERT_LEVEL);
            put("Performance Alert Type", ColumnConstants.PERFORMANCE_ALERT_TYPE);
            put("Pledges Administrative Level 0", ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_0);
            put("Pledges Administrative Level 1", ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_1);
            put("Pledges Administrative Level 2", ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_2);
            put("Pledges Administrative Level 3", ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_3);
            put("Pledges Administrative Level 4", ColumnConstants.PLEDGES_LOCATION_ADM_LEVEL_4);
            put("Pledges Aid Modality", ColumnConstants.PLEDGES_AID_MODALITY);
            put("Pledge Contact 1 - Organization", ColumnConstants.PLEDGE_CONTACT_1___MINISTRY);
            put("Pledge Contact 2 - Organization", ColumnConstants.PLEDGE_CONTACT_2___MINISTRY);
            put("Pledges Detail Date Range", ColumnConstants.PLEDGES_DETAIL_DATE_RANGE);
            put("Pledges Detail End Date", ColumnConstants.PLEDGES_DETAIL_END_DATE);
            put("Pledges Detail Start Date", ColumnConstants.PLEDGES_DETAIL_START_DATE);
            put("Pledges Donor Group", ColumnConstants.PLEDGES_DONOR_GROUP);
            put("Pledges Donor Type", ColumnConstants.PLEDGES_DONOR_TYPE);
            put("Pledges National Plan Objectives", ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES);
            put("Pledges National Plan Objectives Level 0", ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_0);
            put("Pledges National Plan Objectives Level 1", ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_1);
            put("Pledges National Plan Objectives Level 2", ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_2);
            put("Pledges National Plan Objectives Level 3", ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_3);
            put("Pledges Programs", ColumnConstants.PLEDGES_PROGRAMS);
            put("Pledges Programs Level 0", ColumnConstants.PLEDGES_PROGRAMS_LEVEL_0);
            put("Pledges Programs Level 1", ColumnConstants.PLEDGES_PROGRAMS_LEVEL_1);
            put("Pledges Programs Level 2", ColumnConstants.PLEDGES_PROGRAMS_LEVEL_2);
            put("Pledges Programs Level 3", ColumnConstants.PLEDGES_PROGRAMS_LEVEL_3);
            put("Pledges Secondary Programs", ColumnConstants.PLEDGES_SECONDARY_PROGRAMS);
            put("Pledges Secondary Programs Level 0", ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_0);
            put("Pledges Secondary Programs Level 1", ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_1);
            put("Pledges Secondary Programs Level 2", ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_2);
            put("Pledges Secondary Programs Level 3", ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_3);
            put("Pledges Tertiary Programs", ColumnConstants.PLEDGES_TERTIARY_PROGRAMS);
            put("Pledges Tertiary Programs Level 0", ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_0);
            put("Pledges Tertiary Programs Level 1", ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_1);
            put("Pledges Tertiary Programs Level 2", ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_2);
            put("Pledges Tertiary Programs Level 3", ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_3);
            put("Pledges Sectors", ColumnConstants.PLEDGES_SECTORS);
            put("Pledges Sectors Sub-Sectors", ColumnConstants.PLEDGES_SECTORS_SUBSECTORS);
            put("Pledges Sectors Sub-Sub-Sectors", ColumnConstants.PLEDGES_SECTORS_SUBSUBSECTORS);
            put("Pledges Secondary Sectors", ColumnConstants.PLEDGES_SECONDARY_SECTORS);
            put("Pledges Secondary Sub-Sectors", ColumnConstants.PLEDGES_SECONDARY_SUBSECTORS);
            put("Pledges Secondary Sub-Sub-Sectors", ColumnConstants.PLEDGES_SECONDARY_SUBSUBSECTORS);
            put("Pledges Tertiary Sectors", ColumnConstants.PLEDGES_TERTIARY_SECTORS);
            put("Pledges Tertiary Sub-Sectors", ColumnConstants.PLEDGES_TERTIARY_SUBSECTORS);
            put("Pledges Tertiary Sub-Sub-Sectors", ColumnConstants.PLEDGES_TERTIARY_SUBSUBSECTORS);
            put("Pledges Quaternary Sectors", ColumnConstants.PLEDGES_QUATERNARY_SECTORS);
            put("Pledges Quaternary Sub-Sectors", ColumnConstants.PLEDGES_QUATERNARY_SUBSECTORS);
            put("Pledges Quaternary Sub-Sub-Sectors", ColumnConstants.PLEDGES_QUATERNARY_SUBSUBSECTORS);
            put("Pledges Quinary Sectors", ColumnConstants.PLEDGES_QUINARY_SECTORS);
            put("Pledges Quinary Sub-Sectors", ColumnConstants.PLEDGES_QUINARY_SUBSECTORS);
            put("Pledges Quinary Sub-Sub-Sectors", ColumnConstants.PLEDGES_QUINARY_SUBSUBSECTORS);
            put("Pledges Type Of Assistance", ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE);
            put("Pledge Status", ColumnConstants.PLEDGE_STATUS);
            put("Pledges Titles", ColumnConstants.PLEDGES_TITLES);
            put("Primary Sector Code Official", ColumnConstants.PRIMARY_SECTOR_CODE_OFFICIAL);
            put("Project Age Ratio", ColumnConstants.PROJECT_AGE_RATIO);
            put("Project Implementation Delay", ColumnConstants.PROJECT_IMPLEMENTATION_DELAY);
            put("Project Description", ColumnConstants.PROJECT_DESCRIPTION);
            put("Project Management", ColumnConstants.PROJECT_MANAGEMENT);
            put("Project Period", ColumnConstants.PROJECT_PERIOD);
            put("Related Projects", ColumnConstants.RELATED_PROJECTS);
            put("Related Pledges", ColumnConstants.RELATED_PLEDGES);
            put("Regional Observations Actors", ColumnConstants.REGIONAL_OBSERVATIONS_ACTORS);
            put("Regional Observations Date", ColumnConstants.RELATED_PLEDGES);
            put("Regional Observations Measures Taken", ColumnConstants.REGIONAL_OBSERVATIONS_MEASURES);
            put("Sector Tag", ColumnConstants.SECTOR_TAG);
            put("Sector Tag Sub-Sector", ColumnConstants.SECTOR_TAG_SUB_SECTOR);
            put("Sector Tag Sub-Sub-Sector", ColumnConstants.SECTOR_TAG_SUB_SUB_SECTOR);
            put("SSC Modalities", ColumnConstants.SSC_MODALITIES);
            put("Variance Of Commitments", ColumnConstants.VARIANCE_OF_COMMITMENTS);
            put("Variance Of Disbursements", ColumnConstants.VARIANCE_OF_DISBURSEMENTS);
            put("Disaster Response Marker", ColumnConstants.DISASTER_RESPONSE_MARKER);
            put("Disbursement ID", ColumnConstants.DISBURSEMENT_ID);


            // replicating the same approach as in the ReportWizard (until AMP-20480 is considered)
            String[] colPrefixList = new String[] {"National Planning Objectives Level ", "Primary Program Level ",
                    "Secondary Program Level ", "Tertiary Program Level ", "Indirect Primary Program Level "};
            for (String colPrefix : colPrefixList) {
                for (int i = 1; i <= PROGRAM_LEVEL_COUNT; i++) {
                    String level = colPrefix + i;
                    put(level, level);
            }

            put(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR);
            put(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR);
            put(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR);
            put(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR);
            put(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR);
            put(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR);
            put(ColumnConstants.EFFECTIVE_FUNDING_DATE, ColumnConstants.EFFECTIVE_FUNDING_DATE);
            put(ColumnConstants.FUNDING_CLOSING_DATE, ColumnConstants.FUNDING_CLOSING_DATE);

            putAll(getMtefColumns());
        } }
        
        private Map<String, String> getMtefColumns() {
            String regex = "^(MTEF|Real MTEF"
                    + "|" + MeasureConstants.MTEF_PROJECTIONS
                    + "|" + MeasureConstants.PIPELINE_MTEF_PROJECTIONS
                    + "|" + MeasureConstants.PROJECTION_MTEF_PROJECTIONS
                    + ").*$";
    
            Map<String, String> mtefColumns = AdvancedReportUtil.getColumnList().stream()
                    .filter(col -> col.getColumnName().matches(regex))
                    .collect(Collectors.toMap(AmpColumns::getColumnName, AmpColumns::getColumnName));
    
            return mtefColumns;
        }
    };
    
    
    protected static final List<String> visibleByDefault = Arrays.asList(
            ColumnConstants.ACTIVITY_COUNT,
            ColumnConstants.TEAM,
            ColumnConstants.DONOR_AGENCY,
            ColumnConstants.RELATED_PLEDGES,
            ColumnConstants.RELATED_PROJECTS
    );

    /**
     * Dependency map between column and category value 
     */
    @SuppressWarnings("serial")
    protected static final Map<String, HardCodedCategoryValue> categoryValueDependency = new HashMap<String, HardCodedCategoryValue>() {{
        put(IMPLEMENTATION_LOCATION_ADM_LEVEL_0, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
        put(IMPLEMENTATION_LOCATION_ADM_LEVEL_1, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
        put(IMPLEMENTATION_LOCATION_ADM_LEVEL_2, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_2);
        put(IMPLEMENTATION_LOCATION_ADM_LEVEL_3, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_3);
    }};
    
    
    protected static final Map<String, Collection<String>> dependencyTypeAll = new HashMap<String, Collection<String>>() {{
        put(ColumnConstants.LOCATION_ADM_LEVEL_0,
                Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_ADM_LEVEL_0));
        put(ColumnConstants.LOCATION_ADM_LEVEL_1,
                Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_ADM_LEVEL_1));
        put(ColumnConstants.LOCATION_ADM_LEVEL_2,
                Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_ADM_LEVEL_2));
        put(ColumnConstants.LOCATION_ADM_LEVEL_3,
                Arrays.asList(ColumnConstants.LOCATION, IMPLEMENTATION_LOCATION_ADM_LEVEL_3));
    }};
}
