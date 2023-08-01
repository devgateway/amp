import React from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import AMPOfflineDownload from './components/AMPOfflineDownload';
import rootReducer from './reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup from './components/Startup';



const AMPOfflineDownloadApp = () => {
  const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
  const store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
  return (
      <Provider store={store}>
        <Startup defaultTrnPack={defaultTrnPack}>
          <AMPOfflineDownload />
        </Startup>
      </Provider>
  );
}

export default AMPOfflineDownloadApp;
