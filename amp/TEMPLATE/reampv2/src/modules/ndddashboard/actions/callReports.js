import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { DIRECT_INDIRECT_REPORT, FUNDING_TYPE } from '../utils/constants';

export const callReport = (fundingType) => dispatch => {
  dispatch(fetchIndirectReportPending());
  return fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: { [FUNDING_TYPE]: fundingType }
  })
    .then(payload => dispatch(fetchIndirectReportSuccess(payload)))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
