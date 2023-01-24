import React, { useState } from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import rootReducer from './reducers/rootReducer';
import Startup from './components/Startup';
import defaultTrnPack from './config/initialTranslations.json';
import './index.css';
import '../../../open-sans.css';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const NDDDashboardApp = () => {
  const [store] = useState(() => createStore(rootReducer, composeEnhancer(applyMiddleware(thunk))));

  return (
    <Provider store={store}>
      <Startup defaultTrnPack={defaultTrnPack}>
        <div>Hello</div>
      </Startup>
    </Provider>
  );
};

export default NDDDashboardApp;
