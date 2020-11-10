import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Sunburst } from '@nivo/sunburst';
import data from '../test/data.json';

class SunburstChart extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <Sunburst
        data={data}
        margin={{
          top: 40,
          right: 20,
          bottom: 20,
          left: 20
        }}
        height={500}
        width={500}
        identity="name"
        value="loc"
        cornerRadius={2}
        borderWidth={1}
        borderColor="white"
        colors="set2"
        colorBy="id"
        childColor="inherit"
        animate
        motionStiffness={90}
        motionDamping={15}
        isInteractive
    />
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

Sunburst.contextType = {};
export default connect(mapStateToProps, mapDispatchToProps)(SunburstChart);
