export const UPDATE_REPORT_DETAILS_TOTAL_GROUPING = 'UPDATE_REPORT_DETAILS_TOTAL_GROUPING';
export const UPDATE_REPORT_DETAILS_TOTALS_ONLY = 'UPDATE_REPORT_DETAILS_TOTALS_ONLY';
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
