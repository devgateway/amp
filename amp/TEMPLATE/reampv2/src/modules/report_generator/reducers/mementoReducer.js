import {
  SET_COLUMNS_DATA, SET_FILTERS_DATA, SET_HIERARCHIES_DATA, SET_MEASURES_DATA, SET_SETTINGS_DATA
} from '../actions/mementoAction';

const initialState = {
  initialColumns: [],
  measures: [],
  hierarchies: [],
  filters: undefined,
  settings: undefined,
};

export default (state = initialState, action) => {
  switch (action.type) {
    case SET_SETTINGS_DATA:
      return {
        ...state,
        settings: action.payload
      };
    case SET_FILTERS_DATA: {
      return {
        ...state,
        filters: action.payload,
      };
    }
    case SET_COLUMNS_DATA:
      debugger
      return {
        ...state,
        initialColumns: action.payload,
      };
    case SET_HIERARCHIES_DATA:
      return {
        ...state,
        hierarchies: action.payload,
      };
    case SET_MEASURES_DATA:
      return {
        ...state,
        measures: action.payload,
      };
    default:
      return state;
  }
};
