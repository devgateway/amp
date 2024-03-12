export const FETCH_SHARE_LINK_PENDING = 'FETCH_SHARE_LINK_PENDING';
export const FETCH_SHARE_LINK_SUCCESS = 'FETCH_SHARE_LINK_SUCCESS';
export const FETCH_SHARE_LINK_ERROR = ' FETCH_SHARE_LINK_ERROR';
export const GET_SHARED_DATA_PENDING = 'GET_SHARED_DATA_PENDING';
export const GET_SHARED_DATA_SUCCESS = 'GET_SHARED_DATA_SUCCESS';
export const GET_SHARED_DATA_ERROR = ' GET_SHARED_DATA_ERROR';

export function fetchShareLinkPending() {
  return {
    type: FETCH_SHARE_LINK_PENDING
  };
}

export function fetchShareLinkSuccess(payload) {
  return {
    type: FETCH_SHARE_LINK_SUCCESS,
    payload
  };
}

export function fetchShareLinkError(error) {
  return {
    type: FETCH_SHARE_LINK_ERROR,
    error
  };
}

export function getSharedDataPending() {
  return {
    type: GET_SHARED_DATA_PENDING
  };
}

export function getSharedDataSuccess(payload) {
  return {
    type: GET_SHARED_DATA_SUCCESS,
    payload
  };
}

export function getSharedDataError(error) {
  return {
    type: GET_SHARED_DATA_ERROR,
    error
  };
}
