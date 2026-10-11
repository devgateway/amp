package org.digijava.module.aim.action.dataimporter.util;

/**
 * Central place for all string constants used by the Data Importer (Excel, Txt, config).
 * These are the field names used in column-to-entity mapping and in switch/case logic.
 */
public final class ImporterConstants {

    private ImporterConstants() {
    }

    // ----- Adjustment types (funding: Actual vs Planned) -----
    public static final String ADJUSTMENT_TYPE_ACTUAL = "Actual";
    public static final String ADJUSTMENT_TYPE_PLANNED = "Planned";

    // ----- Organization role types (for updateOrgs) -----
    public static final String ORG_TYPE_DONOR = "donor";
    public static final String ORG_TYPE_RESPONSIBLE_ORG = "responsibleOrg";
    public static final String ORG_TYPE_BENEFICIARY_AGENCY = "beneficiaryAgency";
    public static final String ORG_TYPE_EXECUTING_AGENCY = "executingAgency";
    public static final String ORG_TYPE_IMPLEMENTING_AGENCY = "implementingAgency";
    public static final String ORG_TYPE_CONTRACTING_AGENCY = "contractingAgency";
    public static final String ORG_TYPE_RECORDING_ORGANIZATION = "recordingOrganization";

    // ----- Entity / column field names (template mapping) -----
    public static final String PROJECT_TITLE = "Project Title";
    public static final String PROJECT_CODE = "Project Code";
    public static final String OBJECTIVE = "Objective";
    public static final String PROJECT_DESCRIPTION = "Project Description";
    public static final String PRIMARY_SECTOR = "Primary Sector";
    public static final String SECONDARY_SECTOR = "Secondary Sector";
    public static final String PROJECT_LOCATION = "Project Location";
    public static final String PROJECT_START_DATE = "Project Start Date";
    public static final String PROPOSED_PROJECT_START_DATE = "Proposed Project Start Date";
    public static final String PROPOSED_PROJECT_END_DATE = "Proposed Project End Date";
    public static final String PROJECT_AGREEMENT_SIGN_DATE = "Project Agreement Sign Date";
    public static final String PROJECT_END_DATE = "Project End Date";
    public static final String DONOR_AGENCY = "Donor Agency";
    public static final String DONOR_AGENCY_CODE = "Donor Agency Code";
    public static final String EXCHANGE_RATE = "Exchange Rate";
    public static final String ORG_GROUP = "Organization Group";
    public static final String DONOR_ORGANIZATION_GROUP = "Donor Organization Group";
    public static final String RESPONSIBLE_ORGANIZATION = "Responsible Organization";
    public static final String RESPONSIBLE_ORGANIZATION_GROUP = "Responsible Organization Group";
    public static final String RESPONSIBLE_ORGANIZATION_CODE = "Responsible Organization Code";
    public static final String EXECUTING_AGENCY = "Executing Agency";
    public static final String EXECUTING_AGENCY_GROUP = "Executing Agency Group";
    public static final String IMPLEMENTING_AGENCY = "Implementing Agency";
    public static final String IMPLEMENTING_AGENCY_GROUP = "Implementing Agency Group";
    public static final String CONTRACTING_AGENCY = "Contracting Agency";
    public static final String CONTRACTING_AGENCY_GROUP = "Contracting Agency Group";
    public static final String BENEFICIARY_AGENCY = "Beneficiary Agency";
    public static final String BENEFICIARY_AGENCY_GROUP = "Beneficiary Agency Group";

    public static final String ACTUAL_DISBURSEMENT = "Actual Disbursement";
    public static final String ACTUAL_COMMITMENT = "Actual Commitment";
    public static final String ACTUAL_EXPENDITURE = "Actual Expenditure";
    public static final String PLANNED_DISBURSEMENT = "Planned Disbursement";
    public static final String PLANNED_COMMITMENT = "Planned Commitment";
    public static final String PLANNED_EXPENDITURE = "Planned Expenditure";
    public static final String TRANSACTION_AMOUNT = "Transaction Amount";
    public static final String MEASURE_TYPE = "Measure Type";
    public static final String TRANSACTION_DATE = "Transaction Date";
    public static final String FINANCING_INSTRUMENT = "Financing Instrument";
    public static final String TYPE_OF_ASSISTANCE = "Type Of Assistance";
    public static final String PRIMARY_SUBSECTOR = "Primary Subsector";
    public static final String SECONDARY_SUBSECTOR = "Secondary Subsector";
    public static final String CURRENCY = "Currency";
    public static final String COMPONENT_NAME = "Component Name";
    public static final String COMPONENT_CODE = "Component Code";

    public static final String REPORTING_DATE = "Reporting Date";
    public static final String PROJECT_STATUS = "Project Status";
    public static final String PROCUREMENT_SYSTEM = "Procurement System";
    public static final String ACTIVITY_INTERNAL_ID = "Activity Internal ID";
    public static final String RECORDING_ORGANIZATION = "Recording Organization";

    // ----- Indicator (M&E) columns -----
    public static final String INDICATOR_NAME = "Indicator Name";
    public static final String PROGRAM_NAME = "Program Name";
    public static final String PROGRAM_CLASSIFICATION = "Program Classification";
    public static final String PRIMARY_PROGRAM = "Primary Program";
    public static final String SECONDARY_PROGRAM = "Secondary Program";
    public static final String TERTIARY_PROGRAM = "Tertiary Program";
    public static final String NATIONAL_PLAN_OBJECTIVE = "National Plan Objective";
    /** Used for project-level location (e.g. Project Location). */
    public static final String LOCATION = "Location";
    /** Used for matching indicator value to activity location; distinct from project Location. */
    public static final String INDICATOR_LOCATION = "Indicator Location";
    public static final String ORIGINAL_BASE_VALUE = "Original Base Value";
    public static final String ORIGINAL_BASE_VALUE_DATE = "Original Base Value Date";
    public static final String REVISED_BASE_VALUE = "Revised Base Value";
    public static final String REVISED_BASE_VALUE_DATE = "Revised Base Value Date";
    public static final String ORIGINAL_TARGET_VALUE = "Original Target Value";
    public static final String ORIGINAL_TARGET_VALUE_DATE = "Original Target Value Date";
    public static final String REVISED_TARGET_VALUE = "Revised Target Value";
    public static final String REVISED_TARGET_VALUE_DATE = "Revised Target Value Date";
    public static final String ACTUAL_VALUE = "Actual Value";
    public static final String ACTUAL_VALUE_DATE = "Actual Value Date";
    public static final String UNIT_OF_MEASURE = "Unit of Measure";
}
