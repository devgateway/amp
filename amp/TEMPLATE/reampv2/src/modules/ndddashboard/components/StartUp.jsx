import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../utils/actions/fetchTranslations';
import { Loading } from '../../../utils/components/Loading';
import defaultTrnPack from '../config/initialTranslations';

export const NDDTranslationContext = React.createContext({ translations: defaultTrnPack });

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
class Startup extends Component {
    static propTypes = {
      translationPending: PropTypes.bool,
      translations: PropTypes.object
    };

    componentDidMount() {
      this.props.fetchTranslations(defaultTrnPack);
    }

    render() {
      if (this.props.translationPending) {
        return (<Loading />);
      } else {
        const { translations } = this.props;
        document.title = translations['amp.ndd.dashboard:page-title'];
        return (
          <NDDTranslationContext.Provider value={{ translations }}>
            {this.props.children}
          </NDDTranslationContext.Provider>
        );
      }
    }
}

const mapStateToProps = state => ({
  translationPending: state.translationsReducer.pending,
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({
  fetchTranslations
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);
