import { FETCH_LAYOUT_ERROR, FETCH_LAYOUT_PENDING, FETCH_LAYOUT_SUCCESS } from '../actions/layoutActions';

const initialState = {
  pending: false,
  loaded: false,
  error: false,
  results: undefined
};

export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_LAYOUT_PENDING:
      return {
        ...state,
        pending: true
      };
    case FETCH_LAYOUT_SUCCESS: {
      return {
        ...state,
        pending: false,
        loaded: true,
        results: action.payload,
      };
    }
    case FETCH_LAYOUT_ERROR:
      return {
        ...state,
        loaded: false,
        pending: false,
        error: action.error
      };
    default:
      return state;
  }
};
