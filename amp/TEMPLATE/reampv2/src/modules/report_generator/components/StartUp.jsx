import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../utils/actions/fetchTranslations';
import { Loading } from '../../../utils/components/Loading';
import defaultTrnPack from '../config/initialTranslations.json';

export const ReportGeneratorContext = React.createContext({ translations: defaultTrnPack });

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
class Startup extends Component {
  componentDidMount() {
    const { _fetchTranslations } = this.props;
    _fetchTranslations(defaultTrnPack);
  }

  render() {
    const { translationPending, children } = this.props;
    if (translationPending) {
      return (<Loading />);
    } else {
      const { translations } = this.props;
      document.title = translations['amp.reportGenerator:page-title'];
      return (
        <ReportGeneratorContext.Provider value={{ translations }}>
          {children}
        </ReportGeneratorContext.Provider>
      );
    }
  }
}

const mapStateToProps = state => ({
  translationPending: state.translationsReducer.pending,
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _fetchTranslations: fetchTranslations
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);

Startup.propTypes = {
  children: PropTypes.object.isRequired,
  translationPending: PropTypes.bool,
  translations: PropTypes.object,
  _fetchTranslations: PropTypes.func.isRequired
};

Startup.defaultProps = {
  translationPending: false,
  translations: undefined
};
