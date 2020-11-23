export const FETCH_SHARE_LINK_PENDING = 'FETCH_SHARE_LINK_PENDING';
export const FETCH_SHARE_LINK_SUCCESS = 'F FETCH_SHARE_LINK_SUCCESS';
export const FETCH_SHARE_LINK_ERROR = ' FETCH_SHARE_LINK_ERROR';

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
