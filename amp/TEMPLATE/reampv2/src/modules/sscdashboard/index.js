import React, { Component } from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux'
import rootReducer from './reducers/rootReducer';
import Startup from './components/StartUp';
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

