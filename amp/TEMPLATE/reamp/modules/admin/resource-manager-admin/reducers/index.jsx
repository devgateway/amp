// @flow
import {combineReducers} from "redux";
import {routerReducer as routing} from "react-router-redux";
import startUp from "./StartUpReducer.jsx";
import homePage from "./HomeReducer.jsx";

const rootReducer = combineReducers({
    homePage,
    startUp,
    routing,
});

export default rootReducer;
