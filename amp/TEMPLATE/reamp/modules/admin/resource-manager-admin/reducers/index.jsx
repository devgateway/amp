// @flow
import { combineReducers } from 'redux';
import { routerReducer as routing } from 'react-router-redux';
import startUp from './StartUpReducer.jsx';
import homePage from './HomePageReducer.jsx';
import typeList from './TypeListReducer.jsx';
const rootReducer = combineReducers({
    homePage,
    startUp,
    typeList,
    routing,
});

export default rootReducer;
