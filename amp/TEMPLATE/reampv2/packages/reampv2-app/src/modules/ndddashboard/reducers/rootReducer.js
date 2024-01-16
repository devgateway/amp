import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import reportsReducer from './reportsReducer';
import dashboardSettingsReducer from './dashboardSettingsReducer';
import shareLinkReducer from './shareLinkReducer';
import sharedDataReducer from './sharedDataReducer';
import mappingsReducer from './mappingsReducer';
import fetchSettingsReducer from './fetchSettingsReducer';

//me dashboard
import programConfigurationReducer from '../medashboard/reducers/fetchProgramConfigurationReducer';
import indicatorsByProgramReducer from '../medashboard/reducers/fetchIndicatorsByProgramReducer';
import programReportReducer from '../medashboard/reducers/fetchProgramReportReducer';
import fetchIndicatorsReducer from '../medashboard/reducers/fetchIndicatorsReducer';
import fetchFmReducer from "../medashboard/reducers/fetchFmReducer";
import fetchSectorsReducer from '../medashboard/reducers/fetchSectorsReducer';
import fetchSectorClassificationReducer from '../medashboard/reducers/fetchSectorClassificationReducer';
import fetchSectorReportReducer from '../medashboard/reducers/fetchSectorsReportReducer';
import fetchIndicatorsByClassificationReducer from '../medashboard/reducers/fetchIndicatorsByClassificationReducer';

export default combineReducers({
  startupReducer,
  translationsReducer,
  reportsReducer,
  dashboardSettingsReducer,
  shareLinkReducer,
  sharedDataReducer,
  mappingsReducer,
  fetchSettingsReducer,
  programConfigurationReducer,
  indicatorsByProgramReducer,
  programReportReducer,
  fetchIndicatorsReducer,
  fetchFmReducer,
  fetchSectorsReducer,
  fetchSectorClassificationReducer,
  fetchSectorReportReducer,
  fetchIndicatorsByClassificationReducer
});
