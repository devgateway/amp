import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

// Dont use react-plotly directly: https://github.com/plotly/react-plotly.js/issues/135#issuecomment-501398125
import Plotly from "plotly.js";
import createPlotlyComponent from "react-plotly.js/factory";
const Plot = createPlotlyComponent(Plotly);

class SunburstChart extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <Plot
        data={
          [{
            values: [22.5, 9.2, 20.9, 23.4, 2.9, 1.5, 19.6],
            labels: ['IND11', 'IND12', 'IND13', 'IND21', 'IND22', 'IND31', 'IND32' ],
            text: 'INDIRECT',
            domain: {x: [0.15, 0.85], y: [0.15, 0.85]},
            textposition: 'inside',
            direction: 'clockwise',
            name: 'INDIRECT',
            hoverinfo: 'label+percent+name',
            hole: .5,
            type: 'pie',
            sort: false,
            textinfo: 'label',
            marker:{line: {color: 'white', 'width': 1}}
          }, {
            values: [30, 15, 12],
            labels: ['D1', 'D2', 'D3' ],
            name: 'DIRECT',
            hoverinfo: 'label+percent+name',
            textposition: 'outside',
            hole: .7,
            type: 'pie',
            sort: false,
            direction: 'clockwise',
            textinfo: 'label',
            marker:{line: {'color': 'white', 'width': 1}}
          }]
        }
        layout={{ width: 800, height: 600, title: 'TITLE' }}
      />
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SunburstChart);
