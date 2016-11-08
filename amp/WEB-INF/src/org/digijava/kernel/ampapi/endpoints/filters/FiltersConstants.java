package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public static final String FUNDING_CLOSING_DATE = "funding-closing-date";
    public static final String PROPOSED_APPROVAL_DATE = "proposed-approval-date";
    public static final String PROPOSED_COMPLETION_DATE = "proposed-completion-date";
    public static final String PROPOSED_START_DATE = "proposed-start-date";

    public static final String APPROVAL_STATUS = "approval-status";
    public static final String COMPUTED_YEAR = "computed-year";
    public static final String DISASTER_RESPONSE_MARKER = "disaster-response-marker";
    public static final String DONOR_AGENCY = "donor-agency";
    public static final String DONOR_GROUP = "donor-group";
    public static final String DONOR_TYPE = "donor-type";
    public static final String EXECUTING_AGENCY = "executing-agency";
    public static final String EXPENDITURE_CLASS = "expenditure-class";
    public static final String FINANCING_INSTRUMENT = "financing-instrument";
    public static final String FUNDING_STATUS = "funding-status";
    public static final String HUMANITARIAN_AID = "humanitarian-aid";
    public static final String IMPLEMENTING_AGENCY = "implementing-agency";
    public static final String LOCATION = "location";
    public static final String MODE_OF_PAYMENT = "mode-of-payment";
    public static final String NATIONAL_PLANNING_OBJECTIVES = "national-planning-objectives";
    public static final String ON_OFF_TREASURY_BUDGET = "on-off-treasury-budget";
    public static final String PRIMARY_PROGRAM = "primary-program";
    public static final String PRIMARY_SECTOR = "primary-sector";
    public static final String PRIMARY_SECTOR_SUB_SECTOR = "primary-sector-sub-sector";
    public static final String PRIMARY_SECTOR_SUB_SUB_SECTOR = "primary-sector-sub-sub-sector";
    public static final String PROCUREMENT_SYSTEM = "procurement-system";
    public static final String SECONDARY_PROGRAM = "secondary-program";
    public static final String SECONDARY_PROGRAM_LEVEL_1 = "secondary-program-level-1";
    public static final String SECONDARY_SECTOR = "secondary-sector";
    public static final String SECONDARY_SECTOR_ID = "secondary-sector-id";
    public static final String SECONDARY_SECTOR_SUB_SECTOR = "secondary-sector-sub-sector";
    public static final String SECONDARY_SECTOR_SUB_SUB_SECTOR = "secondary-sector-sub-sub-sector";
    public static final String STATUS = "status";
    public static final String TEAM = "team";
    public static final String TERTIARY_PROGRAM = "tertiary-program";
    public static final String TERTIARY_SECTOR = "tertiary-sector";
    public static final String TERTIARY_SECTOR_SUB_SECTOR = "tertiary-sector-sub-sector";
    public static final String TERTIARY_SECTOR_SUB_SUB_SECTOR = "tertiary-sector-sub-sub-sector";
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
}
