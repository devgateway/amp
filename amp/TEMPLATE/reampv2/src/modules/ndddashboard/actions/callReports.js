import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import {
  DIRECT_INDIRECT_REPORT, FUNDING_TYPE, INDIRECT_MAPPING_CONFIG, MAPPING_CONFIG_NO_INDIRECT
} from '../utils/constants';

export const callReport = (fundingType, filters, programIds) => dispatch => {
  dispatch(fetchIndirectReportPending());
  return Promise.all([fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: {
      settings: { [FUNDING_TYPE]: fundingType, programIds },
      filters: (filters ? filters.filters : null)
    }
  }), fetchApiData({
    url: INDIRECT_MAPPING_CONFIG
  }), fetchApiData({
    url: MAPPING_CONFIG_NO_INDIRECT
  })]).then((data) => dispatch(fetchIndirectReportSuccess(data[0], data[1], data[2])))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
