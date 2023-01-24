import React, { useState } from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import rootReducer from './reducers/rootReducer';
import StartUp from './components/StartUp';
import defaultTrnPack from './config/initialTranslations.json';
import './index.css';
import '../../../open-sans.css';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const AdminIndicatorManagerApp = () => {
  const [store] = useState(() => createStore(rootReducer, composeEnhancer(applyMiddleware(thunk))));

  return (
    <Provider store={store}>
      <StartUp defaultTrnPack={defaultTrnPack}>
        <div>Hello</div>
      </StartUp>
    </Provider>
  );
};

export default AdminIndicatorManagerApp;
