import { combineReducers } from 'redux';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import loadMetadataReducer from './loadMetadataReducer';

export default combineReducers({
  translationsReducer,
  loadMetadataReducer,
});
