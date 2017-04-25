import { combineReducers } from "redux";
import { routerReducer as routing } from "react-router-redux";
import startUp from "./StartUpReducer.jsx";
const rootReducer = combineReducers({
   routing,
   startUp
});

export default rootReducer;