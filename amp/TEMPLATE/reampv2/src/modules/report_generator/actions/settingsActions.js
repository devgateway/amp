import {fetchApiData} from '../../../utils/loadTranslations';
import {URL_GLOBAL_SETTINGS} from '../utils/constants';

export const FETCH_GLOBAL_SETTINGS_PENDING = 'FETCH_GLOBAL_SETTINGS_PENDING';
export const FETCH_GLOBAL_SETTINGS_SUCCESS = 'FETCH_GLOBAL_SETTINGS_SUCCESS';
export const FETCH_GLOBAL_SETTINGS_ERROR = 'FETCH_GLOBAL_SETTINGS_ERROR';

export function fetchGlobalSettingsPending() {
  return {
    type: FETCH_GLOBAL_SETTINGS_PENDING
  };
}

export function fetchGlobalSettingsSuccess(payload, payload2) {
  return {
    type: FETCH_GLOBAL_SETTINGS_SUCCESS,
    payload,
    payload2
  };
}

export function fetchGlobalSettingsError(error) {
  return {
    type: FETCH_GLOBAL_SETTINGS_ERROR,
    error
  };
}

export const fetchGlobalSettings = (settingsURL) => dispatch => {
  dispatch(fetchGlobalSettingsPending());
  return Promise.all([fetchApiData({
    url: URL_GLOBAL_SETTINGS
  }), fetchApiData({
    url: settingsURL
  })]).then((data) => dispatch(fetchGlobalSettingsSuccess(data[0], data[1])))
    .catch(error => dispatch(fetchGlobalSettingsError(error)));
};
