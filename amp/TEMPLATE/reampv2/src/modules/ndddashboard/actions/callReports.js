import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import {
  DIRECT_INDIRECT_REPORT, FUNDING_TYPE
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
