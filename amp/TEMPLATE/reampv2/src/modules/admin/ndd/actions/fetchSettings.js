import {fetchSettingsError, fetchSettingsPending, fetchSettingsSuccess} from './settingsAction';
import {SETTINGS_EP} from '../constants/Constants';

function fetchSettings() {
  return dispatch => {
    dispatch(fetchSettingsPending());
    return fetch(SETTINGS_EP)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchSettingsSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchSettingsError(error));
      });
  };
}

export default fetchSettings;
