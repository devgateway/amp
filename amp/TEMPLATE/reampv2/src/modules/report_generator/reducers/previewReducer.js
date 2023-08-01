import {
    FETCH_PREVIEW_ERROR,
    FETCH_PREVIEW_PENDING,
    FETCH_PREVIEW_SUCCESS,
    IGNORE_PREVIEW,
    UPDATE_PREVIEW_ID
} from '../actions/previewActions';

const initialState = {
  lastReportId: 0,
  lastReportName: undefined,
  pending: false,
  loaded: false,
  error: false,
  results: undefined
};

export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_PREVIEW_PENDING:
      return {
        ...state,
        pending: true
      };
    case FETCH_PREVIEW_SUCCESS: {
      return {
        ...state,
        pending: false,
        loaded: true,
        results: action.payload,
      };
    }
    case FETCH_PREVIEW_ERROR:
      return {
        ...state,
        loaded: false,
        pending: false,
        error: action.error
      };
    case UPDATE_PREVIEW_ID:
      return {
        ...state,
        lastReportId: action.id,
        lastReportName: action.name,
        loaded: false,
        pending: false,
        error: false
      };
    case IGNORE_PREVIEW:
      return {
        ...state,
      };
    default:
      return state;
  }
};
