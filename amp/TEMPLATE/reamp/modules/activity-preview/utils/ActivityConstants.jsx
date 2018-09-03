/**
 * @author Daniel Oliva
 */

export const DEFAULT_CURRENCY = 'XOF';

export const PROJECT_TITLE = 'project_title';
export const AMP_ID = 'amp_id';
export const ACTIVITY_STATUS = 'activity_status';
export const ACTIVITY_BUDGET = 'activity_budget';
export const ACTIVITY_SECTION_IDS =
  [
    { key: 'AcIdentification', hash: '#AcIdentification', value: 'Identification', translationKey: 'amp.activity-preview:sectionIdentification'},
    { key: 'AcInternalIds', hash: '#AcInternalIds', value: 'Agency Internal IDs', translationKey: 'amp.activity-preview:sectionInternalIds' },
    { key: 'AcFundingSources', hash: '#AcFundingSources', value: 'Funding Sources', translationKey: 'amp.activity-preview:fundingSources' }
  ];

export const STATUS_REASON = 'status_reason';
export const OBJECTIVE = 'objective';
export const DESCRIPTION = 'description';
export const PROJECT_COMMENTS = 'project_comments';
export const CRIS_NUMBER = 'cris_number';

  /*export const RICH_TEXT_FIELDS = new Set([STATUS_REASON, OBJECTIVE, DESCRIPTION, PROJECT_COMMENTS,
    LESSONS_LEARNED, PROJECT_IMPACT, ACTIVITY_SUMMARY, CONDITIONALITIES, PROJECT_MANAGEMENT, RESULTS,
  ]);*/
export const RICH_TEXT_FIELDS = new Set([STATUS_REASON, OBJECTIVE, DESCRIPTION, PROJECT_COMMENTS ]);

//Funding
export const ACTUAL = 'Actual';
export const PLANNED = 'Planned';
export const PIPELINE = 'Pipeline';
export const COMMITMENTS = 'Commitments';
export const DISBURSEMENTS = 'Disbursements';
export const ADJUSTMENT_TYPES = [ACTUAL, PLANNED, PIPELINE];
export const TRANSACTION_TYPES = [COMMITMENTS, DISBURSEMENTS];

export const ACTUAL_COMMITMENTS = 'Actual Commitments';
export const ACTUAL_DISBURSEMENTS = 'Actual Disbursements';
export const PLANNED_COMMITMENTS = 'Planned Commitments';
export const PLANNED_DISBURSEMENTS = 'Planned Disbursements';
export const DELIVERY_RATE = 'Delivery Rate';

export const FIXED_EXCHANGE_RATE = "fixed_exchange_rate";
export const CURRENCY = "currency";
export const TRANSACTION_DATE = "transaction_date";
export const TRANSACTION_AMOUNT = "transaction_amount";

//Activity Internal Ids
export const ACTIVITY_INTERNAL_IDS = "activity_internal_ids";
export const INTERNAL_ID = "internal_id";
export const ORGANIZATION = "organization";

//Planning
export const PROPOSED_APPROVAL_DATE = "proposed_approval_date";
export const ACTUAL_APPROVAL_DATE = "actual_approval_date";
export const PROPOSED_START_DATE = "proposed_start_date";
export const ACTUAL_START_DATE = "actual_start_date";
export const CREATION_DATE = "creation_date";
export const PROPOSED_COMPLETION_DATE = "proposed_completion_date";
export const ACTUAL_COMPLETION_DATE = "actual_completion_date";

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
export const FUNDING_DONOR_ORG_ID = 'donor_organization_id';
export const SOURCE_ROLE = 'source_role';
export const TYPE_OF_ASSISTANCE = 'type_of_assistance';
export const FINANCING_INSTRUMENT = 'financing_instrument';
export const FUNDING_STATUS = 'funding_status';
export const FINANCING_ID = 'financing_id';
export const FUNDING_DETAILS = 'funding_details';
export const ADJUSTMENT_TYPE = 'adjustment_type';
export const TRANSACTION_TYPE = 'transaction_type';


