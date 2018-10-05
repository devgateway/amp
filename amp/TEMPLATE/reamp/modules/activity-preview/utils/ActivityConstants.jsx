/**
 *    
 */

export const DEFAULT_CURRENCY = 'XOF';

export const PROJECT_TITLE = 'project_title';
export const AMP_ID = 'amp_id';
export const ACTIVITY_STATUS = 'activity_status';
export const ACTIVITY_BUDGET = 'activity_budget';
export const MINISTRY_CODE = 'ministry_code';
export const VOTE = 'vote';
export const SUB_VOTE = 'sub_vote';
export const SUB_PROGRAM = 'sub_program';
export const PROJECT_CODE = 'project_code';
export const ACTIVITY_SECTION_IDS =
  [
    { key: 'AcIdentification', hash: 'AcIdentification', value: 'Identification', translationKey: 'amp.activity-preview:sectionIdentification'},
    { key: 'AcInternalIds', hash: 'AcInternalIds', value: 'Agency Internal IDs', translationKey: 'amp.activity-preview:sectionInternalIds' },
    { key: 'AcPlanning', hash: 'AcPlanning', value: 'Planning', translationKey: 'Planning' },
    { key: 'AcLocation', hash: 'AcLocation', value: 'Location', translationKey: 'Location' },
    { key: 'AcProgram', hash: 'AcProgram', value: 'Program', translationKey: 'Program' },
    { key: 'AcSector', hash: 'AcSector', value: 'Sectors', translationKey: 'SectorsLabel' },
    { key: 'AcFunding', hash: 'AcFunding', value: 'Funding', translationKey: 'Funding' },
    { key: 'AcRelatedOrganizations', hash: 'AcRelatedOrganizations', value: 'Related Organizations', translationKey: 'RelatedOrganizations' },
    { key: 'AcIssues', hash: 'AcIssues', value: 'Issues', translationKey: 'Issues' },
    { key: 'AcRelatedDocuments', hash: 'AcRelatedDocuments', value: 'Related Documents', translationKey: 'RelatedDocuments' },
    { key: 'AcContacts', hash: 'AcContacts', value: 'Contacts', translationKey: 'Contacts' },
    { key: 'AcFundingSummary', hash: 'AcFundingSummary', value: 'Funding Summary', translationKey: 'FundingInformation' },
    { key: 'AcAdditionalInfo', hash: 'AdditionalInfo', value: 'Additional Info', translationKey: 'AdditionalInfo' }
  ];

export const STATUS_REASON = 'status_reason';
export const OBJECTIVE = 'objective';
export const DESCRIPTION = 'description';
export const PROJECT_COMMENTS = 'project_comments';
export const CRIS_NUMBER = 'cris_number';
export const IS_DRAFT = 'is_draft';

export const ORGANIZATION = "organization";

  /*export const RICH_TEXT_FIELDS = new Set([STATUS_REASON, OBJECTIVE, DESCRIPTION, PROJECT_COMMENTS,
    LESSONS_LEARNED, PROJECT_IMPACT, ACTIVITY_SUMMARY, CONDITIONALITIES, PROJECT_MANAGEMENT, RESULTS,
  ]);*/
export const RICH_TEXT_FIELDS = new Set([STATUS_REASON, OBJECTIVE, DESCRIPTION, PROJECT_COMMENTS, ORGANIZATION]);

//Funding
export const ACTUAL = 'Actual';
export const PLANNED = 'Planned';
export const PIPELINE = 'Pipeline';
export const COMMITMENTS = 'Commitments';
export const DISBURSEMENTS = 'Disbursements';
export const ADJUSTMENT_TYPES = [PLANNED, ACTUAL];
export const TRANSACTION_TYPES = [COMMITMENTS, DISBURSEMENTS];

export const ACTUAL_COMMITMENTS = 'Actual Commitments';
export const ACTUAL_DISBURSEMENTS = 'Actual Disbursements';
export const PLANNED_COMMITMENTS = 'Planned Commitments';
export const PLANNED_DISBURSEMENTS = 'Planned Disbursements';
export const DELIVERY_RATE = 'Delivery Rate';

