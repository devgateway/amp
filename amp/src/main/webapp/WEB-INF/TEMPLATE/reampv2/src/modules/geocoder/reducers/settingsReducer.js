import {FETCH_SETTINGS_ERROR, FETCH_SETTINGS_PENDING, FETCH_SETTINGS_SUCCESS} from "../actions/settingsAction";

const initialState = {
    settingsLoaded: false,
    settingsPending: false,
    settings: {},
    error: null
};

export default (state = initialState, action) => {
    switch (action.type) {
        case FETCH_SETTINGS_PENDING:
            return {
                ...state,
                settingsPending: true
            };
        case FETCH_SETTINGS_SUCCESS:
            return {
                ...state,
                settingsPending: false,
                settingsLoaded: true,
                settings: action.payload
            };
        case FETCH_SETTINGS_ERROR:
            return {
                ...state,
                settingsPending: false,
                settingsLoaded: false,
                error: action.payload.error
            };
        default:
            return state;
    }
};
