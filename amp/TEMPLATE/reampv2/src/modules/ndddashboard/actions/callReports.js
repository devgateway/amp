import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { DIRECT_INDIRECT_REPORT, SETTINGS_EP } from '../utils/constants';

export default () => dispatch => {
  dispatch(fetchIndirectReportPending());
  return Promise.all([fetchApiData({ url: DIRECT_INDIRECT_REPORT }), fetchApiData({ url: SETTINGS_EP })])
    .then(payload => dispatch(fetchIndirectReportSuccess(payload)))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};
