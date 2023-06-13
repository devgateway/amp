// eslint-disable
import React from 'react';
import { createStore, applyMiddleware, compose, Store } from '@reduxjs/toolkit';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import rootReducer from './reducers/rootReducer';
// eslint-disable-next-line import/no-unresolved
import StartUp from './components/StartUp';
import defaultTrnPack from './config/initialTranslations.json';
import './index.css';
import '../../open-sans.css';

const checkreduxDevTools = () => {
  if (typeof window !== 'undefined' && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__) {
    return window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__;
  }
  return (...args: any[]) => {
    if (args.length === 0) {
      return undefined;
    }
    if (args.some(arg => typeof arg === 'object')) {
      return compose;
    }
    return compose(...args);
  };
};

const composeEnhancer = checkreduxDevTools();
// redux store
// eslint-disable-next-line



const MeDashboard = () => {
  const store: Store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));

  return (
    <Provider store={store}>
      <StartUp defaultTrnPack={defaultTrnPack}>
        <div>M&e dashboard</div>
      </StartUp>
    </Provider>
  );
};

export default MeDashboard;
