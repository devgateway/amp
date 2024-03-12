export const FETCH_SETTINGS_PENDING = 'FETCH_SETTINGS_PENDING';
export const FETCH_SETTINGS_SUCCESS = 'FETCH_SETTINGS_SUCCESS';
export const FETCH_SETTINGS_ERROR = 'FETCH_SETTINGS_ERROR';

export function fetchSettingsPending() {
    return {
        type: FETCH_SETTINGS_PENDING
    };
}

export function fetchSettingsSuccess(layout) {
    return {
        type: FETCH_SETTINGS_SUCCESS,
        payload: layout
    };
}

export function fetchSettingsError(error) {
    return {
        type: FETCH_SETTINGS_ERROR,
        error
    };
}
