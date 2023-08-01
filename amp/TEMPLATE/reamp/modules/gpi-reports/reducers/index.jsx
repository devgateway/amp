import {combineReducers} from "redux";
import {routerReducer as routing} from "react-router-redux";
import startUp from "./StartUpReducer";
import reports from "./ReportsReducer";
import commonLists from "./CommonListsReducer.jsx"

const rootReducer = combineReducers({
   routing,
   startUp,
   reports,
   commonLists
});

export default rootReducer;