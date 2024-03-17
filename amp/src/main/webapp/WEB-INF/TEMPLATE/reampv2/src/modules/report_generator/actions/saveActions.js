import { fetchApiData } from '../../../utils/loadTranslations';
import { URL_METADATA } from '../utils/constants';

export const CREATE_REPORT_PENDING = 'CREATE_REPORT_PENDING';
export const CREATE_REPORT_SUCCESS = 'CREATE_REPORT_SUCCESS';
export const CREATE_REPORT_ERROR = 'CREATE_REPORT_ERROR';

export function createReportPending() {
  return {
    type: CREATE_REPORT_PENDING
  };
}

export function createReportSuccess(payload) {
  return {
    type: CREATE_REPORT_SUCCESS,
    payload
  };
}

export function createReportError(error) {
  return {
    type: CREATE_REPORT_ERROR,
    error
  };
}

export const callCreateReportEndpoint = (params) => dispatch => {
  dispatch(createReportPending());
  return Promise.all([fetchApiData({
    url: URL_METADATA,
    body: { params }
  })]).then((data) => dispatch(createReportSuccess(data[0])))
    .catch(error => dispatch(createReportError(error)));
};
