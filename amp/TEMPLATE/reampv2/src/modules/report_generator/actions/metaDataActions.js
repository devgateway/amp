import { fetchApiData } from '../../../utils/loadTranslations';
import { URL_METADATA } from '../utils/constants';

export const FETCH_METADATA_PENDING = 'FETCH_METADATA_PENDING';
export const FETCH_METADATA_SUCCESS = 'FETCH_METADATA_SUCCESS';
export const FETCH_METADATA_ERROR = ' FETCH_METADATA_ERROR';

export function fetchMetaDataPending() {
  return {
    type: FETCH_METADATA_PENDING
  };
}

export function fetchMetaDataSuccess(payload) {
  return {
    type: FETCH_METADATA_SUCCESS,
    payload
  };
}

export function fetchMetaDataError(error) {
  return {
    type: FETCH_METADATA_ERROR,
    error
  };
}

export const getMetadata = () => dispatch => {
  dispatch(fetchMetaDataPending());
  return Promise.all([fetchApiData({
    url: URL_METADATA,
    body: {}
  })]).then((data) => dispatch(fetchMetaDataSuccess(data[0])))
    .catch(error => dispatch(fetchMetaDataError(error)));
};
