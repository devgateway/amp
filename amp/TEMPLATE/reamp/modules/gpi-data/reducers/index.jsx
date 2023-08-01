import {combineReducers} from "redux";
import {routerReducer as routing} from "react-router-redux";
import aidOnBudget from "./AidOnBudgetReducer.jsx";
import donorNotes from "./DonorNotesReducer.jsx";
import commonLists from "./CommonListsReducer.jsx"
import startUp from "./StartUpReducer.jsx";

const rootReducer = combineReducers({
    commonLists,
    aidOnBudget,
    donorNotes,
    routing,
    startUp
});

export default rootReducer;