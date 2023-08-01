import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../utils/actions/fetchTranslations';
import {loadActivities} from '../actions/callReports';
import loadAmpSettings from '../actions/loadAmpSettings';
import defaultTrnPack from '../config/initialTranslations.json';
import {Loading} from '../../../utils/components/Loading';

export const SSCTranslationContext = React.createContext({ translations: defaultTrnPack });

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
class Startup extends Component {
  componentDidMount() {
    const { fetchTranslations, loadAmpSettings, loadActivities } = this.props;
    fetchTranslations(defaultTrnPack);
    loadAmpSettings();
    loadActivities();
  }

  render() {
    const { translationPending, children } = this.props;
    if (translationPending) {
      return (<Loading />);
    } else {
      const { translations } = this.props;
      document.title = translations['amp.ssc.dashboard:page-title'];
      return (
        <SSCTranslationContext.Provider value={{ translations }}>
          {children}
        </SSCTranslationContext.Provider>
      );
    }
  }
}

const mapStateToProps = state => ({
  translationPending: state.translationsReducer.pending,
  translations: state.translationsReducer.translations
});

Startup.propTypes = {
  translationPending: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  children: PropTypes.object.isRequired,
  fetchTranslations: PropTypes.func.isRequired,
  loadAmpSettings: PropTypes.func.isRequired,
  loadActivities: PropTypes.func.isRequired
};

const mapDispatchToProps = dispatch => bindActionCreators({
  fetchTranslations,
  loadAmpSettings,
  loadActivities
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);
