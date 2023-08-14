import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
// eslint-disable-next-line no-unused-vars
import styles from '@devgateway/amp-settings/dist/amp-settings.css';
import { ReportGeneratorContext } from './StartUp';
import {
  URL_SETTINGS_REPORTS, REPORTS
} from '../utils/constants';
import { fetchGlobalSettings } from '../actions/settingsActions';
import { extractSettings } from '../reducers/utils/settingsDataConverter';
import { updateAppliedSettings } from '../actions/stateUIActions';
import { translate } from '../utils/Utils';

const SettingsWidget = require('@devgateway/amp-settings/dist/amp-settings');

let widget = null;

class Settings extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false, changed: false
    };
  }

  componentDidMount() {
    const { settings, _fetchGlobalSettings } = this.props;
    const settingsURL = URL_SETTINGS_REPORTS;
    widget = new SettingsWidget.SettingsWidget({
      el: 'settings-popup',
      draggable: true,
      caller: REPORTS,
      isPopup: true,
      definitionUrl: settingsURL
    });

    // eslint-disable-next-line react/no-string-refs
    widget.setElement(this.refs.settingsPopup);
    widget.on('applySettings', this.applySettings);
    widget.on('close', this.hideSettings);
    _fetchGlobalSettings(settingsURL).then((action) => {
      if (settings === null) {
        const gs = extractSettings(action.payload, action.payload2);
        return widget.restoreFromSaved(gs);
      } else {
        widget.restoreFromSaved(settings);
        // eslint-disable-next-line react/no-did-mount-set-state
        this.setState({ changed: true });
      }
    });
  }

  componentWillUnmount() {
    window.removeEventListener('applySettings', this.applySettings);
    window.removeEventListener('close', this.hideSettings);
  }

  applySettings = (data) => {
    const { onApplySettings, _updateAppliedSettings } = this.props;
    _updateAppliedSettings(null);
    _updateAppliedSettings(data);
    onApplySettings(data);
    this.hideSettings();
    this.setState({ changed: true });
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
    const { show, changed } = this.state;
    const {
      translations, profile, appliedSectionOpen, appliedSectionChange
    } = this.props;
    return (
      <>
        <>
          <div className="filter-title" onClick={this.toggleSettings}>
            {translate('settings', profile, translations)}
          </div>
          {changed ? (
            <div
              className={`filter-title applied-filters-label${appliedSectionOpen ? ' expanded' : ''}`}
              onClick={() => { appliedSectionChange(); }}>
              {appliedSectionOpen
                ? translate('hideAppliedSettings', profile, translations)
                : translate('showAppliedSettings', profile, translations)}
            </div>
          ) : null}
          <div
            id="settings-popup"
            ref="settingsPopup"
            style={{
              display: (!show ? 'none' : 'block'),
              padding: '0px',
              borderColor: '#337ab7'
            }} />
        </>
      </>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  profile: state.uiReducer.profile,
  settings: state.uiReducer.settings,
  reportGlobalSettings: state.settingsReducer.reportGlobalSettings,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _fetchGlobalSettings: (url) => fetchGlobalSettings(url),
  _updateAppliedSettings: (data) => updateAppliedSettings(data),
}, dispatch);

Settings.propTypes = {
  onApplySettings: PropTypes.func.isRequired,
  settings: PropTypes.object,
  translations: PropTypes.object.isRequired,
  _fetchGlobalSettings: PropTypes.func.isRequired,
  _updateAppliedSettings: PropTypes.func.isRequired,
  profile: PropTypes.string,
  appliedSectionOpen: PropTypes.bool.isRequired,
  appliedSectionChange: PropTypes.func.isRequired
};

Settings.defaultProps = {
  settings: null,
  profile: undefined
};

Settings.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
