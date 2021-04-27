import {
  UPDATE_COLUMNS,
  UPDATE_MEASURES,
  UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS, UPDATE_REPORT_DETAILS_DESCRIPTION,
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
        'is-hierarchy': true,
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
      }
    ],
    selected: [],
  },
  measures: {},
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
