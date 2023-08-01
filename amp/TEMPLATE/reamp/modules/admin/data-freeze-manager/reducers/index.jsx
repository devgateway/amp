import {combineReducers} from "redux";
import {routerReducer as routing} from "react-router-redux";
import startUp from "./StartUpReducer";
import dataFreeze from "./DataFreezeReducer";
import commonLists from "./CommonListsReducer";

const rootReducer = combineReducers({
    routing,
    startUp,
    dataFreeze,
    commonLists
});

export default rootReducer;