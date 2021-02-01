import React, { Component } from 'react';
import { Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import FilterOutputItem from './FilterOutputItem';
import { getSharedData } from '../actions/getSharedData';
import { TRN_PREFIX } from '../utils/constants';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false, filtersWithModels: null, showFiltersList: false, savedDashboardLoaded: false
    };
  }

  componentDidMount() {
    filter.on('apply', this.applyFilters);
    filter.on('cancel', this.hideFilters);

    const { dashboardId, _getSharedData } = this.props;
    filter.loaded.done(() => {
      if (dashboardId) {
        _getSharedData(dashboardId);
      } else {
        /* Notice we dont need to define this.state.filters here, we will get it from onApplyFilters. Apparently
        the filter widget takes date.start and date.end automatically from dashboard settings EP. */
        filter.deserialize({});
      }
    });
  }

  // eslint-disable-next-line no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    const {
      dashboardId, _sharedData, _sharedDataPending, _sharedDataLoaded,
    } = this.props;
    const { savedDashboardLoaded } = this.state;
    if (dashboardId && _sharedDataPending === false && _sharedDataLoaded === true && savedDashboardLoaded === false) {
      // eslint-disable-next-line react/no-did-update-set-state
      this.setState({ savedDashboardLoaded: true });
      // Note: no need to explicitly call applyFilters when deserializing (unless you use silent: true).
      filter.deserialize(JSON.parse(_sharedData.stateBlob), { dontSetDefaultDates: true });
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
        // eslint-disable-next-line react/no-string-refs
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
    const { translations, globalSettings } = this.props;
    const ret = [];
    if (filtersWithModels && filtersWithModels.filters) {
      Object.keys(filtersWithModels.filters)
        .forEach(i => {
          ret.push(<FilterOutputItem
            key={Math.random()}
            filters={filtersWithModels.filters}
            i={i}
            translations={translations}
            globalSettings={globalSettings} />);
        });
    }
    return <div style={{ paddingLeft: '10px' }}>{ret}</div>;
  }

  render() {
    const { show, filtersWithModels, showFiltersList } = this.state;
    const { translations } = this.props;
    return (
      <Col md={5}>
        <div className="panel">
          <div className="panel-body">
            <h3 className="inline-heading" style={{ float: 'left' }}>{translations[`${TRN_PREFIX}filters`]}</h3>
            <button
              type="button"
              className="btn btn-sm btn-default filter-button"
              onClick={this.showFilterWidget}
              style={{ float: 'right' }}>
              <span className="glyphicon glyphicon-edit" />
              <span>
                {' '}
                {translations[`${TRN_PREFIX}edit-filters`]}
              </span>
            </button>
            <button
              type="button"
              className="btn btn-sm btn-default filter-button"
              onClick={this.toggleAppliedFilters}
              style={{ float: 'left' }}>
              <span className="glyphicon glyphicon-eye-open" />
              <span>
                {!showFiltersList
                  ? ` ${translations[`${TRN_PREFIX}show-filter-settings`]} `
                  : ` ${translations[`${TRN_PREFIX}hide-filter-settings`]} `}
              </span>
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
        {/* eslint-disable-next-line react/no-string-refs */}
        <div id="filter-popup" ref="filterPopup" style={{ display: (!show ? 'none' : 'block') }} />
      </Col>
    );
  }
}

const mapStateToProps = state => ({
  _sharedDataPending: state.sharedDataReducer.sharedDataPending,
  _sharedDataLoaded: state.sharedDataReducer.sharedDataLoaded,
  _sharedData: state.sharedDataReducer.sharedData,
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _getSharedData: getSharedData,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Filters);

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  dashboardId: PropTypes.number,
  translations: PropTypes.object.isRequired,
  globalSettings: PropTypes.object.isRequired,
  _sharedDataPending: PropTypes.bool.isRequired,
  _sharedDataLoaded: PropTypes.bool.isRequired,
  _sharedData: PropTypes.object,
  _getSharedData: PropTypes.func.isRequired
};

Filters.defaultProps = {
  dashboardId: null,
  _sharedData: undefined
};
