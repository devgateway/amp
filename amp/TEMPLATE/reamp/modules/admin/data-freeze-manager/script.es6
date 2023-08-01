import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {hashHistory, Route, Router} from 'react-router';
import {syncHistoryWithStore} from 'react-router-redux';
import configureStore from './store/configureStore';
import App from "./containers/App.jsx";
import {gpiStartUp} from './actions/StartUpAction';

export const store = configureStore();
const history = syncHistoryWithStore(hashHistory, store);

gpiStartUp(store).then(() => {

ReactDOM.render(  
  <Provider store={store}>
            <Router history={history} store={store}>
                <Route path="/" component={App}>                   
                </Route>
            </Router>
        </Provider>,
   document.getElementById('data-freezing')
 );

});
new boilerplate({showLogin: false});
