import {
  FETCH_LANGUAGES_ERROR,
  FETCH_LANGUAGES_PENDING,
  FETCH_LANGUAGES_SUCCESS
} from '../actions/languagesActions';

const initialState = {
  pending: false,
  loaded: false,
  error: false,
  data: undefined,
};

export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_LANGUAGES_PENDING:
      return {
        ...state,
        pending: true,
      };
    case FETCH_LANGUAGES_SUCCESS: {
      return {
        ...state,
        pending: false,
        loaded: true,
        data: action.payload,
      };
    }
    case FETCH_LANGUAGES_ERROR:
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
