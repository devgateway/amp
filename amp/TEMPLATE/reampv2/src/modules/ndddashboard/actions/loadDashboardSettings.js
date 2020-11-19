import {
  fetchDashboardSettingsPending, fetchDashboardSettingsError, fetchDashboardSettingsSuccess
} from './dashboardSettingsActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { SETTINGS_EP } from '../utils/constants';

export default () => dispatch => {
  // TODO: have 2 methods for settings and report.
  dispatch(fetchDashboardSettingsPending());
  return fetchApiData({ url: SETTINGS_EP })
    .then(payload => dispatch(fetchDashboardSettingsSuccess(payload)))
    .catch(error => dispatch(fetchDashboardSettingsError(error)));
};
