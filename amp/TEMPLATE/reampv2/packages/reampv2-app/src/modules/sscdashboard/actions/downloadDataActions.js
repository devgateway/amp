export const FETCH_XLS_REPORT_PENDING = 'FETCH_XLS_REPORT_PENDING';
export const FETCH_XLS_REPORT_SUCCESS = 'FETCH_XLS_REPORT_SUCCESS';
export const FETCH_XLS_REPORT_ERROR = 'FETCH_XLS_REPORT_ERROR';
export const FETCH_XLS_REPORT_DONE = 'FETCH_XLS_REPORT_DONE';

export function fetchXlsReportPending() {
  return {
    type: FETCH_XLS_REPORT_PENDING
  };
}

export function fetchXlsReportSuccess(dataDownload) {
  return {
    type: FETCH_XLS_REPORT_SUCCESS,
    payload: dataDownload
  };
}

export function fetchXlsReportError(error) {
  return {
    type: FETCH_XLS_REPORT_ERROR,
    error
  };
}

export function fetchXlsReportDone() {
  return {
    type: FETCH_XLS_REPORT_DONE
  };
}
