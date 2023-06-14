/* eslint-disable */
import React, { RefObject } from 'react';
import { Col } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
// @ts-ignore
// eslint-disable-next-line no-unused-vars
import styles from '../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings.css';
import { MeDashboardContext } from './StartUp';

const SettingsWidget = require('../../../../../ampTemplate/node_modules/amp-settings/dist/amp-settings');

const widget = new SettingsWidget.SettingsWidget({
  el: 'settings-popup',
  draggable: true,
  caller: 'TABS',
  isPopup: true,
  definitionUrl: '/rest/settings-definitions/tabs'
});

const Settings = (props: any) => {
    const { onApplySettings, settings } = props;
    const settingsPopup: RefObject<HTMLDivElement> = React.createRef();
    const [show, setShow] = React.useState(false);
    const { translations } = React.useContext(MeDashboardContext);

    const applySettings = (data: any) => {
        onApplySettings(data);
        hideSettings();
      }

      const hideSettings = () => {
        setShow(false);
      }

      const toggleSettings = () => {
        widget.show();
        setShow(!show)
      }

    React.useEffect(() => {
        widget.restoreFromSaved(settings);
        // eslint-disable-next-line react/no-string-refs
        widget.setElement(settingsPopup);
        widget.on('applySettings', applySettings);
        widget.on('close', hideSettings);

        return () => {
            window.removeEventListener('applySettings', applySettings);
            window.removeEventListener('close', hideSettings);
        }
    }, []);

    return (
        <Col md={4}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading">{translations['amp.dashboard:settings']}</h3>
            <button
              type="button"
              className="btn btn-sm btn-default pull-right dash-settings-button"
              onClick={toggleSettings}>
              <span className="glyphicon glyphicon-edit" />
              <span>{` ${translations['amp.me.dashboard:edit-settings']}`}</span>
            </button>
          </div>
        </div>
        <div
          id="settings-popup"
          ref={settingsPopup}
          style={{
            display: (!show ? 'none' : 'block'),
            padding: '0px',
            borderColor: '#337ab7'
          }} />
      </Col>
    );
};

const mapStateToProps = (state: any) => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = (dispatch: any) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
