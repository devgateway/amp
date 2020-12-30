import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

// Dont use react-plotly directly: https://github.com/plotly/react-plotly.js/issues/135#issuecomment-501398125
import Plotly from 'plotly.js';
import createPlotlyComponent from 'react-plotly.js/factory';
import {
  DIRECT_PROGRAM, INDIRECT_PROGRAMS, PROGRAMLVL1, AMOUNT, CODE, DIRECT, INDIRECT,
  TRANSITIONS, PROGRAMLVL2, AVAILABLE_COLORS, TRN_PREFIX, CURRENCY_CODE
} from '../../utils/constants';
import {
  addAlpha, formatKMB, getCustomColor, getGradient
} from '../../utils/Utils';
import styles from '../styles.css';
import ToolTip from '../tooltips/ToolTip';

const Plot = createPlotlyComponent(Plotly);

const SRC_DIRECT = '0';
const SRC_INDIRECT = '1';

class FundingByYearChart extends Component {
  constructor(props) {
    super(props);
    this.getValues = this.getValues.bind(this);
    this.state = {
      source: SRC_DIRECT, showLegend: false, legendTop: 0, legendLeft: 0, tooltipData: null
    };
  }

  onChangeSource = (value) => {
    this.setState({ source: value.target.value });
  }

  getValues() {
    const { selectedDirectProgram } = this.props;
    const { source } = this.state;
    const ret = [];
    const { data } = this.props;
    if (data && data.length > 0) {
      const sourceData = (source === SRC_DIRECT
        ? data.map(i => i[DIRECT_PROGRAM])
        : data.map(i => i[INDIRECT_PROGRAMS]).flat());
      const filteredData = !selectedDirectProgram ? sourceData
        // TODO: maybe we can show the indirect funding for the selected direct program.
        : (sourceData.filter(i => i[PROGRAMLVL1][CODE] === selectedDirectProgram[CODE]));
      filteredData.forEach(i => {
        const directProgram = !selectedDirectProgram ? i[PROGRAMLVL1] : i[PROGRAMLVL2];
        const item = ret.find(j => j[CODE] === directProgram[CODE]);
        const auxAmounts = i.amountsByYear;
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
    return ret;
  }

  // eslint-disable-next-line class-methods-use-this
  sortAmountsByYear(values) {
    return values.sort((i, j) => (Object.keys(i)[0] - Object.keys(j)[0]));
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

  onHover = (data) => {
    debugger
    const { selectedDirectProgram } = this.props;
    if (selectedDirectProgram === null || data.points[0].data.name === DIRECT) {
      // Disable tooltip when outer ring is selected
      this.setState({
        showLegend: true,
        legendTop: data.event.pageY - 200,
        legendLeft: data.event.pageX - 360,
        tooltipData: data
      });
    }
  }

  onUnHover = () => {
    this.setState({ showLegend: false, tooltipData: null });
  }

  createTooltip = () => {
    const { tooltipData } = this.state;
    const { settings, translations } = this.props;
    if (tooltipData) {
      const formatter = formatKMB(translations); // TODO: get precision and separator from GS.
      const program = tooltipData.points[0].data.extraData[tooltipData.points[0].i];
      const totalAmount = tooltipData.points[0].data.extraData.reduce((i, j) => (i + j.amount), 0);
      return (
        <ToolTip
          color={tooltipData.points[0].color}
          currencyCode={settings[CURRENCY_CODE]}
          formattedValue={formatter(program.amount)}
          titleLabel={`${program.name}`}
          total={totalAmount}
          value={program.amount}
          minWidth="400px"
        />
      );
    }
    return null;
  }

  render() {
    const { selectedDirectProgram, translations } = this.props;
    const { source, showLegend, legendTop, legendLeft } = this.state;
    const directData = this.getValues();
    const transition = {
      duration: 2000,
      easing: 'cubic-in-out'
    };
    const annotations = directData.length === 0 ? [
      {
        text: translations[`${TRN_PREFIX}no-data`],
        xref: 'paper',
        yref: 'paper',
        showarrow: false,
        font: {
          size: 28
        }
      }
    ] : [];
    return (
      <div>
        <div>
          <div className="radio-fy-source">
            <input
              type="radio"
              id="fy-direct"
              name="fy-source"
              value="0"
              checked={source === SRC_DIRECT ? 'checked' : null}
              onChange={this.onChangeSource} />
            <label htmlFor="fy-direct">
              {translations[`${TRN_PREFIX}fy-direct`]}
            </label>
          </div>
          <div className="radio-fy-source">
            <input
              type="radio"
              id="fy-indirect"
              name="fy-source"
              value="1"
              checked={source === SRC_INDIRECT ? 'checked' : null}
              onChange={this.onChangeSource} />
            <label htmlFor="fy-indirect">
              {translations[`${TRN_PREFIX}fy-indirect`]}
            </label>
          </div>
        </div>
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
                width: 2,
                color: !selectedDirectProgram ? getCustomColor(i, PROGRAMLVL1) : getCustomColor(i, INDIRECT_PROGRAMS)
              },
              marker: {
                size: 7,
                color: 'white',
                line: {
                  color: !selectedDirectProgram ? getCustomColor(i, PROGRAMLVL1) : getCustomColor(i, INDIRECT_PROGRAMS),
                  width: 2,
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
          onHover={event => this.onHover(event)}
          onUnhover={() => this.onUnHover()}
        />
        <div
          style={{
            display: (!showLegend ? 'none' : 'block'),
            top: legendTop,
            left: legendLeft
          }}
          className="pie-lengend-wrapper">
          {this.createTooltip()}
        </div>
      </div>
    );
  }
}

FundingByYearChart.propTypes = {
  data: PropTypes.array.isRequired,
  selectedDirectProgram: PropTypes.object.isRequired,
  translations: PropTypes.array.isRequired
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(FundingByYearChart);
