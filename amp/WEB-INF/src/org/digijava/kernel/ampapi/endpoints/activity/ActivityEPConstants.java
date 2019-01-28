/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * Activity Endpoint related constants
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityEPConstants {
    // Field configs
    public static final String REQUIRED_NONE = "_NONE_";
    public static final String REQUIRED_ALWAYS = "_ALWAYS_";
    public static final String REQUIRED_ND = "_NOT_DRAFT_"; // temp. special case for project_code when Activity On Budget, do not use for other, use FM that defines
    
    // JSON fields
    public static final String ACTIVITY = "activity";
    public static final String FIELD_NAME = "field_name";
    public static final String FIELD_NAME_INTERNAL = "actual_field_name";
    public static final String FIELD_TYPE = "field_type";
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
    public static final String DEPENDENCIES ="dependencies";
    public static final String TREE_COLLECTION_CONSTRAINT = "tree_collection";
    
    public static final String PREVIEW_CURRENCY_ID = "currency-id";
    
    // fields constants
    public static final String AMP_ACTIVITY_ID_FIELD_NAME = 
            InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID);
    public static final String AMP_ID_FIELD_NAME = InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID);
    public static final String MODIFIED_BY_FIELD_NAME = 
            InterchangeUtils.underscorify(ActivityFieldsConstants.MODIFIED_BY);

    public static final String AMP_ACTIVITY_ID = "amp-activity-id";
    public static final String AMP_ACTIVITY_LAST_VERSION_ID = "amp-activity-last-version-id";

    public static final String VALIDATION_STATUS = "validation-status";
    public static final String DAYS_FOR_AUTOMATIC_VALIDATION = "days-for-automatic-validation";
    public static final String ACTIVITY_TEAM = "activity-team";
    public static final String IATI_IDENTIFIER_AMP_FIELD_DEFAULT_NAME = "project_code";
    public static final String VERSION_HISTORY = "version-history";
    public static final String MODIFIED_BY = "modified-by";
    public static final String MODIFIED_DATE = "modified-date";
    public static final String UPDATE_CURRENT_VERSION = "update-current-version";

    // field types
    public static final String FIELD_TYPE_LIST = "list";
    public static final String FIELD_TYPE_STRING = "string";
    public static final String FIELD_TYPE_BOOLEAN = "boolean";
    public static final String FIELD_TYPE_LONG = "long";
    public static final String FIELD_TYPE_FLOAT = "float";
    public static final String FIELD_TYPE_DATE = "date";
    
    // max length constants
    public static final String TYPE_VARCHAR = "character varying";
    
    // validator names constants
    public static final String UNIQUE_VALIDATOR_NAME = "unique";
    public static final String MAX_SIZE_VALIDATOR_NAME = "maxSize";
    public static final String MIN_SIZE_VALIDATOR_NAME = "minSize";
    public static final String PERCENTAGE_VALIDATOR_NAME = "percentage";
    public static final String TREE_COLLECTION_VALIDATOR_NAME = "treeCollection";
    
    // floating comparison constant
    public static final Double EPSILON = 0.0001;

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
    public static final String COMMITMENTS_DISASTER_RESPONSE_FM_PATH = FUNDING_ITEM_FM_PATH + "/Commitments/Commitments Table/Disaster Response";
    public static final String DISBURSEMENTS_DISASTER_RESPONSE_FM_PATH = FUNDING_ITEM_FM_PATH + "/Disbursements/Disbursements Table/Disaster Response";

    public static final String COMMITMENTS_PLEDGES_FM_PATH = FUNDING_ITEM_FM_PATH + "/Commitments/Commitments Table/Pledges";
    public static final String DISBURSEMENTS_PLEDGES_FM_PATH = FUNDING_ITEM_FM_PATH + "/Disbursements/Disbursements Table/Pledges";
    public static final String ESTIMATED_DISBURSEMENTS_PLEDGES_FM_PATH = FUNDING_ITEM_FM_PATH + "/Estimated Disbursements/Estimated Disbursements Table/Pledges";
    public static final String RELEASE_OF_FUNDS_PLEDGES_FM_PATH = FUNDING_ITEM_FM_PATH + "/Release of Funds/Release of Funds Table/Pledges";

    public static final String AF_ID_FM_PATH = "/Activity Form/Identification";
    public static final String DONOR_PROJECT_CODE_FM_PATH = AF_ID_FM_PATH + "/Donor Project Code";
    public static final String BUDGET_EXTRAS_PROJECT_CODE_FM_PATH = AF_ID_FM_PATH + "/Budget Extras/Project Code";

    public static final String CONTACTS_PATH = "/Activity Form/Contacts";
    
    public static final String REGEX_PATTERN_EMAIL = 
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)";
    public static final String REGEX_PATTERN_PHONE = 
            "^\\+?\\d?(\\([\\d]{1,3}\\))?[\\s\\d\\-\\/]*\\d+[\\s\\d\\-\\/]*";
    public static final String REGEX_PATTERN_PHONE_EXTENSION = "^\\d{1,4}$";
    
}
