import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import reportsReducer from './reportsReducer';
import dashboardSettingsReducer from './dashboardSettingsReducer';
import shareLinkReducer from './shareLinkReducer';
import sharedDataReducer from './sharedDataReducer';
import mappingsReducer from './mappingsReducer';

//me dashboard
import programConfigurationReducer from '../medashboard/reducers/fetchProgramConfigurationReducer';
import indicatorsByProgramReducer from '../medashboard/reducers/fetchIndicatorsByProgramReducer';
import programReportReducer from '../medashboard/reducers/fetchProgramReportReducer';
import fetchIndicatorsReducer from '../medashboard/reducers/fetchIndicatorsReducer';
import indicatorReportReducer from '../medashboard/reducers/fetchIndicatorReportReducer';
import fetchFmReducer from "../medashboard/reducers/fetchFmReducer";
import fetchSectorsReducer from '../medashboard/reducers/fetchSectorsReducer';
import fetchSectorSchemesReducer from '../medashboard/reducers/fetchSectorSchemesReducer';

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
  programReportReducer,
  fetchIndicatorsReducer,
  indicatorReportReducer,
  fetchFmReducer,
  fetchSectorsReducer,
  fetchSectorSchemesReducer
});
