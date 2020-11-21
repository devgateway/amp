import React, { Component } from 'react';
import { Col } from 'react-bootstrap';

export default class Share extends Component {
  render() {
    return (
      <Col md={4}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading">Share</h3>
            <button type="button" className="btn btn-sm btn-default pull-right dash-share-button">
              <span className="glyphicon glyphicon-link" />
              <span>link</span>
            </button>
          </div>
        </div>
      </Col>
    );
  }
}
