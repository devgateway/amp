import React, { Suspense, lazy } from 'react';
import { Router, Route } from 'react-router-dom';
import { createHashHistory } from 'history';
import './App.css';

const SSCDashboardApp = lazy(() => import('./modules/sscdashboard'));
const AMPOfflineDownloadApp = lazy(() => import('./modules/ampoffline/Download'));
const AdminNDDApp = lazy(() => import('./modules/admin/ndd'));
const AdminIndicatorManagerApp = lazy(() => import('./modules/admin/indicator_manager'));
const NDDDashboardApp = lazy(() => import('./modules/ndddashboard'));
const ReportGeneratorApp = lazy(() => import('./modules/report_generator'));
const GeocoderApp = lazy(() => import('./modules/geocoder'));

const AppRoute = () => (
  <Router history={createHashHistory()}>
    <Suspense fallback={<div />}>
      <Route path="/ssc" component={SSCDashboardApp} />
      <Route path="/ampofflinedownload" component={AMPOfflineDownloadApp} />
      <Route path="/ndd" component={AdminNDDApp} />
      <Route path="/ndddashboard" component={NDDDashboardApp} />
      <Route path="/report_generator" component={ReportGeneratorApp} />
      <Route path="/geocoder" component={GeocoderApp} />
      <Route path="/indicatormanager" component={AdminIndicatorManagerApp} />
    </Suspense>
  </Router>
);

export default AppRoute;
