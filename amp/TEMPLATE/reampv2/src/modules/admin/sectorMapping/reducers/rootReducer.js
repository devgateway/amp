import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import saveSectorMappingReducer from './saveSectorMappingReducer';
import translationsReducer from '../../../../utils/reducers/translationsReducer';
import updateActivitiesReducer from './updateActivitiesReducer';

export default combineReducers({
  startupReducer,
  translationsReducer,
  saveSectorMappingReducer,
  updateActivitiesReducer
});
