import React, {Component} from 'react';
import {createStore, applyMiddleware, compose} from 'redux';
import thunk from 'redux-thunk';
import {Provider} from 'react-redux'

import AMPOfflineDownload from "./components/AMPOfflineDownload";
import rootReducer from './reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations';
import fetchTranslations from './actions/fetchTranslations';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class AMPOfflineDownloadAPP extends Component {
    constructor(props) {
        super(props)
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));

    }

    componentDidMount() {
        this.store.dispatch(fetchTranslations({defaultTrnPack}));
    }

    render() {
        return (<Provider store={this.store}>
            <AMPOfflineDownload/>
        </Provider>);

    }
}

export default AMPOfflineDownloadAPP;
