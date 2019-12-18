import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import startUpReducer from "./StartUpReducer";
import contactReducer from "./ContactsReducer";
import activityReducer from './ActivityReducer';

/**
 *
 */

const rootReducer = combineReducers({
   routing,
   startUpReducer,
   activityReducer,
   contactReducer
});

export default rootReducer;
