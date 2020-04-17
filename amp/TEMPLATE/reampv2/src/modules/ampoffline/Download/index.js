import React, {Component} from 'react';
import {createStore, applyMiddleware, compose} from 'redux';
import thunk from 'redux-thunk';
import AMPOfflineDownload from "./components/AMPOfflineDownload";
import rootReducer from './reducers/rootReducer';
import {Provider} from 'react-redux'

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class AMPOfflineDownloadAPP extends Component {

    constructor(props) {
        super(props)
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));

    }

    render() {
        return (<Provider store={this.store}>
            <AMPOfflineDownload/>
        </Provider>);

    }
}

export default AMPOfflineDownloadAPP;
