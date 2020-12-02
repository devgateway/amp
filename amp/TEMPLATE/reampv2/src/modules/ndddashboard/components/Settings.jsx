import React, { Component } from 'react';
import { Col } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { TRN_PREFIX } from '../utils/constants';

class Settings extends Component {
  render() {
    const { translations } = this.props;
    return (
      <Col md={3}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading">Settings</h3>
            <button type="button" className="btn btn-sm btn-default pull-right dash-settings-button">
              <span className="glyphicon glyphicon-edit" />
              <span>{` ${translations[`${TRN_PREFIX}edit-settings`]}`}</span>
            </button>
          </div>
        </div>
      </Col>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
