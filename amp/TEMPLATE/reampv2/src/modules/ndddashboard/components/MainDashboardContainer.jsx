import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Col } from 'react-bootstrap';
import NestedDonutsProgramChart from './charts/NestedDonutsProgramChart';
import FundingTypeSelector from './FundingTypeSelector';
import {
  CHART_COLOR_MAP,
  INDIRECT_PROGRAMS,
  PROGRAMLVL1, DIRECT, AVAILABLE_COLORS, FUNDING_TYPE
} from '../utils/constants';
import CustomLegend from '../../../utils/components/CustomLegend';
import './legends/legends.css';
import { getCustomColor, getGradient, extractPrograms } from '../utils/Utils';
import TopChart from './TopChart';
import { callTopReport } from '../actions/callReports';
import FundingByYearChart from './charts/FundingByYearChart';
import PieChartTypeSelector from './PieChartTypeSelector';
import { NDDTranslationContext } from './StartUp';

class MainDashboardContainer extends Component {

  getProgramLegend() {
    const { ndd } = this.props;
    const { selectedDirectProgram } = this.props;
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

  generate2LevelColors() {
    const { selectedDirectProgram } = this.props;
    if (selectedDirectProgram && !AVAILABLE_COLORS.get(`${PROGRAMLVL1}_${selectedDirectProgram.code}`)) {
      const colors = getGradient(getCustomColor(selectedDirectProgram, PROGRAMLVL1), '#FFFFFF');
      AVAILABLE_COLORS.set(`${PROGRAMLVL1}_${selectedDirectProgram.code}`, colors);
    }
  }

  getTopChart() {
    const { topLoaded, topLoadingPending, top } = this.props;
    const { translations } = this.context;
    return topLoaded && !topLoadingPending ? (
      <div>
        <div className="legend-title row funding-sources-title">
          <div className="col-md-8">
            {translations['amp.ndd.dashboard:top-donor-agencies']}
          </div>
          <div className="amount col-md-4">
            {`${top.sumarizedTotal} ${top.currency}`}
          </div>
        </div>
        <TopChart data={top} />
      </div>
    ) : <div className="loading" />;
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

  render() {
    const {
      error, ndd, nddLoadingPending, nddLoaded, dashboardSettings, onChangeFundingType, fundingType, mapping,
      onChangeProgram, selectedPrograms, noIndirectMapping, top, settings, selectedDirectProgram, handleOuterChartClick,
      globalSettings
    } = this.props;
    const { translations } = this.context;
    const formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: top && top.currency ? top.currency : 'USD',
      // These options are needed to round to whole numbers if that's what you want.
      // minimumFractionDigits: 0,
      // maximumFractionDigits: 0,
    });
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      const programs = extractPrograms(mapping, noIndirectMapping);
      this.generate2LevelColors();
      const programLegend = nddLoaded && !nddLoadingPending ? this.getProgramLegend() : null;
      return (
        <div>
          <Col md={5}>
            <div>
              <div className="chart-container">
                <div className="chart">
                  <div className="section_title">
                    <span>
                      {programs.direct
                        ? (`${programs.direct.value} and ${programs.indirect1.value}`)
                        : 'Loading...'}
                    </span>
                  </div>
                  {nddLoaded && !nddLoadingPending
                    ? (
                      <div>
                        <div>
                          {dashboardSettings
                            ? (
                              <PieChartTypeSelector
                                onChange={onChangeProgram}
                                defaultValue={fundingType}
                                mapping={mapping}
                                noIndirectMapping={noIndirectMapping}
                                selectedPrograms={selectedPrograms} />
                            ) : null}
                        </div>
                        <NestedDonutsProgramChart
                          data={ndd}
                          settings={settings}
                          selectedDirectProgram={selectedDirectProgram}
                          handleOuterChartClick={handleOuterChartClick} />
                      </div>
                    )
                    : <div className="loading" />}
                </div>
                <div className="buttons">
                  {dashboardSettings
                    ? (
                      <FundingTypeSelector
                        onChange={onChangeFundingType}
                        defaultValue={fundingType}
                        noIndirectMapping={noIndirectMapping} />
                    ) : null}
                </div>
              </div>
            </div>
          </Col>
          <Col md={7}>
            <div className="section_title">
              <span>Legends</span>
            </div>
            {programLegend ? (
              <div className="legends-container">
                <div className={`even-${selectedDirectProgram ? 'third' : 'middle'}`}>
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
                </div>
                {selectedDirectProgram === null
                && (
                  <div className="even-middle">
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
                {selectedDirectProgram !== null
                && (
                  <div className="even-sixth">
                    {this.getTopChart()}
                  </div>
                )}

              </div>
            ) : <div className="loading" />}
          </Col>
          <div className="separator" />
          <Col md={12}>
            <div className="chart-container">
              <div className="chart">
                <div className="section_title">
                  <span>{translations['amp.dashboard:funding-over-time']}</span>
                </div>
                {nddLoaded && !nddLoadingPending ? (
                  <FundingByYearChart
                    selectedDirectProgram={selectedDirectProgram}
                    settings={settings}
                    data={ndd} />
                ) : <div className="loading" />}
              </div>
            </div>
          </Col>
        </div>
      );
    }
  }
}

const mapStateToProps = state => ({
  top: state.reportsReducer.top,
  translations: state.translationsReducer.translations,
  globalSettings: state.dashboardSettingsReducer.globalSettings,
  topLoaded: state.reportsReducer.topLoaded,
  topLoadingPending: state.reportsReducer.topLoadingPending,
  dashboardSettings: state.dashboardSettingsReducer.dashboardSettings
});

const mapDispatchToProps = dispatch => bindActionCreators({ callTopReport }, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);

MainDashboardContainer.propTypes = {
  filters: PropTypes.object,
  dashboardId: PropTypes.number,
  error: PropTypes.object,
  loadDashboardSettings: PropTypes.func.isRequired,
  ndd: PropTypes.array.isRequired,
  top: PropTypes.object.isRequired,
  nddLoadingPending: PropTypes.bool.isRequired,
  nddLoaded: PropTypes.bool.isRequired,
  topLoadingPending: PropTypes.bool.isRequired,
  topLoaded: PropTypes.bool.isRequired,
  dashboardSettings: PropTypes.array.isRequired,
  onChangeFundingType: PropTypes.func.isRequired,
  fundingType: PropTypes.object,
  mapping: PropTypes.object,
  onChangeProgram: PropTypes.func.isRequired,
  noIndirectMapping: PropTypes.object,
  selectedPrograms: PropTypes.array,
  callTopReport: PropTypes.func.isRequired,
  settings: PropTypes.object,
  selectedDirectProgram: PropTypes.object,
  handleOuterChartClick: PropTypes.func.isRequired,
  globalSettings: PropTypes.object
};

MainDashboardContainer.defaultProps = {
  filters: undefined,
  selectedDirectProgram: null,
  settings: undefined
};

MainDashboardContainer.contextType = NDDTranslationContext;
