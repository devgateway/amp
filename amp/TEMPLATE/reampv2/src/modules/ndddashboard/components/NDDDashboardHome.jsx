import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { NDDTranslationContext } from './StartUp';
import MainDashboardContainer from './MainDashboardContainer';
import HeaderContainer from './HeaderContainer';
import { callReport } from '../actions/callReports';
import { FUNDING_TYPE, REPORT_TYPE_HIDDEN_INDIRECT } from '../utils/constants';
import loadDashboardSettings from '../actions/loadDashboardSettings';

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
    const { loadDashboardSettings, callReport } = this.props;
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    this.setState({ dashboardId: id });
    // This is not a saved dashboard we can load the report without filters.
    if (!id) {
      loadDashboardSettings()
        .then(settings => callReport(settings.payload.find(i => i.id === FUNDING_TYPE).value.defaultId), null,
          REPORT_TYPE_HIDDEN_INDIRECT);
    } else {
      loadDashboardSettings();
    }
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
    if (selectedPrograms.length > 1) {
      callReport(fundingType, filters, selectedPrograms);
    } else {
      // TODO: check if we can use the current ndd data.
    }
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
          <HeaderContainer onApplyFilters={this.onApplyFilters} filters={filters} dashboardId={dashboardId} />
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
  mapping: state.reportsReducer.mapping,
  noIndirectMapping: state.reportsReducer.noIndirectMapping
});

const mapDispatchToProps = dispatch => bindActionCreators({
  callReport, loadDashboardSettings
}, dispatch);

NDDDashboardHome.propTypes = {
  callReport: PropTypes.func.isRequired,
  error: PropTypes.object,
  ndd: PropTypes.array.isRequired,
  nddLoadingPending: PropTypes.bool.isRequired,
  nddLoaded: PropTypes.bool.isRequired,
  dashboardSettings: PropTypes.object,
  loadDashboardSettings: PropTypes.func.isRequired,
  translations: PropTypes.array.isRequired
};

NDDDashboardHome.defaultProps = {
  dashboardSettings: undefined,
  error: undefined,
};

NDDDashboardHome.contextType = NDDTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);
