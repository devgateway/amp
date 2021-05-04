import { combineReducers } from 'redux';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import uiReducer from './stateUIReducer';

export default combineReducers({
  translationsReducer,
  uiReducer,
});
