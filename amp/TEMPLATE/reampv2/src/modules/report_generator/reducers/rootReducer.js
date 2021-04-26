import { combineReducers } from 'redux';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import loadMetadataReducer from './loadMetadataReducer';
import uiReducer from './stateUIReducer';

export default combineReducers({
  translationsReducer,
  loadMetadataReducer,
  uiReducer,
});
