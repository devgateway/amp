import {
  FETCH_XLS_REPORT_DONE,
  FETCH_XLS_REPORT_ERROR,
  FETCH_XLS_REPORT_PENDING,
  FETCH_XLS_REPORT_SUCCESS
} from '../actions/downloadDataActions';
import { INVALID_COLUMN_ERROR_CODE } from '../utils/constants';

const initialState = {
  dataDownloadPending: false,
  dataDownloadLoaded: false,
  dataDownload: null,
  error: null
};

export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_XLS_REPORT_PENDING:
      return {
        ...state,
        dataDownloadPending: false
      };
    case FETCH_XLS_REPORT_SUCCESS:
      return {
        ...state,
        dataDownloadPending: false,
        dataDownloadLoaded: true,
        dataDownload: action.payload
      };
    case FETCH_XLS_REPORT_ERROR:
      return {
        ...state,
        dataDownloadPending: false,
        dataDownloadLoaded: false,
        error: action.error
      };
    case FETCH_XLS_REPORT_DONE:
      return { ...initialState };
    default:
      return state;
  }
};
