import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    applyMiddleware, bindActionCreators, compose, createStore
} from 'redux';
import thunk from 'redux-thunk';
import { connect, Provider } from 'react-redux';

import Main from '../components/Main';
import rootReducer from '../reducers/rootReducer';
import defaultTrnPack from './config/initialTranslations.json';
import Startup, {SectorMappingContext} from '../components/Startup';

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const API = {
    // mappingConfig: '/rest/ndd/indirect-programs-mapping-config',
    // mappingSave: '/rest/ndd/indirect-programs-mapping',
    // programs: '/rest/ndd/available-indirect-programs',
    // programsSave: '/rest/ndd/update-source-destination-indirect-programs',

    mappingConfig: '/rest/sectors-mapping/sectors-mapping-config',
    allMappings: '/rest/sectors-mapping/all-mappings',
    allSchemes: '/rest/sectors-mapping/all-schemes',
    sectorsClassified: '/rest/sectors-mapping/sectors-classified/',
    sectorsByScheme: '/rest/sectors-mapping/sectors-by-scheme/',
    baseEndpoint: '/rest/sectors-mapping' // Create (POST) and Delete (DELETE) endpoints
};

const TRN_PREFIX = 'amp.admin.sectorMapping:';

class AdminSectorMappingApp extends Component {
    constructor(props) {
        super(props);
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
    }

    render() {
        // const { translations } = this.context;
        // const { selected } = this.props;
        // return (
        //     selected
        //         ? (
        //             <Provider store={this.store}>
        //                 <Startup defaultTrnPack={defaultTrnPack}>
        //                     <h1>Admin Sector Mapping (Index 2°)</h1>
        //                     <h2 className="section-title">{translations[`${TRN_PREFIX}title`]}</h2>
        //                     <div className="section-sub-title">{translations[`${TRN_PREFIX}sub-title`]}</div>
        //                     {/*<Main api={API} trnPrefix={TRN_PREFIX} isIndirect />*/}
        //                 </Startup>
        //             </Provider>
        //         ) : null);

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
    selected: PropTypes.bool.isRequired
};

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(AdminSectorMappingApp);
