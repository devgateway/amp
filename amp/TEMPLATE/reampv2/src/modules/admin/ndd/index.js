import React, {Component} from 'react';
import {applyMiddleware, compose, createStore} from 'redux';
import thunk from 'redux-thunk';
import {Provider} from 'react-redux';

import rootReducer from './reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup from './components/Startup';
import NDDAdminRouter from "./components/NDDAdminRouter";
import NDDAdminNavigator from "./components/NDDAdminNavigator";

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class AdminNDDApp extends Component {
  constructor(props) {
    super(props);
    this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
  }

  render() {
    return (
      <Provider store={this.store}>
        <Startup defaultTrnPack={defaultTrnPack} >
          <NDDAdminNavigator/>
          <NDDAdminRouter />
        </Startup>
      </Provider>
    );
  }
}

export default AdminNDDApp;
