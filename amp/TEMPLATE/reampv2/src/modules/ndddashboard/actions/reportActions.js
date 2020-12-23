export const FETCH_DIRECT_INDIRECT_NDD_PENDING = 'FETCH_DIRECT_INDIRECT_NDD_PENDING';
export const FETCH_DIRECT_INDIRECT_NDD_SUCCESS = 'FETCH_DIRECT_INDIRECT_NDD_SUCCESS';
export const FETCH_DIRECT_INDIRECT_NDD_ERROR = ' FETCH_DIRECT_INDIRECT_NDD_ERROR';

export const FETCH_TOP_PENDING = 'FETCH_TOP_PENDING';
export const FETCH_TOP_SUCCESS = 'FETCH_TOP_SUCCESS';
export const FETCH_TOP_ERROR = ' FETCH_TOP_ERROR';

export function fetchIndirectReportPending() {
  return {
    type: FETCH_DIRECT_INDIRECT_NDD_PENDING
  };
}

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

export function fetchTopReportPending() {
  return {
    type: FETCH_TOP_PENDING
  };
}

export function fetchTopReportSuccess(payload) {
  return {
    type: FETCH_TOP_SUCCESS,
    payload
  };
}

export function fetchTopReportError(error) {
  return {
    type: FETCH_TOP_ERROR,
    error
  };
}
