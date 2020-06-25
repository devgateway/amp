import React, { Component } from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux'
import rootReducer from './reducers/rootReducer';
import SSCDashboardRouter from "./components/SSCDashboard.router";
import Sidebar from './components/layout/sidebar/sidebar';
import fetchTranslations from '../../utils/actions/fetchTranslations';
import defaultTrnPack from './config/initialTranslations';
import Startup from './components/StartUp';
import { HOME_CHART, SECTORS_CHART } from './utils/constants';
import MapContainer from "./components/layout/map/map-content";
import SssDashboardHome from './SssDashboardHome';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class SSCDashboardApp extends Component {

    constructor(props) {
        super(props);
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
    }

    render() {
        return (<Provider store={this.store}>
            <Startup>
                <SssDashboardHome/>
            </Startup>
        </Provider>);
    }
}

export default SSCDashboardApp;

