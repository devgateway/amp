import {
  fetchIndirectReportPending,
  fetchIndirectReportError,
  fetchIndirectReportSuccess,
  fetchTopReportPending,
  fetchTopReportSuccess, fetchTopReportError
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { DIRECT_INDIRECT_REPORT, FUNDING_TYPE,TOP_DONOR_REPORT } from '../utils/constants';

export const calNddlReport = (fundingType) => dispatch => {
  dispatch(fetchIndirectReportPending());
  return fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: { [FUNDING_TYPE]: fundingType }
  })
    .then(payload => dispatch(fetchIndirectReportSuccess(payload)))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
export const callTopReport = (reportType) => dispatch => {
  const params = {
    filters: { date: { start: '2000-01-01', end: '' } },
    'include-location-children': true,
    settings: { 'currency-code': 'USD', 'funding-type': 'Actual Commitments' }
  };
  dispatch(fetchTopReportPending());
  return fetchApiData({
    url: TOP_DONOR_REPORT,
    body: params
  })
    .then(payload => dispatch(fetchTopReportSuccess(payload)))
    .catch(error => dispatch(fetchTopReportError(error)));
};
