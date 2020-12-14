import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { DIRECT_INDIRECT_REPORT, FUNDING_TYPE, MAPPING_CONFIG } from '../utils/constants';

export const callReport = (fundingType, filters) => dispatch => {
  dispatch(fetchIndirectReportPending());
  return Promise.all([fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: {
      settings: { [FUNDING_TYPE]: fundingType },
      filters: (filters ? filters.filters : null)
    }
  }), fetchApiData({
    url: MAPPING_CONFIG
  })]).then((data) => dispatch(fetchIndirectReportSuccess(data[0])))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
