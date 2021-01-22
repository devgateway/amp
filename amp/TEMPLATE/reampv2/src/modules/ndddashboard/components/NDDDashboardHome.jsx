import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { NDDTranslationContext } from './StartUp';
import MainDashboardContainer from './MainDashboardContainer';
import HeaderContainer from './HeaderContainer';
import { callReport, callTopReport, clearTopReport } from '../actions/callReports';
import { CURRENCY_CODE, DIRECT, FUNDING_TYPE } from '../utils/constants';
import loadDashboardSettings from '../actions/loadDashboardSettings';
import { getMappings } from '../actions/getMappings';
import { DST_PROGRAM, SRC_PROGRAM } from '../../admin/ndd/constants/Constants';

class NDDDashboardHome extends Component {
  constructor(props) {
    super(props);
    this.state = {
      filters: undefined,
      dashboardId: undefined,
      fundingType: undefined,
      selectedPrograms: undefined,
      settings: undefined,
      selectedDirectProgram: null
    };
  }

  componentDidMount() {
    const { _loadDashboardSettings, _callReport, _getMappings } = this.props;
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    // eslint-disable-next-line react/no-did-mount-set-state
    this.setState({ dashboardId: id });
    // This is not a saved dashboard, we can load the report without filters.
    if (!id) {
      return Promise.all([_loadDashboardSettings(), _getMappings()])
        .then(data => {
          const tempSettings = {
            [CURRENCY_CODE]: data[0].payload[Object.keys(data[0].payload)
              .find(i => data[0].payload[i].id === CURRENCY_CODE)].value.defaultId
          };

          /* TODO: evaluate if is not better to refactor the code to always call applyFilters() instead of running
                   _callReport directly. */
          const defaultFilters = { filters: {} };
          const date = {};
          if (data[0].gs.defaultMinDate || data[0].gs.defaultMaxDate) {
            if (data[0].gs.defaultMinDate) {
              date.start = data[0].gs.defaultMinDate;
            }
            if (data[0].gs.defaultMaxDate) {
              date.end = data[0].gs.defaultMaxDate;
            }
            defaultFilters.filters.date = date;
          }

          const ids = [`${data[1].payload[SRC_PROGRAM].id}`, `${data[1].payload[DST_PROGRAM].id}`];
          this.setState({
            selectedPrograms: ids,
            settings: tempSettings,
            fundingType: data[0].payload.find(i => i.id === FUNDING_TYPE).value.defaultId,
            filters: defaultFilters
          });
          return _callReport(data[0].payload.find(i => i.id === FUNDING_TYPE).value.defaultId, defaultFilters,
            ids, tempSettings);
        });
    } else {
      _loadDashboardSettings();
    }
  }

  handleOuterChartClick(event, outerData) {
    const {
      selectedDirectProgram, filters, settings, fundingType
    } = this.state;
    const { dashboardSettings } = this.props;
    if (event.points[0].data.name === DIRECT) {
      if (!selectedDirectProgram) {
        this.setState({ selectedDirectProgram: outerData[event.points[0].i] });
        const { _callTopReport } = this.props;
        _callTopReport(fundingType || dashboardSettings.find(i => i.id === FUNDING_TYPE).value.defaultId,
          settings, filters, outerData[event.points[0].i]);
      } else {
        const { _clearTopReport } = this.props;
        _clearTopReport();
        this.resetChartAfterUnClick();
      }
    }
  }

  resetChartAfterUnClick = () => {
    const { selectedDirectProgram, filters } = this.state;
    if (selectedDirectProgram) {
      // Remove the filter manually or it will keep affecting the chart.
      filters.filters[selectedDirectProgram.filterColumnName]
        .splice(filters.filters[selectedDirectProgram.filterColumnName]
          .findIndex(i => i === selectedDirectProgram.objectId), 1);
      if (filters.filters[selectedDirectProgram.filterColumnName].length === 0) {
        filters.filters[selectedDirectProgram.filterColumnName] = null;
      }
      this.setState(() => ({
        selectedDirectProgram: null,
        filters
      }));
    }
  }

  onApplyFilters = (data) => {
    const { _callReport, _callTopReport } = this.props;
    const {
      fundingType, selectedDirectProgram, settings, selectedPrograms
    } = this.state;
    this.setState({ filters: data });
    _callReport(fundingType, data, selectedPrograms, settings);
    if (selectedDirectProgram !== null) {
      _callTopReport(fundingType, settings, data, selectedDirectProgram);
    }
  }

