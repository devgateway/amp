import React, { Component } from 'react';
import { applyMiddleware, compose, createStore } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import { Container, Col, Row } from 'react-bootstrap';
import  rootReducer from './reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup from './components/Startup';
import SectorMappingAdminNavigator from './components/SectorMappingAdminNavigator';
// eslint-disable-next-line no-unused-vars
import style from './components/css/style.css';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class AdminSectorMappingApp extends Component {
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
                                <SectorMappingAdminNavigator />
                            </Col>
                        </Row>
                    </Container>
                </Startup>
            </Provider>
        );
    }
}

export default AdminSectorMappingApp;
