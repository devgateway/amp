import React, {useEffect} from 'react';
import {Col, Row} from "react-bootstrap";
import ChartUtils from "../../utils/chart";
import {useSelector, useDispatch } from "react-redux";
import styles from './css/Styles.module.css';
import {FundingType, SectorClassifcation, SectorScheme} from "../../types";
import {SectorObjectType} from "../../../../admin/indicator_manager/types";
import BarChart, {DataType} from "../charts/BarChart";
import {fetchSectorReport} from "../../reducers/fetchSectorsReportReducer";
import {FUNDING_TYPE} from "../../../utils/constants";
import EllipsisText from "react-ellipsis-text";
import ReactTooltip  from "react-tooltip";
import 'react-tooltip/dist/react-tooltip';

const CustomLegend = ({ data }) => (
    <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', marginTop: '20px' }}>
        {data.map(item => (
            <div key={item.id} style={{ margin: '0 10px' }} data-tooltip-id="my-tooltip"
                 data-tooltip-content={item.label}>
                <span style={{
                    backgroundColor: item.color,
                    width: '20px',
                    height: '20px',
                    display: 'inline-block',
                    marginRight: '5px'
                }}></span>
                <EllipsisText text={item.label} length={10} />

            </div>
        ))}
    </div>
);

interface SectorProgressProps {
    translations?: any,
    filters: any,
    settings: any,
    selectedClassification?: number | null,
    setSelectedClassification: React.Dispatch<React.SetStateAction<number | null>>
}

