import React, { Component } from 'react';
import { Col } from 'react-bootstrap';

export default class Filters extends Component {

  render() {
    return (
      <Col md={4}>
        <div className="panel">
          <div className="panel-body">
            <button type="button" className="btn btn-sm btn-default pull-right show-filters">
              <span className="glyphicon glyphicon-edit"></span>
              <span>Edit filters</span>
            </button>
            <h3 className="inline-heading">Filters</h3>
          </div>
        </div>
      </Col>
    );
  }
}
