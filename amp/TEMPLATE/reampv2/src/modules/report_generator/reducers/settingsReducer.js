import {
  FETCH_GLOBAL_SETTINGS_ERROR,
  FETCH_GLOBAL_SETTINGS_PENDING,
  FETCH_GLOBAL_SETTINGS_SUCCESS
} from '../actions/settingsActions';
import { extractSettings } from './utils/settingsDataConverter';

const initialState = {
  pending: false,
  loaded: false,
  error: false,
  globalSettings: undefined
};

export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_GLOBAL_SETTINGS_PENDING:
      return {
        ...state,
        pending: true,
      };
    case FETCH_GLOBAL_SETTINGS_SUCCESS: {
      return {
        ...state,
        pending: false,
        loaded: true,
        globalSettings: action.payload,
      };
    }
    case FETCH_GLOBAL_SETTINGS_ERROR:
      return {
        ...state,
        loaded: false,
        pending: false,
        error: action.error
      };
    default:
      return state;
  }
};
