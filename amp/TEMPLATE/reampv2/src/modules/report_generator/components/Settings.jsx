import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
// eslint-disable-next-line no-unused-vars
import styles from '../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings.css';
import { ReportGeneratorContext } from './StartUp';
import {
  // eslint-disable-next-line no-unused-vars
  TABS, TRN_PREFIX, URL_SETTINGS_TABS, URL_SETTINGS_REPORTS, REPORTS, PROFILE_TAB
} from '../utils/constants';
import { fetchGlobalSettings } from '../actions/settingsActions';
import { extractSettings } from '../reducers/utils/settingsDataConverter';

const SettingsWidget = require('../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings');

let widget = null;

class Settings extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false
    };
  }

  componentDidMount() {
    const { settings, profile, _fetchGlobalSettings } = this.props;
    widget = new SettingsWidget.SettingsWidget({
      el: 'settings-popup',
      draggable: true,
      caller: profile === PROFILE_TAB ? TABS : REPORTS,
      isPopup: true,
      definitionUrl: profile === PROFILE_TAB ? URL_SETTINGS_TABS : URL_SETTINGS_REPORTS
    });
    if (settings === null) {
      _fetchGlobalSettings().then((action) => {
        const gs = extractSettings(action.payload);
        return widget.restoreFromSaved(gs);
      });
    } else {
      widget.restoreFromSaved(settings);
    }
    // eslint-disable-next-line react/no-string-refs
    widget.setElement(this.refs.settingsPopup);
    widget.on('applySettings', this.applySettings);
    widget.on('close', this.hideSettings);
  }

  componentWillUnmount() {
    window.removeEventListener('applySettings', this.applySettings);
    window.removeEventListener('close', this.hideSettings);
  }

  applySettings = (data) => {
    const { onApplySettings } = this.props;
    onApplySettings(data);
    this.hideSettings();
  }

  hideSettings = () => {
    this.setState({ show: false });
  }

  toggleSettings = () => {
    const { show } = this.state;
    widget.show();
    this.setState({ show: !show });
  }

  render() {
    const { show } = this.state;
    const { translations } = this.props;
    return (
      <div className="filter-title settings-title">
        <span className="filter-title" onClick={this.toggleSettings}>{translations[`${TRN_PREFIX}settings`]}</span>
        <div
          id="settings-popup"
          ref="settingsPopup"
          style={{
            display: (!show ? 'none' : 'block'),
            padding: '0px',
            borderColor: '#337ab7'
          }} />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  profile: state.uiReducer.profile,
  settings: state.uiReducer.settings,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _fetchGlobalSettings: () => fetchGlobalSettings()
}, dispatch);

Settings.propTypes = {
  onApplySettings: PropTypes.func.isRequired,
  settings: PropTypes.object,
  translations: PropTypes.object.isRequired,
  profile: PropTypes.string,
  _fetchGlobalSettings: PropTypes.func.isRequired,
};

Settings.defaultProps = {
  settings: null,
  profile: null,
};

Settings.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
