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

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class SSCDashboardApp extends Component {

    constructor(props) {
        super(props);
        this.state = {chartSelected: HOME_CHART};
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
    }

    onChangeChartSelected(chartSelected) {
        this.setState({chartSelected});
    }

    render() {
        return (<Provider store={this.store}>
            <Startup>
                <div className="container-fluid content-wrapper">
                    <div className="row">
                        <Sidebar chartSelected={this.state.chartSelected}
                                 onChangeChartSelected={this.onChangeChartSelected.bind(this)}/>
                        <MapContainer chartSelected={this.state.chartSelected}/>
                    </div>
                </div>
            </Startup>
        </Provider>);
    }
}

export default SSCDashboardApp;

