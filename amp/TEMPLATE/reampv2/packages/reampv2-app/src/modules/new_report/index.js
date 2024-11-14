import React, {Component} from "react";
import ReportData from "./ReportData";
import {applyMiddleware, compose, createStore} from "redux";
import rootReducer from "../report_generator/reducers/rootReducer";
import thunk from "redux-thunk";
import {Provider} from "react-redux";
const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class NewReportApp extends Component {
    constructor(props) {
        super(props);
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
    }

    render() {
        return (
            <Provider store={this.store}>

            <ReportData />
            </Provider>
        );
    }
}
export default NewReportApp;
