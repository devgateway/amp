import React, { Component } from 'react';
import { Header, Segment } from 'semantic-ui-react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Col } from 'react-bootstrap';
import FilterOutputItem from '../../ndddashboard/components/FilterOutputItem';
import { ReportGeneratorContext } from './StartUp';
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
      show: false, filtersWithModels: null, showFiltersList: false
    };
  }

  componentDidMount() {
    filter.on('apply', this.applyFilters);
    filter.on('cancel', this.hideFilters);
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
    // untested code.
    const { onApplyFilters } = this.props;
    this.hideFilters();
    const serializeWithModels = filter.serializeToModels();
    this.setState({ filtersWithModels: serializeWithModels });
    onApplyFilters(filter.serialize(), serializeWithModels);
  };

  toggleAppliedFilters = () => {
    // untested code.
    const { showFiltersList } = this.state;
    this.setState({ showFiltersList: !showFiltersList });
  };

  generateFilterOutput = () => {
    // untested code.
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
    const { show } = this.state;
    const { translations } = this.props;
    return (
      <>
        <Header size="small">
          <span className="pointer" onClick={this.showFilterWidget}>{translations[`${TRN_PREFIX}filters`]}</span>
        </Header>
        {/* eslint-disable-next-line react/no-string-refs */}
        <div id="filter-popup" ref="filterPopup" style={{ display: (!show ? 'none' : 'block') }} />
      </>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Filters);

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  globalSettings: PropTypes.object.isRequired,
};

Filters.defaultProps = {};

Filters.contextType = ReportGeneratorContext;
