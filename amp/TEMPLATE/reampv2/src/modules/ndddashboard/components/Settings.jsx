import React, { Component } from 'react';
import { Col } from 'react-bootstrap';

export default class Filters extends Component {
  render() {
    return (
      <Col md={4}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading">Settings</h3>
            <button type="button" className="btn btn-sm btn-default pull-right dash-settings-button">
              <span className="glyphicon glyphicon-edit" />
              <span>edit settings</span>
            </button>
          </div>
        </div>
      </Col>
    );
  }
}
