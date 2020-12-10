import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../../utils/actions/fetchTranslations';
import {Loading} from '../../../../utils/components/Loading';

export const NDDContext = React.createContext();

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
class Startup extends Component {
    static propTypes = {
        translationPending: PropTypes.bool,
        translations: PropTypes.object,
        api: PropTypes.object
    };

    componentDidMount() {
        this.props.fetchTranslations(this.props.defaultTrnPack);
    }

    render() {
        return this.props.translationPending
            ? (<Loading/>) :
            <NDDContext.Provider value={{translations: this.props.translations, api: this.props.api}}>
                {this.props.children}
            </NDDContext.Provider>;
    }
}

const mapStateToProps = state => {
    return {
        translationPending: state.translationsReducer.pending,
        translations: state.translationsReducer.translations
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({fetchTranslations: fetchTranslations}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);
