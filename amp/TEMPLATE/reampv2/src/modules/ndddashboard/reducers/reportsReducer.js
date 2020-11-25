import {
  FETCH_DIRECT_INDIRECT_NDD_ERROR,
  FETCH_DIRECT_INDIRECT_NDD_PENDING,
  FETCH_DIRECT_INDIRECT_NDD_SUCCESS, FETCH_TOP_ERROR, FETCH_TOP_PENDING, FETCH_TOP_SUCCESS
} from '../actions/reportActions';

const initialState = {
  nddLoaded: false,
  nddLoadingPending: false,
  topLoaded: false,
  topLoadingPending: false,
  error: null,
  top: []
};
export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_DIRECT_INDIRECT_NDD_PENDING:
      return {
        ...state,
        nddLoadingPending: true
      };
    case FETCH_TOP_PENDING:
      return {
        ...state,
        topLoadingPending: true
      };
    case FETCH_DIRECT_INDIRECT_NDD_SUCCESS: {
      return {
        ...state,
        nddLoadingPending: false,
        nddLoaded: true,
        ndd: action.payload,
      };
    }
    case FETCH_TOP_SUCCESS: {
      return {
        ...state,
        topLoadingPending: false,
        topLoaded: true,
        top: action.payload,
      };
    }
    case FETCH_DIRECT_INDIRECT_NDD_ERROR:
      return {
        ...state,
        nddLoaded: false,
        nddLoadingPending: false,
        error: action.error
      };
    case FETCH_TOP_ERROR:
      return {
        ...state,
        topLoaded: false,
        topLoadingPending: false,
        error: action.error
      };
    default:
      return state;
  }
};
