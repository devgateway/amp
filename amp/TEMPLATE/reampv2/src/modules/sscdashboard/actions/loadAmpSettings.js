import { fetchAmpSettingsPending, fetchAmpSettingsError, fetchAmpSettingsSuccess }
    from './startupAction';
import { fetchApiData } from '../../../utils/loadTranslations';

const API_AMP_SETTINGS_URL = '/rest/amp/settings';

function loadAmpSettings() {
    return dispatch => {
        dispatch(fetchAmpSettingsPending());
        return fetchApiData({url: API_AMP_SETTINGS_URL})
            .then(ampSettings => {
                return dispatch(fetchAmpSettingsSuccess(ampSettings));
            })
            .catch(error => {
                return dispatch(fetchAmpSettingsError(error))
            });
    }
}

export default loadAmpSettings;
