import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import startUp from "./StartUpReducer";
import performanceRule from "./PerformanceRuleReducer";
const rootReducer = combineReducers({ 
    routing,
    startUp,    
    performanceRule
});

export default rootReducer;