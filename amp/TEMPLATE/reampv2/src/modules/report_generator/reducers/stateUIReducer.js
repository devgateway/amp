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
  UPDATE_REPORT_DETAILS_USE_ABOVE_FILTERS,
  UPDATE_PROFILE,
  UPDATE_ID,
  SAVE_NEW_REPORT_PENDING,
  SAVE_NEW_REPORT_SUCCESS,
  SAVE_NEW_REPORT_ERROR,
  SAVE_REPORT_PENDING,
  SAVE_REPORT_SUCCESS,
  SAVE_REPORT_ERROR,
  RUN_REPORT_PENDING,
  RUN_REPORT_SUCCESS,
  RUN_REPORT_ERROR, SET_INITIAL_HIERARCHIES,
} from '../actions/stateUIActions';
import {
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
    ownerId: null,
    includeLocationChildren: true
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
  id: null,
  reportSaving: false,
  reportSaved: false,
  reportSaveError: null,
  runReportPending: false,
  runReportSuccess: false,
  runReportError: false,
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
    case SET_INITIAL_HIERARCHIES:
      return {
        ...state,
        hierarchies: {
          available: action.available,
          selected: action.selected,
          order: action.order
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
          ...state.measures,
          available: action.payload.measures,
        },
        columns: {
          ...state.columns,
          available: action.payload.columns,
        },
        options: action.payload.options,
        reportCategories: action.payload.reportCategories,
        type: action.payload.type,
        profile: action.payload.profile,
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
        columns: {
          selected: action.payload.columns.map(i => i.id)
        },
        hierarchies: {
          available: action.payload.hierarchies,
          selected: action.payload.hierarchies.map(i => i.id),
          order: action.payload.hierarchies.map(i => i.id)
        },
        measures: {
          selected: action.payload.measures.map(i => i.id),
          order: action.payload.measures.map(i => i.id)
        },
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
    case SAVE_NEW_REPORT_PENDING:
      return {
        ...state,
        id: null,
        reportSaving: true,
        reportSaved: false,
        reportSaveError: null
      };
    case SAVE_NEW_REPORT_SUCCESS:
      return {
        ...state,
        id: action.payload.id,
        reportSaving: false,
        reportSaved: true,
        reportSaveError: null
      };
    case SAVE_NEW_REPORT_ERROR:
      return {
        ...state,
        id: action.id,
        reportSaving: false,
        reportSaved: false,
        reportSaveError: action.error
      };
    case SAVE_REPORT_PENDING:
      return {
        ...state,
        reportSaving: true,
        reportSaved: false,
        reportSaveError: null
      };
    case SAVE_REPORT_SUCCESS:
      return {
        ...state,
        reportSaving: false,
        reportSaved: true,
        reportSaveError: null
      };
    case SAVE_REPORT_ERROR:
      return {
        ...state,
        reportSaving: false,
        reportSaved: false,
        reportSaveError: action.payload
      };
    case RUN_REPORT_PENDING:
      return {
        ...state,
        runReportPending: true,
        runReportError: false,
        runReportSuccess: false,
      };
    case RUN_REPORT_SUCCESS:
      return {
        ...state,
        runReportPending: false,
        runReportError: false,
        runReportSuccess: true,
      };
    case RUN_REPORT_ERROR:
      return {
        ...state,
        runReportPending: false,
        runReportError: true,
        runReportSuccess: false,
      };
    default:
      return state;
  }
};
