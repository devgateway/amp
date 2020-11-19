import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import NestedDonutsProgramChart from './NestedDonutsProgramChart';
import callReport from '../actions/callReports';
import loadDashboardSettings from '../actions/loadDashboardSettings';
import { Col } from 'react-bootstrap';
import FundingTypeSelector from './FundingTypeSelector';

class MainDashboardContainer extends Component {

  constructor(props) {
    super(props);
    this.onChangeFundingType = this.onChangeFundingType.bind(this);
    this.state = { fundingType: null };
  }

  componentDidMount() {
    this.props.loadDashboardSettings();
    this.props.callReport();
  }

  onChangeFundingType(value) {
    console.error(value);
    this.setState({ fundingType: value });
  }

  render() {
    const { error, ndd, nddLoadingPending, nddLoaded } = this.props;
    const { fundingType } = this.state;
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      if (nddLoaded && !nddLoadingPending) {
        return (
          <div>
            <Col md={6}>
              <div>
                <div className="solar-container">
                  <div>
                    <NestedDonutsProgramChart data={ndd} fundingType={fundingType}/>
                    <FundingTypeSelector onChange={this.onChangeFundingType} default={null}/>
                  </div>
                </div>
                <div className="year-chart-container">amounts by year</div>
              </div>
            </Col>
            <Col md={6}>
              <div>legends</div>
            </Col>
          </div>
        );
      }
      // TODO: proper loading component.
      return (<div>Loading...</div>);
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
  callReport, loadDashboardSettings
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
