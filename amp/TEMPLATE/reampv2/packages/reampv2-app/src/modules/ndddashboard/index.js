import React, { Component } from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import rootReducer from './reducers/rootReducer';
import Startup from './components/StartUp';
import NDDDashboardRouter from './components/NDDDashboard.router';
import defaultTrnPack from './config/initialTranslations.json';
import './index.css';
import '../../open-sans.css';

const checkreduxDevTools = () => {
  if (typeof window !== 'undefined' && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__) {
    return window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__;
  }
  return (...args) => {
    if (args.length === 0) {
      return undefined;
    }
    if (args.some(arg => typeof arg === 'object')) {
      return compose;
    }
    return compose(...args);
  };
};

const composeEnhancer = checkreduxDevTools() || compose;

class NDDDashboardApp extends Component {
  constructor(props) {
    super(props);
    this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
  }

  render() {
    return (
      <Provider store={this.store}>
        <Startup defaultTrnPack={defaultTrnPack}>
          <NDDDashboardRouter />
        </Startup>
      </Provider>
    );
  }
}

export default NDDDashboardApp;
