import React from 'react';
import { createStore, applyMiddleware, compose } from '@reduxjs/toolkit';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import rootReducer from './reducers/rootReducer';
// eslint-disable-next-line import/no-unresolved
import StartUp from './components/StartUp';
import defaultTrnPack from './config/initialTranslations.json';
import './index.css';
import '../../../open-sans.css';
import InidcatorTable from './components/table/IndicatorTable.tsx';

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

const composeEnhancer = checkreduxDevTools();

const AdminIndicatorManagerApp = () => {
 const store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));

  return (
    <Provider store={store}>
      <StartUp defaultTrnPack={defaultTrnPack}>
        <InidcatorTable />
      </StartUp>
    </Provider>
  );
};

export default AdminIndicatorManagerApp;
