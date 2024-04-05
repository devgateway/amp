export const FETCH_AMP_SETTINGS_PENDING = 'FETCH_AMP_SETTINGS_PENDING';
export const FETCH_AMP_SETTINGS_SUCCESS = 'FETCH_AMP_SETTINGS_SUCCESS';
export const FETCH_AMP_SETTINGS_ERROR = 'FETCH_AMP_SETTINGS_ERROR';

export function fetchAmpSettingsPending() {
  return {
    type: FETCH_AMP_SETTINGS_PENDING
  };
}

export function fetchAmpSettingsSuccess(settings) {
  return {
    type: FETCH_AMP_SETTINGS_SUCCESS,
    payload: settings
  };
}

export function fetchAmpSettingsError(error) {
  return {
    type: FETCH_AMP_SETTINGS_ERROR,
    error
  };
}
