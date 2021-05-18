import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
// eslint-disable-next-line no-unused-vars
import { Header } from 'semantic-ui-react';
import styles from '../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings.css';
import { ReportGeneratorContext } from './StartUp';
import { TRN_PREFIX } from '../utils/constants';

const SettingsWidget = require('../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings');

const widget = new SettingsWidget.SettingsWidget({
  el: 'settings-popup',
  draggable: true,
  caller: 'TABS',
  isPopup: true,
  definitionUrl: '/rest/settings-definitions/tabs'
});

class Settings extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false
    };
  }

  componentDidMount() {
    const { settings } = this.props;
    widget.restoreFromSaved(settings);
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
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

Settings.propTypes = {
  onApplySettings: PropTypes.func.isRequired,
  settings: PropTypes.object,
  translations: PropTypes.object.isRequired,
};

Settings.defaultProps = {
  settings: null
};

Settings.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
