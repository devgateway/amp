import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import reportsReducer from './reportsReducer';
import dashboardSettingsReducer from './dashboardSettingsReducer';
import shareLinkReducer from './shareLinkReducer';
import sharedDataReducer from './sharedDataReducer';
import mappingsReducer from './mappingsReducer';

//me dashboard
import programConfigurationReducer from '../medashboard/reducers/fetchProgramConfiguration';
import indicatorsByProgramReducer from '../medashboard/reducers/fetchIndicatorsByProgram';
import programProgressReportReducer from '../medashboard/reducers/fetchProgramProgressReport';

export default combineReducers({
  startupReducer,
  translationsReducer,
  reportsReducer,
  dashboardSettingsReducer,
  shareLinkReducer,
  sharedDataReducer,
  mappingsReducer,
  programConfigurationReducer,
  indicatorsByProgramReducer,
  programProgressReportReducer
});
