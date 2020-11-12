import {
  fetchIndirectReportPending, fetchIndirectReportError, fetchIndirectReportSuccess
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { DIRECT_INDIRECT_REPORT } from '../utils/constants';

export default () => dispatch => {
  dispatch(fetchIndirectReportPending());
  return fetchApiData({ url: DIRECT_INDIRECT_REPORT })
    .then(activitiesDetail => dispatch(fetchIndirectReportSuccess(activitiesDetail)))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};