import { fetchAmpSettingsPending, fetchAmpSettingsError, fetchAmpSettingsSuccess }
  from './startupAction';
import { fetchApiData } from '../../../utils/loadTranslations';
import { API_AMP_SETTINGS_URL } from '../utils/constants';

function loadAmpSettings() {
  return dispatch => {
    dispatch(fetchAmpSettingsPending());
    return fetchApiData({ url: API_AMP_SETTINGS_URL })
      .then(ampSettings => dispatch(fetchAmpSettingsSuccess(ampSettings)))
      .catch(error => dispatch(fetchAmpSettingsError(error)));
  };
}

export default loadAmpSettings;
