import React, { Component } from 'react';
import { Container } from 'react-bootstrap';
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
import { getSharedData } from '../actions/getSharedData';
import PrintDummy from '../../sscdashboard/utils/PrintDummy';
import { printChart } from '../../sscdashboard/utils/PrintUtils';
import './print.css';
import { removeFilter } from '../utils/Utils';
import { SRC_DIRECT } from "./charts/FundingByYearChart";

const queryString = require('query-string');

class NDDDashboardHome extends Component {
  constructor(props) {
    super(props);
    // eslint-disable-next-line react/prop-types
    const params = queryString.parse(props.location.search);
    this.state = {
      filters: undefined,
      dashboardId: undefined,
      fundingType: undefined,
      selectedPrograms: undefined,
      settings: undefined,
      selectedDirectProgram: null,
      embedded: !!params.embedded,
      fundingByYearSource: SRC_DIRECT
    };
  }

  // eslint-disable-next-line react/sort-comp
  getSharedDataOrResolve = (id) => {
    const { _getSharedData } = this.props;
    if (id) {
      return _getSharedData(id);
    }
    return Promise.resolve();
  }

  componentDidMount() {
    const { _loadDashboardSettings, _getMappings } = this.props;
    const { embedded } = this.state;
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    // eslint-disable-next-line react/no-did-mount-set-state
    this.setState({ dashboardId: id });
    // Load settings and mappings but dont call _callReport directly, Filters.jsx will do the call.
    return Promise.all([_loadDashboardSettings(), _getMappings(), this.getSharedDataOrResolve(id)])
      .then(data => {
        const tempSettings = {
          [CURRENCY_CODE]: data[0].payload[Object.keys(data[0].payload)
            .find(i => data[0].payload[i].id === CURRENCY_CODE)].value.defaultId
        };
        let ids = [`${data[1].payload[SRC_PROGRAM].id}`, `${data[1].payload[DST_PROGRAM].id}`];
        let fundingType = data[0].payload.find(i => i.id === FUNDING_TYPE).value.defaultId;

        if (id) {
          const savedData = JSON.parse(data[2].payload.stateBlob);
          if (savedData && savedData.settings) {
            if (savedData.settings[CURRENCY_CODE]) {
              tempSettings[CURRENCY_CODE] = savedData.settings[CURRENCY_CODE];
            }
          }
          if (savedData && savedData.fundingType) {
            fundingType = savedData.fundingType;
          }
          if (savedData && savedData.selectedPrograms) {
            ids = savedData.selectedPrograms;
          }
        }

        this.setState({
          selectedPrograms: ids,
          settings: tempSettings,
          fundingType
        });
        /* Notice we dont need to define this.state.filters here, we will get it from onApplyFilters. Apparently
        the filter widget takes date.start and date.end automatically from dashboard settings EP. */
        return data;
      }).finally(() => {
        if (embedded) {
          const { _callReport } = this.props;
          const { fundingType, settings, selectedPrograms } = this.state;
          _callReport(fundingType, {}, selectedPrograms, settings);
        }
      });
  }

  handleOuterChartClick(event, outerData) {
    const {
      selectedDirectProgram, filters, settings, fundingType
    } = this.state;
    const { dashboardSettings } = this.props;
    if (event.points[0].data.name === DIRECT) {
      if (!selectedDirectProgram) {
        this.setState({ selectedDirectProgram: outerData[event.points[0].i], fundingByYearSource: SRC_DIRECT });
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

  onChangeSource(value) {
    this.setState({ fundingByYearSource: value.target.value });
  }

  resetChartAfterUnClick = () => {
    const { selectedDirectProgram, filters, embedded } = this.state;
    if (selectedDirectProgram) {
      if (!embedded) {
        // Remove the filter manually or it will keep affecting the chart.
        const fixedFilters = removeFilter(filters, selectedDirectProgram);
        this.setState(() => ({
          selectedDirectProgram: null,
          filters: fixedFilters
        }));
      } else {
        this.setState(() => ({
          selectedDirectProgram: null,
        }));
      }
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
    this.resetChartAfterUnClick();
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

  downloadImage() {
    const { translations } = this.context;
    printChart(translations['amp.ndd.dashboard:page-title'], 'ndd-main-container',
      [], 'png', false, 'print-simple-dummy-container', false);
  }

  render() {
    const {
      filters,
      dashboardId,
      fundingType,
      selectedPrograms,
      settings,
      selectedDirectProgram,
      embedded,
      fundingByYearSource
    } = this.state;
    const {
      ndd, nddLoadingPending, nddLoaded, dashboardSettings, mapping, noIndirectMapping, globalSettings
    } = this.props;
    return (
      <Container fluid className="main-container" id="ndd-main-container">
        <div className="row header" style={{ marginRight: '-30px', marginLeft: '-30px' }}>
          {mapping && settings && globalSettings && selectedPrograms && !embedded ? (
            <HeaderContainer
              onApplySettings={this.onApplySettings}
              onApplyFilters={this.onApplyFilters}
              filters={filters}
              globalSettings={globalSettings}
              settings={settings}
              fundingType={fundingType}
              selectedPrograms={selectedPrograms}
              dashboardId={dashboardId} />
          ) : null}
        </div>
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
          noIndirectMapping={noIndirectMapping}
          downloadImage={this.downloadImage.bind(this)}
          embedded={embedded}
          onChangeSource={this.onChangeSource.bind(this)}
          fundingByYearSource={fundingByYearSource}
        />
        <PrintDummy />
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
    _clearTopReport: clearTopReport,
    _getSharedData: getSharedData,
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
  globalSettings: PropTypes.object,
  _getSharedData: PropTypes.func.isRequired
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
