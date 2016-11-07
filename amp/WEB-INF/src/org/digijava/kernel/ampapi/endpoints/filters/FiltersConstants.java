package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;

/**
 * Filters Constants
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersConstants {
    
    // filters IDs 
    public static final String COMPUTED_YEAR = "computedYear";
    public static final String CURRENT = "current";

    public static final String ACTUAL_APPROVAL_DATE = "actual-approval-date";
    public static final String ACTUAL_COMPLETION_DATE = "actual-completion-date";
    public static final String ACTUAL_START_DATE = "actual-start-date";
    public static final String APPROVAL_STATUS = "approval-status";
    public static final String ARCHIVED = "archived";
    public static final String BENEFICIARY_AGENCY = "beneficiary-agency";
    public static final String CONTRACTING_AGENCY = "contracting-agency";
    public static final String CONTRACTING_AGENCY_GROUPS = "contracting-agency-groups";
    public static final String COUNTRY = "country";
    public static final String DISASTER_RESPONSE_MARKER = "disaster-response-marker";
    public static final String DISTRICT = "district";
    public static final String DONOR_AGENCY = "donor-agency";
    public static final String DONOR_GROUP = "donor-group";
    public static final String DONOR_TYPE = "donor-type";
    public static final String EFFECTIVE_FUNDING_DATE = "effective-funding-date";
    public static final String EXECUTING_AGENCY = "executing-agency";
    public static final String EXPENDITURE_CLASS = "expenditure-class";
    public static final String FINAL_DATE_FOR_CONTRACTING = "final-date-for-contracting";
    public static final String FINANCING_INSTRUMENT = "financing-instrument";
    public static final String FUNDING_CLOSING_DATE = "funding-closing-date";
    public static final String FUNDING_STATUS = "funding-status";
    public static final String GOVERNMENT_APPROVAL_PROCEDURES = "government-approval-procedures";
    public static final String HUMANITARIAN_AID = "humanitarian-aid";
    public static final String IMPLEMENTING_AGENCY = "implementing-agency";
    public static final String JOINT_CRITERIA = "joint-criteria";
    public static final String LOCATION = "location";
    public static final String MODE_OF_PAYMENT = "mode-of-payment";
    public static final String NATIONAL_PLANNING_OBJECTIVES = "national-planning-objectives";
    public static final String ON_OFF_TREASURY_BUDGET = "on-off-treasury-budget";
    public static final String PLEDGES_AID_MODALITY = "pledges-aid-modality";
    public static final String PLEDGES_DONOR_GROUP = "pledges-donor-group";
    public static final String PLEDGES_NATIONAL_PLAN_OBJECTIVES = "pledges-national-plan-objectives";
    public static final String PLEDGES_PROGRAMS = "pledges-programs";
    public static final String PLEDGES_SECONDARY_PROGRAMS = "pledges-secondary-programs";
    public static final String PLEDGES_SECONDARY_SECTORS = "pledges-secondary-sectors";
    public static final String PLEDGES_SECTORS = "pledges-sectors";
    public static final String PLEDGES_TERTIARY_PROGRAMS = "pledges-tertiary-programs";
    public static final String PLEDGES_TERTIARY_SECTORS = "pledges-tertiary-sectors";
    public static final String PLEDGES_TITLES = "pledges-titles";
    public static final String PRIMARY_PROGRAM = "primary-program";
    public static final String PRIMARY_SECTOR = "primary-sector";
    public static final String PROJECT_IMPLEMENTING_UNIT = "project-implementing-unit";
    public static final String PROPOSED_APPROVAL_DATE = "proposed-approval-date";
    public static final String PROPOSED_COMPLETION_DATE = "proposed-completion-date";
    public static final String PROPOSED_START_DATE = "proposed-start-date";
    public static final String REGION = "region";
    public static final String RESPONSIBLE_ORGANIZATION = "responsible-organization";
    public static final String SECONDARY_PROGRAM = "secondary-program";
    public static final String SECONDARY_SECTOR = "secondary-sector";
    public static final String SECTOR_TAG = "sector-tag";
    public static final String STATUS = "status";
    public static final String TEAM = "team";
    public static final String TERTIARY_PROGRAM = "tertiary-program";
    public static final String TERTIARY_SECTOR = "tertiary-sector";
    public static final String TYPE_OF_ASSISTANCE = "type-of-assistance";
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

    public static final Map<String, String> COLUMN_TO_ID = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(ColumnConstants.ACTUAL_APPROVAL_DATE, ACTUAL_APPROVAL_DATE);
        put(ColumnConstants.ACTUAL_COMPLETION_DATE, ACTUAL_COMPLETION_DATE);
        put(ColumnConstants.ACTUAL_START_DATE, ACTUAL_START_DATE);
        put(ColumnConstants.APPROVAL_STATUS, APPROVAL_STATUS);
        put(ColumnConstants.ARCHIVED, ARCHIVED);
        put(ColumnConstants.BENEFICIARY_AGENCY, BENEFICIARY_AGENCY);
        put(ColumnConstants.CONTRACTING_AGENCY, CONTRACTING_AGENCY);
        put(ColumnConstants.CONTRACTING_AGENCY_GROUPS, CONTRACTING_AGENCY_GROUPS);
        put(ColumnConstants.COUNTRY, COUNTRY);
        put(ColumnConstants.DISASTER_RESPONSE_MARKER, DISASTER_RESPONSE_MARKER);
        put(ColumnConstants.DISTRICT, DISTRICT);
        put(ColumnConstants.DONOR_AGENCY, DONOR_AGENCY);
        put(ColumnConstants.DONOR_GROUP, DONOR_GROUP);
        put(ColumnConstants.DONOR_TYPE, DONOR_TYPE);
        put(ColumnConstants.EFFECTIVE_FUNDING_DATE, EFFECTIVE_FUNDING_DATE);
        put(ColumnConstants.EXECUTING_AGENCY, EXECUTING_AGENCY);
        put(ColumnConstants.EXPENDITURE_CLASS, EXPENDITURE_CLASS);
        put(ColumnConstants.FINAL_DATE_FOR_CONTRACTING, FINAL_DATE_FOR_CONTRACTING);
        put(ColumnConstants.FINANCING_INSTRUMENT, FINANCING_INSTRUMENT);
        put(ColumnConstants.FUNDING_CLOSING_DATE, FUNDING_CLOSING_DATE);
        put(ColumnConstants.FUNDING_STATUS, FUNDING_STATUS);
        put(ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES, GOVERNMENT_APPROVAL_PROCEDURES);
        put(ColumnConstants.HUMANITARIAN_AID, HUMANITARIAN_AID);
        put(ColumnConstants.IMPLEMENTING_AGENCY, IMPLEMENTING_AGENCY);
        put(ColumnConstants.JOINT_CRITERIA, JOINT_CRITERIA);
        put(ColumnConstants.LOCATION, LOCATION);
        put(ColumnConstants.MODE_OF_PAYMENT, MODE_OF_PAYMENT);
        put(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, NATIONAL_PLANNING_OBJECTIVES);
        put(ColumnConstants.ON_OFF_TREASURY_BUDGET, ON_OFF_TREASURY_BUDGET);
        put(ColumnConstants.PLEDGES_AID_MODALITY, PLEDGES_AID_MODALITY);
        put(ColumnConstants.PLEDGES_DONOR_GROUP, PLEDGES_DONOR_GROUP);
        put(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES, PLEDGES_NATIONAL_PLAN_OBJECTIVES);
        put(ColumnConstants.PLEDGES_PROGRAMS, PLEDGES_PROGRAMS);
        put(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS, PLEDGES_SECONDARY_PROGRAMS);
        put(ColumnConstants.PLEDGES_SECONDARY_SECTORS, PLEDGES_SECONDARY_SECTORS);
        put(ColumnConstants.PLEDGES_SECTORS, PLEDGES_SECTORS);
        put(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS, PLEDGES_TERTIARY_PROGRAMS);
        put(ColumnConstants.PLEDGES_TERTIARY_SECTORS, PLEDGES_TERTIARY_SECTORS);
        put(ColumnConstants.PLEDGES_TITLES, PLEDGES_TITLES);
        put(ColumnConstants.PRIMARY_PROGRAM, PRIMARY_PROGRAM);
        put(ColumnConstants.PRIMARY_SECTOR, PRIMARY_SECTOR);
        put(ColumnConstants.PROJECT_IMPLEMENTING_UNIT, PROJECT_IMPLEMENTING_UNIT);
        put(ColumnConstants.PROPOSED_APPROVAL_DATE, PROPOSED_APPROVAL_DATE);
        put(ColumnConstants.PROPOSED_COMPLETION_DATE, PROPOSED_COMPLETION_DATE);
        put(ColumnConstants.PROPOSED_START_DATE, PROPOSED_START_DATE);
        put(ColumnConstants.REGION, REGION);
        put(ColumnConstants.RESPONSIBLE_ORGANIZATION, RESPONSIBLE_ORGANIZATION);
        put(ColumnConstants.SECONDARY_PROGRAM, SECONDARY_PROGRAM);
        put(ColumnConstants.SECONDARY_SECTOR, SECONDARY_SECTOR);
        put(ColumnConstants.SECTOR_TAG, SECTOR_TAG);
        put(ColumnConstants.STATUS, STATUS);
        put(ColumnConstants.TEAM, TEAM);
        put(ColumnConstants.TERTIARY_PROGRAM, TERTIARY_PROGRAM);
        put(ColumnConstants.TERTIARY_SECTOR, TERTIARY_SECTOR);
        put(ColumnConstants.TYPE_OF_ASSISTANCE, TYPE_OF_ASSISTANCE);
        put(ColumnConstants.ZONE, ZONE);
    }});

    public static final Map<String, String> ID_TO_COLUMN = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(ACTUAL_APPROVAL_DATE, ColumnConstants.ACTUAL_APPROVAL_DATE);
        put(ACTUAL_COMPLETION_DATE, ColumnConstants.ACTUAL_COMPLETION_DATE);
        put(ACTUAL_START_DATE, ColumnConstants.ACTUAL_START_DATE);
        put(APPROVAL_STATUS, ColumnConstants.APPROVAL_STATUS);
        put(ARCHIVED, ColumnConstants.ARCHIVED);
        put(BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY);
        put(CONTRACTING_AGENCY, ColumnConstants.CONTRACTING_AGENCY);
        put(CONTRACTING_AGENCY_GROUPS, ColumnConstants.CONTRACTING_AGENCY_GROUPS);
        put(COUNTRY, ColumnConstants.COUNTRY);
        put(DISASTER_RESPONSE_MARKER, ColumnConstants.DISASTER_RESPONSE_MARKER);
        put(DISTRICT, ColumnConstants.DISTRICT);
        put(DONOR_AGENCY, ColumnConstants.DONOR_AGENCY);
        put(DONOR_GROUP, ColumnConstants.DONOR_GROUP);
        put(DONOR_TYPE, ColumnConstants.DONOR_TYPE);
        put(EFFECTIVE_FUNDING_DATE, ColumnConstants.EFFECTIVE_FUNDING_DATE);
        put(EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY);
        put(EXPENDITURE_CLASS, ColumnConstants.EXPENDITURE_CLASS);
        put(FINAL_DATE_FOR_CONTRACTING, ColumnConstants.FINAL_DATE_FOR_CONTRACTING);
        put(FINANCING_INSTRUMENT, ColumnConstants.FINANCING_INSTRUMENT);
        put(FUNDING_CLOSING_DATE, ColumnConstants.FUNDING_CLOSING_DATE);
        put(FUNDING_STATUS, ColumnConstants.FUNDING_STATUS);
        put(GOVERNMENT_APPROVAL_PROCEDURES, ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES);
        put(HUMANITARIAN_AID, ColumnConstants.HUMANITARIAN_AID);
        put(IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY);
        put(JOINT_CRITERIA, ColumnConstants.JOINT_CRITERIA);
        put(LOCATION, ColumnConstants.LOCATION);
        put(MODE_OF_PAYMENT, ColumnConstants.MODE_OF_PAYMENT);
        put(NATIONAL_PLANNING_OBJECTIVES, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES);
        put(ON_OFF_TREASURY_BUDGET, ColumnConstants.ON_OFF_TREASURY_BUDGET);
        put(PLEDGES_AID_MODALITY, ColumnConstants.PLEDGES_AID_MODALITY);
        put(PLEDGES_DONOR_GROUP, ColumnConstants.PLEDGES_DONOR_GROUP);
        put(PLEDGES_NATIONAL_PLAN_OBJECTIVES, ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES);
        put(PLEDGES_PROGRAMS, ColumnConstants.PLEDGES_PROGRAMS);
        put(PLEDGES_SECONDARY_PROGRAMS, ColumnConstants.PLEDGES_SECONDARY_PROGRAMS);
        put(PLEDGES_SECONDARY_SECTORS, ColumnConstants.PLEDGES_SECONDARY_SECTORS);
        put(PLEDGES_SECTORS, ColumnConstants.PLEDGES_SECTORS);
        put(PLEDGES_TERTIARY_PROGRAMS, ColumnConstants.PLEDGES_TERTIARY_PROGRAMS);
        put(PLEDGES_TERTIARY_SECTORS, ColumnConstants.PLEDGES_TERTIARY_SECTORS);
        put(PLEDGES_TITLES, ColumnConstants.PLEDGES_TITLES);
        put(PRIMARY_PROGRAM, ColumnConstants.PRIMARY_PROGRAM);
        put(PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR);
        put(PROJECT_IMPLEMENTING_UNIT, ColumnConstants.PROJECT_IMPLEMENTING_UNIT);
        put(PROPOSED_APPROVAL_DATE, ColumnConstants.PROPOSED_APPROVAL_DATE);
        put(PROPOSED_COMPLETION_DATE, ColumnConstants.PROPOSED_COMPLETION_DATE);
        put(PROPOSED_START_DATE, ColumnConstants.PROPOSED_START_DATE);
        put(REGION, ColumnConstants.REGION);
        put(RESPONSIBLE_ORGANIZATION, ColumnConstants.RESPONSIBLE_ORGANIZATION);
        put(SECONDARY_PROGRAM, ColumnConstants.SECONDARY_PROGRAM);
        put(SECONDARY_SECTOR, ColumnConstants.SECONDARY_SECTOR);
        put(SECTOR_TAG, ColumnConstants.SECTOR_TAG);
        put(STATUS, ColumnConstants.STATUS);
        put(TEAM, ColumnConstants.TEAM);
        put(TERTIARY_PROGRAM, ColumnConstants.TERTIARY_PROGRAM);
        put(TERTIARY_SECTOR, ColumnConstants.TERTIARY_SECTOR);
        put(TYPE_OF_ASSISTANCE, ColumnConstants.TYPE_OF_ASSISTANCE);
        put(ZONE, ColumnConstants.ZONE);
    }});
}