  onChangeFundingType = (value) => {
    const { _callReport, _clearTopReport } = this.props;
    const { filters, selectedPrograms, settings } = this.state;
    this.resetChartAfterUnClick();
    this.setState({ fundingType: value });
    _callReport(value, filters, selectedPrograms, settings);
    _clearTopReport();
  }

  onChangeProgram = (value) => {
    const { _callReport } = this.props;
    const { filters, fundingType, settings } = this.state;
    const selectedPrograms = value.split('-');
    this.setState({ selectedPrograms });
    _callReport(fundingType, filters, selectedPrograms, settings);
  }

  onApplySettings = (data) => {
    const { _callReport, _callTopReport } = this.props;
    const {
      fundingType, selectedPrograms, filters, selectedDirectProgram
    } = this.state;
    this.setState({ settings: data });
    _callReport(fundingType, filters, selectedPrograms, data);
    if (selectedDirectProgram !== null) {
      _callTopReport(fundingType, data, filters, selectedDirectProgram);
    }
  }

  render() {
    const {
      filters, dashboardId, fundingType, selectedPrograms, settings, selectedDirectProgram
    } = this.state;
    const {
      ndd, nddLoadingPending, nddLoaded, dashboardSettings, mapping, noIndirectMapping, globalSettings
    } = this.props;
    return (
      <Container fluid className="main-container">
        <Row style={{ backgroundColor: '#f6f6f6', paddingTop: '15px' }}>
          <HeaderContainer
            onApplySettings={this.onApplySettings}
            onApplyFilters={this.onApplyFilters}
            filters={filters}
            globalSettings={globalSettings}
            dashboardId={dashboardId} />
        </Row>
        <Row>
          <Col md={12}>
            <div><br /></div>
          </Col>
        </Row>
        <Row style={{ marginRight: '-30px', marginLeft: '-30px' }}>
          <MainDashboardContainer
            handleOuterChartClick={this.handleOuterChartClick.bind(this)}
            selectedDirectProgram={selectedDirectProgram}
            filters={filters}
            ndd={ndd}
            nddLoaded={nddLoaded}
            nddLoadingPending={nddLoadingPending}
            dashboardSettings={dashboardSettings}
            onChangeFundingType={this.onChangeFundingType}
            onChangeProgram={this.onChangeProgram}
            fundingType={fundingType}
            selectedPrograms={selectedPrograms}
            mapping={mapping}
            settings={settings}
            globalSettings={globalSettings}
            noIndirectMapping={noIndirectMapping} />
        </Row>
      </Container>
    );
  }
}

const
  mapStateToProps = state => ({
    ndd: state.reportsReducer.ndd,
    error: state.reportsReducer.error,
    nddLoaded: state.reportsReducer.nddLoaded,
    nddLoadingPending: state.reportsReducer.nddLoadingPending,
    dashboardSettings: state.dashboardSettingsReducer.dashboardSettings,
    globalSettings: state.dashboardSettingsReducer.globalSettings,
    translations: state.translationsReducer.translations,
    mapping: state.mappingsReducer.mapping,
    noIndirectMapping: state.mappingsReducer.noIndirectMapping
  });

const
  mapDispatchToProps = dispatch => bindActionCreators({
    _callReport: callReport,
    _loadDashboardSettings: loadDashboardSettings,
    _getMappings: getMappings,
    _callTopReport: callTopReport,
    _clearTopReport: clearTopReport
  }, dispatch);

NDDDashboardHome.propTypes = {
  _callReport: PropTypes.func.isRequired,
  mapping: PropTypes.object,
  ndd: PropTypes.array,
  nddLoadingPending: PropTypes.bool.isRequired,
  nddLoaded: PropTypes.bool.isRequired,
  dashboardSettings: PropTypes.array,
  _loadDashboardSettings: PropTypes.func.isRequired,
  _getMappings: PropTypes.func.isRequired,
  _callTopReport: PropTypes.func.isRequired,
  _clearTopReport: PropTypes.func.isRequired,
  noIndirectMapping: PropTypes.object,
  globalSettings: PropTypes.object
};

NDDDashboardHome.defaultProps = {
  dashboardSettings: undefined,
  mapping: undefined,
  ndd: null,
  noIndirectMapping: undefined,
  globalSettings: undefined
};

NDDDashboardHome
  .contextType = NDDTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);
