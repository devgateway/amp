import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import startUp from "./StartUpReducer";
//import commonLists from "./CommonListsReducer"
import activityReducer from './ActivityReducer';

/**
 *
 */

const rootReducer = combineReducers({
   routing,
   startUp,
   activityReducer
});

export default rootReducer;
