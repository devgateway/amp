import React, { Component } from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';

import Main from '../components/Main';
import rootReducer from '../reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup from '../components/Startup';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const API = {
  mappingConfig: '/rest/ndd/programs-mapping-config',
  mappingSave: '/rest/ndd/programs-mapping',
  programs: '/rest/ndd/available-programs',
  programsSave: '/rest/ndd/update-source-destination-programs',
};

const TRN_PREFIX = 'amp.admin.ndd.program:';

class AdminNDDProgramApp extends Component {
  constructor(props) {
    super(props);
    this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
  }

  render() {
    return (
      <Provider store={this.store}>
        <Startup defaultTrnPack={defaultTrnPack}>
          <Main api={API} trnPrefix={TRN_PREFIX} isIndirect={false} />
        </Startup>
      </Provider>
    );
  }
}

export default AdminNDDProgramApp;
