import React, { Component } from 'react';
import {
  applyMiddleware, bindActionCreators, compose, createStore
} from 'redux';
import thunk from 'redux-thunk';
import { connect, Provider } from 'react-redux';

import Main from '../components/Main';
import rootReducer from '../reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup, { NDDContext } from '../components/Startup';

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
    const { translations } = this.context;
    return (
      <Provider store={this.store}>
        <Startup defaultTrnPack={defaultTrnPack} >
          <h2 className="section-title">{translations[`${TRN_PREFIX}title`]}</h2>
          <div className="section-sub-title">{translations[`${TRN_PREFIX}sub-title`]}</div>
          <Main api={API} trnPrefix={TRN_PREFIX} isIndirect />
        </Startup>
      </Provider>
    );
  }
}

AdminNDDIndirectProgramApp.contextType = NDDContext;

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(AdminNDDIndirectProgramApp);
