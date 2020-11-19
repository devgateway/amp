import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import reportsReducer from './reportsReducer';
import dashboardSettingsReducer from './dashboardSettingsReducer';

export default combineReducers({
  startupReducer, translationsReducer, reportsReducer, dashboardSettingsReducer
});
