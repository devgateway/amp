import {combineReducers} from 'redux';
import translationsReducer from "../../../utils/reducers/translationsReducer";
import activitiesReducer from "./activitiesReducer";
import geocodingReducer from "./geocodingReducer";
import settingsReducer from "./settingsReducer";

export default combineReducers({
    translationsReducer,
    activitiesReducer,
    geocodingReducer,
    settingsReducer
});
