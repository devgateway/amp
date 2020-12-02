import React, { Component } from 'react';
import { Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import FilterOutputItem from './FilterOutputItem';
import { getSharedData } from '../actions/getSharedData';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false, filtersWithModels: null, showFiltersList: false, loadedSavedData: false
    };
  }

  componentDidMount() {
    filter.on('apply', this.applyFilters);
    filter.on('cancel', this.hideFilters);
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    const {
      dashboardId, sharedData, sharedDataPending, sharedDataLoaded, getSharedData
    } = this.props;
    const { loadedSavedData } = this.state;
    if (dashboardId) {
      filter.loaded.done(() => {
        if (!sharedDataPending && !sharedDataLoaded) {
          getSharedData(dashboardId);
        }
        if (sharedDataPending === false && sharedDataLoaded === true && !loadedSavedData) {
          this.setState({ loadedSavedData: true });
          // Note: no need to explicitly call applyFilters when deserializing (unless you use silent: true).
          filter.deserialize(JSON.parse(sharedData.stateBlob));
        }
      });
    }
  }

  componentWillUnmount() {
    window.removeEventListener('apply', this.applyFilters);
    window.removeEventListener('cancel', this.hideFilters);
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
    const serializeWithModels = filter.serializeToModels();
    this.setState({ filtersWithModels: serializeWithModels });
    onApplyFilters(filter.serialize(), serializeWithModels);
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
    return (
      <Col md={6}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading" style={{ float: 'left' }}>Filters</h3>
            <button
              type="button"
              className="btn btn-sm btn-default filter-button"
              onClick={this.showFilterWidget}
              style={{ float: 'right' }}>
              <span className="glyphicon glyphicon-edit" />
              <span>Edit filters</span>
            </button>
            <button
              type="button"
              className="btn btn-sm btn-default filter-button"
              onClick={this.toggleAppliedFilters}
              style={{ float: 'left' }}>
              <span className="glyphicon glyphicon-eye-open" />
              <span>{!showFiltersList ? ' Show filter settings ' : ' Hide filter settings '}</span>
              (
              <b>{filtersWithModels ? Object.keys(filtersWithModels.filters).length : 0}</b>
              )
            </button>
          </div>
          <div className="applied-filters">
            {showFiltersList ? (
              <div>
                {this.generateFilterOutput()}
              </div>
            ) : null}
          </div>
        </div>
        <div id="filter-popup" ref="filterPopup" style={{ display: (!show ? 'none' : 'block') }} />
      </Col>
    );
  }
}

const mapStateToProps = state => ({
  sharedDataPending: state.sharedDataReducer.sharedDataPending,
  sharedDataLoaded: state.sharedDataReducer.sharedDataLoaded,
  sharedData: state.sharedDataReducer.sharedData,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  getSharedData,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Filters);

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  dashboardId: PropTypes.number
};
