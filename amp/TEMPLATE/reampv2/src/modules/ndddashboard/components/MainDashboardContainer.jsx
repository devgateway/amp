import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

class MainDashboardContainer extends Component {

  render() {
    return (<div>
      <div className="solar-container">
        <div>solar chart</div>
        <div>legends</div>
      </div>
      <div className="year-chart-container">amounts by year</div>
    </div>);
  }
}

const mapStateToProps = state => {
  return {};
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);
