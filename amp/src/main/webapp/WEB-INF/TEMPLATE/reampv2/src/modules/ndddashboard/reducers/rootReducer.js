import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import reportsReducer from './reportsReducer';
import dashboardSettingsReducer from './dashboardSettingsReducer';
import shareLinkReducer from './shareLinkReducer';
import sharedDataReducer from './sharedDataReducer';
import mappingsReducer from './mappingsReducer';

export default combineReducers({
  startupReducer,
  translationsReducer,
  reportsReducer,
  dashboardSettingsReducer,
  shareLinkReducer,
  sharedDataReducer,
  mappingsReducer
});
