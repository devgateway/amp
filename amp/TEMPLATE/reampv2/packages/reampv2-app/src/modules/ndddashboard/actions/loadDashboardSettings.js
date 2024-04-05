import {
  fetchDashboardSettingsPending, fetchDashboardSettingsError, fetchDashboardSettingsSuccess
} from './dashboardSettingsActions';
import { fetchApiData } from '../../../utils/apiOperations';
import { SETTINGS_EP, GS_EP } from '../utils/constants';

export default () => dispatch => {
  dispatch(fetchDashboardSettingsPending());
  return Promise.all([fetchApiData({ url: SETTINGS_EP }), fetchApiData({ url: GS_EP })])
    .then(payload => dispatch(fetchDashboardSettingsSuccess(payload[0], payload[1])))
    .catch(error => dispatch(fetchDashboardSettingsError(error)));
};
