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
    const { selectedDirectProgram, selectedPrograms } = this.props;
    if (selectedPrograms && selectedDirectProgram
      && !SELECTED_COLORS.get(`${selectedPrograms[0]}_${selectedDirectProgram.code}`)) {
      const colors = getGradient(getCustomColor(selectedDirectProgram, selectedPrograms[0]), '#FFFFFF');
      SELECTED_COLORS.set(`${selectedPrograms[0]}_${selectedDirectProgram.code}`, colors);
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
      downloadImage
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
                <div className="export-wrapper">
                  <img
                    alt=""
                    className="download-image"
                    src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPCEtLSBHZW5lcmF0b3I6IEFkb2JlIElsbHVzdHJhdG9yIDIzLjAuNiwgU1ZHIEV4cG9ydCBQbHVnLUluIC4gU1ZHIFZlcnNpb246IDYuMDAgQnVpbGQgMCkgIC0tPgo8c3ZnIHZlcnNpb249IjEuMSIgaWQ9IkxheWVyXzEiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiIHg9IjBweCIgeT0iMHB4IgoJIHZpZXdCb3g9IjAgMCA1MTIgNTEyIiBzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA1MTIgNTEyOyIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSI+CjxzdHlsZSB0eXBlPSJ0ZXh0L2NzcyI+Cgkuc3Qwe2ZpbGw6IzAwMDAwMDt9Cjwvc3R5bGU+CjxjaXJjbGUgY2xhc3M9InN0MCIgY3g9IjI1NiIgY3k9IjI3NSIgcj0iNTcuNSIvPgo8cGF0aCBjbGFzcz0ic3QwIiBkPSJNNDE3LjUsMTYwSDM2M2MtNC42LDAtOC45LTItMTItNS40Yy0yOC40LTMxLjgtMzkuMS00Mi42LTUwLjctNDIuNmgtODUuNWMtMTEuNywwLTIzLjIsMTAuOC01MS43LDQyLjcKCWMtMywzLjQtNy40LDUuMy0xMS45LDUuM2gtNC4xdi04YzAtNC40LTMuNi04LTgtOGgtMjZjLTQuNCwwLTgsMy42LTgsOHY4aC03LjVDNzkuOSwxNjAsNjQsMTczLjIsNjQsMTkwLjd2MTc2CgljMCwxNy41LDE1LjksMzMuMywzMy41LDMzLjNoMzIwYzE3LjYsMCwzMC41LTE1LjgsMzAuNS0zMy4zdi0xNzZDNDQ4LDE3My4yLDQzNS4xLDE2MCw0MTcuNSwxNjBMNDE3LjUsMTYweiBNMjYwLDM2MC40CgljLTUwLjMsMi4zLTkxLjctMzkuMS04OS40LTg5LjRjMi00My45LDM3LjUtNzkuNCw4MS40LTgxLjRjNTAuMy0yLjMsOTEuNywzOS4xLDg5LjQsODkuNEMzMzkuNCwzMjIuOSwzMDMuOSwzNTguNCwyNjAsMzYwLjR6CgkgTTM1MiwyMThjLTcuMiwwLTEzLTUuOC0xMy0xM3M1LjgtMTMsMTMtMTNzMTMsNS44LDEzLDEzUzM1OS4yLDIxOCwzNTIsMjE4eiIvPgo8L3N2Zz4K"
                    onClick={() => downloadImage()}
                  />
                </div>
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
            <Col md={5} style={{ paddingRight: 0, paddingLeft: 0, backgroundColor: 'white' }}>
              <div className="chart-container">
                <div className="chart">
                  {nddLoaded && !nddLoadingPending
                    ? (
                      <>
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
                      </>
                    )
                    : <div className="loading" />}
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
          </Row>
          <Row>
            <Col md={12}>
              <div className="separator" />
            </Col>
          </Row>
          <Row style={{ marginRight: '-15px', marginLeft: '-15px', border: '1px solid #ddd' }}>
            <Col md={12} style={{ paddingLeft: 0, paddingRight: 0 }}>
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
          </Row>
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
  downloadImage: PropTypes.func.isRequired
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
