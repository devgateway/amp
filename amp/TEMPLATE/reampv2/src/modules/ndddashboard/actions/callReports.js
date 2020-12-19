import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import {
  DIRECT_INDIRECT_REPORT, FUNDING_TYPE
} from '../utils/constants';

export const callReport = (fundingType, filters, programIds) => dispatch => {
  dispatch(fetchIndirectReportPending());
  return Promise.all([fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: {
      settings: { [FUNDING_TYPE]: fundingType, programIds },
      filters: (filters ? filters.filters : null)
    }
  })]).then((data) => dispatch(fetchIndirectReportSuccess(data[0])))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
