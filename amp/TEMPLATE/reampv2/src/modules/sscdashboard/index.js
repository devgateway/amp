import React, {Component} from 'react';
import {createStore, applyMiddleware, compose} from 'redux';
import thunk from 'redux-thunk';
import rootReducer from './reducers/rootReducer';
import {Provider} from 'react-redux'
import {Link} from "react-router-dom";
import SSCDashboardRouter from "./components/SSCDashboard.router";
import Sidebar from "./components/layout/sidebar/sidebar";
import MapContainer from "./components/layout/map/map-content";

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class SSCDashboardApp extends Component {

    constructor(props) {
        super(props)
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
        this.state = {
            currentTab: ''
        };
    }
    render() {
        const listGroupItem = 'list-group-item';
        const active = 'active';
        const modules = [];
        // modules need to be loaded dynamically
        modules.push({name: 'Map', link: '/sscdashboard/home'});
        modules.push({name: 'Sectors', link: '/sscdashboard/sectors'});
        modules.push({name: 'MapWithFigures', link: '/sscdashboard/figures'});
        const navItem = 'nav-item';
        const classActive = 'active';
        const styleMargin = {'marginLeft': '0px'};
        const classHeight = {height: '700px'};
        return (<Provider store={this.store}>
            return (
            <div className="container-fluid content-wrapper">
                <div className="row">
                    <Sidebar/>
                    <SSCDashboardRouter/>
                </div>
            </div>);
        </Provider>);
    }
}
export default SSCDashboardApp;

