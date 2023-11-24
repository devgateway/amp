export const FETCH_LAYOUT_PENDING = 'FETCH_LAYOUT_PENDING';
export const FETCH_LAYOUT_SUCCESS = 'FETCH_LAYOUT_SUCCESS';
export const FETCH_LAYOUT_ERROR = 'FETCH_LAYOUT_ERROR';
export const FETCH_SECTORS_PENDING = 'FETCH_SECTORS_PENDING';
export const FETCH_SECTORS_SUCCESS = 'FETCH_SECTORS_SUCCESS';
export const FETCH_SECTORS_ERROR = 'FETCH_SECTORS_ERROR';

export function fetchLayoutPending() {
  return {
    type: FETCH_LAYOUT_PENDING
  };
}

export function fetchLayoutSuccess(layout) {
  return {
    type: FETCH_LAYOUT_SUCCESS,
    payload: layout
  };
}
export function fetchLayoutError(error) {
  return {
    type: FETCH_LAYOUT_ERROR,
    error
  };
}

export function fetchSectorsPending() {
  return {
    type: FETCH_SECTORS_PENDING
  };
}

export function fetchSectorsSuccess(sectors) {
  return {
    type: FETCH_SECTORS_SUCCESS,
    payload: sectors
  };
}
export function fetchSectorsError(error) {
  return {
    type: FETCH_SECTORS_ERROR,
    error
  };
}
