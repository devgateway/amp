/**
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * Constants for Column Names. <br>
 * Values are pulled from amp_columns.columnname and several are defined manually.
 * @author Nadejda Mandrescu
 *
 */
public class ColumnConstants {
    public static final String ACCESSION_INSTRUMENT = "Accession Instrument";
    public static final String AC_CHAPTER = "A.C. Chapter";
    public static final String ACTIVITY_COUNT = "Activity Count";
    public static final String ACTIVITY_APPROVED_BY = "Activity Approved By";
    public static final String ACTIVITY_CREATED_BY = "Activity Created By";
    public static final String ACTIVITY_CREATED_ON = "Activity Created On";
    public static final String ACTIVITY_ID = "Activity Id";
    public static final String ACTIVITY_PLEDGES_TITLE = "Activity Pledges Title";
    public static final String ACTIVITY_UPDATED_BY = "Activity Updated By";
    public static final String ACTIVITY_UPDATED_ON = "Activity Updated On";
    public static final String ACTORS = "Actors";
    public static final String ACTUAL_APPROVAL_DATE = "Actual Approval Date";
    public static final String ACTUAL_COMPLETION_DATE = "Actual Completion Date";
    public static final String ACTUAL_START_DATE = "Actual Start Date";
    public static final String AGE_OF_PROJECT_MONTHS = "Age of Project (Months)";
    public static final String AGREEMENT_CLOSE_DATE = "Agreement Close Date";
    public static final String AGREEMENT_CODE = "Agreement Code";
    public static final String AGREEMENT_EFFECTIVE_DATE = "Agreement Effective Date";
    public static final String AGREEMENT_SIGNATURE_DATE = "Agreement Signature Date";
    public static final String AGREEMENT_PARLIAMENTARY_APPROVAL_DATE = "Agreement Parlimentary Approval Date";
    public static final String AGREEMENT_TITLE_CODE = "Agreement Title + Code";
    public static final String AMP_ID = "AMP ID";
    
    /**
     * text column which will always have a constant value (currently "constant", but do not count on it)
     */
    public static final String CONSTANT = "Constant";
    
