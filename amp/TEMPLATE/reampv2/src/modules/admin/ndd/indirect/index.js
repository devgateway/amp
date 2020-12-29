import React, { Component } from 'react';
import { applyMiddleware, compose, createStore } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';

import Nav from 'react-bootstrap/Nav';
import Main from '../components/Main';
import rootReducer from '../reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup from '../components/Startup';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const API = {
  mappingConfig: '/rest/ndd/indirect-programs-mapping-config',
  mappingSave: '/rest/ndd/indirect-programs-mapping',
  programs: '/rest/ndd/available-indirect-programs',
  programsSave: '/rest/ndd/update-source-destination-indirect-programs',
};

const TRN_PREFIX = 'amp.admin.ndd.indirect:';

class AdminNDDIndirectProgramApp extends Component {
  constructor(props) {
    super(props);
    this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
  }

  render() {
    return (
      <Provider store={this.store}>
        <Startup defaultTrnPack={defaultTrnPack} >
          <Main api={API} trnPrefix={TRN_PREFIX} isIndirect />
        </Startup>
      </Provider>
    );
  }
}

export default AdminNDDIndirectProgramApp;