export const FIXED_EXCHANGE_RATE = "fixed_exchange_rate";
export const CURRENCY = "currency";
export const TRANSACTION_DATE = "transaction_date";
export const TRANSACTION_ID = "transaction_id";
export const TRANSACTION_AMOUNT = "transaction_amount";

//Activity Internal Ids
export const ACTIVITY_INTERNAL_IDS = "activity_internal_ids";
export const INTERNAL_ID = "internal_id";

//Planning
export const PROPOSED_APPROVAL_DATE = "proposed_approval_date";
export const ACTUAL_APPROVAL_DATE = "actual_approval_date";
export const PROPOSED_START_DATE = "proposed_start_date";
export const ACTUAL_START_DATE = "actual_start_date";
export const CREATION_DATE = "creation_date";
export const PROPOSED_COMPLETION_DATE = "proposed_completion_date";
export const ACTUAL_COMPLETION_DATE = "actual_completion_date";
export const PROPOSED_PROJECT_LIFE = "proposed_project_life";

//Locations
export const LOCATIONS = 'locations';
export const LOCATION = 'location';
export const LOCATION_PERCENTAGE = 'location_percentage';
export const IMPLEMENTATION_LOCATION = 'implementation_location';
export const IMPLEMENTATION_LEVEL = 'implementation_level';

//National Plan Objective
export const NATIONAL_PLAN_OBJECTIVE = 'national_plan_objective';
export const PROGRAM = 'program';
export const PROGRAM_PERCENTAGE = 'program_percentage';

//Programs
export const PROGRAM_SETTINGS = 'program_settings';
export const PRIMARY_PROGRAMS = 'primary_programs';
export const SECONDARY_PROGRAMS = 'secondary_programs';

//Sectors
export const PRIMARY_SECTORS = 'primary_sectors';
export const SECONDARY_SECTORS = 'secondary_sectors';
export const SECTOR = 'sector';
export const SECTOR_PERCENTAGE = 'sector_percentage';

//Funding Section
export const FUNDINGS = 'fundings';
export const AMP_FUNDING_ID = 'amp_funding_id';
export const FUNDING_ID = 'funding_id';
export const FUNDING_DONOR_ORG_ID = 'donor_organization_id';
export const SOURCE_ROLE = 'source_role';
export const TYPE_OF_ASSISTANCE = 'type_of_assistance';
export const FINANCING_INSTRUMENT = 'financing_instrument';
export const FUNDING_STATUS = 'funding_status';
export const FINANCING_ID = 'financing_id';
export const FUNDING_DETAILS = 'funding_details';
export const ADJUSTMENT_TYPE = 'adjustment_type';
export const TRANSACTION_TYPE = 'transaction_type';

//Aditional Info
export const TEAM = 'team';
export const CREATED_BY = 'created_by';
export const MODIFIED_BY = 'modified_by';
export const UPDATE_DATE = 'update_date';

//Related Orgs
export const RESPONSIBLE_ORGANIZATION = 'responsible_organization';
export const DONOR_ORGANIZATION = 'donor_organization';
export const EXECUTING_AGENCY = 'executing_agency';

//Issues
export const ISSUES = 'issues';
export const ISSUE_DATE = 'issue_date';
export const ISSUE_NAME = 'name';
export const MEASURES = 'measures';
export const MEASURE_NAME = 'name';
export const MEASURE_DATE = 'measure_date';
export const ACTORS = 'actors';
export const ACTOR_NAME = 'name';

//Contacts
export const DONOR_CONTACT_INFORMATION = 'donor_contact_information';
export const SECTOR_MINISTRY_CONTACT_INFORMATION = 'sector_ministry_contact_information';
export const IMPLEMENTING_AGENCY_CONTACT_INFORMATION = 'implementing/executing_agency_contact_information';
export const MOFED_CONTACT_INFORMATION = 'mofed_contact_information';
export const MARK_AS_PRIMARY = 'mark_as_primary';
export const CONTACT = 'contact';

