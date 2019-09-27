import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import startUp from "./StartUpReducer";
//import commonLists from "./CommonListsReducer"

/**
 *
 */

const rootReducer = combineReducers({
   routing,
   startUp
   //,commonLists
});

export default rootReducer;
