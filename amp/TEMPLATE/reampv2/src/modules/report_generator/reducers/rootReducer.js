import {combineReducers} from 'redux';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import uiReducer from './stateUIReducer';
import previewReducer from './previewReducer';
import settingsReducer from './settingsReducer';
import layoutReducer from './layoutReducer';
import mementoReducer from './mementoReducer';
import languagesReducer from './languagesReducer';

export default combineReducers({
  translationsReducer,
  uiReducer,
  previewReducer,
  settingsReducer,
  layoutReducer,
  mementoReducer,
  languagesReducer
});
