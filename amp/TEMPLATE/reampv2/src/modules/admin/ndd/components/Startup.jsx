import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../../utils/actions/fetchTranslations';
import defaultTrnPack from '../config/initialTranslations.json';
import { Loading } from '../../../../utils/components/Loading';

export const NDDContext = React.createContext({ translations: defaultTrnPack });

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
    const { children, translations, translationPending } = this.props;
    if (translationPending) {
      return (<Loading />);
    } else {
      document.title = translations['amp.admin.ndd:page-title'];
      return (
        <NDDContext.Provider value={{ translations }}>
          {children}
        </NDDContext.Provider>
      );
    }
  }
}

const mapStateToProps = state => ({
  translationPending: state.translationsReducer.pending,
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({ fetchTranslations }, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);

Startup.propTypes = {
  translationPending: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  children: PropTypes.object.isRequired,
  fetchTranslations: PropTypes.func.isRequired
};
