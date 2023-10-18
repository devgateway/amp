import { combineReducers } from '@reduxjs/toolkit';
import startupReducer from './startupReducer';
import translationsReducer from '../../../../utils/reducers/translationsReducer';
import shareLinkReducer from './shareLinkReducer';
import sharedDataReducer from './sharedDataReducer';
import fetchSettingsReducer from './fetchSettingsReducer';

//indicators reducers
import fetchIndicatorsReducer from './fetchIndicatorsReducer';
import fetchSectorsReducer from './fetchSectorsReducer';
import fetchProgramsReducer from './fetchProgramsReducer';
import createIndicatorReducer from './createIndicatorReducer';
import updateIndicatorReducer from './updateIndicatorReducer';
import deleteIndicatorReducer from './deleteIndicatorReducer';

export default combineReducers({
  startupReducer,
  translationsReducer,
  shareLinkReducer,
  sharedDataReducer,
  fetchIndicatorsReducer,
  fetchSectorsReducer,
  fetchProgramsReducer,
  createIndicatorReducer,
  updateIndicatorReducer,
  deleteIndicatorReducer,
  fetchSettingsReducer
});
