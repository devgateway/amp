import { FETCH_MAPPINGS_ERROR, FETCH_MAPPINGS_PENDING, FETCH_MAPPINGS_SUCCESS } from '../actions/mappingsActions';

const initialState = {
  mappingsLoaded: false,
  mappingsLoadingPending: false,
  error: null
};
export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_MAPPINGS_PENDING:
      return {
        ...state,
        mappingsLoadingPending: true
      };
    case FETCH_MAPPINGS_SUCCESS: {
      return {
        ...state,
        mappingsLoadingPending: false,
        mappingsLoaded: true,
        mapping: action.payload,
        noIndirectMapping: action.payload2
      };
    }
    case FETCH_MAPPINGS_ERROR:
      return {
        ...state,
        mappingsLoaded: false,
        mappingsLoadingPending: false,
        error: action.error
      };
    default:
      return state;
  }
};
