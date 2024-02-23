import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';
import Gauge from './charts/GaugesChart';
import BarChart, {DataType} from './charts/BarChart';
import LineChart from './charts/LineChart';
import Select from 'react-select';
import { ComponentProps } from '../types';
import { IndicatorObjectType } from '../../../admin/indicator_manager/types';
import ChartUtils from '../utils/chart';
import {fetchIndicatorReportData} from "../utils/fetchIndicatorReport";
import NoData from "./NoData";

interface IndicatorProgressChartProps extends ComponentProps {
    filters: any;
    settings: any;
    indicator: IndicatorObjectType;
    section: 'right' | 'left';
    index: number;
    title?: string;
}

const IndicatorProgressChart: React.FC<IndicatorProgressChartProps> = (props: IndicatorProgressChartProps) => {
    const { translations, filters, settings, indicator, section, title } = props;
    const dispatch = useDispatch();

    // @ts-ignore
    const globalSettings = useSelector(state => state.fetchSettingsReducer.settings);

    const [indicatorReportData, setIndicatorReportData] = useState<any>(null);
    const [selectedIndicatorName, setSelectedIndicatorName] = useState<string | null>(null);
    const [progressValue, setProgressValue] = useState<number>(0);
    const [yearCount, setYearCount] = useState<number>(5);
    const [reportData, setReportData] = useState<DataType[]>();

    const [reportLoading, setReportLoading] = useState<boolean>(false);
    const [defaultYearOption, setDefaultYearOption] = useState<{label: string, value: number}>();
    const [yearOptions, setYearOptions] = useState<{label: string, value: number}[]>([]);

    const handleGetYearOptions = () => {
        const startDate = globalSettings["dashboard-default-min-date"];
        const endDate = globalSettings["dashboard-default-max-date"];
        const dateFormat = globalSettings["default-date-format"];
        const options = ChartUtils.getYearOptions(startDate, endDate, dateFormat, translations);
        setYearOptions(options);
        setDefaultYearOption(options[0]);
    }

    useEffect(() => {
        handleGetYearOptions();
    }, []);


    const calculateProgressValue = (apiData : any) => {
            const actualValue = ChartUtils.getActualValueForCurrentYear(apiData.actualValues);
            const progress = ChartUtils.generateGaugeValue({
                baseValue: apiData.baseValue,
                targetValue: apiData.targetValue,
                actualValue: actualValue === 0 ? apiData.baseValue : actualValue
            });

            setProgressValue(progress);
    }

    const promiseFetchIndicatorReport = async (id: number, count: number) => {
        if (id) {
            await fetchIndicatorReportData(id, { setLoading: setReportLoading, filters, yearCount: count, settings })
                .then((data) => {
                    setIndicatorReportData(data);
                    const generatedReport = ChartUtils.generateValuesDataset({
                        data,
                        translations
                    });
                    calculateProgressValue(data);
                    setReportData(generatedReport);

                }).catch((error) => {
                    console.log(error);
                });
        }

    }

    useEffect(() => {
        if (indicator) {
            setSelectedIndicatorName(indicator.name);
            promiseFetchIndicatorReport(indicator.id, yearCount)
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [indicator.id, yearCount, filters, settings]);

    return (
        <div>
            <Col md={12} style={{
                borderBottom: '1px solid #ddd',
            }}>
                <Row style={{
                    width: '100%',
                    paddingBottom: 4,
                    paddingTop: 10,
                    marginLeft: 0,
                    borderBottom: '1px solid #e5e5e5',

                }}>
                    <Col md={11} style={{
                        paddingRight: 10
                    }}>

                        <div style={{
                            fontSize: 14,
                            fontWeight: 600,
                            color: 'rgba(0, 0, 0, 0.5)',
                        }}>{selectedIndicatorName || translations["amp.ndd.dashboard:me-no-data"]}</div>
                    </Col>
                </Row>
                { !reportLoading ? (
                    <>
                        <Row style={{
                            paddingLeft: -10
                        }}>
                            {reportData && reportData.length > 0 ? (
                                <>
                                    <Col md={6} style={{
                                    }}>
                                        <Gauge innerValue={progressValue} suffix={'%'} />
                                    </Col>
                                    <Col md={6}>

                                        <div style={{
                                            height: 250
                                        }}>

                                            <BarChart
                                                translations={translations}
                                                data={reportData}
                                                title={title ?? translations["amp.ndd.dashboard:me-indicator-report"]}/>

                                        </div>
                                    </Col>
                                </>
                            ) : (
                                <NoData translations={translations} />
                            )}
                        </Row>

                        {reportData && reportData.length > 0 && (
                            <Row style={{
                                padding: '15px',
                                borderTop: '1px solid #ddd',
                                borderBottom: '1px solid #ddd'
                            }}>
                                <Col md={12}
                                     style={{
                                         paddingLeft: 15,
                                         paddingRight: 5,
                                     }}>
                                    <Row md={12} style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyItems: 'center',
                                    }}>
                                        <Col md={6} style={{
                                            display: "flex"
                                        }}>
                                            <p
                                                style={{
                                                    fontWeight: 700
                                                }}>{translations["amp.ndd.dashboard:me-indicator-progress"]}</p>
                                            <p style={{
                                                display: "flex",
                                                alignItems: "center",
                                                paddingLeft: 10
                                            }}>
                                                ({yearCount + ' ' + translations["amp.ndd.dashboard:years"]})
                                            </p>
                                        </Col>
                                        <Col md={6}>
                                            { yearOptions.length > 0  && (
                                                <Select
                                                    options={yearOptions}
                                                    defaultValue={defaultYearOption}
                                                    isSearchable={false}
                                                    onChange={(option) => {
                                                        if  (option) {
                                                            setYearCount(option.value as any);
                                                            setDefaultYearOption(option);
                                                        }
                                                    }}
                                                    components={{
                                                        IndicatorSeparator: () => null,
                                                    }}
                                                    styles={{
                                                        //@ts-ignore
                                                        control: (base) => ({
                                                            ...base,
                                                            boxShadow: 'none',
                                                            border: 'none',
                                                            display: 'flex',
                                                            justifyContent: 'space-between',
                                                        }),
                                                        // @ts-ignore
                                                        valueContainer: (base) => ({
                                                            ...base,
                                                            paddingLeft: 20,
                                                            justifyContent: 'flex-end',
                                                        }),
                                                        // @ts-ignore
                                                        singleValue: (base) => ({
                                                            ...base,
                                                            color: '#116282',
                                                            fontWeight: 600,
                                                            border: 'none',
                                                            tetAlign: 'right',
                                                            order: 1
                                                        })
                                                    }}
                                                />
                                            )}
                                        </Col>
                                    </Row>
                                </Col>
                                <Col md={12}>
                                    {reportData && (
                                        <LineChart data={indicatorReportData}/>
                                    )}
                                </Col>
                            </Row>
                        )}

                    </>

                ) : (
                    <div className="loading"></div>
                )}

            </Col>
        </div>
    )
}

export default React.memo(IndicatorProgressChart);
