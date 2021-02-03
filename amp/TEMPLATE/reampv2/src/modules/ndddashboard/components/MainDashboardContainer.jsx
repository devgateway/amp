import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Col } from 'react-bootstrap';
import NestedDonutsProgramChart from './charts/NestedDonutsProgramChart';
import FundingTypeSelector from './FundingTypeSelector';
import './legends/legends.css';
import {
  extractPrograms, getCustomColor, getGradient
} from '../utils/Utils';
import FundingByYearChart from './charts/FundingByYearChart';
import PieChartTypeSelector from './PieChartTypeSelector';
import { NDDTranslationContext } from './StartUp';
import TopChartContainer from './TopChartContainer';
import { AVAILABLE_COLORS, PROGRAMLVL1 } from '../utils/constants';

class MainDashboardContainer extends Component {
  // eslint-disable-next-line react/sort-comp
  generate2LevelColors() {
    const { selectedDirectProgram } = this.props;
    if (selectedDirectProgram && !AVAILABLE_COLORS.get(`${PROGRAMLVL1}_${selectedDirectProgram.code}`)) {
      const colors = getGradient(getCustomColor(selectedDirectProgram, PROGRAMLVL1), '#FFFFFF');
      AVAILABLE_COLORS.set(`${PROGRAMLVL1}_${selectedDirectProgram.code}`, colors);
    }
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
      filters,
      top,
      topLoaded,
      topLoadingPending
    } = this.props;
    const { translations } = this.context;
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      const programs = extractPrograms(mapping, noIndirectMapping);
      this.generate2LevelColors();
      return (
        <>
          <Col md={5}>
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
          </Col>
          <Col md={7}>
            <TopChartContainer
              noIndirectMapping={noIndirectMapping}
              ndd={ndd}
              globalSettings={globalSettings}
              mapping={mapping}
              settings={settings}
              top={top}
              topLoaded={topLoaded}
              topLoadingPending={topLoadingPending}
              selectedDirectProgram={selectedDirectProgram}
              nddLoaded={nddLoaded}
              nddLoadingPending={nddLoadingPending} />
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
                    selectedPrograms={selectedPrograms}
                    settings={settings}
                    filters={filters}
                    fundingType={fundingType}
                    globalSettings={globalSettings}
                    data={ndd} />
                ) : <div className="loading" />}
              </div>
            </div>
          </Col>
        </>
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
