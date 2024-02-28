import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { applyMiddleware, bindActionCreators, compose, createStore } from 'redux';
import thunk from 'redux-thunk';
import { connect, Provider } from 'react-redux';
import Main from '../components/Main';
import rootReducer from '../reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup, {SectorMappingContext} from '../components/Startup';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const API = {
    mappingConfig: '/rest/sector-mappings/sector-mappings-config',
    allMappings: '/rest/sector-mappings/all-mappings',
    allSchemes: '/rest/sector-mappings/all-schemes',
    sectorsClassified: '/rest/sector-mappings/sectors-classified/', // TODO: check if this endpoint is still used
    sectorsByScheme: '/rest/sector-mappings/sectors-by-scheme/',
    baseEndpoint: '/rest/sector-mappings' // Create (POST) and Delete (DELETE) endpoints
};

const TRN_PREFIX = 'amp.admin.sectorMapping:';

class AdminSectorMappingApp extends Component {
    constructor(props) {
        super(props);
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
    }

    render() {
        const { translations } = this.context;
        return (
            <Provider store={this.store}>
                <Startup defaultTrnPack={defaultTrnPack}>
                    <h2 className="section-title">{translations[`${TRN_PREFIX}title`]}</h2>
                    <div className="section-sub-title">{translations[`${TRN_PREFIX}sub-title`]}</div>
                    <Main api={API} trnPrefix={TRN_PREFIX} />
                </Startup>
            </Provider>
            );
    }
}

AdminSectorMappingApp.contextType = SectorMappingContext;

AdminSectorMappingApp.propTypes = {
};

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(AdminSectorMappingApp);
