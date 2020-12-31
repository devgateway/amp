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
        dashboardSettingsLoadingPending: true
      };
    case FETCH_DASHBOARD_SETTINGS_SUCCESS: {
      return {
        ...state,
        dashboardSettingsLoadingPending: false,
        dashboardSettingsLoaded: true,
        dashboardSettings: action.payload,
        globalSettings: extractSettings(action.gs)
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

const extractSettings = (gs) => {
  const format = {};
  format.numberFormat = gs['number-format'] || '#,#.#';
  format.precision = 0;
  if (format.numberFormat.indexOf('.') !== -1) {
    format.precision = format.numberFormat.length - format.numberFormat.indexOf('.') - 1;
  }
  if (format.numberFormat.indexOf(',') !== -1) {
    format.groupSeparator = gs['number-group-separator'] || ',';
  } else {
    format.groupSeparator = '';
  }
  format.decimalSeparator = gs['number-decimal-separator'] || '.';
  format.numberDivider = gs['number-divider'];
  if (format.numberDivider === 1) {
    format.numberDividerDescriptionKey = 'amp.ndd.dashboard:chart-tops-inunits';
  } else if (format.numberDivider === 1000) {
    format.numberDividerDescriptionKey = 'amp.ndd.dashboard:chart-tops-inthousands';
  } else if (format.numberDivider === 1000000) {
    format.numberDividerDescriptionKey = 'amp.ndd.dashboard:chart-tops-inmillions';
  } else if (format.numberDivider === 1000000000) {
    format.numberDividerDescriptionKey = 'amp.ndd.dashboard:chart-tops-inbillions';
  }

  format.dateFormat = gs['default-date-format'];
  format.defaultMinDate = gs['dashboard-default-min-date'];
  format.defaultMaxDate = gs['dashboard-default-max-date'];
  return format;
};
