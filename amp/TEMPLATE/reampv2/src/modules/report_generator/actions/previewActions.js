import { URL_PREVIEW } from '../utils/constants';
import { fetchApiData } from '../../../utils/loadTranslations';

export const FETCH_PREVIEW_PENDING = 'FETCH_PREVIEW_PENDING';
export const FETCH_PREVIEW_SUCCESS = 'FETCH_PREVIEW_SUCCESS';
export const FETCH_PREVIEW_ERROR = 'FETCH_PREVIEW_ERROR';
export const UPDATE_PREVIEW_ID = 'UPDATE_PREVIEW_ID';

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

// TODO: keep a cache of previous calls.
export const getPreview = (body) => dispatch => {
  dispatch(fetchPreviewPending());
  return fetchApiData({
    url: URL_PREVIEW,
    body,
    headers: { 'Content-Type': 'application/json', Accept: 'text/html' }
  }).then((data) => dispatch(fetchPreviewSuccess(data)))
    .catch(error => dispatch(fetchPreviewError(error)));
};
