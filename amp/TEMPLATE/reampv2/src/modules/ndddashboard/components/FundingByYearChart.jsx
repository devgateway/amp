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
      data.forEach(i => {
        const directProgram = i[DIRECT_PROGRAM][PROGRAMLVL1];
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
    }
    console.info(ret);
    return ret;
  }

  sortAmountsByYear(values) {
    return values.sort((i, j) => (Object.keys(i)[0] > Object.keys(j)[0]));
  }

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
    values.forEach((item, i) => {
      const key = Object.keys(item)[0];
      const value = item[key];
      const currValue = current.find(item2 => item2[key]);
      if (currValue === undefined || currValue === null) {
        current.push({ [key]: value });
      }
    });
    return ret;
  }

  fillGapsInYears(currentList, values) {
    const ret = [];
    const currentMin = Math.min(currentList);
    const currentMax = Math.max(currentList);
    const valuesMin = Math.min(...Object.keys(values).map(i => parseInt(i, 10)));
    const valuesMax = Math.max(...Object.keys(values).map(i => parseInt(i, 10)));
    const min = currentMin > 0 && currentMin <= valuesMin ? currentMin : valuesMin;
    const max = currentMax > 0 && currentMax >= valuesMax ? currentMax : valuesMax;
    for (let i = min; i <= max; i++) {
      ret.push({ year: i, amount: values[i] });
    }
    debugger;
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
    // Note: Remove prop 'key' if you want to disable the fade effect after clicking the outer ring.
    return (
      <Plot
        key="fundingByYearChart"
        data={
            directData.map(i => ({
              x: i.values.map(j => Object.keys(j)[0]),
              y: i.values.map(j => j[Object.keys(j)[0]]),
              name: i.code,
              type: 'scatter',
              mode: 'lines+markers',
              line: {
                dash: 'solid',
                width: 3
              }
            }))
          }
        layout={{
          autosize: true,
          paper_bgcolor: 'rgba(0,0,0,0)',
          width: '100%',
          height: 400,
          title: '',
          showlegend: true,
          transition,
          margin: {
            l: 50,
            r: 10,
            b: 30,
            t: 20,
            pad: 10
          },
          annotations
        }}
        config={{ displaylogo: false }}
        onClick={event => this.handleOuterChartClick(event, directData)}
        />
    );
  }
}

FundingByYearChart.propTypes = {
  data: PropTypes.array.isRequired,
  handleOuterChartClick: PropTypes.func.isRequired,
  selectedDirectProgram: PropTypes.object.isRequired
};

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(FundingByYearChart);
