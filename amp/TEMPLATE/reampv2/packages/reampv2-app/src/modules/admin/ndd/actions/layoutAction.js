export const FETCH_LAYOUT_PENDING = 'FETCH_LAYOUT_PENDING';
export const FETCH_LAYOUT_SUCCESS = 'FETCH_LAYOUT_SUCCESS';
export const FETCH_LAYOUT_ERROR = 'FETCH_LAYOUT_ERROR';
export const FETCH_PROGRAMS_PENDING = 'FETCH_PROGRAMS_PENDING';
export const FETCH_PROGRAMS_SUCCESS = 'FETCH_PROGRAMS_SUCCESS';
export const FETCH_PROGRAMS_ERROR = 'FETCH_PROGRAMS_ERROR';

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

export function fetchProgramsPending() {
  return {
    type: FETCH_PROGRAMS_PENDING
  };
}

export function fetchProgramsSuccess(programs) {
  return {
    type: FETCH_PROGRAMS_SUCCESS,
    payload: programs
  };
}
export function fetchProgramsError(error) {
  return {
    type: FETCH_PROGRAMS_ERROR,
    error
  };
}
