import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Col, Row } from 'react-bootstrap';
import NestedDonutsProgramChart from './charts/NestedDonutsProgramChart';
import FundingTypeSelector from './FundingTypeSelector';
import './legends/legends.css';
import {
  getCustomColor, getGradient
} from '../utils/Utils';
import FundingByYearChart from './charts/FundingByYearChart';
import PieChartTypeSelector from './PieChartTypeSelector';
import { NDDTranslationContext } from './StartUp';
import TopChartContainer from './TopChartContainer';
import { SELECTED_COLORS } from '../utils/constants';
import { ALL_PROGRAMS } from '../../admin/ndd/constants/Constants';

class MainDashboardContainer extends Component {
  // eslint-disable-next-line react/sort-comp
  generate2LevelColors() {
    const { selectedDirectProgram, selectedPrograms, ndd } = this.props;
    if (selectedPrograms && selectedDirectProgram) {
      const subColors = SELECTED_COLORS.get(`${selectedPrograms[0]}_${selectedDirectProgram.code}`);
      const countArray = ndd.filter(p => p.directProgram.programLvl1.code === selectedDirectProgram.code);
      if (!subColors || subColors.length !== countArray.length) {
        const colors = getGradient(getCustomColor(selectedDirectProgram,
          selectedPrograms[0]), '#FFFFFF', countArray.length + 1);
        SELECTED_COLORS.set(`${selectedPrograms[0]}_${selectedDirectProgram.code}`, colors);
      }
    }
  }

  generateSectionTitle = () => {
    const {
      mapping,
      selectedPrograms
    } = this.props;
    let title = '';
    if (mapping && selectedPrograms) {
      title = mapping[ALL_PROGRAMS].find(i => `${i.id}` === selectedPrograms[0]).value;
      if (selectedPrograms[1]) {
        title += ` / ${mapping[ALL_PROGRAMS].find(i => `${i.id}` === selectedPrograms[1]).value}`;
      }
    }
    return title;
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
      topLoadingPending,
      downloadImage,
      embedded,
      onChangeSource,
      fundingByYearSource
    } = this.props;
    const { translations } = this.context;
    if (error) {
      // TODO proper error handling
      return (<div>ERROR</div>);
    } else {
      this.generate2LevelColors();
      return (
        <>
          <Row style={{
            marginRight: '-15px', marginLeft: '-15px', border: '1px solid #ddd', borderBottom: 'none'
          }}>
            <Col md={12} style={{ paddingRight: 0, paddingLeft: 0 }}>
              <div className="section_title">
                <span>
                  {this.generateSectionTitle()}
                </span>
                {!embedded && nddLoaded && !nddLoadingPending ? (
                  <div className="export-wrapper">
                    <div
                      className="download-image"
                    >
                      <span
                        className="glyphicon glyphicon-cloud-download download-image-img "
                        onClick={() => downloadImage()} />
                    </div>
                  </div>
                ) : (null)}
              </div>
            </Col>
          </Row>
          <Row style={{
            marginRight: '-15px',
            marginLeft: '-15px',
            border: '1px solid #ddd',
            display: 'flex',
            borderTop: 'none',
            backgroundColor: 'white'
          }}>
            {nddLoaded && !nddLoadingPending
              ? (
                <>
                  <Col md={5} style={{ paddingRight: 0, paddingLeft: 0, backgroundColor: 'white' }}>
                    <div className="chart-container">
                      <div className="chart">
                        <PieChartTypeSelector
                          onChange={onChangeProgram}
                          defaultValue={fundingType}
                          mapping={mapping}
                          noIndirectMapping={noIndirectMapping}
                          selectedPrograms={selectedPrograms} />
                        <NestedDonutsProgramChart
                          data={ndd}
                          settings={settings}
                          globalSettings={globalSettings}
                          selectedDirectProgram={selectedDirectProgram}
                          handleOuterChartClick={handleOuterChartClick}
                          selectedPrograms={selectedPrograms}
                        />
                      </div>
                      <div className="buttons" style={{ position: 'absolute', bottom: 0 }}>
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
                  <Col md={7} style={{ paddingLeft: 0, paddingRight: 0 }}>
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
                      selectedPrograms={selectedPrograms}
                      nddLoadingPending={nddLoadingPending} />
                  </Col> 
                </>
              ) : (
                <Col
                  md={12}
                  style={{
                    paddingRight: 0, paddingLeft: 0, backgroundColor: 'white', height: 400
                  }}>
                  <div className="loading loading-absolute" />
                </Col>
              )}
          </Row>
          {!embedded ? (
            <>
              <Row>
                <Col md={12} style={{ paddingLeft: 0, paddingRight: 0, backgroundColor: 'white' }}>
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
                          data={ndd}
                          onChangeSource={onChangeSource}
                          fundingByYearSource={fundingByYearSource}
                        />
                      ) : <div className="loading" />}
                    </div>
                </Col>
              </Row>
            </>
          ) : null}
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

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

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
  globalSettings: PropTypes.object,
  downloadImage: PropTypes.func.isRequired,
  embedded: PropTypes.bool,
  onChangeSource: PropTypes.func.isRequired,
  fundingByYearSource: PropTypes.string.isRequired
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
  dashboardSettings: null,
  embedded: false
};
MainDashboardContainer.contextType = NDDTranslationContext;
