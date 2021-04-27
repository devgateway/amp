import {
  UPDATE_COLUMNS,
  UPDATE_MEASURES,
  UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS,
  UPDATE_REPORT_DETAILS_FUNDING_GROUPING, UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES,
  UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING,
  UPDATE_REPORT_DETAILS_TOTAL_GROUPING,
  UPDATE_REPORT_DETAILS_TOTALS_ONLY
} from '../actions/stateUIActions';

const initialState = {
  reportDetails: {
    selectedTotalGrouping: null,
    selectedTotalsOnly: false,
    selectedFundingGrouping: null,
    selectedAllowEmptyFundingColumns: false,
    selectedSplitByFunding: false,
    selectedShowOriginalCurrencies: false,
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
    case UPDATE_REPORT_DETAILS_FUNDING_GROUPING:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedFundingGrouping: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedAllowEmptyFundingColumns: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedSplitByFunding: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedShowOriginalCurrencies: action.payload
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
