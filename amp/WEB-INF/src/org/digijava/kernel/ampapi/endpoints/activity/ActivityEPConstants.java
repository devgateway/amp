/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

import java.util.ArrayList;
import java.util.List;

import org.dgfoundation.amp.ar.ArConstants;

/**
 * Activity Endpoint related constants
 *
 * @author Nadejda Mandrescu
 */
public class ActivityEPConstants {

    public enum RequiredValidation {
        NONE,
        SUBMIT,
        ALWAYS
    }

    // JSON fields
    public static final String ACTIVITY = "activity";
    public static final String FIELD_NAME = "field_name";
    public static final String FIELD_NAME_INTERNAL = "actual_field_name";
    public static final String FIELD_TYPE = "field_type";
    public static final String ITEM_TYPE = "item-type";
    public static final String FIELD_LENGTH = "field_length";
    public static final String FIELD_LABEL = "field_label";
    public static final String IMPORTABLE = "importable";
    public static final String MULTIPLE_VALUES = "multiple_values";
    public static final String CHILDREN = "children";
    public static final String REGEX_PATTERN = "regex-pattern";
    public static final String REGEX_CONSTRAINT = "regex-constraint";
    public static final String PERCENTAGE = "percentage";
    public static final String SIZE_LIMIT = "size-limit";
    public static final String UNIQUE = "unique";
    public static final String REQUIRED = "required";
    public static final String ID_ONLY = "id_only";
    public static final String TRANSLATABLE = "translatable";
    public static final String INVALID = "invalid";
    public static final String INPUT = "input";
    public static final String FILTER_FIELDS = "fields";
    public static final String VIEW = "view";
    public static final String EDIT = "edit";
    public static final String FIELD_NON_DRAFT_REQUIRED = "ND";
    public static final String FIELD_ALWAYS_REQUIRED = "Y";
    public static final String FIELD_NOT_REQUIRED = "N";
    public static final String ID = "id";
    public static final String VALUE = "value";
    public static final String UNIQUE_CONSTRAINT = "unique_constraint";
    public static final String PERCENTAGE_CONSTRAINT = "percentage_constraint";
    public static final String DEPENDENCIES = "dependencies";
    public static final String TREE_COLLECTION_CONSTRAINT = "tree_collection";
    public static final String COMMON_POSSIBLE_VALUES = "common-possible-values";


    public static final String PREVIEW_CURRENCY_ID = "currency-id";

    public static final String API_WS_MEMBER_IDS = "ws-member-ids";
    public static final String API_FIELDS = "fields";

