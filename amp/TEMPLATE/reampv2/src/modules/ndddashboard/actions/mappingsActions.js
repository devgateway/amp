export const FETCH_MAPPINGS_PENDING = 'FETCH_MAPPINGS_PENDING';
export const FETCH_MAPPINGS_SUCCESS = 'F FETCH_MAPPINGS_SUCCESS';
export const FETCH_MAPPINGS_ERROR = ' FETCH_MAPPINGS_ERROR';

export function fetchDashboardSettingsPending() {
  return {
    type: FETCH_MAPPINGS_PENDING
  };
}

export function fetchDashboardSettingsSuccess(payload, payload2) {
  return {
    type: FETCH_MAPPINGS_SUCCESS,
    payload,
    payload2
  };
}

export function fetchDashboardSettingsError(error) {
  return {
    type: FETCH_MAPPINGS_ERROR,
    error
  };
}
