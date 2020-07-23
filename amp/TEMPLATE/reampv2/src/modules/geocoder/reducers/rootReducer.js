import {combineReducers} from 'redux';
import translationsReducer from "../../../utils/reducers/translationsReducer";
import activitiesReducer from "./activitiesReducer";
import geocodingReducer from "./geocodingReducer";

export default combineReducers({
    translationsReducer,
    activitiesReducer,
    geocodingReducer,
});
