import React, {Component, lazy, Suspense} from 'react';
import {Route, Router} from "react-router-dom";
import {createHashHistory} from 'history';
import './App.css';

const SSCDashboardApp = lazy(() => import('./modules/sscdashboard'));
const AMPOfflineDownloadApp = lazy(() => import('./modules/ampoffline/Download'));
const GeocoderApp = lazy(() => import('./modules/geocoder'));

class AppRoute extends Component {
    render() {
        return (
            <Router history={createHashHistory()}>
                <Suspense fallback={<div>Loading...</div>}>
                    <Route path="/ssc" component={SSCDashboardApp}/>
                    <Route path="/ampofflinedownload" component={AMPOfflineDownloadApp}/>
                    <Route path="/geocoder" component={GeocoderApp}/>
                </Suspense>
            </Router>

        );
    }
}

export default AppRoute;
