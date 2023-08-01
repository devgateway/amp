import {combineReducers} from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import filtersReducer from './filtersReducer';
import reportsReducer from './reportsReducer';
import dataDownloadReducer from './dataDownloadReducer';

export default combineReducers({
  startupReducer, translationsReducer, filtersReducer, reportsReducer, dataDownloadReducer
});
