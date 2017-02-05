// @flow
import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { Router, hashHistory, Route, IndexRoute } from 'react-router';
import { syncHistoryWithStore } from 'react-router-redux';
import configureStore from './store/configureStore.jsx';
import HomePage from './containers/HomePage.jsx';
import { resourceManagerStartUp } from './actions/StartUpAction.jsx';

import boilerplate from "../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";

//import './app.global.css';
import App from './containers/App.jsx';

export const store = configureStore();
const history = syncHistoryWithStore(hashHistory, store);

resourceManagerStartUp().then(() => {
    debugger;
    render(
        <Provider store={store}>
            <Router history={history} store={store}>
                <Route path="/" component={App}>
                    <IndexRoute component={HomePage} dispatch={store.dispatch}/>
                </Route>
            </Router>
        </Provider>,
        document.getElementById('root')
    );
});

new boilerplate.layout({ showLogin: false });