    // fields constants
    public static final String AMP_ACTIVITY_ID_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID);
    public static final String AMP_ID_FIELD_NAME = FieldMap.underscorify(ActivityFieldsConstants.AMP_ID);
    public static final String MODIFIED_BY_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.MODIFIED_BY);
    public static final String VERSION_FIELD_NAME = FieldMap.underscorify(ActivityFieldsConstants.VERSION);

    public static final String LOCATIONS_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.LOCATIONS);

    public static final String AMP_ACTIVITY_ID = "amp-activity-id";
    public static final String AMP_ACTIVITY_LAST_VERSION_ID = "amp-activity-last-version-id";

    public static final String VALIDATION_STATUS = "validation-status";
    public static final String DAYS_FOR_AUTOMATIC_VALIDATION = "days-for-automatic-validation";
    public static final String ACTIVITY_WORKSPACE = "activityWorkspace";

    public static final String TEAM_MEMBER = "teamMember";
    public static final String TEAM_MEMBER_ROLE = "teamMemberRole";
    public static final String ACTIVITY_TEAM_LEAD_DATA = "activity-workspace-lead-data";


    public static final String VERSION_HISTORY = "version-history";
    public static final String MODIFIED_BY = "modified-by";
    public static final String MODIFIED_DATE = "modified-date";
    public static final String UPDATE_CURRENT_VERSION = "update-current-version";

    // max length constants
    public static final String TYPE_VARCHAR = "character varying";

    // validator names constants
    public static final String UNIQUE_VALIDATOR_NAME = "unique";
    public static final String TREE_COLLECTION_VALIDATOR_NAME = "treeCollection";

    // floating comparison constant
    public static final Double EPSILON = 0.0001;

    public static final Integer MAX_BULK_ACTIVITIES_ALLOWED = 20;

    /*
     * Based on local stats, 7 is the minimum DB query batch size with best outcome. 6-5 is pretty close, but going
     * lower makes the difference more noticeable. Growing the value up won't make a significant difference for some
     * time. At about 16 and especially higher like 32 the performance impact is seen.
     * Keeping it at the smallest optimal will allow a better performance for multiple users sync up (e.g. training).
     */
    public static final Integer BATCH_DB_QUERY_SIZE = 7;

    public static final String FIELD_TITLE = "_FIELD_TITLE_";
    private static final String FUNDING_ITEM_FM_PATH = "/Activity Form/Funding/Funding Group/Funding Item";

    // some FM paths
    public static final String COMMITMENTS_TABLE_FM_PATH = FUNDING_ITEM_FM_PATH + "/Commitments/Commitments Table";
    public static final String DISB_TABLE_FM_PATH = FUNDING_ITEM_FM_PATH + "/Disbursements/Disbursements Table";
    public static final String ARREARS_TABLE_FM_PATH = FUNDING_ITEM_FM_PATH + "/Arrears/Arrears Table";
    public static final String DISB_ORDERS_TABLE_FM_PATH = FUNDING_ITEM_FM_PATH
            + "/Disbursement Orders/Disbursement Orders Table";
    public static final String EST_DISB_TABLE_FM_PATH = FUNDING_ITEM_FM_PATH
            + "/Estimated Disbursements/Estimated Disbursements Table";
    public static final String RELEASE_FUNDS_TABLE_FM_PATH = FUNDING_ITEM_FM_PATH
            + "/Release of Funds/Release of Funds Table";
    public static final String EXPENDITURES_TABLE_FM_PATH = FUNDING_ITEM_FM_PATH
            + "/Expenditures/Expenditures Table";
    public static final String FUNDING_ITEM_CLASSIFICATION_FM_PATH = FUNDING_ITEM_FM_PATH + "/Funding Classification";

    public static final String REGIONAL_FUNDING_FM_PATH = "/Activity Form/Regional Funding";
    private static final String REGIONAL_FUNDING_ITEM_FM_PATH = REGIONAL_FUNDING_FM_PATH + "/Region Item";
    public static final String REGIONAL_COMMITMENTS_FM_PATH =
            REGIONAL_FUNDING_ITEM_FM_PATH + "/Commitments/Commitments Table";
    public static final String REGIONAL_DISBURSEMENTS_FM_PATH =
            REGIONAL_FUNDING_ITEM_FM_PATH + "/Disbursements/Disbursements Table";
    public static final String REGIONAL_EXPENDITURES_FM_PATH =
            REGIONAL_FUNDING_ITEM_FM_PATH + "/Expenditures/Expenditures Table";

    public static final String RECIPIENT_ROLE_FM_PATH = "/Funding Flows OrgRole Selector/Recipient Org Role";
    public static final String RECIPIENT_ORG_FM_PATH = "/Funding Flows OrgRole Selector/Recipient Organization";

    public static final String AF_ID_FM_PATH = "/Activity Form/Identification";
    public static final String DONOR_PROJECT_CODE_FM_PATH = AF_ID_FM_PATH + "/Donor Project Code";
    public static final String BUDGET_EXTRAS_PROJECT_CODE_FM_PATH = AF_ID_FM_PATH + "/Budget Extras/Project Code";

    public static final String CONTACTS_PATH = "/Activity Form/Contacts";

    public static final String REGEX_PATTERN_EMAIL =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)";
    public static final String REGEX_PATTERN_PHONE =
            "^\\+?\\d?(\\([\\d]{1,3}\\))?[\\s\\d\\-\\/]*\\d+[\\s\\d\\-\\/]*";
    public static final String REGEX_PATTERN_PHONE_EXTENSION = "^\\d{1,4}$";

    public static final List PUBLIC_ACTIVITY_FIELDS = new ArrayList<String>() {{
        add("fundings~commitments~adjustment_type");
        add("fundings~disbursements~adjustment_type");
        add("fundings~expenditures~adjustment_type");
        add("fundings~estimated_disbursements~adjustment_type");
        add("locations~location");
        add(ActivityFieldsConstants.FUNDINGS.toLowerCase() + "~" + ArConstants.COMMITMENT.toLowerCase()
                + "~" + ActivityFieldsConstants.ADJUSTMENT_TYPE);
        add(ActivityFieldsConstants.FUNDINGS.toLowerCase() + "~" + ArConstants.DISBURSEMENT.toLowerCase()
                + "~" + ActivityFieldsConstants.ADJUSTMENT_TYPE);
        add(ActivityFieldsConstants.FUNDINGS.toLowerCase() + "~" + ArConstants.EXPENDITURE.toLowerCase()
                + "~" + ActivityFieldsConstants.ADJUSTMENT_TYPE);
        add(ActivityFieldsConstants.FUNDINGS.toLowerCase() + "~"
                + FieldMap.underscorify(ArConstants.ESTIMATED_DISBURSEMENTS).toLowerCase()
                + "~" + ActivityFieldsConstants.ADJUSTMENT_TYPE);
        add(ActivityFieldsConstants.LOCATIONS.toLowerCase() + "~"
                + ActivityFieldsConstants.Locations.LOCATION.toLowerCase());
    }};

    public static final String FUNDING_VULNERABLE_GROUP_LABEL =
            "Which vulnerable groups does this project/action work with?";
    public static final String FUNDING_PROJECT_RESULTS_AVAILABLE_LABEL =
            "Are the project results available to the public?";
    public static final String FUNDING_PROJECT_RESULTS_LINK_LABEL = "Please provide link if available";
    public static final String FUNDING_PROJECT_JOINT_DECISION_LABEL =
            "How is joint decision making made in this project/action?";
    public static final String FUNDING_PROJECT_MONITORING_LABEL =
            "How do you monitor and evaluate this project/action?";
    public static final String FUNDING_PROJECT_SUSTAINABILITY_LABEL =
            "Sustainability: what happens when the project/action ends?";
    public static final String FUNDING_PROJECT_PROBLEMS_LABEL = "What problems were encountered in this project?";

    public static final Integer SECONDS_TO_SLEEP = 5;

}
