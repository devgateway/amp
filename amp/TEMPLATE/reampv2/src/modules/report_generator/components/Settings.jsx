import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
// eslint-disable-next-line no-unused-vars
import styles from '../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings.css';
import { ReportGeneratorContext } from './StartUp';
import {
  // eslint-disable-next-line no-unused-vars
  TABS, URL_SETTINGS_TABS, URL_SETTINGS_REPORTS, REPORTS, PROFILE_TAB
} from '../utils/constants';
import { fetchGlobalSettings } from '../actions/settingsActions';
import { extractSettings } from '../reducers/utils/settingsDataConverter';
import { updateAppliedSettings } from '../actions/stateUIActions';
import { translate } from '../utils/Utils';

const SettingsWidget = require('../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings');

let widget = null;

class Settings extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false, changed: false, appliedSettingsOpen: false
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
    if (settings === null) {
      _fetchGlobalSettings(settingsURL).then((action) => {
        const gs = extractSettings(action.payload, action.payload2);
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
    const { onApplySettings, _updateAppliedSettings } = this.props;
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

  generateAppliedSettings = () => {
    const {
      settings, profile, translations, reportGlobalSettings
    } = this.props;
    if (!reportGlobalSettings || !settings) {
      return null;
    }
    return (
      <div className="applied-filters">
        <div>
          <ul id="previsualization_tree">
            <li>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('calendar', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {reportGlobalSettings.find(i => i.id === 'calendar-id')
                    .value.options.find(i => i.id === settings['calendar-id']).name}
                </li>
              </ul>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('currency', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {reportGlobalSettings.find(i => i.id === 'currency-code')
                    .value.options.find(i => i.id === settings['currency-code']).name}
                </li>
              </ul>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('amountUnits', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {reportGlobalSettings.find(i => i.id === 'number-divider')
                    .value.options.find(i => i.value === `${settings['amount-format']['number-divider']}`).name}
                </li>
              </ul>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('yearRange', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {settings['year-range'].from} - {settings['year-range'].to}
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    );
  }

  render() {
    const { show, changed, appliedSettingsOpen } = this.state;
    const { translations, profile } = this.props;
    return (
      <>
        <div className="filter-title settings-title">
          <span className="filter-title" onClick={this.toggleSettings}>
            {translate('settings', profile, translations)}
          </span>
          {changed ? (
            <div
              className={`filter-title applied-filters-label${appliedSettingsOpen ? ' expanded' : ''}`}
              onClick={() => { this.setState({ appliedSettingsOpen: !appliedSettingsOpen }); }}>
              {appliedSettingsOpen
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
        </div>
        <div className="applied-filters-wrapper">
          <div className={!appliedSettingsOpen ? 'invisible-applied-filters' : 'applied-filters'}>
            {this.generateAppliedSettings()}
          </div>
        </div>
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
  reportGlobalSettings: PropTypes.object.isRequired
};

Settings.defaultProps = {
  settings: null,
  profile: undefined
};

Settings.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
