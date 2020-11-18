import {
  FETCH_DIRECT_INDIRECT_NDD_ERROR,
  FETCH_DIRECT_INDIRECT_NDD_PENDING,
  FETCH_DIRECT_INDIRECT_NDD_SUCCESS
} from '../actions/reportActions';

const initialState = {
  nddLoaded: false,
  nddLoadingPending: false,
  error: null
};
export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_DIRECT_INDIRECT_NDD_PENDING:
      return {
        ...state,
        nddLoadingPending: true
      };
    case FETCH_DIRECT_INDIRECT_NDD_SUCCESS: {
      return {
        ...state,
        nddLoadingPending: false,
        nddLoaded: true,
        ndd: action.payload,
        settings: action.settings
      };
    }
    case FETCH_DIRECT_INDIRECT_NDD_ERROR:
      return {
        ...state,
        nddLoaded: false,
        nddLoadingPending: false,
        error: action.error
      };
    default:
      return state;
  }
};
