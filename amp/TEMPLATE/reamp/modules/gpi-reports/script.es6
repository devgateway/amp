import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {hashHistory, Route, Router} from 'react-router';
import {syncHistoryWithStore} from 'react-router-redux';
import configureStore from './store/configureStore.jsx';
import App from "./containers/App.jsx";
//import boilerplate from "../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";
import {startUp} from './actions/StartUpAction.jsx';

export const store = configureStore();
const history = syncHistoryWithStore(hashHistory, store);


startUp(store).then(() => {

ReactDOM.render(
  
  <Provider store={store}>
            <Router history={history} store={store}>
                <Route path="/" component={App}>                   
                </Route>
            </Router>
        </Provider>,
   document.getElementById('gpi-reports')
 );

});


new boilerplate({showLogin: false});