    public static final String APPROVAL_STATUS = "Approval Status";
    public static final String VALIDATION_STATUS = "Filtered Approval Status";
    public static final String ARCHIVED = "Archived";
    public static final String AUDIT_SYSTEM = "Audit System";
    public static final String AVERAGE_SIZE_OF_DISBURSEMENTS = "Average Size of Disbursements";
    public static final String AVERAGE_SIZE_OF_PROJECTS = "Average Size of Projects";
    public static final String BENEFICIARY_AGENCY = "Beneficiary Agency";
    public static final String BENEFICIARY_AGENCY__DEPARTMENT_DIVISION = "Beneficiary Agency  Department/Division";
    public static final String BENEFICIARY_AGENCY_GROUPS = "Beneficiary Agency Groups";
    public static final String BENEFICIARY_AGENCY_TYPE = "Beneficiary Agency Type";
    public static final String BENEFICIARY_AGENCY_COUNTRY = "Beneficiary Agency Country";
    public static final String BUDGET_CODE_PROJECT_ID = "Budget Code Project Id";
    public static final String BUDGET_DEPARTMENT = "Budget Department";
    public static final String BUDGET_ORGANIZATION = "Budget Organization";
    public static final String BUDGET_PROGRAM = "Budget Program";
    public static final String BUDGET_SECTOR = "Budget Sector";
    public static final String BUDGET_STRUCTURE = "Budget Structure";
    public static final String CALCULATED_PROJECT_LIFE = "Calculated Project Life";
    public static final String CAPITAL_EXPENDITURE = "Capital Expenditure";
    public static final String COMPONENT_DESCRIPTION = "Component description";
    public static final String COMPONENT_FUNDING_ORGANIZATION = "Component Funding Organization";
    public static final String COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION = "Component Second Responsible Organization";
    public static final String COMPONENT_NAME = "Component Name";
    public static final String COMPONENT_TYPE = "Component Type";
    public static final String COMPUTED_YEAR = "Computed Year";
    public static final String CONCESSIONALITY_LEVEL = "Concessionality Level";
    public static final String CONTRACTING_AGENCY = "Contracting Agency";
    public static final String CONTRACTING_AGENCY_ACRONYM = "Contracting Agency Acronym";
    public static final String CONTRACTING_AGENCY_DEPARTMENT_DIVISION = "Contracting Agency Department/Division";
    public static final String CONTRACTING_AGENCY_GROUPS = "Contracting Agency Groups";
    public static final String CONTRACTING_AGENCY_TYPE = "Contracting Agency Type";
    public static final String CREDIT_DONATION = "Credit/Donation";
    public static final String CRIS_NUMBER = "Cris Number";
    public static final String CUMULATIVE_EXECUTION_RATE = "Cumulative Execution Rate";
    public static final String CURRENT_COMPLETION_DATE_COMMENTS = "Current Completion Date Comments";
    public static final String DESCRIPTION_OF_COMPONENT_FUNDING = "Description of Component Funding";
    public static final String LOCATION = "Location";
    public static final String RAW_LOCATION = "Raw Location";
    public static final String GEOCODE = "GeoId";
    public static final String DISBURSEMENT_ID = "Disbursement ID";
    public static final String DONOR_ACRONYM = "Organization Acronym";
    public static final String DONOR_AGENCY = "Donor Agency";
    public static final String DONOR_ID = "Donor Id";
    public static final String DONOR_COMMITMENT_DATE = "Donor Commitment Date";
    public static final String DONOR_CONTACT_EMAIL = "Donor Contact Email";
    public static final String DONOR_CONTACT_FAX = "Donor Contact Fax";
    public static final String DONOR_CONTACT_NAME = "Donor Contact Name";
    public static final String DONOR_CONTACT_ORGANIZATION = "Donor Contact Organization";
    public static final String DONOR_CONTACT_PHONE = "Donor Contact Phone";
    public static final String DONOR_CONTACT_TITLE = "Donor Contact Title";
    public static final String DONOR_GROUP = "Donor Group";
    public static final String DONOR_TYPE = "Donor Type";
    public static final String DONOR_BUDGET_CODE = "Donor Budget Code";
    public static final String DONOR_COUNTRY = "Donor Country";
    public static final String DRAFT = "Draft";
    public static final String ENVIRONMENT = "Environment";
    public static final String EQUAL_OPPORTUNITY = "Equal Opportunity";
    public static final String EXECUTING_AGENCY = "Executing Agency";
    public static final String EXECUTING_AGENCY_DEPARTMENT_DIVISION = "Executing Agency Department/Division";
    public static final String EXECUTING_AGENCY_GROUPS = "Executing Agency Groups";
    public static final String EXECUTING_AGENCY_TYPE = "Executing Agency Type";
    public static final String EXECUTING_AGENCY_COUNTRY = "Executing Agency Country";
    public static final String EXECUTION_RATE = "Execution Rate";
    public static final String FINAL_DATE_FOR_CONTRACTING = "Final Date for Contracting";
    public static final String FINAL_DATE_FOR_DISBURSEMENTS = "Final Date for Disbursements";
    public static final String FINAL_DATE_FOR_DISBURSEMENTS_COMMENTS = "Final Date for Disbursements Comments";
    public static final String FINANCIAL_INSTRUMENT = "Financial Instrument";
    public static final String FINANCING_INSTRUMENT = "Financing Instrument";
    public static final String FUNDING_ID = "Funding Id";
    public static final String FUNDING_CLASSIFICATION_DATE = "Funding Classification Date";
    public static final String FUNDING_END_DATE = "Funding end date";
    public static final String FUNDING_ORGANIZATION_ID = "Funding Organization Id";
    public static final String FUNDING_START_DATE = "Funding start date";
    public static final String FUNDING_STATUS = "Funding Status";
    public static final String FY = "FY";
    public static final String GOVERNMENT_AGREEMENT_NUMBER = "Government Agreement Number";
    public static final String GOVERNMENT_APPROVAL_PROCEDURES = "Government Approval Procedures";
    public static final String GPI_1_Q6 = "Q6";
    public static final String GPI_1_Q6_DESCRIPTION = "Q6 Description";
    public static final String GPI_1_Q7 = "Q7";
    public static final String GPI_1_Q8 = "Q8";
    public static final String GPI_1_Q9 = "Q9";
    public static final String GPI_1_Q10 = "Q10";
    public static final String GPI_1_Q10_DESCRIPTION = "Q10 Description";

