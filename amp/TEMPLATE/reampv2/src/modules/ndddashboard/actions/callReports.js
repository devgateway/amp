import {
  fetchIndirectReportPending,
  fetchIndirectReportError,
  fetchIndirectReportSuccess,
  fetchTopReportPending,
  fetchTopReportSuccess, fetchTopReportError
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import {
  DIRECT_INDIRECT_REPORT, FUNDING_TYPE, TOP_DONOR_REPORT
} from '../utils/constants';

export const callReport = (fundingType, filters, programIds, settings) => dispatch => {
  dispatch(fetchIndirectReportPending());
  const newSettings = { [FUNDING_TYPE]: fundingType, programIds, ...settings };
  return Promise.all([fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: {
      settings: newSettings,
      filters: (filters ? filters.filters : null)
    }
  })]).then((data) => dispatch(fetchIndirectReportSuccess(data[0])))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
export const callTopReport = (filters, fundingType) => dispatch => {
  const params = {
    filters: { date: { start: '2000-01-01', end: '' } },
    'include-location-children': true,
    settings: { 'currency-code': 'USD', [FUNDING_TYPE]: 'Actual Commitments' }
  };
  dispatch(fetchTopReportPending());
  return fetchApiData({
    url: TOP_DONOR_REPORT,
    body: params
  })
    .then(payload => dispatch(fetchTopReportSuccess(payload)))
    .catch(error => dispatch(fetchTopReportError(error)));
};
