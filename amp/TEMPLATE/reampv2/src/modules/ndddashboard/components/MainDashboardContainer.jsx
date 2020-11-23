import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Col } from 'react-bootstrap';
import NestedDonutsProgramChart from './NestedDonutsProgramChart';
import { callReport } from '../actions/callReports';
import loadDashboardSettings from '../actions/loadDashboardSettings';
import FundingTypeSelector from './FundingTypeSelector';
import {
  FUNDING_TYPE,
  CHART_COLOR_MAP,
  INDIRECT_PROGRAMS,
  PROGRAMLVL1, DIRECT
} from '../utils/constants';
import CustomLegend from '../../../utils/components/CustomLegend';
import './legends/legends.css';
import { getCustomColor } from '../utils/Utils';

class MainDashboardContainer extends Component {
  constructor(props) {
    super(props);
    this.onChangeFundingType = this.onChangeFundingType.bind(this);
    this.state = { fundingType: null, selectedDirectProgram: null };
  }

  componentDidMount() {
    const { loadDashboardSettings, callReport } = this.props;
    loadDashboardSettings()
      .then(settings => callReport(settings.payload.find(i => i.id === FUNDING_TYPE).value.defaultId));
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

  onChangeFundingType(value) {
    const { callReport } = this.props;
    this.setState({ fundingType: value });
    callReport(value);
  }

  getProgramLegend() {
    const { ndd } = this.props;
    const legends = [];
    const directLegend = new Map();
    const indirectLegend = new Map();
    let directTotal = 0;
    let indirectTotal = 0;
    ndd.forEach(dp => {
      directTotal += this.generateLegend(dp.directProgram, directLegend, PROGRAMLVL1, directTotal);
      dp.indirectPrograms.forEach(idp => {
        indirectTotal += this.generateLegend(idp, indirectLegend, INDIRECT_PROGRAMS, indirectTotal);
      });
    });

    legends.push({ total: directTotal, legends: [...directLegend.values()] });
    legends.push({ total: indirectTotal, legends: [...indirectLegend.values()] });
    return legends;
  }

  generateLegend(program, legend, programColor, total) {
    let prog = legend.get(program.programLvl1.code);
    if (!prog) {
      prog = {};
      prog.amount = 0;
      prog.code = program.programLvl1.code;
      prog.simpleLabel = `${program.programLvl1.code}:${program.programLvl1.name}`;
    }
    prog.amount += program.amount;
    total += program.amount;
    getCustomColor(prog, programColor);
    legend.set(prog.code, prog);
    return total;
  }

  render() {
    const {
      error, ndd, nddLoadingPending, nddLoaded, dashboardSettings
    } = this.props;
    const { fundingType } = this.state;
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
      const programLegend = nddLoaded && !nddLoadingPending ? this.getProgramLegend() : null;
      const { selectedDirectProgram } = this.state;
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
                  PNSD
                  <span
                    className="amount">
                    {formatter.format(programLegend[0].total)}
                  </span>
                </div>
                <CustomLegend
                  formatter={formatter}
                  data={programLegend[0].legends}
                  colorMap={CHART_COLOR_MAP.get(PROGRAMLVL1)} />
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
            ) : null}
          </Col>
        </div>
      );
    }
  }
}

const mapStateToProps = state => ({
  ndd: state.reportsReducer.ndd,
  dashboardSettings: state.dashboardSettingsReducer.dashboardSettings,
  error: state.reportsReducer.error,
  nddLoaded: state.reportsReducer.nddLoaded,
  nddLoadingPending: state.reportsReducer.nddLoadingPending,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  callReport,
  loadDashboardSettings
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);

MainDashboardContainer.propTypes = {
  callReport: PropTypes.func.isRequired,
  error: PropTypes.object,
  loadDashboardSettings: PropTypes.func.isRequired,
  ndd: PropTypes.array.isRequired,
  nddLoadingPending: PropTypes.bool.isRequired
};

MainDashboardContainer.defaultProps = {
  error: undefined
};
