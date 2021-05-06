export const TRN_PREFIX = 'amp.reportGenerator:';
export const URL_METADATA = '/rest/reports/designer';
export const URL_PREVIEW = '/rest/reports/designer/preview';

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
export const FUNDING_GROUPING_RADIO_OPTIONS = [FUNDING_DONOR, REGIONAL_REPORT, COMPONENT_REPORT];

export const SHOW_PLEDGES = 'show-pledges';
export const EMPTY_FUNDING_COLUMNS = 'empty-funding-columns';
export const SPLIT_BY_FUNDING = 'split-by-funding';
export const SHOW_ORIGINAL_CURRENCY = 'show-original-currency';
export const USE_FILTERS = 'use-filters';
export const OPTIONS_CHECKBOX_OPTIONS = [EMPTY_FUNDING_COLUMNS, SPLIT_BY_FUNDING, SHOW_ORIGINAL_CURRENCY, SHOW_PLEDGES];
