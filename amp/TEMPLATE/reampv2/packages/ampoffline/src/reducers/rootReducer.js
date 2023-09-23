import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from './translationsReducer';

export default combineReducers({
  startupReducer,
  translationsReducer,
});
