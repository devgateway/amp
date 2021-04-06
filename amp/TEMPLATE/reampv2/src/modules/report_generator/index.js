import React, { Component } from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import rootReducer from './reducers/rootReducer';
import Startup from './components/StartUp';
import ReportGeneratorRouter from './components/ReportGenerator.router';
import defaultTrnPack from './config/initialTranslations.json';
import 'fomantic-ui-css/semantic.css';
import '../ndddashboard/index.css';
import '../ndddashboard/open-sans.css';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class ReportGeneratorApp extends Component {
  constructor(props) {
    super(props);
    this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
  }

  render() {
    return (
      <Provider store={this.store}>
        <Startup defaultTrnPack={defaultTrnPack}>
          <ReportGeneratorRouter />
        </Startup>
      </Provider>
    );
  }
}

export default ReportGeneratorApp;
