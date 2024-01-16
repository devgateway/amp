import React, { useState, useEffect, useContext, useCallback } from 'react';
import { Container } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import { connect, useSelector } from 'react-redux';
import PropTypes from 'prop-types';
import { NDDTranslationContext } from './StartUp';
import HeaderContainer from './HeaderContainer';
import { callReport, callTopReport, clearTopReport } from '../actions/callReports';
import { CURRENCY_CODE, DIRECT, FUNDING_TYPE, MEPath, NDDPath } from '../utils/constants';
import loadDashboardSettings from '../actions/loadDashboardSettings';
import { getMappings } from '../actions/getMappings';
import { DST_PROGRAM, SRC_PROGRAM } from '../../admin/ndd/constants/Constants';
import { getSharedData } from '../actions/getSharedData';
import NDDDashboardTabs  from './NDDDashboardTabs';
import { printChart } from '../../sscdashboard/utils/PrintUtils';
import './print.css';
import { removeFilter } from '../utils/Utils';
import { SRC_DIRECT } from './charts/FundingByYearChart';
import { useLocation, useParams } from 'react-router-dom';

const NDDDashboardHome = (props) => {
    const {
        _callReport,
        mapping,
        ndd,
        nddLoadingPending,
        nddLoaded,
        dashboardSettings,
        _loadDashboardSettings,
        _getMappings,
        _callTopReport,
        _clearTopReport,
        noIndirectMapping,
        globalSettings,
        _getSharedData,
    } = props;

    const location = useLocation();
    const urlParams = useParams();
    const params = new URLSearchParams(window.location.search);
    const [state, setState] = useState({
        filters: undefined,
        dashboardId: undefined,
        fundingType: undefined,
        selectedPrograms: undefined,
        settings: undefined,
        selectedDirectProgram: null,
        embedded: !!params.embedded,
        fundingByYearSource: SRC_DIRECT
    });

    const fetchFmReducer = useSelector(state => state.fetchFmReducer);

    const nddDashboard = fetchFmReducer.data.find(d => d === NDDPath);
    const meDashboard = fetchFmReducer.data.find(d => d === MEPath);

    const getSharedDataOrResolve = (id) => {
        if (id) {
            return _getSharedData(id);
        }
        return Promise.resolve();
    }

    useEffect(() => {
        const { embedded } = state;

        const { id } = urlParams;

        setState(prevState => ({ ...prevState, dashboardId: id }));

        const fetchData = async () => {
            try {
                const settingsResponse = await _loadDashboardSettings();

                const sharedDataResponse = await getSharedDataOrResolve(id);

                const tempSettings = {
                    [CURRENCY_CODE]: settingsResponse.payload[Object.keys(settingsResponse.payload)
                      .find(i => settingsResponse.payload[i].id === CURRENCY_CODE)].value.defaultId
                };

                let ids = []

                if (nddDashboard) {
                    const mappingsResponse = await _getMappings();
                    ids = [`${mappingsResponse.payload[SRC_PROGRAM].id}`, `${mappingsResponse.payload[DST_PROGRAM].id}`];
                }

                let fundingType = settingsResponse.payload.find(i => i.id === FUNDING_TYPE).value.defaultId;

                if (id) {
                    const savedData = JSON.parse(sharedDataResponse.payload.stateBlob);
                    if (savedData && savedData.settings) {
                        if (savedData.settings[CURRENCY_CODE]) {
                            tempSettings[CURRENCY_CODE] = savedData.settings[CURRENCY_CODE];
                        }
                    }
                    if (savedData && savedData.fundingType) {
                        fundingType = savedData.fundingType;
                    }
                    if (savedData && savedData.selectedPrograms) {
                        ids = savedData.selectedPrograms;
                    }
                }

                setState(prevState => ({
                    ...prevState,
                    selectedPrograms: ids,
                    settings: tempSettings,
                    fundingType
                }));

                if (embedded) {
                    _callReport(fundingType, {}, ids, tempSettings);
                }
            } catch (error) {
                console.error(error);
                // Handle your error here
            } finally {
                if (embedded) {
                    const { fundingType, settings, selectedPrograms } = state;
                    _callReport(fundingType, {}, selectedPrograms, settings);
                }
            }
        };

        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const resetChartAfterUnClick = () => {
        const { selectedDirectProgram, filters, embedded } = state;
        if (selectedDirectProgram) {
            if (!embedded) {
                // Remove the filter manually or it will keep affecting the chart.
                const fixedFilters = removeFilter(filters, selectedDirectProgram);
                setState(prevState => ({
                    ...prevState,
                    selectedDirectProgram: null,
                    filters: fixedFilters
                }));
            } else {
                setState(prevState => ({
                    ...prevState,
                    selectedDirectProgram: null,
                }));
            }
        }
    }

    const handleOuterChartClick = (event, outerData) => {
        const {
            selectedDirectProgram, filters, settings, fundingType
        } = state;
        if (event.points[0].data.name === DIRECT) {
            if (!selectedDirectProgram) {
                setState(prevState => ({ ...prevState, selectedDirectProgram: outerData[event.points[0].i], fundingByYearSource: SRC_DIRECT }));
                _callTopReport(fundingType || dashboardSettings.find(i => i.id === FUNDING_TYPE).value.defaultId,
                  settings, filters, outerData[event.points[0].i]);
            } else {
                _clearTopReport();
                resetChartAfterUnClick();
            }
        }
    }
    const memoizedHandleChartClick = useCallback(handleOuterChartClick, []);

    const {
        filters,
        dashboardId,
        fundingType,
        selectedPrograms,
        settings,
        selectedDirectProgram,
        embedded,
        fundingByYearSource
    } = state;

    const onChangeSource = (e) => {
        setState(prevState => ({
              ...prevState,
              fundingByYearSource: e.target.value
          })
        );
    }

    const memoizedOnChangeSource = useCallback(onChangeSource, []);

    const onApplyFilters = (data) => {
        const {
            fundingType, selectedDirectProgram, settings, selectedPrograms
        } = state;

        setState(prevState => ({ ...prevState, filters: data }));
        _callReport(fundingType, data, selectedPrograms, settings);
        if (selectedDirectProgram !== null) {
            _callTopReport(fundingType, settings, data, selectedDirectProgram);
        }
    }

    const onChangeFundingType = (value) => {
        const { filters, selectedPrograms, settings } = state;
        resetChartAfterUnClick();
        setState(prevState => ({ ...prevState, fundingType: value }));
        _callReport(value, filters, selectedPrograms, settings);
        _clearTopReport();
    }

    const onChangeProgram = (value) => {
        const { filters, fundingType, settings } = state;
        const selectedPrograms = value.split('-');
        setState(prevState => ({ ...prevState, selectedPrograms }));
        resetChartAfterUnClick();
        _callReport(fundingType, filters, selectedPrograms, settings);
    }

    const onApplySettings = (data) => {
        const {
            fundingType, selectedPrograms, filters, selectedDirectProgram
        } = state;

        setState(prevState => ({ ...prevState, settings: data }));
        _callReport(fundingType, filters, selectedPrograms, data);
        if (selectedDirectProgram !== null) {
            _callTopReport(fundingType, data, filters, selectedDirectProgram);
        }
    }

    const { translations } = useContext(NDDTranslationContext);
    const downloadImage = () => {
        printChart(translations['amp.ndd.dashboard:page-title'], 'ndd-main-container',
          [], 'png', false, 'print-simple-dummy-container', false);
    }

    const memoizedDownloadImage = useCallback(downloadImage, []);


    return (
      <Container fluid className="main-container" id="ndd-main-container">
          <div className="row header" style={{marginRight: '-30px', marginLeft: '-30px'}}>
              {mapping && settings && globalSettings && selectedPrograms && !embedded && (
                <HeaderContainer
                  onApplySettings={onApplySettings}
                  onApplyFilters={onApplyFilters}
                  filters={filters}
                  globalSettings={globalSettings}
                  settings={settings}
                  fundingType={fundingType}
                  selectedPrograms={selectedPrograms}
                  dashboardId={dashboardId}/>
              )}
          </div>

          <NDDDashboardTabs
            handleOuterChartClick={memoizedHandleChartClick}
            selectedDirectProgram={selectedDirectProgram}
            filters={filters}
            ndd={ndd}
            nddLoaded={nddLoaded}
            nddLoadingPending={nddLoadingPending}
            dashboardSettings={dashboardSettings}
            onChangeFundingType={onChangeFundingType}
            onChangeProgram={onChangeProgram}
            fundingType={fundingType}
            selectedPrograms={selectedPrograms}
            mapping={mapping}
            settings={settings}
            globalSettings={globalSettings}
            noIndirectMapping={noIndirectMapping}
            downloadImage={memoizedDownloadImage}
            embedded={embedded}
            onChangeSource={memoizedOnChangeSource}
            fundingByYearSource={fundingByYearSource}
            translations={translations}
            nddDashboard={nddDashboard}
            meDashboard={meDashboard}
          />
      </Container>
    );
}

const
  mapStateToProps = state => ({
      ndd: state.reportsReducer.ndd,
      error: state.reportsReducer.error,
      nddLoaded: state.reportsReducer.nddLoaded,
      nddLoadingPending: state.reportsReducer.nddLoadingPending,
      dashboardSettings: state.dashboardSettingsReducer.dashboardSettings,
      globalSettings: state.dashboardSettingsReducer.globalSettings,
      translations: state.translationsReducer.translations,
      mapping: state.mappingsReducer.mapping,
      noIndirectMapping: state.mappingsReducer.noIndirectMapping
  });

const
  mapDispatchToProps = dispatch => bindActionCreators({
      _callReport: callReport,
      _loadDashboardSettings: loadDashboardSettings,
      _getMappings: getMappings,
      _callTopReport: callTopReport,
      _clearTopReport: clearTopReport,
      _getSharedData: getSharedData,
  }, dispatch);

NDDDashboardHome.propTypes = {
    _callReport: PropTypes.func.isRequired,
    mapping: PropTypes.object,
    ndd: PropTypes.array,
    nddLoadingPending: PropTypes.bool.isRequired,
    nddLoaded: PropTypes.bool.isRequired,
    dashboardSettings: PropTypes.array,
    _loadDashboardSettings: PropTypes.func.isRequired,
    _getMappings: PropTypes.func.isRequired,
    _callTopReport: PropTypes.func.isRequired,
    _clearTopReport: PropTypes.func.isRequired,
    noIndirectMapping: PropTypes.object,
    globalSettings: PropTypes.object,
    _getSharedData: PropTypes.func.isRequired
};

NDDDashboardHome.defaultProps = {
    dashboardSettings: undefined,
    mapping: undefined,
    ndd: null,
    noIndirectMapping: undefined,
    globalSettings: undefined
};

export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);
