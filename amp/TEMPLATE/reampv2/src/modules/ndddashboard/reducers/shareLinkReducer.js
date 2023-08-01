import {
  FETCH_SHARE_LINK_ERROR,
  FETCH_SHARE_LINK_PENDING,
  FETCH_SHARE_LINK_SUCCESS
} from '../actions/shareLinkActions';

const initialState = {
  shareLinkLoaded: false,
  shareLinkPending: false,
  error: null
};
export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_SHARE_LINK_PENDING:
      return {
        ...state,
        shareLinkPending: true
      };
    case FETCH_SHARE_LINK_SUCCESS: {
      return {
        ...state,
        shareLinkPending: false,
        shareLinkLoaded: true,
        shareLink: action.payload,
      };
    }
    case FETCH_SHARE_LINK_ERROR:
      return {
        ...state,
        shareLinkLoaded: false,
        shareLinkPending: false,
        error: action.error
      };
    default:
      return state;
  }
};