//Structures
export const STRUCTURES = 'structures';
export const STRUCTURES_TITLE = 'title';
export const STRUCTURES_DESCRIPTION = 'description';
export const STRUCTURES_LATITUDE = 'latitude';
export const STRUCTURES_LONGITUDE = 'longitude';
export const STRUCTURES_COLOR = 'structure_color';
export const STRUCTURES_LAT = 'lat';
export const STRUCTURES_LNG = 'lng';
export const STRUCTURES_SHAPE = 'shape';
export const STRUCTURES_POINT = 'Point';
export const STRUCTURES_POLYGON = 'Polygon';
export const STRUCTURES_POLYLINE = 'Polyline';
export const STRUCTURES_COORDINATES = 'coordinates';


//SETTINGS
export const HIDE_EXPORT = 'hide-editable-export-formats-public-view';
export const DATE_FORMAT = 'default-date-format';
export const NUMBER_FORMAT = 'number-format';
export const NUMBER_DIVIDER = 'number-divider';
export const EFFECTIVE_CURRENCY = 'effective-currency';
export const CURRENCY_CODE = 'code';
export const CURRENCY_ID = 'id';
export const LANGUAGE = 'language';
export const TRANSLATED_VALUE = 'translated-value';
export const TEAM_ID = 'team-id';
export const PUBLIC_VERSION_HISTORY = 'public-version-history';
export const PUBLIC_CHANGE_SUMMARY = 'public-change-summary';
export const FIELD_NAME = 'field_name';
export const CHILDREN = 'children';
export const ID_ONLY = 'id_only';

//INFO
export const INFO_EDIT = 'edit';
export const INFO_VALIDATE = 'validate';
export const INFO_ACTIVITY_TEAM = 'activity-team';
export const INFO_IS_PRIVATE = 'is-private';
export const INFO_IS_COMPUTED = 'is-computed';
export const INFO_VALIDATION_STATUS = 'validation-status';
export const AUTOMATIC_VALIDATION = 'AUTOMATIC_VALIDATION';
export const AWAITING_VALIDATION = 'AWAITING_VALIDATION';
export const CANNOT_BE_VALIDATE = 'CANNOT_BE_VALIDATED';
export const INFO_LAST_VERSION = 'amp-activity-last-version-id';
export const INFO_ACTIVITY_ID = 'amp-activity-id';
export const ANCESTOR_VALUES = 'ancestor-values';
export const VERSION_HISTORY = 'version-history';
export const MODIFIED_BY_INFO = 'modified-by';
export const MODIFIED_DATE = 'modified-date';
export const UPDATE_CURRENT_VERSION = 'update-current-version';

//FUNDING
export const FUNDING_INFORMATION = 'funding_information';
export const PPC_AMOUNT = 'ppc_amount';
export const RPC_AMOUNT = 'rpc_amount';
export const TRANSACTIONS = 'transactions';
export const SUBTOTAL = 'subtotal';
export const UNDISBURSED_BALANCE = 'undisbursed_balance';
export const TOTALS = 'totals';
export const DELIVERY_RATE_PROP = 'delivery_rate';
export const FUNDING_TOTALS = 'funding_totals';
export const TRX_TYPE_PATH = 'fundings~funding_details~transaction_type';
export const ADJ_TYPE_PATH = 'fundings~funding_details~adjustment_type';
export const AMOUNT = 'amount';
export const REORDER_TRX = 'reorder-funding-item';

//RELATED DOCUMENTS
export const ACTIVITY_DOCUMENTS = 'activity_documents';
export const DOC_UUID = 'uuid';
export const DOC_TITLE = 'title';
export const DOC_DESC = 'description';
export const DOC_WEB_LINK = 'web_link';
export const DOC_URL = 'url';
export const DOC_DATE = 'adding_date';
export const FILE_NAME = 'file_name';





