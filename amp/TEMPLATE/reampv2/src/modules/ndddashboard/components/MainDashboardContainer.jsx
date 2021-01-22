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
  PROGRAMLVL1, AVAILABLE_COLORS, CURRENCY_CODE
} from '../utils/constants';
import CustomLegend from '../../../utils/components/CustomLegend';
import './legends/legends.css';
import {
  getCustomColor, getGradient, extractPrograms, formatNumberWithSettings
} from '../utils/Utils';
import TopChart from './charts/TopChart';
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
        dp.indirectPrograms.forEach(idp => {
          indirectTotal += this.generateLegend(idp, 1, indirectLegend, INDIRECT_PROGRAMS);
        });
      }
    });
    legends.push({ total: directTotal, legends: [...directLegend.values()] });
    legends.push({ total: indirectTotal, legends: [...indirectLegend.values()] });
    return legends;
  }

  // eslint-disable-next-line react/sort-comp
  generate2LevelColors() {
    const { selectedDirectProgram } = this.props;
    if (selectedDirectProgram && !AVAILABLE_COLORS.get(`${PROGRAMLVL1}_${selectedDirectProgram.code}`)) {
      const colors = getGradient(getCustomColor(selectedDirectProgram, PROGRAMLVL1), '#FFFFFF');
      AVAILABLE_COLORS.set(`${PROGRAMLVL1}_${selectedDirectProgram.code}`, colors);
    }
  }

  getTopChart() {
    const {
      topLoaded, topLoadingPending, top, globalSettings
    } = this.props;
    const { translations } = this.context;
    return topLoaded && !topLoadingPending ? (
      <div>
        <div className="funding-sources-title">
          <div className="row">
            <div className="col-md-8">
              {translations['amp.ndd.dashboard:top-donor-agencies']}
            </div>
            <div className="amount col-md-4">
              {`${top.sumarizedTotal} ${top.currency}`}
            </div>
          </div>
          <TopChart data={top} globalSettings={globalSettings} translations={translations} />
        </div>
      </div>
    ) : <div className="loading" />;
  }

  // eslint-disable-next-line class-methods-use-this
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
      error,
      ndd,
      nddLoadingPending,
      nddLoaded,
      dashboardSettings,
      onChangeFundingType,
      fundingType,
      mapping,
      onChangeProgram,
      selectedPrograms,
      noIndirectMapping,
      settings,
      selectedDirectProgram,
      handleOuterChartClick,
      globalSettings,
      filters
    } = this.props;
    const { translations } = this.context;
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
                        : translations['amp.ndd.dashboard:loading']}
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
                          globalSettings={globalSettings}
                          selectedDirectProgram={selectedDirectProgram}
                          handleOuterChartClick={handleOuterChartClick} />
                      </div>
                    )
                    : <div className="loading" />}
                </div>
                <div className="buttons">
                  {dashboardSettings && !nddLoadingPending
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
              <span>{translations['amp.ndd.dashboard:legends']}</span>
            </div>
            {programLegend ? (
              <div className="legends-container">
                <div className={`even-${selectedDirectProgram ? 'third' : 'middle'}`}>
                  <div className="legend-title">
                    {programs.direct
                      ? (`${programs.direct.value}`)
                      : translations['amp.ndd.dashboard:loading']}
                    :
                    <span className="amount">
                      {formatNumberWithSettings(translations, globalSettings, programLegend[0].total, true)}
                      {` ${settings[CURRENCY_CODE]}`}
                    </span>
                  </div>
                  <CustomLegend
                    settings={globalSettings}
                    translations={translations}
                    data={programLegend[0].legends.sort((a, b) => b.amount - a.amount)}
                    colorMap={CHART_COLOR_MAP.get(selectedDirectProgram ? `${PROGRAMLVL1}_${selectedDirectProgram.code}`
                      : PROGRAMLVL1)} />
                </div>
                {selectedDirectProgram === null
                && (
                  <div className="even-middle">
                    <div className="legend-title">
                      {programs.direct
                        ? (`${programs.indirect1.value}`)
                        : translations['amp.ndd.dashboard:loading']}
                      :
                      <span className="amount">
                        {formatNumberWithSettings(translations, globalSettings, programLegend[0].total, true)}
                        {` ${settings[CURRENCY_CODE]}`}
                      </span>
                    </div>
                    <CustomLegend
                      settings={globalSettings}
                      translations={translations}
                      data={programLegend[1].legends.sort((a, b) => b.amount - a.amount)}
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
                    filters={filters}
                    fundingType={fundingType}
                    globalSettings={globalSettings}
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

const mapDispatchToProps = dispatch => bindActionCreators({ }, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);

MainDashboardContainer.propTypes = {
  filters: PropTypes.object,
  error: PropTypes.object,
  ndd: PropTypes.array,
  top: PropTypes.object,
  nddLoadingPending: PropTypes.bool.isRequired,
  nddLoaded: PropTypes.bool.isRequired,
  topLoadingPending: PropTypes.bool.isRequired,
  topLoaded: PropTypes.bool.isRequired,
  dashboardSettings: PropTypes.array,
  onChangeFundingType: PropTypes.func.isRequired,
  fundingType: PropTypes.string,
  mapping: PropTypes.object,
  onChangeProgram: PropTypes.func.isRequired,
  noIndirectMapping: PropTypes.object,
  selectedPrograms: PropTypes.array,
  settings: PropTypes.object,
  selectedDirectProgram: PropTypes.object,
  handleOuterChartClick: PropTypes.func.isRequired,
  globalSettings: PropTypes.object
};

MainDashboardContainer.defaultProps = {
  filters: undefined,
  selectedDirectProgram: null,
  settings: undefined,
  error: null,
  fundingType: null,
  mapping: null,
  noIndirectMapping: null,
  selectedPrograms: null,
  globalSettings: null,
  ndd: null,
  top: undefined,
  dashboardSettings: null
};

MainDashboardContainer.contextType = NDDTranslationContext;
