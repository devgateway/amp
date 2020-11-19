import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import NestedDonutsProgramChart from './NestedDonutsProgramChart';
import { callReport } from '../actions/callReports';
import loadDashboardSettings from '../actions/loadDashboardSettings';
import { Col } from 'react-bootstrap';
import FundingTypeSelector from './FundingTypeSelector';
import { FUNDING_TYPE } from '../utils/constants';

class MainDashboardContainer extends Component {

  constructor(props) {
    super(props);
    this.onChangeFundingType = this.onChangeFundingType.bind(this);
    this.state = { fundingType: null };
  }

  componentDidMount() {
    const { loadDashboardSettings, callReport } = this.props;
    loadDashboardSettings()
      .then(settings => callReport(settings.payload.find(i => i.id === FUNDING_TYPE).value.defaultId));
  }

  onChangeFundingType(value) {
    const { callReport } = this.props;
    this.setState({ fundingType: value });
    callReport(value);
  }

  render() {
    const { error, ndd, nddLoadingPending, nddLoaded, dashboardSettings } = this.props;
    const { fundingType } = this.state;
    console.error(this.state);
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      return (<div>
        <Col md={6}>
          <div>
            <div className="solar-container">
              <div>
                {(nddLoaded && !nddLoadingPending) ? <NestedDonutsProgramChart data={ndd}/> : <div>Loading...</div>}
                {dashboardSettings ?
                  <FundingTypeSelector onChange={this.onChangeFundingType} defaultValue={fundingType}/> : null}
              </div>
            </div>
            <div className="year-chart-container">amounts by year</div>
          </div>
        </Col>
        <Col md={6}>
          <div>legends</div>
        </Col>
      </div>);
    }
  }
}

const mapStateToProps = state => ({
  ndd: state.reportsReducer.ndd,
  dashboardSettings: state.dashboardSettingsReducer.dashboardSettings,
  error: state.reportsReducer.error,
  nddLoaded: state.reportsReducer.nddLoaded,
  nddLoadingPending: state.reportsReducer.nddLoadingPending,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  callReport,
  loadDashboardSettings
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);

MainDashboardContainer.propTypes = {
  callReport: PropTypes.func.isRequired,
  loadDashboardSettings: PropTypes.func.isRequired,
  error: PropTypes.object
};
MainDashboardContainer.defaultProps = {
  error: undefined
};
