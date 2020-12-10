import React, { Component } from 'react';
import Plotly from 'plotly.js';
import createPlotlyComponent from 'react-plotly.js/factory';
import PropTypes from 'prop-types';
import { AMOUNT, CODE, DIRECT_PROGRAM } from '../utils/constants';

const Plot = createPlotlyComponent(Plotly);

class TopChart extends Component {
  texto(value) {
    console.log(value);
    return 'hola';
  }

  render() {
    const { data } = this.props;
    //  Modebar Buttons names at https://github.com/plotly/plotly.js/blob/master/src/components/modebar/buttons.js
    /* modeBarButtonsToRemove: ['sendDataToCloud', 'autoScale2d', 'hoverClosestCartesian', 'hoverCompareCartesian',
        'lasso2d', 'zoom2d', 'pan2d', 'select2d', 'zoomIn2d', 'zoomOut2d',
        'resetScale2d', 'toImage', 'toggleSpikelines'], */
    /* modeBarButtonsToAdd: [
      {
        name: 'color toggler',
        icon: icon1,
        click: function(gd) {
          var newColor = colors[Math.floor(3 * Math.random())]
          Plotly.restyle(gd, 'line.color', newColor)
        }} */
    const defaultPlotlyConfiguration = {

      displayModeBar: false,
      displaylogo: false,
      showTips: false,
      staticPlot: true
    };
    const others = data.total - data.values.reduce((acc, cur) => (acc + cur.amount), 0);
    const xValue = data.values.map(v => v.name);
    xValue.push('others');
    const yValue = data.values.map(v => v.amount);
    yValue.push(others);
    const trace1 = {
      x: xValue,
      y: yValue,
      type: 'bar',
      texttemplate: '%{y:.3s}',
      textposition: 'outside',
      hoverinfo: 'none',
      marker: {
        color: ['rgba(90, 153, 199, 1)',
          'rgba(195, 214, 238, 1)',
          'rgba(255, 160, 87, 1)',
          'rgba(255, 204, 154, 1)',
          'rgba(99, 184, 99, 1)',
          'rgba(151, 219, 152, 1)',
          'rgba(153, 153, 153, 1)',
          'rgba(217, 91, 95, 1)',
          'rgba(253, 170, 170, 1)',
          'rgba(166, 133, 196, 1)',
          'rgba(206, 189, 218, 1)',

        ]
      }
    };
    const theLayout = {
      barmode: 'stack',
      width: 553,
      height: 300,
      xaxis: {
        showgrid: false,
        zeroline: false,
        visible: false,
      },
      yaxis: {
        showgrid: false,
        zeroline: false,
        visible: false,
      }
    };
    return (
      <Plot
        data={[trace1]}
        layout={theLayout}
        config={defaultPlotlyConfiguration}
      />
    );
  }
}

TopChart.propTypes = {
  data: PropTypes.array.isRequired
};
export default TopChart;
