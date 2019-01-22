package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.helper.Constants;

/**
 * Filters Constants
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersConstants {
    
    // filters IDs 
    public static final String CURRENT = "current";

    public static final String DATE = "date";

    public static final String ACTUAL_APPROVAL_DATE = "actual-approval-date";
    public static final String ACTUAL_COMPLETION_DATE = "actual-completion-date";
    public static final String ACTUAL_START_DATE = "actual-start-date";
    public static final String EFFECTIVE_FUNDING_DATE = "effective-funding-date";
    public static final String FINAL_DATE_FOR_CONTRACTING = "final-date-for-contracting";
    public static final String ISSUE_DATE = "issue-date";
    public static final String FUNDING_CLOSING_DATE = "funding-closing-date";
    public static final String PROPOSED_APPROVAL_DATE = "proposed-approval-date";
    public static final String PROPOSED_COMPLETION_DATE = "proposed-completion-date";
    public static final String PROPOSED_START_DATE = "proposed-start-date";

    public static final String ACTIVITY_ID = "activity-id";
    public static final String APPROVAL_STATUS = "approval-status";
    public static final String ARCHIVED = "archived";
    public static final String BENEFICIARY_AGENCY = "beneficiary-agency";
    public static final String COMMUNAL_SECTION = "communal-section";
    public static final String COMPONENT_FUNDING_ORGANIZATION = "component-funding-organization";
    public static final String COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION = "component-second-responsible-organization";
    public static final String COMPUTED_YEAR = "computed-year";
    public static final String CONCESSIONALITY_LEVEL = "concessionality-level";
    public static final String CONTRACTING_AGENCY = "contracting-agency";
    public static final String CONTRACTING_AGENCY_GROUPS = "contracting-agency-groups";
    public static final String COUNTRY = "country";
    public static final String DISASTER_RESPONSE_MARKER = "disaster-response-marker";
    public static final String DISTRICT = "district";
    public static final String DONOR_AGENCY = "donor-agency";
    public static final String DONOR_GROUP = "donor-group";
    public static final String DONOR_TYPE = "donor-type";
    public static final String EXECUTING_AGENCY = "executing-agency";
    public static final String EXPENDITURE_CLASS = "expenditure-class";
    public static final String FINANCING_INSTRUMENT = "financing-instrument";
    public static final String FUNDING_STATUS = "funding-status";
    public static final String GOVERNMENT_APPROVAL_PROCEDURES = "government-approval-procedures";
    public static final String HUMANITARIAN_AID = "humanitarian-aid";
    public static final String IMPLEMENTING_AGENCY = "implementing-agency";
    public static final String JOINT_CRITERIA = "joint-criteria";
    public static final String LOCATION = "location";
    public static final String MODE_OF_PAYMENT = "mode-of-payment";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_1 = "national-planning-objectives-level-1";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_2 = "national-planning-objectives-level-2";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_3 = "national-planning-objectives-level-3";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_4 = "national-planning-objectives-level-4";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_5 = "national-planning-objectives-level-5";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_6 = "national-planning-objectives-level-6";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_7 = "national-planning-objectives-level-7";
    public static final String NATIONAL_PLANNING_OBJECTIVES_LEVEL_8 = "national-planning-objectives-level-8";
    public static final String ON_OFF_TREASURY_BUDGET = "on-off-treasury-budget";
    public static final String PERFORMANCE_ALERT_LEVEL = "performance-alert-level";
    public static final String PERFORMANCE_ALERT_TYPE = "performance-alert-type";
    public static final String PLEDGES_AID_MODALITY = "pledges-aid-modality";
    public static final String PLEDGES_DONOR_GROUP = "pledges-donor-group";
    public static final String PLEDGES_NATIONAL_PLAN_OBJECTIVES = "pledges-national-plan-objectives";
    public static final String PLEDGES_PROGRAMS = "pledges-programs";
    public static final String PLEDGES_SECONDARY_PROGRAMS = "pledges-secondary-programs";
    public static final String PLEDGES_SECONDARY_SECTORS = "pledges-secondary-sectors";
    public static final String PLEDGES_SECTORS = "pledges-sectors";
    public static final String PLEDGES_TERTIARY_SECTORS = "pledges-tertiary-sectors";
    public static final String PLEDGES_QUATERNARY_SECTORS = "pledges-quaternary-sectors";
    public static final String PLEDGES_QUINARY_SECTORS = "pledges-quinary-sectors";
    public static final String PLEDGES_TITLES = "pledges-titles";
    public static final String PRIMARY_PROGRAM_LEVEL_1 = "primary-program-level-1";
    public static final String PRIMARY_PROGRAM_LEVEL_2 = "primary-program-level-2";
    public static final String PRIMARY_PROGRAM_LEVEL_3 = "primary-program-level-3";
    public static final String PRIMARY_PROGRAM_LEVEL_4 = "primary-program-level-4";
    public static final String PRIMARY_PROGRAM_LEVEL_5 = "primary-program-level-5";
    public static final String PRIMARY_PROGRAM_LEVEL_6 = "primary-program-level-6";
    public static final String PRIMARY_PROGRAM_LEVEL_7 = "primary-program-level-7";
    public static final String PRIMARY_PROGRAM_LEVEL_8 = "primary-program-level-8";
    public static final String PRIMARY_SECTOR = "primary-sector";
    public static final String PRIMARY_SECTOR_SUB_SECTOR = "primary-sector-sub-sector";
    public static final String PRIMARY_SECTOR_SUB_SUB_SECTOR = "primary-sector-sub-sub-sector";
    public static final String PROCUREMENT_SYSTEM = "procurement-system";
    public static final String PROJECT_IMPLEMENTING_UNIT = "project-implementing-unit";
    public static final String REGION = "region";
    public static final String RESPONSIBLE_ORGANIZATION = "responsible-organization";
    public static final String SECONDARY_PROGRAM_LEVEL_1 = "secondary-program-level-1";
    public static final String SECONDARY_PROGRAM_LEVEL_2 = "secondary-program-level-2";
    public static final String SECONDARY_PROGRAM_LEVEL_3 = "secondary-program-level-3";
    public static final String SECONDARY_PROGRAM_LEVEL_4 = "secondary-program-level-4";
    public static final String SECONDARY_PROGRAM_LEVEL_5 = "secondary-program-level-5";
    public static final String SECONDARY_PROGRAM_LEVEL_6 = "secondary-program-level-6";
    public static final String SECONDARY_PROGRAM_LEVEL_7 = "secondary-program-level-7";
    public static final String SECONDARY_PROGRAM_LEVEL_8 = "secondary-program-level-8";
    public static final String SECONDARY_SECTOR = "secondary-sector";
    public static final String SECONDARY_SECTOR_SUB_SECTOR = "secondary-sector-sub-sector";
    public static final String SECONDARY_SECTOR_SUB_SUB_SECTOR = "secondary-sector-sub-sub-sector";
    public static final String SECTOR_TAG = "sector-tag";
    public static final String STATUS = "status";
    public static final String TEAM = "team";
    public static final String TERTIARY_PROGRAM_LEVEL_1 = "tertiary-program-level-1";
    public static final String TERTIARY_PROGRAM_LEVEL_2 = "tertiary-program-level-2";
    public static final String TERTIARY_PROGRAM_LEVEL_3 = "tertiary-program-level-3";
    public static final String TERTIARY_PROGRAM_LEVEL_4 = "tertiary-program-level-4";
    public static final String TERTIARY_PROGRAM_LEVEL_5 = "tertiary-program-level-5";
    public static final String TERTIARY_PROGRAM_LEVEL_6 = "tertiary-program-level-6";
    public static final String TERTIARY_PROGRAM_LEVEL_7 = "tertiary-program-level-7";
    public static final String TERTIARY_PROGRAM_LEVEL_8 = "tertiary-program-level-8";
    public static final String TERTIARY_SECTOR = "tertiary-sector";
    public static final String TERTIARY_SECTOR_SUB_SECTOR = "tertiary-sector-sub-sector";
    public static final String TERTIARY_SECTOR_SUB_SUB_SECTOR = "tertiary-sector-sub-sub-sector";
    public static final String QUATERNARY_SECTOR = "quaternary-sector";
    public static final String QUATERNARY_SECTOR_SUB_SECTOR = "quaternary-sector-sub-sector";
    public static final String QUATERNARY_SECTOR_SUB_SUB_SECTOR = "quaternary-sector-sub-sub-sector";
    public static final String QUINARY_SECTOR = "quinary-sector";
    public static final String QUINARY_SECTOR_SUB_SECTOR = "quinary-sector-sub-sector";
    public static final String QUINARY_SECTOR_SUB_SUB_SECTOR = "quinary-sector-sub-sub-sector";
    public static final String TYPE_OF_ASSISTANCE = "type-of-assistance";
    public static final String WORKSPACES = "workspaces";
    public static final String ZONE = "zone";

    /** filters IDs to Name mapping */
    public static final Map<String, String> ID_NAME_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(COMPUTED_YEAR, "Computed Year");
        put(CURRENT, "Current");
    }});
    
    // groups
    public static final String OTHER = "Other";
    
    /** filters IDs to main Group (Tab) mapping */
    public static final Map<String, String> ID_GROUP_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(COMPUTED_YEAR, OTHER);
    }});

    /**
     * Maps Organisation Role Code to Filter Id.
     */
    public static final Map<String, String> ORG_ROLE_CODE_TO_FILTER_ID = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(Constants.FUNDING_AGENCY, DONOR_AGENCY);
        put(Constants.IMPLEMENTING_AGENCY, IMPLEMENTING_AGENCY);
        put(Constants.EXECUTING_AGENCY, EXECUTING_AGENCY);
        put(Constants.BENEFICIARY_AGENCY, BENEFICIARY_AGENCY);
        put(Constants.CONTRACTING_AGENCY, CONTRACTING_AGENCY);
        put(Constants.RESPONSIBLE_ORGANISATION, RESPONSIBLE_ORGANIZATION);
        put(Constants.COMPONENT_FUNDING_ORGANIZATION, COMPONENT_FUNDING_ORGANIZATION);
        put(Constants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION);
    }});
}
