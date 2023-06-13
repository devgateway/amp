import { combineReducers } from '@reduxjs/toolkit';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import shareLinkReducer from './shareLinkReducer';
import sharedDataReducer from './sharedDataReducer';
import fetchSettingsReducer from './fetchSettingsReducer';


export default combineReducers({
  startupReducer,
  translationsReducer,
  shareLinkReducer,
  sharedDataReducer,
  fetchSettingsReducer,
});
