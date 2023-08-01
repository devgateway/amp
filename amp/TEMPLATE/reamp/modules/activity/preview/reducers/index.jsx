import {combineReducers} from "redux";
import {routerReducer as routing} from "react-router-redux";
import startUpReducer from "./StartUpReducer";
import contactReducer from "./ContactsReducer";
import activityReducer from './ActivityReducer';
import resourceReducer from './ResourceReducer';

/**
 *
 */

const rootReducer = combineReducers({
   routing,
   startUpReducer,
   activityReducer,
   contactReducer,
   resourceReducer
});

export default rootReducer;
