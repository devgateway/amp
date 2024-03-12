import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import saveNDDReducer from './saveNDDReducer';
import translationsReducer from '../../../../utils/reducers/translationsReducer';
import updateActivitiesReducer from './updateActivitiesReducer';

export default combineReducers({
  startupReducer,
  translationsReducer,
  saveNDDReducer,
  updateActivitiesReducer
});
