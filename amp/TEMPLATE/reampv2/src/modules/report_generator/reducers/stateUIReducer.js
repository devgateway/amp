import {
  UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS, UPDATE_REPORT_DETAILS_DESCRIPTION,
  UPDATE_REPORT_DETAILS_FUNDING_GROUPING, UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES,
  UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING,
  UPDATE_REPORT_DETAILS_TOTAL_GROUPING,
  UPDATE_REPORT_DETAILS_TOTALS_ONLY,
  UPDATE_COLUMNS_SELECTED_COLUMN, UPDATE_COLUMNS_SORT_COLUMN,
  UPDATE_MEASURES_SELECTED_COLUMN, UPDATE_MEASURES_SORT_COLUMN,
  UPDATE_HIERARCHIES_SELECTED_COLUMN, UPDATE_HIERARCHIES_SORT_COLUMN, UPDATE_HIERARCHIES_LIST
} from '../actions/stateUIActions';

const initialState = {
  reportDetails: {
    selectedTotalGrouping: null,
    selectedTotalsOnly: false,
    selectedFundingGrouping: null,
    selectedAllowEmptyFundingColumns: false,
    selectedSplitByFunding: false,
    selectedShowOriginalCurrencies: false,
    description: null,
  },
  columns: {
    available: [
      {
        id: 256,
        name: 'Project Title',
        label: 'Project Title',
        description: 'The Project Title tooltip',
        category: 'Identification',
        'is-hierarchy': true,
        'is-amount': false
      },
      {
        id: 257,
        name: 'AMP ID',
        label: 'AMP ID',
        description: 'The AMP ID tooltip',
        category: 'Identification',
        'is-hierarchy': false,
        'is-amount': false
      },
      {
        id: 532,
        name: 'Donor Agency',
        label: 'Donor Agency',
        description: 'The Donor Agency tooltip',
        category: 'Funding Information',
        'is-hierarchy': true,
        'is-amount': false
      },
      {
        id: 533,
        name: 'Primary Sector',
        label: 'Primary Sector',
        description: 'blablabla',
        category: 'Sectors',
        'is-hierarchy': true,
        'is-amount': false
      },
      {
        id: 534,
        name: 'Primary Sector Sub-Sector',
        label: 'Primary Sector Sub-Sector',
        description: 'blablablafasdf',
        category: 'Sectors',
        'is-hierarchy': true,
        'is-amount': false
      },
      {
        id: 535,
        name: 'Primary Sector Sub-Sub-Sector',
        label: 'Primary Sector Sub-Sub-Sector',
        description: 'blablablafasdf asdfasdf asd',
        category: 'Sectors',
        'is-hierarchy': true,
        'is-amount': false
      }],
    selected: [],
  },
  hierarchies: {
    available: [],
    selected: [],
    order: []
  },
  measures: {
    available: [{
      id: 365,
      name: 'Actual Commitments',
      label: 'Actual Commitments',
      description: null,
      type: 'A'
    },
    {
      id: 333,
      name: 'Actual Pledge',
      label: 'Actual Pledge',
      description: null,
      type: 'P'
    },
    {
      id: 345,
      name: 'Forecast Execution Rate',
      label: 'Forecast Execution Rate',
      description: 'Sum of Actual Disbursements / Sum (Most recent of (Pipeline MTEF for the year, Projection MTEF for the year)).',
      type: 'D'
    },
    {
      id: 456,
      name: 'Previous Month Disbursements',
      label: 'Previous Month Disbursements',
      description: 'Actual Disbursements Of Previous Month',
      type: 'A'
    },
    {
      id: 457,
      name: 'Previous Month Disbursements',
      label: 'Previous Month Disbursements',
      description: 'Actual Disbursements Of Previous Month',
      type: 'A'
    },
    {
      id: 458,
      name: 'Previous Month Disbursements',
      label: 'Previous Month Disbursements',
      description: 'Actual Disbursements Of Previous Month',
      type: 'A'
    },
    {
      id: 459,
      name: 'Previous Month Disbursements',
      label: 'Previous Month Disbursements',
      description: 'Actual Disbursements Of Previous Month',
      type: 'A'
    },
    {
      id: 460,
      name: 'Previous Month Disbursements',
      label: 'Previous Month Disbursements',
      description: 'Actual Disbursements Of Previous Month',
      type: 'A'
    },
    {
      id: 461,
      name: 'Previous Month Disbursements',
      label: 'Previous Month Disbursements',
      description: 'Actual Disbursements Of Previous Month',
      type: 'A'
    }],
    selected: [],
    order: [],
  },
  activeStep: 0,
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
    case UPDATE_REPORT_DETAILS_DESCRIPTION:
      return {
        ...state,
        reportDetails: {
          ...state.reportDetails,
          description: action.payload
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
    default:
      return state;
  }
};
