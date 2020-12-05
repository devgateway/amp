import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

// Dont use react-plotly directly: https://github.com/plotly/react-plotly.js/issues/135#issuecomment-501398125
import Plotly from 'plotly.js';
import createPlotlyComponent from 'react-plotly.js/factory';
import {
  DIRECT_PROGRAM, INDIRECT_PROGRAMS, PROGRAMLVL1, AMOUNT, CODE, DIRECT, INDIRECT,
  TRANSITIONS, PROGRAMLVL2, AVAILABLE_COLORS
} from '../utils/constants';
import {
  addAlpha, getCustomColor, getGradient
} from '../utils/Utils';
import styles from './styles.css';

const Plot = createPlotlyComponent(Plotly);

class FundingByYearChart extends Component {
  constructor(props) {
    super(props);
    this.getDirectValues = this.getDirectValues.bind(this);
  }

  getDirectValues() {
    const { selectedDirectProgram } = this.props;
    const ret = [];
    const { data } = this.props;
    if (data && data.length > 0) {
      const filteredData = !selectedDirectProgram ? data
        : (data.filter(i => i[DIRECT_PROGRAM][PROGRAMLVL1][CODE] === selectedDirectProgram[CODE]));
      filteredData.forEach(i => {
        const directProgram = !selectedDirectProgram ? i[DIRECT_PROGRAM][PROGRAMLVL1] : i[DIRECT_PROGRAM][PROGRAMLVL2];
        const item = ret.find(j => j[CODE] === directProgram[CODE]);
        const auxAmounts = i[DIRECT_PROGRAM].amountsByYear;
        if (item) {
          item.values = this.sortAmountsByYear(this.addAmountsByYear(item.values, Object.keys(auxAmounts)
            .map(j => ({ [j]: auxAmounts[j] }))));
        } else {
          ret.push({
            [CODE]: directProgram[CODE],
            name: directProgram.name,
            values: Object.keys(auxAmounts).map(j => ({ [j]: auxAmounts[j] }))
          });
        }
      });
      ret.forEach(i => {
        i.values = this.sortAmountsByYear(this.fillGapsInYears(i.values));
      });
    }
    console.info(ret);
    return ret;
  }

  // eslint-disable-next-line class-methods-use-this
  sortAmountsByYear(values) {
    return values.sort((i, j) => (Object.keys(i)[0] > Object.keys(j)[0]));
  }

  // eslint-disable-next-line class-methods-use-this
  addAmountsByYear(current, values) {
    const ret = current;
    current.forEach((item, i) => {
      const key = Object.keys(item)[0];
      const value = item[key];
      const newValue = values.find(item2 => item2[key]);
      if (newValue) {
        current[i][key] = value + newValue[key];
      }
    });
    values.forEach(item => {
      const key = Object.keys(item)[0];
      const value = item[key];
      const currValue = current.find(item2 => item2[key]);
      if (currValue === undefined || currValue === null) {
        current.push({ [key]: value });
      }
    });
    return ret;
  }

  // eslint-disable-next-line class-methods-use-this
  fillGapsInYears(values) {
    const ret = values;
    const min = Math.min(...values.map(i => parseInt(Object.keys(i)[0], 10)));
    const max = Math.max(...values.map(i => parseInt(Object.keys(i)[0], 10)));
    for (let i = min; i <= max; i++) {
      if (!values.find(j => Object.keys(j)[0] === i.toString())) {
        values.push({ [i]: 0 });
      }
    }
    return ret;
  }

  render() {
    const { selectedDirectProgram } = this.props;
    const directData = this.getDirectValues();
    const transition = {
      duration: 2000,
      easing: 'cubic-in-out'
    };
    const annotations = directData.length === 0 ? [
      {
        text: 'No matching data found',
        xref: 'paper',
        yref: 'paper',
        showarrow: false,
        font: {
          size: 28
        }
      }
    ] : [];
    return (
      <Plot
        key="fundingByYearChart"
        data={
            directData.map(i => ({
              x: i.values.map(j => Object.keys(j)[0]),
              y: i.values.map(j => j[Object.keys(j)[0]]),
              text: (`${i[CODE]}: ${i.name}`).substr(0, 50),
              name: '',
              type: 'scatter',
              mode: 'lines+markers',
              line: {
                shape: 'spline',
                smoothing: 0.5,
                dash: 'solid',
                width: 3,
                color: !selectedDirectProgram ? getCustomColor(i, PROGRAMLVL1) : getCustomColor(i, INDIRECT_PROGRAMS)
              },
              marker: {
                size: 7.5,
                line: {
                  color: !selectedDirectProgram ? getCustomColor(i, PROGRAMLVL1) : getCustomColor(i, INDIRECT_PROGRAMS),
                  width: 1,
                }
              }
            }))
          }
        layout={{
          autosize: true,
          paper_bgcolor: 'rgba(0,0,0,0)',
          height: 400,
          title: '',
          showlegend: false,
          transition,
          margin: {
            l: 50,
            r: 30,
            b: 50,
            t: 20,
            pad: 10
          },
          annotations,
          xaxis: {
            showgrid: false,
            showline: false,
            autotick: false,
            tickangle: 45,
          },
          yaxis: {
            automargin: false,
          }
        }}
        config={{ displaylogo: false, responsive: true }}
        useResizeHandler
        style={{ width: '100%', height: '100%' }}
        />
    );
  }
}

FundingByYearChart.propTypes = {
  data: PropTypes.array.isRequired,
  selectedDirectProgram: PropTypes.object.isRequired
};

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(FundingByYearChart);
