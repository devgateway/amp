export const UPDATE_REPORT_DETAILS_TOTAL_GROUPING = 'UPDATE_REPORT_DETAILS_TOTAL_GROUPING';
export const UPDATE_REPORT_DETAILS_TOTALS_ONLY = 'UPDATE_REPORT_DETAILS_TOTALS_ONLY';
export const UPDATE_REPORT_DETAILS_FUNDING_GROUPING = 'UPDATE_REPORT_DETAILS_FUNDING_GROUPING';
export const UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS = 'UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS';
export const UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING = 'UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING';
export const UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES = 'UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES';
export const UPDATE_COLUMNS = 'UPDATE_COLUMNS';
export const UPDATE_MEASURES = 'UPDATE_MEASURES';

export function updateReportDetailsTotalGrouping(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_TOTAL_GROUPING,
    payload
  };
}

export function updateReportDetailsTotalsOnly(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_TOTALS_ONLY,
    payload
  };
}

export function updateReportDetailsFundingGrouping(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_FUNDING_GROUPING,
    payload
  };
}

export function updateReportDetailsAllowEmptyFundingColumns(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS,
    payload
  };
}

export function updateReportDetailsSplitByFunding(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING,
    payload
  };
}

export function updateReportDetailsShowOriginalCurrencies(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES,
    payload
  };
}

export function updateColumns(payload) {
  return {
    type: UPDATE_COLUMNS,
    payload
  };
}

export function updateMeasures(payload) {
  return {
    type: UPDATE_MEASURES,
    payload
  };
}
