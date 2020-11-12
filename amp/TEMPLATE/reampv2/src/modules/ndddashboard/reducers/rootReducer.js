import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import reportsReducer from './reportsReducer';

export default combineReducers({
  startupReducer, translationsReducer, reportsReducer
});
