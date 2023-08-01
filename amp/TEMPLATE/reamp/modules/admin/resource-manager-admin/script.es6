// @flow
import React from 'react';
import {render} from 'react-dom';
import {Provider} from 'react-redux';
import {hashHistory, IndexRoute, Route, Router} from 'react-router';
import {syncHistoryWithStore} from 'react-router-redux';
import configureStore from './store/configureStore.jsx';
import HomePage from './containers/HomePage.jsx';
import {resourceManagerStartUp} from './actions/StartUpAction.jsx';
import AppPage from './containers/AppPage.jsx';
import boilerplate from "../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";

export const store = configureStore();
const history = syncHistoryWithStore(hashHistory, store);

resourceManagerStartUp(store).then(() => {
    render(
        <Provider store={store}>
            <Router history={history} store={store}>
                <Route path="/" component={AppPage}>
                    <IndexRoute component={HomePage} dispatch={store.dispatch}/>
                </Route>
            </Router>
        </Provider>,
        document.getElementById('root')
    );
});

new boilerplate.layout({ showLogin: false });