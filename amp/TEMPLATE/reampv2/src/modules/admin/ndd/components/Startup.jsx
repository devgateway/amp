import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../../utils/actions/fetchTranslations';
import { Loading } from '../../../../utils/components/Loading';

export const NDDContext = React.createContext();

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
class Startup extends Component {
  componentDidMount() {
    const { defaultTrnPack, _fetchTranslations } = this.props;
    _fetchTranslations(defaultTrnPack);
  }

  render() {
    const {
      children, translations, translationPending, api
    } = this.props;
    if (translationPending) {
      return (<Loading />);
    } else {
      document.title = translations['amp.admin.ndd:page-title'];
      return (
        <NDDContext.Provider value={{ translations, api }}>
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

const mapDispatchToProps = dispatch => bindActionCreators({ _fetchTranslations: fetchTranslations }, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);

Startup.propTypes = {
  translationPending: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  children: PropTypes.object.isRequired,
  _fetchTranslations: PropTypes.func.isRequired,
  api: PropTypes.object.isRequired,
  defaultTrnPack: PropTypes.object.isRequired,
};
