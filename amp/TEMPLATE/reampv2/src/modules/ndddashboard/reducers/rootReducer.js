import {combineReducers} from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../utils/reducers/translationsReducer';

export default combineReducers({
    startupReducer, translationsReducer
});
