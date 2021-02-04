import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  CHART_COLOR_MAP,
  INDIRECT_PROGRAMS,
  PROGRAMLVL1, CURRENCY_CODE
} from '../utils/constants';
import CustomLegend from '../../../utils/components/CustomLegend';
import './legends/legends.css';
import {
  getCustomColor, formatNumberWithSettings
} from '../utils/Utils';
import TopChart from './charts/TopChart';
import { NDDTranslationContext } from './StartUp';
import { ALL_PROGRAMS } from '../../admin/ndd/constants/Constants';

export default class TopChartContainer extends Component {
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
    ) : <div style={{ position: 'relative', top: '10px' }} className="loading" />;
  }

  // eslint-disable-next-line class-methods-use-this,react/sort-comp
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

  getTitles = () => {
    const { selectedPrograms, mapping } = this.props;
    const titles = [];
    if (mapping && selectedPrograms) {
      titles[0] = mapping[ALL_PROGRAMS].find(i => `${i.id}` === selectedPrograms[0]).value;
      if (selectedPrograms[1]) {
        titles[1] = mapping[ALL_PROGRAMS].find(i => `${i.id}` === selectedPrograms[1]).value;
      }
    }
    return titles;
  }

  render() {
    const {
      error,
      nddLoadingPending,
      nddLoaded,
      settings,
      selectedDirectProgram,
      globalSettings
    } = this.props;
    const { translations } = this.context;
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      const programLegend = nddLoaded && !nddLoadingPending ? this.getProgramLegend() : null;
      const titles = this.getTitles();
      return (
        <div>
          {programLegend && programLegend[0].total ? (
            <div className="legends-container">
              <div className={`even-${selectedDirectProgram ? 'third' : 'middle'}`}>
                <div className="legend-title">
                  <span>{titles[0]}</span>
                  <span className="amount">
                    {formatNumberWithSettings(settings[CURRENCY_CODE], translations, globalSettings,
                      programLegend[0].total, true)}
                  </span>
                </div>
                <CustomLegend
                  settings={globalSettings}
                  translations={translations}
                  currency={settings[CURRENCY_CODE]}
                  data={programLegend[0].legends.sort((a, b) => b.amount - a.amount)}
                  colorMap={CHART_COLOR_MAP.get(selectedDirectProgram ? `${PROGRAMLVL1}_${selectedDirectProgram.code}`
                    : PROGRAMLVL1)} />
              </div>
              {selectedDirectProgram === null && programLegend[1].total
                ? (
                  <div className="even-middle">
                    <div className="legend-title">
                      <span>
                        {titles[1] ? titles[1] : null}
                      </span>
                      <span className="amount">
                        {formatNumberWithSettings(settings[CURRENCY_CODE], translations, globalSettings,
                          programLegend[1].total, true)}
                      </span>
                    </div>
                    <CustomLegend
                      settings={globalSettings}
                      translations={translations}
                      currency={settings[CURRENCY_CODE]}
                      data={programLegend[1].legends.sort((a, b) => b.amount - a.amount)}
                      colorMap={CHART_COLOR_MAP.get(INDIRECT_PROGRAMS)} />
                  </div>
                ) : null}
              {selectedDirectProgram !== null
                && (
                  <div className="even-sixth">
                    {this.getTopChart()}
                  </div>
                )}

            </div>
          ) : null}
        </div>
      );
    }
  }
}

TopChartContainer.propTypes = {
  error: PropTypes.object,
  ndd: PropTypes.array,
  top: PropTypes.object,
  nddLoadingPending: PropTypes.bool.isRequired,
  nddLoaded: PropTypes.bool.isRequired,
  topLoadingPending: PropTypes.bool.isRequired,
  topLoaded: PropTypes.bool.isRequired,
  mapping: PropTypes.object,
  settings: PropTypes.object,
  selectedDirectProgram: PropTypes.object,
  globalSettings: PropTypes.object,
  selectedPrograms: PropTypes.array
};

TopChartContainer.defaultProps = {
  selectedDirectProgram: null,
  settings: undefined,
  error: null,
  mapping: null,
  globalSettings: null,
  ndd: null,
  top: undefined,
  selectedPrograms: null
};

TopChartContainer.contextType = NDDTranslationContext;
