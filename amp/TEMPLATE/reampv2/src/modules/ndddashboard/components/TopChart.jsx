import React, { Component } from 'react';
import Plotly from 'plotly.js';
import createPlotlyComponent from 'react-plotly.js/factory';
import PropTypes from 'prop-types';
import { AMOUNT, CODE, DIRECT_PROGRAM } from '../utils/constants';

const Plot = createPlotlyComponent(Plotly);

class TopChart extends Component {
  render() {
    const { data } = this.props;
    console.log(data);

    const others = data.total - data.values.reduce((acc, cur) => (acc + cur.amount), 0);
    const x = data.values.map(v => v.name);
    x.push('others');
    const y = data.values.map(v => v.amount);
    y.push(others);

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
    const chartData = [
      {
        x,
        y,
        type: 'bar',
        marker: {
          color: ['rgba(90, 153, 199, 1)',
            'rgba(195, 214, 238, 1)',
            'rgba(255, 160, 87, 1)',
            'rgba(255, 204, 154, 1)',
            'rgba(99, 184, 99, 1)',
            'rgba(153, 153, 153, 1)',
            'rgba(151, 219, 152, 1)',
            'rgba(217, 91, 95, 1)',
            'rgba(253, 170, 170, 1)',
            'rgba(166, 133, 196, 1)',
            'rgba(206, 189, 218, 1)',

          ]
        }
      }
    ];
    return (
      <Plot
        data={chartData}
        layout={
          {
            width: 585,
            height: 300,
            showlegend: false,
            showLabels: false,
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
          }
        }
        config={defaultPlotlyConfiguration}
      />
    );
  }
}

TopChart.propTypes = {
  data: PropTypes.array.isRequired
};
export default TopChart;
