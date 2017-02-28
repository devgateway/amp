import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import aidOnBudget from "./AidOnBudgetReducer.jsx";
import commonLists from "./CommonListsReducer.jsx"
import startUp from "./StartUpReducer.jsx";
const rootReducer = combineReducers({   
    commonLists,
    aidOnBudget,   
    routing,
    startUp
});

export default rootReducer;