import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { NDDTranslationContext } from './StartUp';
import MainDashboardContainer from './MainDashboardContainer';
import HeaderContainer from './HeaderContainer';
import { callReport } from '../actions/callReports';
import { FUNDING_TYPE } from '../utils/constants';
import loadDashboardSettings from '../actions/loadDashboardSettings';
import { getMappings } from '../actions/getMappings';
import { DST_PROGRAM, SRC_PROGRAM } from '../../admin/ndd/constants/Constants';

class NDDDashboardHome extends Component {
  constructor(props) {
    super(props);
    this.state = {
      filters: undefined,
      filtersWithModels: undefined,
      dashboardId: undefined,
      fundingType: undefined,
      selectedPrograms: undefined
    };
  }

  componentDidMount() {
    const { loadDashboardSettings, callReport, getMappings } = this.props;
    const { selectedPrograms } = this.state;
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    this.setState({ dashboardId: id });
    // This is not a saved dashboard we can load the report without filters.
    if (!id) {
      return Promise.all([loadDashboardSettings(), getMappings()])
        .then(data => {
          const ids = [`${data[1].payload[SRC_PROGRAM].id}`, `${data[1].payload[DST_PROGRAM].id}`];
          this.setState({ selectedPrograms: ids });
          return callReport(data[0].payload.find(i => i.id === FUNDING_TYPE).value.defaultId, null, ids);
        });
    } else {
      loadDashboardSettings();
    }
  }

  onApplySettings = (data) => {
    alert(data);
  }

  onApplyFilters = (data, dataWithModels) => {
    const { callReport } = this.props;
    const { fundingType, selectedPrograms } = this.state;
    this.setState({ filters: data, filtersWithModels: dataWithModels });
    callReport(fundingType, data, selectedPrograms);
  }

  onChangeFundingType = (value) => {
    const { callReport } = this.props;
    const { filters, selectedPrograms } = this.state;
    this.setState({ fundingType: value });
    callReport(value, filters, selectedPrograms);
  }

  onChangeProgram = (value) => {
    const { callReport } = this.props;
    const { filters, fundingType } = this.state;
    const selectedPrograms = value.split('-');
    this.setState({ selectedPrograms });
    callReport(fundingType, filters, selectedPrograms);
  }

  render() {
    const {
      filters, dashboardId, fundingType, selectedPrograms
    } = this.state;
    const {
      error, ndd, nddLoadingPending, nddLoaded, dashboardSettings, mapping, noIndirectMapping
    } = this.props;
    const { translations } = this.context;
    return (
      <Container fluid className="main-container">
        <Row>
          <HeaderContainer
            onApplySettings={this.onApplySettings}
            onApplyFilters={this.onApplyFilters}
            filters={filters}
            dashboardId={dashboardId} />
        </Row>
        <Row>
          <MainDashboardContainer
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
            noIndirectMapping={noIndirectMapping} />
        </Row>
      </Container>
    );
  }
}

const mapStateToProps = state => ({
  ndd: state.reportsReducer.ndd,
  error: state.reportsReducer.error,
  nddLoaded: state.reportsReducer.nddLoaded,
  nddLoadingPending: state.reportsReducer.nddLoadingPending,
  dashboardSettings: state.dashboardSettingsReducer.dashboardSettings,
  translations: state.translationsReducer.translations,
  mapping: state.mappingsReducer.mapping,
  noIndirectMapping: state.mappingsReducer.noIndirectMapping
});

const mapDispatchToProps = dispatch => bindActionCreators({
  callReport, loadDashboardSettings, getMappings
}, dispatch);

NDDDashboardHome.propTypes = {
  callReport: PropTypes.func.isRequired,
  error: PropTypes.object,
  ndd: PropTypes.array.isRequired,
  nddLoadingPending: PropTypes.bool.isRequired,
  nddLoaded: PropTypes.bool.isRequired,
  dashboardSettings: PropTypes.object,
  loadDashboardSettings: PropTypes.func.isRequired,
  getMappings: PropTypes.func.isRequired,
  translations: PropTypes.array.isRequired
};

NDDDashboardHome.defaultProps = {
  dashboardSettings: undefined,
  error: undefined,
};

NDDDashboardHome.contextType = NDDTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);
