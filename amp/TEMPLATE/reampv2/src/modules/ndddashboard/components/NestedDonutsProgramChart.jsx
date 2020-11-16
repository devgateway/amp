import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { CSSTransitionGroup } from 'react-transition-group'

// Dont use react-plotly directly: https://github.com/plotly/react-plotly.js/issues/135#issuecomment-501398125
import Plotly from "plotly.js";
import createPlotlyComponent from "react-plotly.js/factory";
import { DIRECT_PROGRAM, INDIRECT_PROGRAMS, PROGRAMLVL1, AMOUNT, CODE, DIRECT, INDIRECT } from '../utils/constants';
import {intToRGB, hashCode, addAlpha} from '../utils/Utils';
import styles from './styles.css'

const Plot = createPlotlyComponent(Plotly);

class NestedDonutsProgramChart extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedDirectProgram: null
    };
    this.extractOuterData = this.extractOuterData.bind(this);
    this.extractInnerData = this.extractInnerData.bind(this);
    this.handleOuterChartClick = this.handleOuterChartClick.bind(this);
    this.calculateOpacity = this.calculateOpacity.bind(this);
  }

  /**
   * Create an array with each unique Direct Program Level 1 adding the amounts when it has multiple children.
   * @returns {[]}
   */
  extractOuterData() {
    const ret = [];
    const {data} = this.props;
    if (data && data.length > 0) {
      const totalAmount = data.reduce((acc, cur) => (acc + cur[DIRECT_PROGRAM][AMOUNT]), 0);
      data.forEach(i => {
        const p1 = i[DIRECT_PROGRAM][PROGRAMLVL1];
        if (ret.filter(d => d[CODE] === p1[CODE]).length === 0) {
          const item = {
            [CODE]: p1[CODE],
            name: p1.name,
            [AMOUNT]: data.filter(d2 => d2[DIRECT_PROGRAM][PROGRAMLVL1][CODE] === p1[CODE])
              .reduce((accumulator, currentValue) => (
                accumulator + currentValue[DIRECT_PROGRAM][AMOUNT]
              ), 0)
          };
          item.percentageInTotal = item[AMOUNT] / totalAmount * 100;
          ret.push(item);
        }
      });
    }
    return ret;
  }

  extractInnerData() {
    const ret = [];
    const {data} = this.props;
    if (data && data.length > 0) {
      const outerData = this.extractOuterData();
      outerData.forEach((i, index) => {
        const innerSubGroup = [];
        const subProgramsData = data.filter(j => j[DIRECT_PROGRAM][PROGRAMLVL1][CODE] === i[CODE]);
        subProgramsData.forEach(j => {
          j[INDIRECT_PROGRAMS].forEach(k => {
            if (innerSubGroup.filter(l => l[CODE] === k[PROGRAMLVL1][CODE]).length === 0) {
              innerSubGroup.push({
                [CODE]: k[PROGRAMLVL1][CODE] + Array(index).fill(' ').join(''),
                originalAmount: k[AMOUNT],
                name: k[PROGRAMLVL1].name,
                directProgramCode: j[DIRECT_PROGRAM][PROGRAMLVL1][CODE]
              });
            }
          });
        });
        const totalForSubGroup = innerSubGroup.reduce((acc, cur) => (acc + cur.originalAmount), 0);
        innerSubGroup.forEach(j => {
          j.percentageInSubGroup = (j.originalAmount * 100 / totalForSubGroup);
        });
        ret.push(innerSubGroup);
      });
    }
    return ret;
  }

  innerDataToChartValues(data, outerData) {
    const ret = [];
    if (data) {
      data.forEach(i => {
        i.forEach(j=>{
          const parentPercentage = outerData.find(k => k[CODE] === j.directProgramCode).percentageInTotal;
          ret.push({[CODE]: j[CODE], name: j.name, [AMOUNT]: j.percentageInSubGroup / 100 * parentPercentage});
        });
      });
    }
    return ret;
  }

  handleOuterChartClick(event, outerData) {
    const { selectedDirectProgram } = this.state;
    if (event.points[0].data.name === DIRECT) {
      console.error(event);
      console.error(outerData);
      if (!selectedDirectProgram) {
        this.setState({ selectedDirectProgram: outerData[event.points[0].i] });
      } else {
        this.setState({ selectedDirectProgram: null });
      }
    }
  }

  calculateOpacity(colors, data) {
    const { selectedDirectProgram } = this.state;
    let newColors = [];
    if (selectedDirectProgram) {
      colors.forEach((c, i) => {
        const index = data.findIndex(i => i.code === selectedDirectProgram.code);
        if (i !== index) {
          c = addAlpha(c, 0.25);
          newColors.push(c);
        } else {
          newColors.push(c);
        }
      });
    } else {
      newColors = colors;
    }
    return newColors;
  }

  render() {
    const { selectedDirectProgram } = this.state;
    const outerData = this.extractOuterData();
    const innerData = this.extractInnerData();
    const innerDataForChart = this.innerDataToChartValues(innerData, outerData);
    const innerColors = this.calculateOpacity(innerDataForChart.map(i => intToRGB(hashCode(i.name))),
      innerDataForChart);
    const outerColors = this.calculateOpacity(outerData.map(i => intToRGB(hashCode(i.name))), outerData);
    const transition = {
      duration: 2000,
      easing: 'cubic-in-out'
    };
    // Note: Remove prop 'key' if you want to disable the fade effect after clicking the outer ring.
    return (
      <CSSTransitionGroup
        key={selectedDirectProgram}
        transitionName="solar-chart"
        transitionAppear={true}
        transitionAppearTimeout={100}>
        <Plot
          key="solarChart"
          data={
            [{
              values: innerDataForChart.map(i => i[AMOUNT]),
              labels: innerDataForChart.map(i => i[CODE]),
              text: INDIRECT,
              domain: {
                x: [0.15, 0.85],
                y: [0.15, 0.85]
              },
              textposition: 'inside',
              direction: 'clockwise',
              name: INDIRECT,
              hoverinfo: 'label',
              hole: .5,
              type: 'pie',
              sort: false,
              textinfo: 'label',
              marker: {
                colors: innerColors,
                line: {
                  color: 'white',
                  'width': 1
                }
              }
            }, {
              values: outerData.map(i => i[AMOUNT]),
              labels: outerData.map(i => i[CODE]),
              name: DIRECT,
              hoverinfo: 'percent+label',
              textposition: 'outside',
              hole: .7,
              type: 'pie',
              sort: false,
              direction: 'clockwise',
              textinfo: 'label',
              marker: {
                colors: outerColors,
                line: {
                  'color': 'white',
                  'width': 1
                }
              }
            }]
          }
          layout={{
            width: 800,
            height: 600,
            title: 'Title',
            showlegend: false,
            displaylogo: false,
            transition
          }}
          config={{ displaylogo: false }}
          onClick={event => this.handleOuterChartClick(event, outerData)}
        />
      </CSSTransitionGroup>
    );
  }
}

NestedDonutsProgramChart.propTypes = {
  data: PropTypes.array.isRequired
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(NestedDonutsProgramChart);
