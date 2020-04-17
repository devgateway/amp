import React, {Suspense, lazy, useState} from 'react';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import './App.css';
const SSCDashboardApp = lazy(() => import('./modules/sscdashboard'));

const AMPOfflineDownloadApp = lazy(() => import('./modules/ampoffline/Download'));

function App() {
    const [currentTab, setCurrentTab] = useState('dashboard');
    return (
        <Router>
            <Suspense fallback={<div>Loading...</div>}>
                <Switch>
                    <Route exact path="/" component={SSCDashboardApp}/>
                    <Route path="/ampofflinedownload" component={AMPOfflineDownloadApp}/>
                </Switch>
            </Suspense>
        </Router>
    );
}

export default App;
