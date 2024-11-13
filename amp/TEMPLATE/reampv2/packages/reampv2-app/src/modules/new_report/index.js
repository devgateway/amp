import React, {Component} from "react";
import {applyMiddleware, createStore} from "redux";
import rootReducer from "../ndddashboard/reducers/rootReducer";
import thunk from "redux-thunk";
import {Provider} from "react-redux";
import Startup from "../ndddashboard/components/StartUp";
import defaultTrnPack from "../ndddashboard/config/initialTranslations.json";
import NDDDashboardRouter from "../ndddashboard/components/NDDDashboard.router";
import ReportData from "./ReportData";

class NewReportApp extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
             <ReportData />
        );
    }
}
export default NewReportApp;