    public static final String GRACE_PERIOD = "Loan Grace Period";
    public static final String HAS_EXECUTING_AGENCY = "Has Executing Agency";
    public static final String IATI_IDENTIFIER = "IATI Identifier";
    public static final String IMPLEMENTATION_LEVEL = "Implementation Level";
    public static final String IMPLEMENTATION_LOCATION = "Implementation Location";
    public static final String IMPLEMENTING_AGENCY = "Implementing Agency";
    public static final String IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION = "Implementing Agency Department/Division";
    public static final String IMPLEMENTING_AGENCY_GROUPS = "Implementing Agency Groups";
    public static final String IMPLEMENTING_AGENCY_TYPE = "Implementing Agency Type";
    public static final String IMPLEMENTING_EXECUTING_AGENCY_CONTACT_EMAIL = "Implementing/Executing Agency Contact Email";
    public static final String IMPLEMENTING_EXECUTING_AGENCY_CONTACT_FAX = "Implementing/Executing Agency Contact Fax";
    public static final String IMPLEMENTING_EXECUTING_AGENCY_CONTACT_NAME = "Implementing/Executing Agency Contact Name";
    public static final String IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION = "Implementing/Executing Agency Contact Organization";
    public static final String IMPLEMENTING_EXECUTING_AGENCY_CONTACT_PHONE = "Implementing/Executing Agency Contact Phone";
    public static final String IMPLEMENTING_EXECUTING_AGENCY_CONTACT_TITLE = "Implementing/Executing Agency Contact Title";
    public static final String INDIRECT_ON_BUDGET = "Indirect On Budget";
    public static final String INSTITUTIONS = "Institutions";
    public static final String INTERNAL_USE_ID = "Internal Use Id";
    public static final String INTEREST_RATE = "Loan Interest Rate";
    public static final String ISSUES = "Issues";
    public static final String ISSUE_DATE = "Issue Date";
    public static final String ISSUES___MEASURES___ACTORS = "Issues / Measures / Actors";
    public static final String JOINT_CRITERIA = "Joint Criteria";
    public static final String LINE_MINISTRY_OBSERVATIONS = "Line Ministry Observations";
    public static final String LINE_MINISTRY_OBSERVATIONS_ACTORS = "Line Ministry Observations Actors";
    public static final String LINE_MINISTRY_OBSERVATIONS_DATE = "Line Ministry Observations Date";
    public static final String LINE_MINISTRY_OBSERVATIONS_MEASURES = "Line Ministry Observations Measures";
    public static final String LOCATION_ADM_LEVEL_0 = "Administrative Level 0";
    public static final String LOCATION_ADM_LEVEL_1 = "Administrative Level 1";
    public static final String LOCATION_ADM_LEVEL_2 = "Administrative Level 2";
    public static final String LOCATION_ADM_LEVEL_3 = "Administrative Level 3";
    public static final String LOCATION_ADM_LEVEL_4 = "Administrative Level 4";
    public static final String MATURITY = "Loan Maturity Date";
    public static final String MEASURES_TAKEN = "Measures Taken";
    public static final String MINISTRY_CODE = "Ministry Code";
    public static final String MINISTRY_OF_FINANCE_CONTACT_EMAIL = "Ministry Of Finance Contact Email";
    public static final String MINISTRY_OF_FINANCE_CONTACT_FAX = "Ministry Of Finance Contact Fax";
    public static final String MINISTRY_OF_FINANCE_CONTACT_NAME = "Ministry Of Finance Contact Name";
    public static final String MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION = "Ministry Of Finance Contact Organization";
    public static final String MINISTRY_OF_FINANCE_CONTACT_PHONE = "Ministry Of Finance Contact Phone";
    public static final String MINISTRY_OF_FINANCE_CONTACT_TITLE = "Ministry Of Finance Contact Title";
    public static final String MINORITIES = "Minorities";
    public static final String MODE_OF_PAYMENT = "Mode of Payment";
    public static final String MULTI_DONOR = "Multi Donor";
    public static final String NATIONAL_PLAN_OBJECTIVE = "National Plan Objective";
    public static final String NATIONAL_PLANNING_OBJECTIVES = "National Planning Objectives";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_0 = "National Planning Objectives Level 0";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_1 = "National Planning Objectives Level 1";
    public static final String NATIONAL_PLANNING_OBJECTIVES_CODE = "National Planning Objectives Code";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_2 = "National Planning Objectives Level 2";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_3 = "National Planning Objectives Level 3";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_4 = "National Planning Objectives Level 4";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_5 = "National Planning Objectives Level 5";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_6 = "National Planning Objectives Level 6";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_7 = "National Planning Objectives Level 7";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_8 = "National Planning Objectives Level 8";
    public static final String OBJECTIVE = "Objective";
    public static final String ACTIVITY_BUDGET = "Activity Budget";
    public static final String HUMANITARIAN_AID = "Humanitarian Aid";
    public static final String MULTI_STAKEHOLDER_PARTNERS = "Multi Stakeholder Partners";
    public static final String MULTI_STAKEHOLDER_PARTNERSHIP = "Multi Stakeholder Partnership";
    public static final String DISASTER_RESPONSE_MARKER = "Disaster Response Marker";
    public static final String EXPENDITURE_CLASS = "Expenditure Class";
    public static final String ORGANIZATIONS_AND_PROJECT_ID = "Organizations and Project ID";
    public static final String ORIGINAL_COMPLETION_DATE = "Original Completion Date";
    public static final String MODALITIES = "Modalities";
    public static final String OVERAGE_PROJECT = "Overage Project";
    public static final String PAYMENT_CAPITAL___RECURRENT = "Payment Capital - Recurrent";
    public static final String PERFORMANCE_ALERT_LEVEL = "Performance Alert Level";
    public static final String PERFORMANCE_ALERT_TYPE = "Performance Alert Type";
    public static final String PLEDGE_CONTACT_1___ADDRESS = "Pledge Contact 1 - Address";
    public static final String PLEDGE_CONTACT_1___ALTERNATE_CONTACT = "Pledge Contact 1 - Alternate Contact";
    public static final String PLEDGE_CONTACT_1___ALTERNATE_EMAIL = "Pledge Contact 1 - Alternate Email";
    public static final String PLEDGE_CONTACT_1___ALTERNATE_PHONE = "Pledge Contact 1 - Alternate Phone";
    public static final String PLEDGE_CONTACT_1___EMAIL = "Pledge Contact 1 - Email";
    public static final String PLEDGE_CONTACT_1___MINISTRY = "Pledge Contact 1 - Ministry";
    public static final String PLEDGE_CONTACT_1___NAME = "Pledge Contact 1 - Name";
    public static final String PLEDGE_CONTACT_1___TELEPHONE = "Pledge Contact 1 - Telephone";
    public static final String PLEDGE_CONTACT_1___FAX = "Pledge Contact 1 - Fax";
    public static final String PLEDGE_CONTACT_1___TITLE = "Pledge Contact 1 - Title";
    public static final String PLEDGE_CONTACT_2___ADDRESS = "Pledge Contact 2 - Address";
    public static final String PLEDGE_CONTACT_2___ALTERNATE_CONTACT = "Pledge Contact 2 - Alternate Contact";
    public static final String PLEDGE_CONTACT_2___ALTERNATE_EMAIL = "Pledge Contact 2 - Alternate Email";
    public static final String PLEDGE_CONTACT_2___ALTERNATE_PHONE = "Pledge Contact 2 - Alternate Phone";
    public static final String PLEDGE_CONTACT_2___EMAIL = "Pledge Contact 2 - Email";
    public static final String PLEDGE_CONTACT_2___MINISTRY = "Pledge Contact 2 - Ministry";
    public static final String PLEDGE_CONTACT_2___NAME = "Pledge Contact 2 - Name";
    public static final String PLEDGE_CONTACT_2___TELEPHONE = "Pledge Contact 2 - Telephone";
    public static final String PLEDGE_CONTACT_2___FAX = "Pledge Contact 2 - Fax";
    public static final String PLEDGE_CONTACT_2___TITLE = "Pledge Contact 2 - Title";
    public static final String PLEDGES_AID_MODALITY = "Pledges Aid Modality";
    public static final String PLEDGES_DETAIL_DATE_RANGE = "Pledges Detail Date Range";
    public static final String PLEDGES_DETAIL_END_DATE = "Pledges Detail End Date";
    public static final String PLEDGES_DETAIL_START_DATE = "Pledges Detail Start Date";
    public static final String PLEDGES_DONOR_GROUP = "Pledges Donor Group";
    public static final String PLEDGES_DONOR_TYPE = "Pledges Donor Type";
    
