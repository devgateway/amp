import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import NestedDonutsProgramChart from './NestedDonutsProgramChart';
import callReport from '../actions/callReports';
import { Col } from 'react-bootstrap';

class MainDashboardContainer extends Component {
  componentDidMount() {
    this.props.callReport();
  }

  render() {
    const { error, ndd, nddLoadingPending, nddLoaded } = this.props;
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
                  <div><NestedDonutsProgramChart data={ndd}/></div>
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
