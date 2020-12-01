import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Col } from 'react-bootstrap';
import NestedDonutsProgramChart from './NestedDonutsProgramChart';
import FundingTypeSelector from './FundingTypeSelector';
import {
  FUNDING_TYPE,
  CHART_COLOR_MAP,
  INDIRECT_PROGRAMS,
  PROGRAMLVL1, DIRECT, AVAILABLE_COLORS
} from '../utils/constants';
import CustomLegend from '../../../utils/components/CustomLegend';
import './legends/legends.css';
import { getCustomColor, getGradient } from '../utils/Utils';

class MainDashboardContainer extends Component {
  constructor(props) {
    super(props);
    this.onChangeFundingType = this.onChangeFundingType.bind(this);
    this.state = { fundingType: null, selectedDirectProgram: null };
  }

  handleOuterChartClick(event, outerData) {
    const { selectedDirectProgram } = this.state;
    if (event.points[0].data.name === DIRECT) {
      if (!selectedDirectProgram) {
        this.setState({ selectedDirectProgram: outerData[event.points[0].i] });
      } else {
        this.setState({ selectedDirectProgram: null });
      }
    }
  }

  /* shouldComponentUpdate(nextProps, nextState, nextContext) {
    const { filters, callReport } = this.props;
    const { fundingType } = this.state;
    if (nextProps.filters !== filters) {
      this.setState({ filters: nextProps.filters, selectedDirectProgram: null });
      callReport(fundingType, nextProps.filters);
      return true;
    } else if (nextProps !== this.props || nextState !== this.state || nextContext !== this.context) {
      return true;
    }
    return false;
  } */

  onChangeFundingType(value) {
    const { callReport } = this.props;
    const { filters } = this.state;
    this.setState({ fundingType: value });
    callReport(value, filters);
  }

  getProgramLegend() {
    const { ndd } = this.props;
    const { selectedDirectProgram } = this.state;
    const legends = [];
    const directLegend = new Map();
    const indirectLegend = new Map();
    let directTotal = 0;
    let indirectTotal = 0;
    ndd.forEach(dp => {
      if (selectedDirectProgram) {
        if (dp.directProgram.programLvl1.code === selectedDirectProgram.code) {
          directTotal += this.generateLegend(dp.directProgram, 2, directLegend,
            `${PROGRAMLVL1}_${selectedDirectProgram.code}`, directTotal);
        }
      } else {
        directTotal += this.generateLegend(dp.directProgram, 1, directLegend, PROGRAMLVL1);
      }
      if (!selectedDirectProgram) { // We only need indirect if no direct is selected
        dp.indirectPrograms.forEach(idp => indirectTotal
          += this.generateLegend(idp, 1, indirectLegend, INDIRECT_PROGRAMS));
      }
    });

    legends.push({ total: directTotal, legends: [...directLegend.values()] });
    legends.push({ total: indirectTotal, legends: [...indirectLegend.values()] });
    return legends;
  }

  generateLegend(program, level, legend, programColor) {
    const programLevel = program[`programLvl${level}`];
    let prog = legend.get(programLevel.code.trim());
    if (!prog) {
      prog = {};
      prog.amount = 0;
      prog.code = programLevel.code.trim();
      prog.simpleLabel = `${programLevel.code}:${programLevel.name}`;
    }
    prog.amount += program.amount;
    getCustomColor(prog, programColor);
    legend.set(prog.code, prog);
    return program.amount;
  }

  generate2LevelColors() {
    const { selectedDirectProgram } = this.state;
    if (selectedDirectProgram && !AVAILABLE_COLORS.get(`${PROGRAMLVL1}_${selectedDirectProgram.code}`)) {
      const colors = getGradient(getCustomColor(selectedDirectProgram, PROGRAMLVL1), '#FFFFFF');
      AVAILABLE_COLORS.set(`${PROGRAMLVL1}_${selectedDirectProgram.code}`, colors);
    }
  }

  render() {
    const {
      error, ndd, nddLoadingPending, nddLoaded, dashboardSettings
    } = this.props;
    const { fundingType, selectedDirectProgram } = this.state;
    const formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      // These options are needed to round to whole numbers if that's what you want.
      // minimumFractionDigits: 0,
      // maximumFractionDigits: 0,
    });
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      this.generate2LevelColors();
      const programLegend = nddLoaded && !nddLoadingPending ? this.getProgramLegend() : null;
      return (
        <div>
          <Col md={6}>
            <div>
              <div className="chart-container">
                <div className="chart">
                  {nddLoaded && !nddLoadingPending
                    ? (
                      <NestedDonutsProgramChart
                        data={ndd}
                        selectedDirectProgram={selectedDirectProgram}
                        handleOuterChartClick={this.handleOuterChartClick.bind(this)} />
                    )
                    : <div className="loading" />}
                </div>
                <div className="buttons">
                  {dashboardSettings
                    ? (
                      <FundingTypeSelector
                        onChange={this.onChangeFundingType}
                        defaultValue={fundingType} />
                    ) : null}
                </div>
              </div>
            </div>
          </Col>
          <Col md={6}>
            {programLegend ? (
              <div className="legends-container">
                <div className="legend-title">
                  {selectedDirectProgram ? selectedDirectProgram.name : 'PNSD'}
                  :
                  <span
                    className="amount">
                    {formatter.format(programLegend[0].total)}
                  </span>
                </div>
                <CustomLegend
                  formatter={formatter}
                  data={programLegend[0].legends}
                  colorMap={CHART_COLOR_MAP.get(selectedDirectProgram ? `${PROGRAMLVL1}_${selectedDirectProgram.code}`
                    : PROGRAMLVL1)} />
                {selectedDirectProgram === null
                && (
                  <div>
                    <div className="legend-title">
                      New Deal
                      <span
                        className="amount">
                        {formatter.format(programLegend[1].total)}
                      </span>
                    </div>
                    <CustomLegend
                      formatter={formatter}
                      data={programLegend[1].legends}
                      colorMap={CHART_COLOR_MAP.get(INDIRECT_PROGRAMS)} />
                  </div>
                )}
              </div>
            ) : null}
          </Col>
        </div>
      );
    }
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);

MainDashboardContainer.propTypes = {
  filters: PropTypes.object,
  dashboardId: PropTypes.number,
  error: PropTypes.object,
  loadDashboardSettings: PropTypes.func.isRequired,
  ndd: PropTypes.array.isRequired,
  nddLoadingPending: PropTypes.bool.isRequired,
  nddLoaded: PropTypes.bool.isRequired,
  dashboardSettings: PropTypes.object
};

MainDashboardContainer.defaultProps = {
  filters: undefined
};
