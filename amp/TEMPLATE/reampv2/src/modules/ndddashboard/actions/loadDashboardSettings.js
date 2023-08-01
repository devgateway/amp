import {
    fetchDashboardSettingsError,
    fetchDashboardSettingsPending,
    fetchDashboardSettingsSuccess
} from './dashboardSettingsActions';
import {fetchApiData} from '../../../utils/apiOperations';
import {GS_EP, SETTINGS_EP} from '../utils/constants';

export default () => dispatch => {
  dispatch(fetchDashboardSettingsPending());
  return Promise.all([fetchApiData({ url: SETTINGS_EP }), fetchApiData({ url: GS_EP })])
    .then(payload => dispatch(fetchDashboardSettingsSuccess(payload[0], payload[1])))
    .catch(error => dispatch(fetchDashboardSettingsError(error)));
};
