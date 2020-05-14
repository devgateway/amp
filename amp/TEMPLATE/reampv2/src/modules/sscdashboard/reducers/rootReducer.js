import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';
import filtersReducer from './filtersReducer';

export default combineReducers({
    startupReducer, translationsReducer, filtersReducer
});
