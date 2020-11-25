import React, { Component } from 'react';
import { Col } from 'react-bootstrap';
// import $ from 'jquery';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');
const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

export default class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = { registered: false };
  }

  render() {
    if (filter && !this.state.registered) {
      this.setState({ registered: true });
      filter.loaded.then(() => {
        filter.setElement(this.refs.filterPopup);
        return filter.showFilters();
      });
    }
    // $(this.refs.filterPopup).show();
    return (
      <Col md={4}>
        <div className="panel">
          <div className="panel-body">
            <button type="button" className="btn btn-sm btn-default pull-right show-filters">
              <span className="glyphicon glyphicon-edit" />
              <span>Edit filters</span>
            </button>
            <h3 className="inline-heading">Filters</h3>
          </div>
        </div>
        <div id="filter-popup" ref="filterPopup"> </div>
      </Col>
    );
  }
}
