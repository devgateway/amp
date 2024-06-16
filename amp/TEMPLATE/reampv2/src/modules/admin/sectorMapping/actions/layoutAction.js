export const FETCH_LAYOUT_PENDING = 'FETCH_LAYOUT_PENDING';
export const FETCH_LAYOUT_SUCCESS = 'FETCH_LAYOUT_SUCCESS';
export const FETCH_LAYOUT_ERROR = 'FETCH_LAYOUT_ERROR';

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
