import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../utils/actions/fetchTranslations';
import defaultTrnPack from '../config/initialTranslations.json';
import {Loading} from '../../../utils/components/Loading';
import {loadGeocoding} from "../actions/geocodingAction";
import fetchSettings from "../../admin/ndd/actions/fetchSettings";


export const TranslationContext = React.createContext({translations: defaultTrnPack});

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
class AppContext extends Component {
    static propTypes = {
        translationPending: PropTypes.bool,
        translations: PropTypes.object,
        settings: PropTypes.object
    };

    componentDidMount() {
        const {
            _fetchTranslations, _loadGeocoding, _fetchSettings
        } = this.props;

        _fetchTranslations(defaultTrnPack);
        _loadGeocoding();
        _fetchSettings();
    }

    render() {
        if (this.props.translationPending || this.props.geocodingPending) {
            return <Loading/>;
        }

        return (
            <TranslationContext.Provider value={{translations: this.props.translations}}>
                {this.props.children}
            </TranslationContext.Provider>);
    }
}

AppContext.propTypes = {
    _fetchTranslations: PropTypes.func.isRequired,
    _loadGeocoding: PropTypes.func.isRequired,
    _fetchSettings: PropTypes.func.isRequired
};

const mapStateToProps = state => {
    return {
        translationPending: state.translationsReducer.pending,
        translations: state.translationsReducer.translations,
        geocoding: state.geocodingReducer.geocoding,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    _fetchTranslations: fetchTranslations,
    _loadGeocoding: loadGeocoding,
    _fetchSettings: fetchSettings
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(AppContext);
