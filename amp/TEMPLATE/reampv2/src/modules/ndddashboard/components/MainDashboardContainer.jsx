import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import SunburstChart from './SunburstChart';
import callReport from '../actions/callReports';

class MainDashboardContainer extends Component {
  componentDidMount() {
    this.props.callReport();
  }

  render() {
    const { error } = this.props;
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      return (
        <div>
          <div className="solar-container">
            <div><SunburstChart /></div>
            <div>legends</div>
          </div>
          <div className="year-chart-container">amounts by year</div>
        </div>
      );
    }
  }
}

const mapStateToProps = state => ({
  ndd: state.reportsReducer.ndd,
  error: state.reportsReducer.error,
  nddLoaded: state.reportsReducer.nddLoaded,
  nddLoadingPending: state.reportsReducer.nddLoadingPending,
});
const mapDispatchToProps = dispatch => bindActionCreators({
  callReport
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);

MainDashboardContainer.propTypes = {
  callReport: PropTypes.func.isRequired,
  error: PropTypes.object
};
MainDashboardContainer.defaultProps = {
  error: undefined
};
