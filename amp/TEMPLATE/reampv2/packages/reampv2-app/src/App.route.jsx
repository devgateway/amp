import React, {Suspense, lazy, useEffect} from 'react';
import {Route, useLocation, useHistory, Router} from 'react-router-dom';
import {createHashHistory} from 'history';
import './App.css';

const SSCDashboardApp = lazy(() => import('./modules/sscdashboard'));
const AMPOfflineDownloadApp = lazy(() => import('./modules/ampoffline/Download'));
const AdminNDDApp = lazy(() => import('./modules/admin/ndd'));
const NDDDashboardApp = lazy(() => import('./modules/ndddashboard'));
const ReportGeneratorApp = lazy(() => import('./modules/report_generator'));
const GeocoderApp = lazy(() => import('./modules/geocoder'));

const AppRoute = () => {
    // eslint-disable-next-line no-restricted-globals
    const history = createHashHistory();
    const location = history.location;

    useEffect(() => {
        function containerNavigationHandler(event) {
            const pathname = event.detail;
            if (location.pathname === pathname) {
                return;
            }
            history.push(pathname);
        }

        window.addEventListener('[container] navigated', containerNavigationHandler);
        // eslint-disable-next-line
    }, [location]);

    useEffect(() => {
        console.log("location", location);
        window.dispatchEvent(
            new CustomEvent("[reampv2] navigated", {detail: location.pathname})
        );
    }, [location]);

    return (
        <Router history={history}>
            <Suspense fallback={<div/>}>
                <Route path="/ssc" component={SSCDashboardApp}/>
                <Route path="/ampofflinedownload" component={AMPOfflineDownloadApp}/>
                <Route path="/ndd" component={AdminNDDApp}/>
                <Route path="/ndddashboard" component={NDDDashboardApp}/>
                <Route path="/report_generator" component={ReportGeneratorApp}/>
                <Route path="/geocoder" component={GeocoderApp}/>
            </Suspense>
        </Router>
    );
}

export default AppRoute;
