import React, { Component } from 'react';
import { Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import FilterOutputItem from './FilterOutputItem';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

export default class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = { show: false, filtersWithModels: null, showFiltersList: false };
  }

  componentDidMount() {
    filter.on('apply', this.applyFilters);
    filter.on('cancel', this.hideFilters);
  }

  showFilterWidget = () => {
    const { show } = this.state;
    if (filter && !show) {
      this.setState({ show: true });
      return filter.loaded.then(() => {
        filter.setElement(this.refs.filterPopup);
        return filter.showFilters();
      });
    }
  };

  hideFilters = () => {
    this.setState({ show: false });
  };

  applyFilters = () => {
    const { onApplyFilters } = this.props;
    this.hideFilters();
    this.setState({ filtersWithModels: filter.serializeToModels() });
    onApplyFilters(filter.serialize());
  };

  toggleAppliedFilters = () => {
    const { showFiltersList } = this.state;
    this.setState({ showFiltersList: !showFiltersList });
  };

  generateFilterOutput = () => {
    const { filtersWithModels } = this.state;
    const ret = [];
    if (filtersWithModels && filtersWithModels.filters) {
      Object.keys(filtersWithModels.filters)
        .forEach(i => {
          ret.push(<FilterOutputItem filters={filtersWithModels.filters} i={i} />);
        });
    }
    return <div>{ret}</div>;
  }

  render() {
    const { show, filtersWithModels, showFiltersList } = this.state;
    console.info(filtersWithModels);
    return (
      <Col md={6}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading">Filters</h3>
            <button
              type="button"
              className="btn btn-sm btn-default pull-right show-filters"
              onClick={this.showFilterWidget}>
              <span className="glyphicon glyphicon-edit" />
              <span>Edit filters</span>
            </button>
            <div className="applied-filters">
              <span>
                <button
                  type="button"
                  className="btn btn-default btn-sm show-filter-details"
                  onClick={this.toggleAppliedFilters}>
                  <span className="glyphicon glyphicon-eye-open" />
                  <span data-i18n="amp.dashboard:filters-show-settings">Show filter settings </span>
                  (
                  <b>{filtersWithModels ? Object.keys(filtersWithModels.filters).length : 0}</b>
                  )
                </button>
              </span>
              {showFiltersList ? (
                <div>
                  {this.generateFilterOutput()}
                </div>
              ) : null}
            </div>
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
