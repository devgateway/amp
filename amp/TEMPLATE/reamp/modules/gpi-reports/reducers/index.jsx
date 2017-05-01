import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import startUp from "./StartUpReducer";
import reports from "./ReportsReducer";
const rootReducer = combineReducers({
   routing,
   startUp,
   reports
});

export default rootReducer;