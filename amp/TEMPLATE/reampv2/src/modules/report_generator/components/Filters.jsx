import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Loader } from 'semantic-ui-react';
import { ReportGeneratorContext } from './StartUp';
import { getMetadata, updateAppliedFilters, updateReportDetailsUseAboveFilters } from '../actions/stateUIActions';
import { toggleIcon } from '../utils/appliedFiltersExtenalCode';
import { translate, hasFilters } from '../utils/Utils';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

let filter = null;

class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false, showFiltersList: false, filterLoaded: false
    };
  }

  componentDidMount() {
    const { _getMetadata } = this.props;

    /* NOTICE WE DONT SEND ANY PARAM HERE BECAUSE WE JUST WANT THE PROMISE */
    return _getMetadata().then((action) => {
      filter = new Filter({
        draggable: true,
        caller: 'REPORTS',
        reportType: action.payload.type
      });

      filter.on('apply', this.applyFilters);
      filter.on('cancel', this.hideFilters);

      return filter.loaded.then(() => {
        // eslint-disable-next-line react/no-string-refs
        filter.setElement(this.refs.filterPopup);
        this.setState({ filterLoaded: true });
        return true;
      });
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
      return filter.loaded.then(() => {
        this.setState({ show: true });
        return filter.showFilters();
      });
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

  render() {
    const { show, filterLoaded } = this.state;
    const {
      translations, filters, profile, appliedSectionChange, appliedSectionOpen
    } = this.props;
    return (
      <>
        {filterLoaded ? (
          <>
            <div className="filter-title" onClick={this.showFilterWidget}>
              {translate('filters', profile, translations)}
&nbsp;
            </div>
            {hasFilters(filters) ? (
              <div
                className={`filter-title applied-filters-label${appliedSectionOpen ? ' expanded' : ''}`}
                onClick={() => appliedSectionChange(filter)}>
                {appliedSectionOpen
                  ? translate('hideAppliedFilters', profile, translations)
                  : translate('showAppliedFilters', profile, translations)}
              </div>
            ) : null}
          </>
        ) : <div style={{ float: 'left' }}><Loader active inline /></div>}
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
  type: state.type,
  profile: state.uiReducer.profile
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateAppliedFilters: (data, html) => updateAppliedFilters(data, html),
  _updateReportDetailsUseAboveFilters: (data) => updateReportDetailsUseAboveFilters(data),
  _getMetadata: () => getMetadata(),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Filters);

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  _updateAppliedFilters: PropTypes.func.isRequired,
  _updateReportDetailsUseAboveFilters: PropTypes.func.isRequired,
  filters: PropTypes.object,
  html: PropTypes.string,
  type: PropTypes.string.isRequired,
  _getMetadata: PropTypes.func.isRequired,
  profile: PropTypes.string,
  appliedSectionChange: PropTypes.func.isRequired,
  appliedSectionOpen: PropTypes.bool.isRequired
};

Filters.defaultProps = {
  filters: null,
  html: null,
  profile: undefined
};

Filters.contextType = ReportGeneratorContext;
