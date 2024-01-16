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

const options = [
    { value: 5, label: '5 Years' },
    { value: 10, label: '10 Years' },
    { value: 15, label: '15 Years' },
    { value: 20, label: '20 Years' },
    { value: 25, label: '25 Years' }
]

interface IndicatorProgressChartProps extends ComponentProps {
    filters: any;
    settings: any;
    indicator: IndicatorObjectType;
    section: 'right' | 'left';
    index: number;
}

const IndicatorProgressChart: React.FC<IndicatorProgressChartProps> = (props: IndicatorProgressChartProps) => {
    const { translations, filters, settings, indicator, section, index } = props;
    const dispatch = useDispatch();

    const [indicatorReportData, setIndicatorReportData] = useState<any>(null);
    const [selectedIndicatorName, setSelectedIndicatorName] = useState<string | null>(null);
    const [progressValue, setProgressValue] = useState<number>(0);
    const [yearCount, setYearCount] = useState<number>(5);
    const [reportData, setReportData] = useState<DataType[]>();

    const [reportLoading, setReportLoading] = useState<boolean>(false);


    const calculateProgressValue = () => {
        if (indicatorReportData) {
            const actualValue = ChartUtils.getActualValueForCurrentYear(indicatorReportData.actualValues);
            const progress = ChartUtils.generateGaugeValue({
                baseValue: indicatorReportData.baseValue,
                targetValue: indicatorReportData.targetValue,
                actualValue: actualValue === 0 ? indicatorReportData.baseValue : actualValue
            });

            setProgressValue(progress);
        }
    }

    const promiseFetchIndicatorReport = async (id: number, count: number) => {
        await fetchIndicatorReportData(id, { setLoading: setReportLoading, filters, yearCount: count, settings })
            .then((data) => {
                setIndicatorReportData(data);
                const generatedReport = ChartUtils.generateValuesDataset({
                    data,
                    translations
                });
                setReportData(generatedReport);
                calculateProgressValue();
            }).catch((error) => {
                console.log(error);
            })
    }

    useEffect(() => {
        setSelectedIndicatorName(indicator.name);
        promiseFetchIndicatorReport(indicator.id, yearCount)
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [indicator.id, index]);

    return (
        <div>
            <Col md={12} id="program-grouped-by-indicators" style={{
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
                { !reportLoading && (
                    <Row style={{
                        paddingLeft: -10
                    }}>

                        <Col md={6} style={{
                        }}>
                            <Gauge innerValue={progressValue} suffix={'%'} />
                        </Col>
                        <Col md={6}>

                            <div style={{
                                height: 250
                            }}>
                                {reportData && reportData.length > 0 && (
                                    <BarChart
                                        translations={translations}
                                        data={reportData}
                                        title={translations["amp.ndd.dashboard:me-indicator-report"]}/>
                                )}

                            </div>
                        </Col>
                    </Row>
                )}
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
                            alignItems: 'center',
                        }}>
                            <Col md={6}>
                                <Form.Check
                                    type="radio"
                                    label="Indicator Progress"
                                />
                            </Col>
                            <Col md={6}>
                                <Select
                                    options={options}
                                    defaultValue={options[0]}
                                    isSearchable={false}
                                    onChange={(option) => {
                                        if  (option) {
                                            setYearCount(option.value as any);
                                            promiseFetchIndicatorReport(indicator.id, option.value as any);


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
                            </Col>
                        </Row>
                    </Col>
                    <Col md={12}>
                        {!reportLoading && (
                            <LineChart data={indicatorReportData}/>
                        )}
                    </Col>
                </Row>
            </Col>
        </div>
    )
}

export default React.memo(IndicatorProgressChart);
