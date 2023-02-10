import { combineReducers } from 'redux';
import startupReducer from './startupReducer';
import translationsReducer from '../../../../utils/reducers/translationsReducer';
import shareLinkReducer from './shareLinkReducer';
import sharedDataReducer from './sharedDataReducer';

//indicators reducers
import fetchIndicatorsReducer from './fetchIndicatorsReducer';

export default combineReducers({
  startupReducer,
  translationsReducer,
  shareLinkReducer,
  sharedDataReducer,
  fetchIndicatorsReducer,
});
