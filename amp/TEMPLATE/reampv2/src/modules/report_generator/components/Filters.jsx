import React, { Component } from 'react';
import { Header } from 'semantic-ui-react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { ReportGeneratorContext } from './StartUp';
import { TRN_PREFIX } from '../utils/constants';
import { updateAppliedFilters, updateReportDetailsUseAboveFilters } from '../actions/stateUIActions';
import { toggleIcon } from '../utils/appliedFiltersExtenalCode';
import { hasFilters } from '../utils/Utils';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false, showFiltersList: false,
    };
  }

  componentDidMount() {
    filter.on('apply', this.applyFilters);
    filter.on('cancel', this.hideFilters);

    return filter.loaded.then(() => {
      // eslint-disable-next-line react/no-string-refs
      filter.setElement(this.refs.filterPopup);
      return true;
    });
  }

  shouldComponentUpdate(nextProps, nextState, nextContext) {
    const { filters, html, _updateAppliedFilters } = this.props;
    // Load saved filters (only one time).
    if (filters && html === null) {
      filter.deserialize({ filters, silent: true });
      const html_ = filter.getAppliedFilters({ returnHTML: true });
      _updateAppliedFilters(filters, html_);
    }
    return (nextProps !== this.props || nextState !== this.state || nextContext !== this.context);
  }

  componentWillUnmount() {
    window.removeEventListener('apply', this.applyFilters);
    window.removeEventListener('cancel', this.hideFilters);
  }

  showFilterWidget = () => {
    const { show } = this.state;
    if (filter && !show) {
      this.setState({ show: true });
      return filter.loaded.then(() => filter.showFilters());
    }
  };

  hideFilters = () => {
    this.setState({ show: false });
  };

  applyFilters = () => {
    const { onApplyFilters, _updateAppliedFilters, _updateReportDetailsUseAboveFilters } = this.props;
    this.hideFilters();
    const serialized = filter.serialize();
    const html = filter.getAppliedFilters({ returnHTML: true });
    _updateAppliedFilters(serialized.filters, html);
    if (hasFilters(serialized.filters)) {
      _updateReportDetailsUseAboveFilters(true);
    } else {
      _updateReportDetailsUseAboveFilters(false);
    }
    onApplyFilters(serialized.filters);
  };

  toggleAppliedFilters = () => {
    const { showFiltersList } = this.state;
    this.setState({ showFiltersList: !showFiltersList });
  };

  generateAppliedFilters = () => {
    const {
      filters, html, _updateAppliedFilters
    } = this.props;
    // If this is a saved report we might need to create the html for the first time.
    if (html === null && filters) {
      const html_ = filter.getAppliedFilters({ returnHTML: true });
      _updateAppliedFilters(filters, html_);
      return <div dangerouslySetInnerHTML={{ __html: html_ }} />;
    } else if (filters) {
      return <div dangerouslySetInnerHTML={{ __html: html }} />;
    }
    return null;
  }

  render() {
    const { show, appliedFiltersOpen } = this.state;
    const { translations, filters } = this.props;
    return (
      <>
        <div className="filter-title">
          <div className="filter-title" onClick={this.showFilterWidget}>
            {translations[`${TRN_PREFIX}filters`]}
&nbsp;
          </div>
          {hasFilters(filters) ? (
            <div
              className="filter-title"
              style={{ color: 'green', fontWeight: 'normal', fontSize: '0.9em' }}
              onClick={() => { this.setState({ appliedFiltersOpen: !appliedFiltersOpen }); }}>
              {appliedFiltersOpen
                ? translations[`${TRN_PREFIX}hideAppliedFilters`] : translations[`${TRN_PREFIX}showAppliedFilters`]}
            </div>
          ) : null}
        </div>
        <div className={!appliedFiltersOpen ? 'invisible-applied-filters' : 'applied-filters'}>
          {this.generateAppliedFilters()}
        </div>
        {/* eslint-disable-next-line react/no-string-refs */}
        <div id="filter-popup" ref="filterPopup" style={{ display: (!show ? 'none' : 'block') }} />
      </>
    );
  }

  // eslint-disable-next-line react/sort-comp,no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    // The js code that animates the applied filters tree has to be re-implemented and re-run.
    toggleIcon();
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  reportLoaded: state.uiReducer.reportLoaded,
  reportPending: state.uiReducer.reportPending,
  filters: state.uiReducer.filters,
  html: state.uiReducer.appliedFilters,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateAppliedFilters: (data, html) => dispatch(updateAppliedFilters(data, html)),
  _updateReportDetailsUseAboveFilters: (data) => dispatch(updateReportDetailsUseAboveFilters(data)),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Filters);

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  _updateAppliedFilters: PropTypes.func.isRequired,
  _updateReportDetailsUseAboveFilters: PropTypes.func.isRequired,
  filters: PropTypes.object,
  html: PropTypes.string,
};

Filters.defaultProps = {
  filters: null,
  html: null,
};

Filters.contextType = ReportGeneratorContext;
