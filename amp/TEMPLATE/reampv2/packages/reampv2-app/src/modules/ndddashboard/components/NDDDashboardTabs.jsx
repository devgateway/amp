import React, {Suspense, useEffect, useState} from "react";
import {Col, Nav, Row, Tab, Tabs} from "react-bootstrap";
import PrintDummy from "../../sscdashboard/utils/PrintDummy";
import PropTypes from "prop-types";
import {useSelector} from "react-redux";
import { MEPath, NDDPath } from '../utils/constants';

const MainDashboardContainer = React.lazy(() => import('./MainDashboardContainer'));
const MeDashboardContainer = React.lazy(() => import('../medashboard'));


const NDDDashboardTabs = (props)  => {
    const { translations, nddDashboard, meDashboard } = props;
    const [currentTab, setCurrentTab] = useState('ndd');
    const fetchFmReducer = useSelector(state => state.fetchFmReducer);



    useEffect(() => {
        if (nddDashboard && !meDashboard) {
            setCurrentTab('ndd');
        } else if (!nddDashboard && meDashboard) {
            setCurrentTab('me');
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    if (fetchFmReducer.loading) {
        return <div className="loading" />;
    }

    if (fetchFmReducer.error) {
        return <div className="error">An error occurred: {fetchFmReducer.error}</div>;
    }

    return (


    <Tabs
        id="ndd-tabs"
        activeKey={nddDashboard ? currentTab : "me"}
        onSelect={(key) => setCurrentTab(key)}
        defaultActiveKey={nddDashboard ? "ndd" : "me"}
        className="mb-3"
    >
        {nddDashboard && (
            <Tab eventKey="ndd" title={translations["amp.ndd.dashboard:ndd-dashboard"]}>
                <Col style={{
                    backgroundColor: "#f5f5f5",
                    paddingTop: 25,
                    borderRadius: 5,
                }}>
                    <Suspense fallback={<div className="loading" />}>
                        <MainDashboardContainer
                            handleOuterChartClick={props.handleOuterChartClick}
                            selectedDirectProgram={props.selectedDirectProgram}
                            filters={props.filters}
                            ndd={props.ndd}
                            nddLoaded={props.nddLoaded}
                            nddLoadingPending={props.nddLoadingPending}
                            dashboardSettings={props.dashboardSettings}
                            onChangeFundingType={props.onChangeFundingType}
                            onChangeProgram={props.onChangeProgram}
                            fundingType={props.fundingType}
                            selectedPrograms={props.selectedPrograms}
                            mapping={props.mapping}
                            settings={props.settings}
                            globalSettings={props.globalSettings}
                            noIndirectMapping={props.noIndirectMapping}
                            downloadImage={props.downloadImage}
                            embedded={props.embedded}
                            onChangeSource={props.onChangeSource}
                            fundingByYearSource={props.fundingByYearSource}
                        />
                    </Suspense>
                    <PrintDummy />
                </Col>
            </Tab>
        )}

        {meDashboard && (
            <Tab eventKey="me" title={translations["amp.ndd.dashboard:me-dashboard"]}>
                <Col style={{
                    backgroundColor: "#f5f5f5",
                    paddingTop: 25,
                    borderRadius: 5,
                }}>
                    <Suspense fallback={<div className="loading" />}>
                        <MeDashboardContainer
                            dashboardSettings={props.dashboardSettings}
                            settings={props.settings}
                            globalSettings={props.globalSettings}
                            filters={props.filters}
                        />
                    </Suspense>
                </Col>
            </Tab>
        )}
    </Tabs>
    );
}

NDDDashboardTabs.propTypes = {
    activeKey: PropTypes.string,
    onSelect: PropTypes.func,
    handleOuterChartClick: PropTypes.func,
    selectedDirectProgram: PropTypes.any,
    filters: PropTypes.any,
    ndd: PropTypes.any,
    nddLoaded: PropTypes.any,
    nddLoadingPending: PropTypes.any,
    dashboardSettings: PropTypes.any,
    onChangeFundingType: PropTypes.func,
    onChangeProgram: PropTypes.func,
    fundingType: PropTypes.any,
    selectedPrograms: PropTypes.any,
    mapping: PropTypes.any,
    settings: PropTypes.any,
    globalSettings: PropTypes.any,
    noIndirectMapping: PropTypes.any,
    downloadImage: PropTypes.func,
    embedded: PropTypes.bool,
    onChangeSource: PropTypes.func,
    fundingByYearSource: PropTypes.string,
    nddDashboard: PropTypes.bool,
    meDashboard: PropTypes.bool
};

export default NDDDashboardTabs;
