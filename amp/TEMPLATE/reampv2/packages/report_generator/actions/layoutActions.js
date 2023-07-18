import { URL_LAYOUT } from '../utils/constants';
import { fetchApiData } from '../../../utils/loadTranslations';

export const FETCH_LAYOUT_PENDING = 'FETCH_LAYOUT_PENDING';
export const FETCH_LAYOUT_SUCCESS = 'FETCH_LAYOUT_SUCCESS';
export const FETCH_LAYOUT_ERROR = 'FETCH_LAYOUT_ERROR';
export const UPDATE_PREVIEW_ID = 'UPDATE_PREVIEW_ID';
export const IGNORE_PREVIEW = 'IGNORE_PREVIEW';

export function fetchLayoutPending() {
  return {
    type: FETCH_LAYOUT_PENDING
  };
}

export function fetchLayoutSuccess(payload) {
  return {
    type: FETCH_LAYOUT_SUCCESS,
    payload
  };
}

export function fetchLayoutError(error) {
  return {
    type: FETCH_LAYOUT_ERROR,
    error
  };
}

export const getLayout = () => (dispatch) => {
  dispatch(fetchLayoutPending());
  return fetchApiData({ url: URL_LAYOUT })
    .then((data) => dispatch(fetchLayoutSuccess(data)))
    .catch(error => dispatch(fetchLayoutError(error)));
};
