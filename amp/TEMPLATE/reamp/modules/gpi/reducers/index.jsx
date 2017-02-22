import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import aidOnBudgetList from "./AidOnBudgetReducer.jsx";
import commonLists from "./CommonListsReducer.jsx"
const rootReducer = combineReducers({   
    commonLists,
    aidOnBudgetList,   
    routing,
});

export default rootReducer;