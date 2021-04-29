export const UPDATE_REPORT_DETAILS_TOTAL_GROUPING = 'UPDATE_REPORT_DETAILS_TOTAL_GROUPING';
export const UPDATE_REPORT_DETAILS_TOTALS_ONLY = 'UPDATE_REPORT_DETAILS_TOTALS_ONLY';
export const UPDATE_REPORT_DETAILS_FUNDING_GROUPING = 'UPDATE_REPORT_DETAILS_FUNDING_GROUPING';
export const UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS = 'UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS';
export const UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING = 'UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING';
export const UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES = 'UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES';
export const UPDATE_REPORT_DETAILS_DESCRIPTION = 'UPDATE_REPORT_DETAILS_DESCRIPTION';
export const UPDATE_COLUMNS_SELECTED_COLUMN = 'UPDATE_COLUMNS_SELECTED_COLUMN';
export const UPDATE_COLUMNS_SORT_COLUMN = 'UPDATE_COLUMNS_SORT_COLUMN';
export const UPDATE_MEASURES_SORT_COLUMN = 'UPDATE_MEASURES_SORT_COLUMN';
export const UPDATE_MEASURES_SELECTED_COLUMN = 'UPDATE_MEASURES_SELECTED_COLUMN';
export const UPDATE_HIERARCHIES_SORT_COLUMN = 'UPDATE_HIERARCHIES_SORT_COLUMN';
export const UPDATE_HIERARCHIES_SELECTED_COLUMN = 'UPDATE_HIERARCHIES_SELECTED_COLUMN';

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

export function updateReportDetailsDescription(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_DESCRIPTION,
    payload
  };
}

export function updateColumnsSelected(payload) {
  return {
    type: UPDATE_COLUMNS_SELECTED_COLUMN,
    payload
  };
}

export function updateColumnsSorting(payload) {
  return {
    type: UPDATE_COLUMNS_SORT_COLUMN,
    payload
  };
}

export function updateMeasuresSelected(payload) {
  return {
    type: UPDATE_MEASURES_SELECTED_COLUMN,
    payload
  };
}

export function updateMeasuresSorting(payload) {
  return {
    type: UPDATE_MEASURES_SORT_COLUMN,
    payload
  };
}
