import React, {Component} from 'react';
import {applyMiddleware, bindActionCreators, compose, createStore} from 'redux';
import thunk from 'redux-thunk';
import {connect, Provider} from 'react-redux';

import PropTypes from 'prop-types';
import Main from '../components/Main';
import rootReducer from '../reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup, {NDDContext} from '../components/Startup';

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
    const { translations } = this.context;
    const { selected } = this.props;
    return (
      selected ? (
        <Provider store={this.store}>
          <Startup defaultTrnPack={defaultTrnPack}>
            <h2 className="section-title">{translations[`${TRN_PREFIX}title`]}</h2>
            <div className="section-sub-title">{translations[`${TRN_PREFIX}sub-title`]}</div>
            <Main api={API} trnPrefix={TRN_PREFIX} isIndirect={false} />
          </Startup>
        </Provider>
      ) : null
    );
  }
}

AdminNDDProgramApp.contextType = NDDContext;

AdminNDDProgramApp.propTypes = {
  selected: PropTypes.bool.isRequired
};
const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(AdminNDDProgramApp);
