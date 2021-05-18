import React, { Component } from 'react';
import { Header } from 'semantic-ui-react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { ReportGeneratorContext } from './StartUp';
import { TRN_PREFIX } from '../utils/constants';
import { updateAppliedFilters } from '../actions/stateUIActions';
import { toggleIcon } from '../utils/appliedFiltersExtenalCode';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false, showFiltersList: false
    };
  }

  componentDidMount() {
    const { filters } = this.props;
    filter.on('apply', this.applyFilters);
    filter.on('cancel', this.hideFilters);

    return filter.loaded.then(() => {
      // eslint-disable-next-line react/no-string-refs
      filter.setElement(this.refs.filterPopup);
      if (filters) {
        // Load saved filters into the widget.
        filter.deserialize({ filters, silent: true });
      }
      return true;
    });
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
    const { onApplyFilters, _updateAppliedFilters } = this.props;
    this.hideFilters();
    const serialized = filter.serialize();
    const html = filter.getAppliedFilters({ returnHTML: true });
    _updateAppliedFilters(serialized.filters, html);
    onApplyFilters(serialized.filters);
    console.log(serialized.filters);
    toggleIcon();
  };

  toggleAppliedFilters = () => {
    // untested code.
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
      console.log('first time');
      console.log(filters);
      return <div dangerouslySetInnerHTML={{ __html: html_ }} />;
    } else if (filters) {
      console.log('generateAppliedFilters');
      return <div dangerouslySetInnerHTML={{ __html: html }} />;
    }
    return null;
  }

  render() {
    console.log('render');
    const { show } = this.state;
    const { translations } = this.props;
    return (
      <>
        <Header size="small">
          <span className="pointer" onClick={this.showFilterWidget}>{translations[`${TRN_PREFIX}filters`]}</span>
          <div className="applied-filters">
            {this.generateAppliedFilters()}
          </div>
        </Header>
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
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateAppliedFilters: (data, html) => dispatch(updateAppliedFilters(data, html)),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Filters);

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  _updateAppliedFilters: PropTypes.func.isRequired,
  reportLoaded: PropTypes.bool,
  reportPending: PropTypes.bool,
  filters: PropTypes.object,
  html: PropTypes.string,
};

Filters.defaultProps = {
  reportLoaded: false,
  reportPending: false,
  filters: null,
  html: null,
};

Filters.contextType = ReportGeneratorContext;
