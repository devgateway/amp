import React, { Component } from 'react';
import { Col } from 'react-bootstrap';
import PropTypes from 'prop-types';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

export default class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = { show: false };
  }

  componentDidMount() {
    filter.on('apply', this.applyFilters);
    filter.on('cancel', this.hideFilters);
  }

  showFilters = () => {
    const { show } = this.state;
    if (filter && !show) {
      this.setState({ show: true });
      return filter.loaded.then(() => {
        filter.setElement(this.refs.filterPopup);
        return filter.showFilters();
      });
    }
  }

  hideFilters = () => {
    this.setState({ show: false });
  }

  applyFilters = () => {
    const { onApplyFilters } = this.props;
    const data = filter.serialize();
    this.hideFilters();
    onApplyFilters(data);
  }

  render() {
    const { show } = this.state;
    return (
      <Col md={4}>
        <div className="panel">
          <div className="panel-body">
            <button type="button" className="btn btn-sm btn-default pull-right show-filters" onClick={this.showFilters}>
              <span className="glyphicon glyphicon-edit" />
              <span>Edit filters</span>
            </button>
            <h3 className="inline-heading">Filters</h3>
          </div>
        </div>
        <div id="filter-popup" ref="filterPopup" style={{ display: (!show ? 'none' : 'block') }} />
      </Col>
    );
  }
}

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired
};
