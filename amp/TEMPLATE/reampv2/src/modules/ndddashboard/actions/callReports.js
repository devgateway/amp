import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { DIRECT_INDIRECT_REPORT, FUNDING_TYPE } from '../utils/constants';

export const callReport = (fundingType, filters) => dispatch => {
  dispatch(fetchIndirectReportPending());
  return fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: {
      settings: { [FUNDING_TYPE]: fundingType },
      filters: (filters ? filters.filters : null)
    }
  })
    .then(payload => dispatch(fetchIndirectReportSuccess(payload)))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