    public static final String PLEDGES_NATIONAL_PLAN_OBJECTIVES = "Pledges National Plan Objectives";
    public static final String PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_0 = "Pledges National Plan Objectives Level 0";
    public static final String PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_1 = "Pledges National Plan Objectives Level 1";
    public static final String PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_2 = "Pledges National Plan Objectives Level 2";
    public static final String PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_3 = "Pledges National Plan Objectives Level 3";
    
    public static final String PLEDGES_PROGRAMS = "Pledges Programs";
    public static final String PLEDGES_PROGRAMS_LEVEL_0 = "Pledges Programs Level 0";
    public static final String PLEDGES_PROGRAMS_LEVEL_1 = "Pledges Programs Level 1";
    public static final String PLEDGES_PROGRAMS_LEVEL_2 = "Pledges Programs Level 2";
    public static final String PLEDGES_PROGRAMS_LEVEL_3 = "Pledges Programs Level 3";
    
    public static final String PLEDGES_LOCATION_ADM_LEVEL_0 = "Pledges Administrative Level 0";
    public static final String PLEDGES_LOCATION_ADM_LEVEL_1 = "Pledges Administrative Level 1";
    public static final String PLEDGES_LOCATION_ADM_LEVEL_2 = "Pledges Administrative Level 2";
    public static final String PLEDGES_LOCATION_ADM_LEVEL_3 = "Pledges Administrative Level 3";
    public static final String PLEDGES_LOCATION_ADM_LEVEL_4 = "Pledges Administrative Level 4";

