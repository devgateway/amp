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
      show: false, showFiltersList: false, filterLoaded: false,
    };
  }

  componentDidMount() {
    const {
      _getMetadata, id, filters, _updateAppliedFilters
    } = this.props;

    /* NOTICE WE DONT SEND ANY PARAM HERE BECAUSE WE JUST WANT THE PROMISE */
    return _getMetadata().then((action) => {
      filter = new Filter({
        draggable: true,
        caller: 'REPORTS',
        reportType: action.payload.type,
      });

      filter.on('apply', this.applyFilters);
      filter.on('cancel', this.hideFilters);

      return filter.loaded.then(() => {
        filter.setElement(this.refs.filterPopup);

        /* IMPORTANT: AT THIS POINT WE ASSUME THE REPORT DATA HAVE BEEN FETCHED!
        (through 'loading' prop in FiltersAndSettings.jsx). We deserialize (only one time) with empty/saved
        filters to set includeLocationChildren. */
        if (id) {
          const _filters = { ...filters };
          if (_filters.includeLocationChildren === undefined || _filters.includeLocationChildren === null) {
            _filters.includeLocationChildren = true;
          }
          filter.deserialize({ filters, silent: true });
          const html_ = filter.getAppliedFilters({ returnHTML: true });
          _updateAppliedFilters(filters, html_);
        } else {
          filter.deserialize({ filters: { includeLocationChildren: true }, silent: true });
        }
        this.setState({ filterLoaded: true });
        return true;
      });
    });
  }

  // eslint-disable-next-line no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    // The js code that animates the applied filters tree has to be re-implemented and re-run.
    toggleIcon();
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
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  reportLoaded: state.uiReducer.reportLoaded,
  reportPending: state.uiReducer.reportPending,
  filters: state.uiReducer.filters,
  html: state.uiReducer.appliedFilters,
  type: state.type,
  profile: state.uiReducer.profile,
  id: state.uiReducer.id,
  includeLocationChildren: state.uiReducer.includeLocationChildren,
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
  type: PropTypes.string.isRequired,
  _getMetadata: PropTypes.func.isRequired,
  profile: PropTypes.string,
  appliedSectionChange: PropTypes.func.isRequired,
  appliedSectionOpen: PropTypes.bool.isRequired,
  id: PropTypes.number,
};

Filters.defaultProps = {
  filters: null,
  profile: undefined,
  id: undefined,
};

Filters.contextType = ReportGeneratorContext;
