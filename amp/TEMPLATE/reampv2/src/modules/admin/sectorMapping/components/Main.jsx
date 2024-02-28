import React, { Component } from 'react';
import './css/style.css';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { SectorMappingContext } from './Startup';
import {
    getSectorMappings,
    getSectorMappingError,
    getSectorMappingPending,
    getSchemes,
    getSchemesPending
} from '../reducers/startupReducer';
import fetchSectorMappings from "../actions/fetchSectorMappings";
import fetchSchemes from "../actions/fetchSchemes";
import fetchLayout from "../actions/fetchLayout";
import fetchSettings from "../actions/fetchSettings";
import BlockUI from "./common/BlockUI";
import FormSectors from "./FormSectors";

class Main extends Component {
    constructor(props) {
        super(props);
        this.shouldComponentRender = this.shouldComponentRender.bind(this);
        this.state = {};
    }

    componentDidMount() {
        const { _fetchSectorMappings, _fetchSchemes, api, _fetchLayout, _fetchSettings } = this.props;

        _fetchSettings().then(settings => {
            _fetchLayout().then(layout => {
                if (layout && layout.logged && layout.administratorMode === true) {
                    _fetchSectorMappings(api.mappingConfig);
                    _fetchSchemes(api.allSchemes);
                } else {
                    window.location.replace('/login.do');
                }
            }).catch(e => console.error(e));
        }).catch(e => console.error(e));
    }

    shouldComponentRender() {
        const { pendingSectorMapping, pendingSectors } = this.props;
        return !pendingSectorMapping && !pendingSectors;
    }

    render() {
        const { mappings, schemes, api, trnPrefix } = this.props;
        const { translations } = this.context;
        const { isSuperAdmin, settings } = this.state;

        if (!this.shouldComponentRender() || mappings.length === 0) {
            return <div className="loading">{translations[`${trnPrefix}loading`]}</div>;
        } else {
            return (
                <div className="sector-map-container">
                    <SectorMappingContext.Provider value={{
                        mappings, translations, schemes, api, trnPrefix, isSuperAdmin, settings
                    }}>
                        <div className="col-md-12">
                            <div>
                                <FormSectors />
                            </div>
                        </div>
                    </SectorMappingContext.Provider>
                </div>
            );
        }

    }
}

Main.contextType = SectorMappingContext;

Main.propTypes = {
    _fetchSectorMappings: PropTypes.func.isRequired,
    _fetchSchemes: PropTypes.func.isRequired,
    _fetchLayout: PropTypes.func.isRequired,
    _fetchSettings: PropTypes.func.isRequired,
    api: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
    error: getSectorMappingError(state.startupReducer),
    mappings: getSectorMappings(state.startupReducer),
    schemes: getSchemes(state.startupReducer),
    pendingMappings: getSectorMappingPending(state.startupReducer),
    pendingSchemes: getSchemesPending(state.startupReducer),
    translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({
    _fetchSectorMappings: fetchSectorMappings,
    _fetchSchemes: fetchSchemes,
    _fetchLayout: fetchLayout,
    _fetchSettings: fetchSettings
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Main);