    public static final String PLEDGES_SECONDARY_PROGRAMS = "Pledges Secondary Programs";
    public static final String PLEDGES_SECONDARY_PROGRAMS_LEVEL_0 = "Pledges Secondary Programs Level 0";
    public static final String PLEDGES_SECONDARY_PROGRAMS_LEVEL_1 = "Pledges Secondary Programs Level 1";
    public static final String PLEDGES_SECONDARY_PROGRAMS_LEVEL_2 = "Pledges Secondary Programs Level 2";
    public static final String PLEDGES_SECONDARY_PROGRAMS_LEVEL_3 = "Pledges Secondary Programs Level 3";
    
    public static final String PLEDGES_TERTIARY_PROGRAMS = "Pledges Tertiary Programs";
    public static final String PLEDGES_TERTIARY_PROGRAMS_LEVEL_0 = "Pledges Tertiary Programs Level 0";
    public static final String PLEDGES_TERTIARY_PROGRAMS_LEVEL_1 = "Pledges Tertiary Programs Level 1";
    public static final String PLEDGES_TERTIARY_PROGRAMS_LEVEL_2 = "Pledges Tertiary Programs Level 2";
    public static final String PLEDGES_TERTIARY_PROGRAMS_LEVEL_3 = "Pledges Tertiary Programs Level 3";
    
    public static final String PLEDGES_SECTORS = "Pledges Sectors";
    public static final String PLEDGES_SECTORS_SUBSECTORS = "Pledges Sectors Sub-Sectors";
    public static final String PLEDGES_SECTORS_SUBSUBSECTORS = "Pledges Sectors Sub-Sub-Sectors";
    
    public static final String PLEDGES_SECONDARY_SECTORS = "Pledges Secondary Sectors";
    public static final String PLEDGES_SECONDARY_SUBSECTORS = "Pledges Secondary Sub-Sectors";
    public static final String PLEDGES_SECONDARY_SUBSUBSECTORS = "Pledges Secondary Sub-Sub-Sectors";
    
    public static final String PLEDGES_TERTIARY_SECTORS = "Pledges Tertiary Sectors";
    public static final String PLEDGES_TERTIARY_SUBSECTORS = "Pledges Tertiary Sub-Sectors";
    public static final String PLEDGES_TERTIARY_SUBSUBSECTORS = "Pledges Tertiary Sub-Sub-Sectors";

    public static final String PLEDGES_QUATERNARY_SECTORS = "Pledges Quaternary Sectors";
    public static final String PLEDGES_QUATERNARY_SUBSECTORS = "Pledges Quaternary Sub-Sectors";
    public static final String PLEDGES_QUATERNARY_SUBSUBSECTORS = "Pledges Quaternary Sub-Sub-Sectors";

    public static final String PLEDGES_QUINARY_SECTORS = "Pledges Quinary Sectors";
    public static final String PLEDGES_QUINARY_SUBSECTORS = "Pledges Quinary Sub-Sectors";
    public static final String PLEDGES_QUINARY_SUBSUBSECTORS = "Pledges Quinary Sub-Sub-Sectors";

    public static final String PLEDGE_STATUS = "Pledge Status";
    public static final String PLEDGES_TITLES = "Pledges Titles";
    public static final String PLEDGES_TYPE_OF_ASSISTANCE = "Pledges Type Of Assistance";
    public static final String RELATED_PLEDGES = "Related Pledges";
    
