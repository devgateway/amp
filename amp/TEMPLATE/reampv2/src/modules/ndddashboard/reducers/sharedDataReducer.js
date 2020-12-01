import {
  GET_SHARED_DATA_ERROR,
  GET_SHARED_DATA_PENDING,
  GET_SHARED_DATA_SUCCESS
} from '../actions/shareLinkActions';

const initialState = {
  sharedDataLoaded: false,
  sharedDataPending: false,
  error: null
};
export default (state = initialState, action) => {
  switch (action.type) {
    case GET_SHARED_DATA_PENDING:
      return {
        ...state,
        sharedDataPending: true
      };
    case GET_SHARED_DATA_SUCCESS: {
      return {
        ...state,
        sharedDataPending: false,
        sharedDataLoaded: true,
        sharedData: action.payload,
      };
    }
    case GET_SHARED_DATA_ERROR:
      return {
        ...state,
        sharedDataLoaded: false,
        sharedDataPending: false,
        error: action.error
      };
    default:
      return state;
  }
};