const SectorClassification: React.FC<SectorProgressProps> = (props) => {
    const { translations, filters, settings, setSelectedClassification } = props;

    const dispatch = useDispatch();

    const sectorReportReducer = useSelector((state: any) => state.fetchSectorReportReducer);
    const dashboardSettingsReducer = useSelector((state: any) => state.dashboardSettingsReducer);

    const [selectedSectorClassification, setSelectedSectorClassification] = React.useState<number | null>(null);
    const [selectedSectorScheme, setSelectedSectorScheme] = React.useState<any>(null);

    const [sectorScheme, setSectorScheme] = React.useState<SectorScheme>();

    const sectorClassification: SectorClassifcation [] = useSelector((state: any) => state.fetchSectorClassificationReducer.data);
    const [sectors, setSectors] = React.useState<SectorObjectType[]>([]);
    const [sectorReport, setSectorReport] = React.useState<DataType[]>();
    const [defaultFundingType, setDefaultFundingType] = React.useState<string | undefined>(undefined);
    const [fundingTypeList, setFundingTypeList] = React.useState<FundingType[]>([]);

    const handleSectorClassificationChange = () => {
        const classification = sectorClassification.find(item => item.id === selectedSectorClassification);

        if (classification) {
            setSectorScheme(classification.sectorScheme);
            setSectors(classification.sectorScheme.children);
        }
    }

    const handleFetchSectorReport = () => {
        const classification = sectorClassification.find(item => item.id === selectedSectorClassification);

        if (classification && filters && settings) {
            const dashboardSettings = {
                ...settings,
                "funding-type": defaultFundingType
            };

            dispatch(fetchSectorReport({ filters, classificationType : classification.name, settings: dashboardSettings }));
        }
    }

    const extractFundingType = () => {
        if (dashboardSettingsReducer.dashboardSettingsLoaded) {
            const fundingType = dashboardSettingsReducer.dashboardSettings.find((item: any) => item.id === FUNDING_TYPE);

            if (fundingType) {
                setDefaultFundingType(fundingType.value.defaultId);
                setFundingTypeList(fundingType.value.options);
            }
        }
    }

    useEffect(() => {
        extractFundingType();
    }, [dashboardSettingsReducer]);

    useEffect(() => {
        handleFetchSectorReport();
    }, [selectedSectorClassification, filters, settings, defaultFundingType]);

    useEffect(() => {
        if (sectorClassification.length > 0) {
            setSelectedSectorClassification(sectorClassification[0].id);
            setSelectedClassification(sectorClassification[0].id);
        }
    }, []);

    useEffect(() => {
        handleSectorClassificationChange();
    }, [selectedSectorClassification]);

    useEffect(() => {
        setSectorReport(undefined);
        if (!sectorReportReducer.loading && !sectorReportReducer.error && sectorReportReducer.data) {
            const generatedData = ChartUtils.generateSectorsReport({
                data: sectorReportReducer.data,
                translations
            });

            setSectorReport(generatedData);
        }

    }, [sectorReportReducer]);

    return (
        <div>
            <Col  md={12} style={{
                borderBottom: '1px solid #ddd',
                minHeight: 500
            }}>
            <div id="sector-progress-by-funding">


                <Row md={12} style={{
                    width: '100%',
                    marginLeft: 0,
                    paddingBottom: 10,
                    alignItems: 'center',
                    justifyContent: 'center',
                }}>
                    <Col md={11} style={{
                        paddingRight: 10
                    }}>
                        {sectorClassification.length === 0 ? (
                            <select
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                <option>{translations['amp.ndd.dashboard:me-no-data']}</option>
                            </select>
                        ) : (
                            <select
                                defaultValue={sectorClassification[0].id}
                                onChange={(e) => {
                                        setSelectedSectorClassification(parseInt(e.target.value));
                                        setSelectedClassification(parseInt(e.target.value));
                                        handleSectorClassificationChange();
                                    }
                                }
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                {sectorClassification.map((item, index: number) => (<option key={index} value={item.id}>{item.name}</option>))}
                            </select>
                        )}
                        <span className="cheat-lineheight" />
                    </Col>
                    <Col md={1} style={{
                        padding: 0,
                        cursor: 'pointer'
                    }}>
                        <div className={styles.download_btn_wrapper} onClick={() => ChartUtils.downloadChartImage(
                            `${translations['amp.ndd.dashboard:me-dashboard']}-sector-progress`, 'sector-progress-by-funding')}>
                            <span className="glyphicon glyphicon-cloud-download" />
                        </div>
                    </Col>
                </Row>

                <Row style={{
                    width: '100%',
                    paddingBottom: 20,
                    marginLeft: 0,
                    borderBottom: '1px solid #e5e5e5',

                }}>
                    <Col md={11} style={{
                        paddingRight: 10
                    }}>
                        {!sectorScheme ? (
                            <select
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                <option>{translations['amp.ndd.dashboard:me-no-data']}</option>
                            </select>
                        ) : (
                            <select
                                defaultValue={sectorScheme.ampSecSchemeId}
                                onChange={(e) => setSelectedSectorScheme(parseInt(e.target.value))}
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                <option key={sectorScheme.ampSecSchemeId} value={sectorScheme.ampSecSchemeId}>{sectorScheme.secSchemeName}</option>
                            </select>
                        )}

                        <span className="cheat-lineheight"/>
                    </Col>
                </Row>

                <Row style={{
                    paddingLeft: -10,
                }}>
                    <Col md={12}>
                        { (sectorReport && dashboardSettingsReducer.dashboardSettingsLoaded && fundingTypeList && defaultFundingType) ? (
                            <div>
                                {
                                    sectorReport && (
                                        <>
                                            <span style={{
                                                fontSize: 14,
                                                paddingTop: 15,
                                                marginBottom: 10,
                                            }}>{translations['amp.ndd.dashboard:sector-progress']}</span>
                                            <CustomLegend data={sectorReport}/>
                                            <ReactTooltip id="my-tooltip"  />
                                            <BarChart
                                                translations={translations}
                                                data={sectorReport}
                                                width={400}
                                                height={300}
                                                margin={{top: 40, right: 30, left: 20}}
                                                tooltipSuffix={settings && settings["currency-code"] ? settings["currency-code"] : undefined}
                                                labelFormat={
                                                    labelValue => (
                                                        (<tspan y={0}>{labelValue}</tspan>) as unknown
                                                    ) as string
                                                }
                                                legendProps={[]}
                                                 />
                                        </>
                                    )
                                }

                                <Col md={12} style={{
                                    marginTop: 10,
                                    marginBottom: 10
                                }}>
                                    {(dashboardSettingsReducer.dashboardSettingsLoaded && fundingTypeList && defaultFundingType) ? (
                                        <select
                                            defaultValue={defaultFundingType}
                                            onChange={(e) => setDefaultFundingType(e.target.value)}
                                            style={{
                                                backgroundColor: '#f3f5f8',
                                                boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                            }}
                                            className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                            {
                                                fundingTypeList.map((item: FundingType) => (
                                                    <option key={item.id} value={item.id}>{item.name}</option>
                                                ))
                                            }
                                        </select>
                                    ) : (
                                        <select
                                            style={{
                                                backgroundColor: '#f3f5f8',
                                                boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                            }}
                                            className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                            <option>{translations['amp.ndd.dashboard:me-no-data']}</option>
                                        </select>
                                    )}
                                </Col>

                            </div>
                        ): (
                            <div className="loading"></div>
                        )}

                    </Col>
                </Row>
            </div>
            </Col>

            <div>

            </div>
        </div>
    );
};

export default SectorClassification;
