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

function onAppInit(dispatch) {
    return (nextState, replace, callback) => {
        startUp(dispatch).then(() => callback());
    };
}

ReactDOM.render(
    <Provider store={store}>
        <Router history={history} store={store}>
            <Route path="/" component={App}/>
            <Route path="/activity/:activityId" component={App} onEnter={onAppInit(store.dispatch)}/>
        </Router>
    </Provider>,
    document.getElementById('activity-preview')
);

new boilerplate.layout({});
