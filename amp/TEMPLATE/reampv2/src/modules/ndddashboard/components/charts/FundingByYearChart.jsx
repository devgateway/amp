import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

// Dont use react-plotly directly: https://github.com/plotly/react-plotly.js/issues/135#issuecomment-501398125
import Plotly from 'plotly.js';
import createPlotlyComponent from 'react-plotly.js/factory';
import { callYearDetailReport } from '../../actions/callReports';
import {
  DIRECT_PROGRAM, INDIRECT_PROGRAMS, PROGRAMLVL1, CODE,
  PROGRAMLVL2, TRN_PREFIX, CURRENCY_CODE
} from '../../utils/constants';
import {
  formatNumberWithSettings, getCustomColor
} from '../../utils/Utils';
// eslint-disable-next-line no-unused-vars
import styles from '../styles.css';
import ToolTip from '../tooltips/ToolTip';
import YearDetail from './YearDetail';

const Plot = createPlotlyComponent(Plotly);

const SRC_DIRECT = '0';
const SRC_INDIRECT = '1';

class FundingByYearChart extends Component {
  constructor(props) {
    super(props);
    this.getValues = this.getValues.bind(this);
    this.state = {
      source: SRC_DIRECT, showLegend: false, legendTop: 0, legendLeft: 0, tooltipData: null, showDetail: false
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
        const program = !selectedDirectProgram ? i[PROGRAMLVL1] : i[PROGRAMLVL2];
        const item = ret.find(j => j[CODE] === program[CODE]);
        const auxAmounts = i.amountsByYear;
        if (item) {
          item.values = this.sortAmountsByYear(this.addAmountsByYear(item.values, Object.keys(auxAmounts)
            .map(j => ({ [j]: auxAmounts[j] }))));
        } else {
          ret.push({
            [CODE]: program[CODE],
            name: program.name,
            values: Object.keys(auxAmounts).map(j => ({ [j]: auxAmounts[j] })),
            id: program.objectId
          });
        }
      });
      ret.forEach(i => {
        i.values = this.sortAmountsByYear(this.fillGapsInYears(i.values));
      });
    }
    return ret;
  }

  // eslint-disable-next-line class-methods-use-this,react/sort-comp
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
    const { text } = data.points[0].data;
    const lines = Math.ceil(text.length / 30);
    this.setState({
      showLegend: true,
      legendTop: data.event.pointerY - 20 - (lines * 25),
      legendLeft: data.event.pointerX - 100,
      tooltipData: data
    });
  }

  onUnHover = () => {
    this.setState({ showLegend: false, tooltipData: null });
  }

  onClick = (event) => {
    const {
      _callYearDetailReport, settings, filters, fundingType, selectedPrograms
    } = this.props;
    const { source } = this.state;
    this.setState({ showDetail: true, year: event.points[0].x, programName: event.points[0].data.text });
    const newSettings = { ...settings };
    newSettings.isShowIndirectDataForActivitiesDetail = (source === SRC_INDIRECT);
    newSettings.dontUseMapping = (selectedPrograms && selectedPrograms.length === 1);
    _callYearDetailReport(fundingType,
      filters,
      event.points[0].data.extraData.find(i => i.name === event.points[0].data.text).id,
      event.points[0].x,
      newSettings);
  }

  createModalWindow = () => {
    const {
      translations, yearDetailPending, yearDetail, error, fundingType, settings, globalSettings
    } = this.props;
    const { showDetail, year, programName } = this.state;
    return (
      <YearDetail
        translations={translations}
        show={showDetail}
        handleClose={() => { this.setState({ showDetail: false }); }}
        data={yearDetail}
        loading={yearDetailPending}
        error={error}
        fundingType={fundingType}
        currencyCode={settings[CURRENCY_CODE]}
        globalSettings={globalSettings}
        title={`${year} ${programName}`} />
    );
  }

  createTooltip = () => {
    const { tooltipData } = this.state;
    const { settings, translations, globalSettings } = this.props;
    if (tooltipData) {
      const year = tooltipData.points[0].x;
      return (
        <ToolTip
          color={tooltipData.points[0].data.line.color}
          currencyCode={settings[CURRENCY_CODE]}
          formattedValue={formatNumberWithSettings(settings[CURRENCY_CODE], translations, globalSettings,
            tooltipData.points[0].y, true)}
          titleLabel={`${year} ${tooltipData.points[0].data.text}`}
          total={tooltipData.points[0].data.extraData
            .reduce((a, b) => (a + (b.values.find(i => i[year]) ? b.values.find(i => i[year])[year] : 0)), 0)}
          value={tooltipData.points[0].y}
          minWidth={400}
          isYearTotal
          globalSettings={globalSettings}
        />
      );
    }
    return null;
  }

  getColor(source, i) {
    const { selectedDirectProgram } = this.props;
    if (source === SRC_DIRECT) {
      if (selectedDirectProgram == null) {
        return getCustomColor(i, PROGRAMLVL1);
      } else {
        return getCustomColor(i, `${PROGRAMLVL1}_${selectedDirectProgram.code}`);
      }
    } else {
      return getCustomColor(i, INDIRECT_PROGRAMS);
    }
  }

  canEnableShowIndirectDataOption = () => {
    const { data, selectedDirectProgram } = this.props;
    const ret = data.find(d => d.indirectPrograms.length > 0) && !selectedDirectProgram;
    return ret;
  }

  render() {
    const { translations, globalSettings } = this.props;
    const {
      source, showLegend, legendTop, legendLeft
    } = this.state;
    const directData = this.getValues();
    /* const transition = {
      duration: 2000,
      easing: 'cubic-in-out'
    }; */
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
          {this.canEnableShowIndirectDataOption() ? (
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
          ) : null }
        </div>
        <Plot
          key="fundingByYearChart"
          data={
            directData.map(i => ({
              x: i.values.map(j => Object.keys(j)[0]),
              y: i.values.map(j => j[Object.keys(j)[0]]),
              text: i.name,
              extraData: directData,
              hoverinfo: 'none',
              name: '',
              type: 'scatter',
              mode: 'lines+markers',
              line: {
                shape: 'spline',
                smoothing: 0.5,
                dash: 'solid',
                width: 2,
                color: this.getColor(source, i),
              },
              marker: {
                size: 7,
                color: 'white',
                line: {
                  color: this.getColor(source, i),
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
            /* transition, */
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
              fixedrange: true,
              automargin: true
            },
            yaxis: {
              automargin: true,
              fixedrange: true
            },
            hovermode: 'closest',
            separators: globalSettings.decimalSeparator + globalSettings.groupSeparator
          }}
          config={{ displaylogo: false, responsive: true, displayModeBar: false }}
          useResizeHandler
          style={{ width: '100%', height: '100%', cursor: 'pointer' }}
          onHover={event => this.onHover(event)}
          onUnhover={() => this.onUnHover()}
          onClick={(event) => this.onClick(event)}
        />
        <div
          style={{
            display: (!showLegend ? 'none' : 'block'),
            top: legendTop,
            left: legendLeft
          }}
          className="line-legend-wrapper">
          {this.createTooltip()}
        </div>
        {this.createModalWindow()}
      </div>
    );
  }
}

FundingByYearChart.propTypes = {
  data: PropTypes.array.isRequired,
  selectedDirectProgram: PropTypes.object,
  translations: PropTypes.object.isRequired,
  settings: PropTypes.object.isRequired,
  globalSettings: PropTypes.object.isRequired,
  _callYearDetailReport: PropTypes.func.isRequired,
  fundingType: PropTypes.string,
  filters: PropTypes.object.isRequired,
  yearDetailPending: PropTypes.bool.isRequired,
  yearDetail: PropTypes.array,
  error: PropTypes.object,
  selectedPrograms: PropTypes.array.isRequired
};

FundingByYearChart.defaultProps = {
  selectedDirectProgram: undefined,
  fundingType: undefined,
  yearDetail: null,
  error: undefined
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  yearDetailPending: state.reportsReducer.yearDetailPending,
  yearDetail: state.reportsReducer.yearDetail,
  error: state.reportsReducer.error
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _callYearDetailReport: callYearDetailReport
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(FundingByYearChart);
