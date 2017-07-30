import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import startUp from "./StartUpReducer";
import commonLists from "./CommonListsReducer";
import performanceRule from "./PerformanceRuleReducer";
const rootReducer = combineReducers({ 
    routing,
    startUp,    
    commonLists,
    performanceRule
});

export default rootReducer;