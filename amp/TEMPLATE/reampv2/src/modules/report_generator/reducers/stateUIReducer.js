import {
  UPDATE_COLUMNS,
  UPDATE_MEASURES,
  UPDATE_REPORT_DETAILS_TOTAL_GROUPING,
  UPDATE_REPORT_DETAILS_TOTALS_ONLY
} from '../actions/stateUIActions';

const initialState = {
  reportDetails: {
    selectedTotalGrouping: null,
    selectedTotalsOnly: false,
  },
  columns: {},
  measures: {}
};

export default (state = initialState, action) => {
  switch (action.type) {
    case UPDATE_REPORT_DETAILS_TOTAL_GROUPING:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedTotalGrouping: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_TOTALS_ONLY:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedTotalsOnly: action.payload
        }
      };
    case UPDATE_COLUMNS: {
      return {
        ...state,
        columns: action.payload
      };
    }
    case UPDATE_MEASURES:
      return {
        ...state,
        measures: action.payload
      };
    default:
      return state;
  }
};
