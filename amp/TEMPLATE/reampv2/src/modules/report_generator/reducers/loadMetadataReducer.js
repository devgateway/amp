import {
  FETCH_METADATA_ERROR,
  FETCH_METADATA_PENDING,
  FETCH_METADATA_SUCCESS
} from '../actions/metaDataActions';

const initialState = {
  metaDataLoaded: false,
  metaDataPending: false,
  error: null
};
export default (state = initialState, action) => {
  switch (action.type) {
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
        metaData: action.payload,
      };
    }
    case FETCH_METADATA_ERROR:
      return {
        ...state,
        metaDataLoaded: false,
        metaDataPending: false,
        error: action.error
      };
    default:
      return state;
  }
};
