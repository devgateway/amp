import { fetchApiData } from '../../../utils/loadTranslations';
import { URL_GLOBAL_SETTINGS } from '../utils/constants';

export const FETCH_GLOBAL_SETTINGS_PENDING = 'FETCH_GLOBAL_SETTINGS_PENDING';
export const FETCH_GLOBAL_SETTINGS_SUCCESS = 'FETCH_GLOBAL_SETTINGS_SUCCESS';
export const FETCH_GLOBAL_SETTINGS_ERROR = 'FETCH_GLOBAL_SETTINGS_ERROR';

export function fetchGlobalSettingsPending() {
  return {
    type: FETCH_GLOBAL_SETTINGS_PENDING
  };
}

export function fetchGlobalSettingsSuccess(payload) {
  return {
    type: FETCH_GLOBAL_SETTINGS_SUCCESS,
    payload
  };
}

export function fetchGlobalSettingsError(error) {
  return {
    type: FETCH_GLOBAL_SETTINGS_ERROR,
    error
  };
}

export const fetchGlobalSettings = () => dispatch => {
  dispatch(fetchGlobalSettingsPending());
  return fetchApiData({
    url: URL_GLOBAL_SETTINGS
  }).then((data) => dispatch(fetchGlobalSettingsSuccess(data)))
    .catch(error => dispatch(fetchGlobalSettingsError(error)));
};
