import React, {Suspense, lazy, useState, Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import './App.css';
import SSCDashboard from "./modules/sscdashboard/components/SSCDashboard";
const SSCDashboardApp = lazy(() => import('./modules/sscdashboard'));
const AMPOfflineDownloadApp = lazy(() => import('./modules/ampoffline/Download'));

class AppRoute extends Component {
    render() {
        return (
            <Router>
                <Suspense fallback={<div>Loading...</div>}>
                    <Switch>
                        <Route path="/" component={SSCDashboard}/>
                        <Route path="/ampofflinedownload" component={AMPOfflineDownloadApp}/>
                    </Switch>
                </Suspense>
            </Router>

        );
    }
}

export default AppRoute;
