export const FETCH_DASHBOARD_SETTINGS_PENDING = 'FETCH_DASHBOARD_SETTINGS_PENDING';
export const FETCH_DASHBOARD_SETTINGS_SUCCESS = 'F FETCH_DASHBOARD_SETTINGS_SUCCESS';
export const FETCH_DASHBOARD_SETTINGS_ERROR = ' FETCH_DASHBOARD_SETTINGS_ERROR';

export function fetchDashboardSettingsPending() {
  return {
    type: FETCH_DASHBOARD_SETTINGS_PENDING
  };
}

export function fetchDashboardSettingsSuccess(payload) {
  return {
    type: FETCH_DASHBOARD_SETTINGS_SUCCESS,
    payload
  };
}

export function fetchDashboardSettingsError(error) {
  return {
    type: FETCH_DASHBOARD_SETTINGS_ERROR,
    error
  };
}