    public static final String RELATED_PROJECTS = "Related Projects";
    public static final String PRIMARY_PROGRAM = "Primary Program";
    public static final String PRIMARY_PROGRAM_LEVEL_0 = "Primary Program Level 0";
    public static final String PRIMARY_PROGRAM_LEVEL_1 = "Primary Program Level 1";
    public static final String PRIMARY_PROGRAM_LEVEL_2 = "Primary Program Level 2";
    public static final String PRIMARY_PROGRAM_LEVEL_3 = "Primary Program Level 3";
    public static final String PRIMARY_PROGRAM_LEVEL_4 = "Primary Program Level 4";
    public static final String PRIMARY_PROGRAM_LEVEL_5 = "Primary Program Level 5";
    public static final String PRIMARY_PROGRAM_LEVEL_6 = "Primary Program Level 6";
    public static final String PRIMARY_PROGRAM_LEVEL_7 = "Primary Program Level 7";
    public static final String PRIMARY_PROGRAM_LEVEL_8 = "Primary Program Level 8";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_0 = "Indirect Primary Program Level 0";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_1 = "Indirect Primary Program Level 1";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_2 = "Indirect Primary Program Level 2";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_3 = "Indirect Primary Program Level 3";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_4 = "Indirect Primary Program Level 4";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_5 = "Indirect Primary Program Level 5";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_6 = "Indirect Primary Program Level 6";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_7 = "Indirect Primary Program Level 7";
    public static final String INDIRECT_PRIMARY_PROGRAM_LEVEL_8 = "Indirect Primary Program Level 8";
    public static final String PRIMARY_SECTOR = "Primary Sector";
    public static final String PRIMARY_SECTOR_CODE_OFFICIAL = "Primary Sector Code Official";
    public static final String PRIMARY_SECTOR_SUB_SECTOR = "Primary Sector Sub-Sector";
    public static final String PRIMARY_SECTOR_SUB_SUB_SECTOR = "Primary Sector Sub-Sub-Sector";
    public static final String PROCUREMENT_SYSTEM = "Procurement System";
    public static final String PROGRAM_DESCRIPTION = "Program Description";
    public static final String PROJECT_AGE_RATIO = "Project Age Ratio";
    public static final String PROJECT_CATEGORY = "Project Category";
    public static final String PROJECT_CODE = "Project Code";
    public static final String PROJECT_COMMENTS = "Project Comments";
    public static final String PROJECT_COORDINATOR_CONTACT_EMAIL = "Project Coordinator Contact Email";
    public static final String PROJECT_COORDINATOR_CONTACT_FAX = "Project Coordinator Contact Fax";
    public static final String PROJECT_COORDINATOR_CONTACT_NAME = "Project Coordinator Contact Name";
    public static final String PROJECT_COORDINATOR_CONTACT_ORGANIZATION = "Project Coordinator Contact Organization";
    public static final String PROJECT_COORDINATOR_CONTACT_PHONE = "Project Coordinator Contact Phone";
    public static final String PROJECT_COORDINATOR_CONTACT_TITLE = "Project Coordinator Contact Title";
    public static final String PROJECT_DESCRIPTION = "Project Description";
    public static final String PROJECT_IMPACT = "Project Impact";
    public static final String PROJECT_IMPLEMENTING_UNIT = "Project Implementing Unit";
    public static final String PROJECT_MANAGEMENT = "Project Management";
    public static final String PROJECT_PERIOD = "Project Period";
    public static final String PROJECT_TITLE = "Project Title";
    public static final String PROPOSED_APPROVAL_DATE = "Proposed Approval Date";
    public static final String PROPOSED_COMPLETION_DATE = "Proposed Completion Date";
    public static final String PROJECT_IMPLEMENTATION_DELAY = "Project Implementation Delay";
    public static final String PROPOSED_PROJECT_AMOUNT = "Proposed Project Amount";
    public static final String PROPOSED_PROJECT_LIFE = "Proposed Project Life";
    public static final String PROPOSED_START_DATE = "Proposed Start Date";
    public static final String PURPOSE = "Purpose";
    public static final String RATIFICATION_DATE = "Loan Ratification Date";
    public static final String REGIONAL_REGION = "Regional Region";
    public static final String REGIONAL_GROUP = "Regional Group";
    public static final String REGIONAL_GROUP_GROUP = "Regional Group Group";   
    public static final String REGIONAL_GROUP_DEPARTMENT_DIVISION = "Regional Group Department/Division";
    public static final String REGIONAL_OBSERVATIONS = "Regional Observations";
    public static final String REGIONAL_OBSERVATIONS_ACTORS = "Regional Observations Actors";
    public static final String REGIONAL_OBSERVATIONS_DATE = "Regional Observations Date";
    public static final String REGIONAL_OBSERVATIONS_MEASURES = "Regional Observations Measures Taken";

