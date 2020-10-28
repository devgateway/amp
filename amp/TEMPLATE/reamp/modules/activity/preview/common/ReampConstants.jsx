import {ActivityConstants} from 'amp-ui';

export const ACTIVITY_API = '/rest/activity/projects/';
export const ACTIVITY_INFO_API = '/rest/activity/info/';

export const FIELDS_DEFINITION_API = '/rest/activity/fields-no-workspace';
export const POSSIBLE_VALUES_API = '/rest/activity/field/values/public';
export const FM_API = '/rest/common/fm';
export const SETTINGS_API = '/rest/amp/settings';
export const ACTIVITY_FIELDS_ID_VALUES_API = '/rest/activity/field/id-values?translations=en&language=en';
export const RESOURCES_POSSIBLE_VALUES_API = '/rest/resource/field/values';
export const RESOURCES_ENABLED_FIELDS_API = '/rest/resource/fields';
export const RESOURCES_API = '/rest/resource';
export const FUNDING_INFORMATION_API = '/rest/activity/{ACTIVITY_ID}/preview/fundings/?currency-id={CURRENCY_ID}';
export const ACTIVITY_WS_INFO = '/rest/activity/{ACTIVITY_ID}/preview/workspaces';
export const FM_ROOT = 'fm-settings';
export const RTL_DIRECTION = 'rtl-direction';
export const SHOW_ACTIVITY_WORKSPACES = 'show-activity-workspaces';
export const HIDE_CONTACTS_PUBLIC_VIEW = 'hide-contacts-public-view';
export const ACTIVITY_WORKSPACE_LEAD_DATA = 'activity-workspace-lead-data';
const CALENDAR = 'calendar';
export const CALENDAR_ID = CALENDAR + '-id';
export const IS_FISCAL = 'is-fiscal';
export const CALENDAR_IS_FISCAL = CALENDAR + '-' + IS_FISCAL;
export const PUBLIC_CHANGE_SUMMARY = 'public-change-summary';
export const PUBLIC_VERSION_HISTORY = 'public-version-history'
export const TEAM_ID = 'team-id';
export const REORDER_FUNDING_ITEM = 'reorder-funding-item';
export const FUNDING_INFORMATION = 'funding_information';
export const TRANSACTION_ID = 'transaction_id';

export const ACTIVITY_FORM_URL = '/wicket/onepager/activity';
export const ACTIVITY_FORM_URL_SSC = '/wicket/onepager/ssc';
export const ACTIVITY_PREVIEW_URL = '/aim/viewActivityPreview.do~activityId=';

export const VERSION_HISTORY_URL = '/aim/viewActivityHistory.do';
export const COMPARE_ACTIVITY_URL = '/aim/compareActivityVersions.do';

export const PRODUCTION = 'production';
export const REGIONAL_FUNDINGS = [ActivityConstants.REGIONAL_FUNDINGS_COMMITMENTS,
    ActivityConstants.REGIONAL_FUNDINGS_DISBURSEMENTS,
    ActivityConstants.REGIONAL_FUNDINGS_EXPENDITURES];