export const TRN_PREFIX_REPORT = 'amp.reportGenerator:';
export const TRN_PREFIX_TAB = 'amp.tabGenerator:';
export const URL_METADATA = '/rest/reports/designer';
export const URL_PREVIEW = '/rest/data/report/preview';
export const URL_GET_REPORT = '/rest/reports/{reportId}';
export const URL_SAVE_NEW = '/rest/reports';
export const URL_SAVE = '/rest/reports/';
export const URL_SETTINGS_REPORTS = '/rest/settings-definitions/reports';
export const URL_SETTINGS_TABS = '/rest/settings-definitions/tabs';
export const URL_GLOBAL_SETTINGS = '/rest/amp/settings';
export const URL_LAYOUT = '/rest/security/layout';
export const URL_LANGUAGES = '/rest/translations/languages';
export const REPORTS = 'REPORTS';
export const TABS = 'TABS';
export const PROFILE_TAB = 'T';
export const PROFILE_REPORT = 'R';
export const TYPE_DONOR = 'D';
export const TYPE_PLEDGE = 'P';
export const TYPE_COMPONENT = 'C';
export const TYPE_REGIONAL = 'R';
export const TYPE_GPI = 'G';
export const SETTINGS_YEAR_RANGE = 'year-range';
export const RUN_REPORT_NAME = 'Dynamic Report';

export const SUMMARY_REPORT = 'summary-report';
export const ANNUAL_REPORT = 'annual-report';
export const QUARTERLY_REPORT = 'quarterly-report';
export const MONTHLY_REPORT = 'monthly-report';
export const TOTALS_ONLY = 'totals-only';
export const TOTAL_GROUPING_RADIO_OPTIONS = [ANNUAL_REPORT, QUARTERLY_REPORT, MONTHLY_REPORT, TOTALS_ONLY];
export const TOTAL_GROUPING_CHECKBOX_OPTIONS = [SUMMARY_REPORT];

export const FUNDING_DONOR = 'funding-donor';
export const REGIONAL_REPORT = 'funding-component';
export const COMPONENT_REPORT = 'funding-contribution';
export const PLEDGES_REPORT = 'funding-pledges';
export const FUNDING_GROUPING_RADIO_OPTIONS = [FUNDING_DONOR, REGIONAL_REPORT, COMPONENT_REPORT, PLEDGES_REPORT];

export const SHOW_PLEDGES = 'show-pledges';
export const EMPTY_FUNDING_COLUMNS = 'empty-funding-columns';
export const SPLIT_BY_FUNDING = 'split-by-funding';
export const SHOW_ORIGINAL_CURRENCY = 'show-original-currency';
export const USE_FILTERS = 'use-filters';
export const OPTIONS_CHECKBOX_OPTIONS = [EMPTY_FUNDING_COLUMNS, SPLIT_BY_FUNDING, SHOW_ORIGINAL_CURRENCY, SHOW_PLEDGES,
  /* USE_FILTERS */];

// Section with constants for the API errors, MUST MATCH ApiError.java and the ids assigned to each API error.
export const ERROR_CLASS_REPORT_DESIGNER_ID = '17';
export const API_ERROR_DUPLICATED_NAME = '05';

export const IS_MEASURELESS_REPORT = true; // One day this could be a GS/FM setting.
export const FM_IS_PUBLIC_REPORT_ENABLED = 'fm-is-public-report-enabled';
