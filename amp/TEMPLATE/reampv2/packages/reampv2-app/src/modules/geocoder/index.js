import React, {Component} from 'react';
import {applyMiddleware, compose, createStore} from 'redux';
import thunk from 'redux-thunk';
import {Provider} from 'react-redux'

import rootReducer from './reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations';
import AppContext from './components/AppContext';
import GeocoderPanel from "./components/layout/panel/GeocoderPanel";
import './index.css';
import '../../open-sans.css';


const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class GeocoderAPP extends Component {
    constructor(props) {
        super(props)
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));

    }

    render() {
        return (<Provider store={this.store}>
            <AppContext defaultTrnPack = {defaultTrnPack}>
                <div className='container'>
                    <GeocoderPanel />
                </div>
            </AppContext>
        </Provider>);

    }
}

export default GeocoderAPP;