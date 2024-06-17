import {
  FETCH_DASHBOARD_SETTINGS_ERROR,
  FETCH_DASHBOARD_SETTINGS_PENDING,
  FETCH_DASHBOARD_SETTINGS_SUCCESS
} from '../actions/dashboardSettingsActions';

const initialState = {
  dashboardSettingsLoaded: false,
  dashboardSettingsLoadingPending: false,
  error: null
};
export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_DASHBOARD_SETTINGS_PENDING:
      return {
        ...state,
        dashboardSettingsLoadingPending: true,
        dashboardSettingsLoaded: false
      };
    case FETCH_DASHBOARD_SETTINGS_SUCCESS: {
      return {
        ...state,
        dashboardSettingsLoadingPending: false,
        dashboardSettingsLoaded: true,
        dashboardSettings: action.payload,
        globalSettings: action.gs
      };
    }
    case FETCH_DASHBOARD_SETTINGS_ERROR:
      return {
        ...state,
        dashboardSettingsLoaded: false,
        dashboardSettingsLoadingPending: false,
        error: action.error
      };
    default:
      return state;
  }
};
