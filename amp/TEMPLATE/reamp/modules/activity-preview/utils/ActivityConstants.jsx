/**
 * @author Daniel Oliva
 */

export const PROJECT_TITLE = 'project_title';
export const AMP_ID = 'amp_id';
export const ACTIVITY_STATUS = 'activity_status';
export const ACTIVITY_BUDGET = 'activity_budget';
export const ACTIVITY_SECTION_IDS =
  [
    { key: 'AcIdentification', hash: '#AcIdentification', value: 'Identification', translationKey: 'amp.activity-preview:sectionIdentification'},
    { key: 'AcInternalIds', hash: '#AcInternalIds', value: 'Agency Internal IDs', translationKey: 'amp.activity-preview:sectionInternalIds' }
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



