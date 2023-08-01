import {URL_PREVIEW} from '../utils/constants';
import {fetchApiData} from '../../../utils/loadTranslations';

export const FETCH_PREVIEW_PENDING = 'FETCH_PREVIEW_PENDING';
export const FETCH_PREVIEW_SUCCESS = 'FETCH_PREVIEW_SUCCESS';
export const FETCH_PREVIEW_ERROR = 'FETCH_PREVIEW_ERROR';
export const UPDATE_PREVIEW_ID = 'UPDATE_PREVIEW_ID';
export const IGNORE_PREVIEW = 'IGNORE_PREVIEW';

export function fetchPreviewPending() {
  return {
    type: FETCH_PREVIEW_PENDING
  };
}

export function fetchPreviewSuccess(payload) {
  return {
    type: FETCH_PREVIEW_SUCCESS,
    payload
  };
}

export function fetchPreviewError(error) {
  return {
    type: FETCH_PREVIEW_ERROR,
    error
  };
}

export function updatePreviewId(id, name) {
  return {
    type: UPDATE_PREVIEW_ID,
    id,
    name
  };
}

/**
 * Use this function to dispatch an action that ignores the data from the EP because is not the
 * newest (the problem is the user can click the UI faster than the server can respond AND we
 * cant be sure the responses come in order from the server).
 * @returns {{type: string}}
 */
export function ignorePreview() {
  return {
    type: IGNORE_PREVIEW,
  };
}

export const getPreview = (body) => (dispatch, getState) => {
  dispatch(fetchPreviewPending());
  return fetchApiData({
    url: URL_PREVIEW,
    body,
    headers: { 'Content-Type': 'application/json', Accept: 'text/html' }
  }).then((data) => {
    const state = getState();
    if (state.previewReducer.lastReportId === body.id) {
      return dispatch(fetchPreviewSuccess(data));
    }
    return dispatch(ignorePreview());
  }).catch(error => dispatch(fetchPreviewError(error)));
};
