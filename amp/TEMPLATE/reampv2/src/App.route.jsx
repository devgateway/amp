import React, { Suspense, lazy, Component } from 'react';
import { Router, Route } from "react-router-dom";
import { createHashHistory } from 'history';
import './App.css';

const SSCDashboardApp = lazy(() => import('./modules/sscdashboard'));
const AMPOfflineDownloadApp = lazy(() => import('./modules/ampoffline/Download'));

class AppRoute extends Component {
    render() {
        return (
            <Router history={createHashHistory()}>
                <Suspense fallback={<div>Loading...</div>}>
                    <Route path="/ssc" component={SSCDashboardApp}/>
                    <Route path="/ampofflinedownload" component={AMPOfflineDownloadApp}/>
                </Suspense>
            </Router>

        );
    }
}

export default AppRoute;
