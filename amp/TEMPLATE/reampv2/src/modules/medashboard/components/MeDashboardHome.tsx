import React, { useState, useCallback } from 'react';
import { Container } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import { connect, useSelector } from 'react-redux';
import HeaderContainer from './HeaderContainer';
import { callReport, callTopReport, clearTopReport } from '../reducers/callReports';
import { loadDashboardSettings } from '../reducers/fetchSettingsReducer';
import { getMappings } from '../reducers/getMappings';
import PrintDummy from '../../sscdashboard/utils/PrintDummy';
import './print.css';
import { SettingsType } from '../types';
import MainDashboardContainer from './MainDashboardContainer';

export const SRC_DIRECT = '0';

const MeDashboardHome = (props: any) => {
  const {
    _callReport,
    _callTopReport,
  } = props;

  const [state, setState] = useState({
    filters: undefined,
    dashboardId: undefined,
    fundingType: undefined,
    selectedPrograms: undefined,
    settings: undefined,
    selectedDirectProgram: null,
    fundingByYearSource: SRC_DIRECT
  });
  const {
    dashboardId,
  } = state;

  const settingsReducer: { settings: SettingsType, loading: boolean, error: any } = useSelector((state: any) => state.fetchSettingsReducer);
    const { settings: globalSettings, loading: settingsLoading } = settingsReducer;


  const onApplyFilters = (data: any) => {
    const {
      fundingType, selectedDirectProgram, settings, selectedPrograms
    } = state;

    setState(prevState => ({ ...prevState, filters: data }));
    _callReport(fundingType, data, selectedPrograms, settings);
    if (selectedDirectProgram !== null) {
      _callTopReport(fundingType, settings, data, selectedDirectProgram);
    }
  }

    const onApplySettings = useCallback((data: any) => {
        const {
            fundingType, selectedPrograms, filters, selectedDirectProgram
        } = state;

        setState(prevState => ({ ...prevState, settings: data }));
        _callReport(fundingType, filters, selectedPrograms, data);
        if (selectedDirectProgram !== null) {
            _callTopReport(fundingType, data, filters, selectedDirectProgram);
        }
        //eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


  // const { translations } = React.useContext(MeDashboardContext);
  // const downloadImage = ()=> {
  //   printChart(translations['amp.me.dashboard:page-title'], 'me-main-container',
  //     [], 'png', false, 'print-simple-dummy-container', false);
  // }

  // const memoizedDownloadImage = useCallback(downloadImage, []);

  return (
    <Container fluid className="main-container" id="me-main-container">
      <div className="row header" style={{ marginRight: '-30px', marginLeft: '-30px' }}>
        { !settingsLoading ? (
          <HeaderContainer
            onApplySettings={onApplySettings}
            onApplyFilters={onApplyFilters}
            globalSettings={globalSettings}
            settings={globalSettings}
            dashboardId={dashboardId} />
        ) : null}
      </div>
      <MainDashboardContainer />
      <PrintDummy />
    </Container>
  );
}

const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations,
});

const mapDispatchToProps = (dispatch: any) => bindActionCreators({
    _callReport: callReport,
    _loadDashboardSettings: loadDashboardSettings.fulfilled,
    _getMappings: getMappings,
    _callTopReport: callTopReport,
    _clearTopReport: clearTopReport,
  }, dispatch);


export default connect(mapStateToProps, mapDispatchToProps)(MeDashboardHome);
