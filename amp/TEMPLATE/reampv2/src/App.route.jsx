import React, { Suspense, lazy, Component } from 'react';
import { Router, Route } from 'react-router-dom';
import { createHashHistory } from 'history';
import './App.css';

const SSCDashboardApp = lazy(() => import('./modules/sscdashboard'));
const AMPOfflineDownloadApp = lazy(() => import('./modules/ampoffline/Download'));
const AdminNDDApp = lazy(() => import('./modules/admin/ndd'));
const NDDDashboardApp = lazy(() => import('./modules/ndddashboard'));
const GeocoderApp = lazy(() => import('./modules/geocoder'));

class AppRoute extends Component {
  render() {
    return (
      <Router history={createHashHistory()}>
        <Suspense fallback={<div />}>
          <Route path="/ssc" component={SSCDashboardApp} />
          <Route path="/ampofflinedownload" component={AMPOfflineDownloadApp} />
          <Route path="/ndd" component={AdminNDDApp} />
          <Route path="/ndddashboard" component={NDDDashboardApp} />
          <Route path="/geocoder" component={GeocoderApp}/>
        </Suspense>
      </Router>
    );
  }
}

export default AppRoute;
