import React from 'react';
import {Provider} from 'react-redux';
import {hashHistory, Route, Router} from 'react-router';
import {syncHistoryWithStore} from 'react-router-redux';
import ReactDOM from "react-dom";
import configureStore from './store/configureStore';
import App from "./containers/App";
import {startUp} from './actions/StartUpAction';
import boilerplate from "../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";

export const store = configureStore();
const history = syncHistoryWithStore(hashHistory, store);

function hashLinkScroll() {
    debugger;
    const {hash} = window.location;
    if (hash !== '') {
        // Push onto callback queue so it runs after the DOM is updated,
        // this is required when navigating from a different page so that
        // the element is rendered on the page before trying to getElementById.
        setTimeout(() => {
            const id = hash.replace('#/#', '');
            const element = document.getElementById(id);
            if (element) element.scrollIntoView();
        }, 0);
    }
}

function onAppInit(dispatch) {
    return (nextState, replace, callback) => {
        startUp(dispatch).then(() => callback());
    };
}

ReactDOM.render(
    <Provider store={store}>
        <Router history={history} store={store} onUpdate={hashLinkScroll}>
            <Route path="/" component={App}/>
            <Route path="/activity/:activityId" component={App} onEnter={onAppInit(store.dispatch)}/>
        </Router>
    </Provider>,
    document.getElementById('activity-preview')
);

new boilerplate.layout({});
