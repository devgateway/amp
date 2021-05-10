import { combineReducers } from 'redux';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import uiReducer from './stateUIReducer';
import previewReducer from './previewReducer';

export default combineReducers({
  translationsReducer,
  uiReducer,
  previewReducer,
});
