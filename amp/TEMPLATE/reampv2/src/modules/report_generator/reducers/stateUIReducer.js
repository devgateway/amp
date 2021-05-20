import {
  UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS,
  UPDATE_REPORT_DETAILS_DESCRIPTION,
  UPDATE_REPORT_DETAILS_FUNDING_GROUPING,
  UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES,
  UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING,
  UPDATE_REPORT_DETAILS_TOTAL_GROUPING,
  UPDATE_REPORT_DETAILS_TOTALS_ONLY,
  UPDATE_COLUMNS_SELECTED_COLUMN,
  UPDATE_COLUMNS_SORT_COLUMN,
  UPDATE_MEASURES_SELECTED_COLUMN,
  UPDATE_MEASURES_SORT_COLUMN,
  UPDATE_HIERARCHIES_SELECTED_COLUMN,
  UPDATE_HIERARCHIES_SORT_COLUMN,
  UPDATE_HIERARCHIES_LIST,
  FETCH_METADATA_PENDING,
  FETCH_METADATA_SUCCESS,
  FETCH_METADATA_ERROR,
  RESET_MEASURES_SELECTED_COLUMN,
  RESET_COLUMNS_SELECTED_COLUMN,
  FETCH_REPORT_SUCCESS,
  UPDATE_REPORT_DETAILS_ALSO_SHOW_PLEDGES,
  FETCH_REPORT_PENDING,
  FETCH_REPORT_ERROR,
  UPDATE_REPORT_DETAILS_NAME,
  UPDATE_REPORT_DETAILS_REPORT_CATEGORY,
  UPDATE_APPLIED_FILTERS,
  UPDATE_APPLIED_SETTINGS,
  UPDATE_REPORT_DETAILS_USE_ABOVE_FILTERS, UPDATE_PROFILE, UPDATE_ID,
} from '../actions/stateUIActions';
import {
  convertColumns,
  convertHierarchies,
  convertMeasures,
  convertReportDetails
} from './utils/stateUIDataConverter';
import { getProfileFromReport } from '../utils/Utils';

const initialState = {
  reportDetails: {
    selectedTotalGrouping: null,
    selectedSummaryReport: false,
    selectedFundingGrouping: null,
    selectedAllowEmptyFundingColumns: false,
    selectedSplitByFunding: false,
    selectedShowOriginalCurrencies: false,
    selectedAlsoShowPledges: false,
    selectedUseAboveFilters: false,
    description: null,
    name: null,
    isTab: false,
    publicView: false,
    workspaceLinked: false,
    selectedReportCategory: null,
  },
  columns: {
    available: [],
    selected: [],
  },
  hierarchies: {
    available: [],
    selected: [],
    order: []
  },
  measures: {
    available: [],
    selected: [],
    order: [],
  },
  activeStep: 0,
  metaDataLoaded: false,
  metaDataPending: false,
  error: null,
  filters: null,
  appliedFilters: null,
  settings: null,
  reportCategories: [],
  reportLoaded: false,
  reportPending: false,
  type: null,
  profile: null,
  id: null
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
          selectedSummaryReport: action.payload
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
    case UPDATE_REPORT_DETAILS_ALSO_SHOW_PLEDGES:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedAlsoShowPledges: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_USE_ABOVE_FILTERS:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedUseAboveFilters: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_DESCRIPTION:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          description: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_NAME:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          name: action.payload
        }
      };
    case UPDATE_REPORT_DETAILS_REPORT_CATEGORY:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          selectedReportCategory: action.payload
        }
      };
    case UPDATE_COLUMNS_SELECTED_COLUMN:
      return {
        ...state,
        columns: {
          ...state.columns,
          selected: action.payload
        }
      };
    case UPDATE_COLUMNS_SORT_COLUMN:
      return {
        ...state,
        columns: {
          ...state.columns,
          selected: action.payload
        }
      };
    case RESET_COLUMNS_SELECTED_COLUMN:
      return {
        ...state,
        columns: {
          ...state.columns,
          selected: [],
        },
        hierarchies: {
          available: [],
          selected: [],
          order: []
        }
      };
    case UPDATE_HIERARCHIES_LIST:
      return {
        ...state,
        hierarchies: {
          ...state.hierarchies,
          available: action.payload
        }
      };
    case UPDATE_HIERARCHIES_SELECTED_COLUMN:
      return {
        ...state,
        hierarchies: {
          ...state.hierarchies,
          selected: action.payload
        }
      };
    case UPDATE_HIERARCHIES_SORT_COLUMN:
      return {
        ...state,
        hierarchies: {
          ...state.hierarchies,
          order: action.payload
        }
      };
    case UPDATE_MEASURES_SELECTED_COLUMN:
      return {
        ...state,
        measures: {
          ...state.measures,
          selected: action.payload
        }
      };
    case UPDATE_MEASURES_SORT_COLUMN:
      return {
        ...state,
        measures: {
          ...state.measures,
          order: action.payload
        }
      };
    case RESET_MEASURES_SELECTED_COLUMN:
      return {
        ...state,
        measures: {
          ...state.measures,
          selected: [],
          order: [],
        }
      };
    case UPDATE_APPLIED_FILTERS:
      return {
        ...state,
        filters: action.payload,
        appliedFilters: action.html,
      };
    case UPDATE_APPLIED_SETTINGS:
      return {
        ...state,
        settings: action.payload
      };
    case FETCH_METADATA_PENDING:
      return {
        ...state,
        metaDataPending: true
      };
    case FETCH_METADATA_SUCCESS: {
      return {
        ...state,
        metaDataPending: false,
        metaDataLoaded: true,
        measures: {
          available: action.payload.measures,
          selected: [],
          order: [],
        },
        columns: {
          available: action.payload.columns,
          selected: [],
        },
        options: action.payload.options,
        reportCategories: action.payload.reportCategories
      };
    }
    case FETCH_METADATA_ERROR:
      return {
        ...state,
        metaDataLoaded: false,
        metaDataPending: false,
        error: action.error
      };
    case FETCH_REPORT_SUCCESS:
      return {
        ...state,
        reportDetails: convertReportDetails(state.reportDetails, action.payload),
        columns: convertColumns(state.columns, action.payload),
        hierarchies: convertHierarchies(state.hierarchies, action.payload, state.columns),
        measures: convertMeasures(state.measures, action.payload),
        settings: action.payload.settings,
        filters: action.payload.filters,
        appliedFilters: null,
        reportLoaded: true,
        reportPending: false,
        type: action.payload.type,
        profile: getProfileFromReport(action.payload),
        id: action.payload.id
      };
    case FETCH_REPORT_PENDING:
      return {
        ...state,
        reportLoaded: false,
        reportPending: true
      };
    case FETCH_REPORT_ERROR:
      return {
        ...state,
        reportLoaded: false,
        reportPending: false
      };
    case UPDATE_PROFILE:
      return {
        ...state,
        profile: action.payload,
      };
    case UPDATE_ID:
      return {
        ...state,
        id: action.payload,
      };
    default:
      return state;
  }
};
