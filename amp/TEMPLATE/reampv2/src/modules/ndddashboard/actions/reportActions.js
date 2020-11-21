export const FETCH_DIRECT_INDIRECT_NDD_PENDING = 'FETCH_DIRECT_INDIRECT_NDD_PENDING';
export const FETCH_DIRECT_INDIRECT_NDD_SUCCESS = 'F FETCH_DIRECT_INDIRECT_NDD_SUCCESS';
export const FETCH_DIRECT_INDIRECT_NDD_ERROR = ' FETCH_DIRECT_INDIRECT_NDD_ERROR';

export function fetchIndirectReportPending() {
  return {
    type: FETCH_DIRECT_INDIRECT_NDD_PENDING
  };
}

// rest/ndd/direct-indirect-report
export function fetchIndirectReportSuccess(payload) {
  return {
    type: FETCH_DIRECT_INDIRECT_NDD_SUCCESS,
    payload
  };
}

export function fetchIndirectReportError(error) {
  return {
    type: FETCH_DIRECT_INDIRECT_NDD_ERROR,
    error
  };
}
