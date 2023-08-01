import React, {Component} from 'react';
import {applyMiddleware, compose, createStore} from 'redux';
import thunk from 'redux-thunk';
import {Provider} from 'react-redux';
import {Col, Container, Row} from 'react-bootstrap';
import rootReducer from './reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup from './components/Startup';
import NDDAdminNavigator from './components/NDDAdminNavigator';
// eslint-disable-next-line no-unused-vars

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class AdminNDDApp extends Component {
  constructor(props) {
    super(props);
    this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
  }

  render() {
    return (
      <Provider store={this.store}>
        <Startup defaultTrnPack={defaultTrnPack} >
          <Container>
            <Row>
              <Col md={12}>
                <NDDAdminNavigator />
              </Col>
            </Row>
          </Container>
        </Startup>
      </Provider>
    );
  }
}

export default AdminNDDApp;
