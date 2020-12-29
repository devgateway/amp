import React, { Component } from 'react';
import { Col } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { TRN_PREFIX } from '../utils/constants';
import styles from '../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings.css';

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
    const { translations, onApplySettings } = this.props;
    const { show } = this.state;
    return (
      <Col md={4}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading">Settings</h3>
            <button
              type="button"
              className="btn btn-sm btn-default pull-right dash-settings-button"
              onClick={this.toggleSettings}>
              <span className="glyphicon glyphicon-edit" />
              <span>{` ${translations[`${TRN_PREFIX}edit-settings`]}`}</span>
            </button>
          </div>
        </div>
        <div
          id="settings-popup"
          ref="settingsPopup"
          style={{
            display: (!show ? 'none' : 'block'),
            padding: '0px',
            borderColor: '#337ab7'
          }} />
      </Col>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

Settings.propTypes = {
  onApplySettings: PropTypes.func.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
