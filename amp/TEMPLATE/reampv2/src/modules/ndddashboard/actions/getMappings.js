import {
  fetchDashboardSettingsError, fetchDashboardSettingsPending, fetchDashboardSettingsSuccess
} from './mappingsActions';
import { fetchApiData } from '../../../utils/apiOperations';
import { INDIRECT_MAPPING_CONFIG, MAPPING_CONFIG_NO_INDIRECT } from '../utils/constants';

export const getMappings = () => dispatch => {
  dispatch(fetchDashboardSettingsPending());
  return Promise.all([fetchApiData({
    url: INDIRECT_MAPPING_CONFIG
  }), fetchApiData({
    url: MAPPING_CONFIG_NO_INDIRECT
  })]).then((data) => dispatch(fetchDashboardSettingsSuccess(data[0], data[1])))
    .catch(error => dispatch(fetchDashboardSettingsError(error)));
};