    public static final String REPORTING_SYSTEM = "Reporting System";
    public static final String RESPONSIBLE_ORGANIZATION = "Responsible Organization";
    public static final String RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION = "Responsible Organization Department/Division";
    public static final String RESPONSIBLE_ORGANIZATION_GROUPS = "Responsible Organization Groups";
    public static final String RESPONSIBLE_ORGANIZATION_TYPE = "Responsible Organization Type";
    public static final String RESULTS = "Results";
    public static final String REVISED_PROJECT_AMOUNT = "Revised Project Amount";
    public static final String SECONDARY_PROGRAM = "Secondary Program";
    public static final String SECONDARY_PROGRAM_LEVEL_0 = "Secondary Program Level 0";
    public static final String SECONDARY_PROGRAM_LEVEL_1 = "Secondary Program Level 1";
    public static final String SECONDARY_PROGRAM_LEVEL_2 = "Secondary Program Level 2";
    public static final String SECONDARY_PROGRAM_LEVEL_3 = "Secondary Program Level 3";
    public static final String SECONDARY_PROGRAM_LEVEL_4 = "Secondary Program Level 4";
    public static final String SECONDARY_PROGRAM_LEVEL_5 = "Secondary Program Level 5";
    public static final String SECONDARY_PROGRAM_LEVEL_6 = "Secondary Program Level 6";
    public static final String SECONDARY_PROGRAM_LEVEL_7 = "Secondary Program Level 7";
    public static final String SECONDARY_PROGRAM_LEVEL_8 = "Secondary Program Level 8";
    public static final String SECONDARY_SECTOR = "Secondary Sector";
    public static final String SECONDARY_SECTOR_SUB_SECTOR = "Secondary Sector Sub-Sector";
    public static final String SECONDARY_SECTOR_SUB_SUB_SECTOR = "Secondary Sector Sub-Sub-Sector";
    public static final String SECTOR_GROUP = "Sector Group";
    public static final String SECTOR_GROUP_GROUP = "Sector Group Group";
    public static final String SECTOR_GROUP_DEPARTMENT_DIVISION = "Sector Group Department/Division";
    public static final String SECTOR_MINISTRY_CONTACT_EMAIL = "Sector Ministry Contact Email";
    public static final String SECTOR_MINISTRY_CONTACT_FAX = "Sector Ministry Contact Fax";
    public static final String SECTOR_MINISTRY_CONTACT_NAME = "Sector Ministry Contact Name";
    public static final String SECTOR_MINISTRY_CONTACT_ORGANIZATION = "Sector Ministry Contact Organization";
    public static final String SECTOR_MINISTRY_CONTACT_PHONE = "Sector Ministry Contact Phone";
    public static final String SECTOR_MINISTRY_CONTACT_TITLE = "Sector Ministry Contact Title";
    public static final String SECTOR_TAG = "Sector Tag";
    public static final String SECTOR_TAG_SUB_SECTOR = "Sector Tag Sub-Sector";
    public static final String SECTOR_TAG_SUB_SUB_SECTOR = "Sector Tag Sub-Sub-Sector";
    public static final String SSC_MODALITIES = "SSC Modalities";
    public static final String STATUS = "Status";
    public static final String STRUCTURES_COLUMN = "Structures Column";
    public static final String SUB_PROGRAM = "Sub-Program";
    public static final String SUB_VOTE = "Sub-Vote";
    public static final String TEAM = "Team";
    public static final String TERTIARY_PROGRAM = "Tertiary Program";
    public static final String TERTIARY_PROGRAM_LEVEL_0 = "Tertiary Program Level 0";
    public static final String TERTIARY_PROGRAM_LEVEL_1 = "Tertiary Program Level 1";
    public static final String TERTIARY_PROGRAM_LEVEL_2 = "Tertiary Program Level 2";
    public static final String TERTIARY_PROGRAM_LEVEL_3 = "Tertiary Program Level 3";
    public static final String TERTIARY_PROGRAM_LEVEL_4 = "Tertiary Program Level 4";
    public static final String TERTIARY_PROGRAM_LEVEL_5 = "Tertiary Program Level 5";
    public static final String TERTIARY_PROGRAM_LEVEL_6 = "Tertiary Program Level 6";
    public static final String TERTIARY_PROGRAM_LEVEL_7 = "Tertiary Program Level 7";
    public static final String TERTIARY_PROGRAM_LEVEL_8 = "Tertiary Program Level 8";
    public static final String TERTIARY_SECTOR = "Tertiary Sector";
    public static final String TERTIARY_SECTOR_SUB_SECTOR = "Tertiary Sector Sub-Sector";
    public static final String TERTIARY_SECTOR_SUB_SUB_SECTOR = "Tertiary Sector Sub-Sub-Sector";
    public static final String QUATERNARY_SECTOR = "Quaternary Sector";
    public static final String QUATERNARY_SECTOR_SUB_SECTOR = "Quaternary Sector Sub-Sector";
    public static final String QUATERNARY_SECTOR_SUB_SUB_SECTOR = "Quaternary Sector Sub-Sub-Sector";
    public static final String QUINARY_SECTOR = "Quinary Sector";
    public static final String QUINARY_SECTOR_SUB_SECTOR = "Quinary Sector Sub-Sector";
    public static final String QUINARY_SECTOR_SUB_SUB_SECTOR = "Quinary Sector Sub-Sub-Sector";
    public static final String TOTAL_GRAND_ACTUAL_COMMITMENTS = "Total Grand Actual Commitments";
    public static final String TOTAL_GRAND_ACTUAL_DISBURSEMENTS = "Total Grand Actual Disbursements";
    public static final String TYPE_OF_ASSISTANCE = "Type Of Assistance";
    public static final String TYPE_OF_COOPERATION = "Type of Cooperation";
    public static final String TYPE_OF_IMPLEMENTATION = "Type of Implementation";
    public static final String UNCOMMITTED_BALANCE = "Uncommitted Balance";
    public static final String VARIANCE_OF_COMMITMENTS = "Variance Of Commitments";
    public static final String VARIANCE_OF_DISBURSEMENTS = "Variance Of Disbursements";
    public static final String VOTE = "Vote";
    public static final String WORKSPACES = "Workspaces";
    public static final String EFFECTIVE_FUNDING_DATE = "Effective Funding Date";
    public static final String FUNDING_CLOSING_DATE = "Funding Closing Date";

    public static final String INDICATOR_NAME = "Indicator Name";
    public static final String INDICATOR_TYPE = "Indicator Type";
    public static final String INDICATOR_DESCRIPTION = "Indicator Description";
    public static final String INDICATOR_CODE = "Indicator Code";
    public static final String INDICATOR_CREATION_DATE = "Indicator Creation Date";
    public static final String INDICATOR_SECTOR = "Indicator Sector";
    public static final String INDICATOR_RISK = "Risk";
    public static final String INDICATOR_LOGFRAME_CATEGORY = "Logframe Category";

    public static final String INDICATOR_ACTUAL_VALUE = "Indicator Current Value";
    public static final String INDICATOR_ACTUAL_DATE = "Indicator Current Date";
    public static final String INDICATOR_ACTUAL_COMMENT = "Indicator Current Comment";

    public static final String INDICATOR_BASE_VALUE = "Indicator Base Value";
    public static final String INDICATOR_BASE_DATE = "Indicator Base Date";
    public static final String INDICATOR_BASE_COMMENT = "Indicator Base Comment";

    public static final String INDICATOR_TARGET_VALUE = "Indicator Target Value";
    public static final String INDICATOR_TARGET_DATE = "Indicator Target Date";
    public static final String INDICATOR_TARGET_COMMENT = "Indicator Target Comment";

    public static final String INDICATOR_REVISED_TARGET_VALUE = "Indicator Revised Target Value";
    public static final String INDICATOR_REVISED_TARGET_DATE = "Indicator Revised Target Date";
    public static final String INDICATOR_REVISED_TARGET_COMMENT = "Indicator Revised Target Comment";

    public static final String PROJECT_RESULTS_AVAILABLE = "Project Results Available";
    public static final String VULNERABLE_GROUP = "Vulnerable Group";
    public static final String PROJECT_RESULTS_LINK = "Project Results Link";
    public static final String PROJECT_JOINT_DECISION = "Project Joint Decision";
    public static final String PROJECT_MONITORING = "Project Monitoring";
    public static final String PROJECT_SUSTAINABILITY = "Project Sustainability";
    public static final String PROJECT_PROBLEMS = "Project Problems";

    public static final String PROGRAM_SETTINGS = "Program Settings";
}
