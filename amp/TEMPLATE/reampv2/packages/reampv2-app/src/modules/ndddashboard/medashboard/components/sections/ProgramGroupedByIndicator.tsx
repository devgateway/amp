import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';
import styles from './css/Styles.module.css';
import Gauge from '../charts/GaugesChart';
import BarChart, {DataType} from '../charts/BarChart';
import LineChart from '../charts/LineChart';
import Select from 'react-select';
import { ComponentProps } from '../../types';
import { IndicatorObjectType } from '../../../../admin/indicator_manager/types';
import { fetchIndicatorsByProgram } from '../../reducers/fetchIndicatorsByProgramReducer';
import { fetchIndicatorReport } from '../../reducers/fetchIndicatorReportReducer';
import ChartUtils from '../../utils/chart';

const options = [
    { value: 5, label: '5 Years' },
    { value: 10, label: '10 Years' },
    { value: 15, label: '15 Years' },
    { value: 20, label: '20 Years' },
    { value: 25, label: '25 Years' }
]

interface ProgramGroupedByIndicatorProps extends ComponentProps {
    level1Child: number | null;
    filters: any;
    settings: any;
}

const ProgramGroupedByIndicator: React.FC<ProgramGroupedByIndicatorProps> = (props) => {
    const { translations, level1Child, filters, settings } = props;
    const dispatch = useDispatch();
    const indicatorsByProgramReducer = useSelector((state: any) => state.indicatorsByProgramReducer);
    const indicatorReportReducer = useSelector((state: any) => state.indicatorReportReducer);

    const [selectedOption, setSelectedOption] = useState<IndicatorObjectType | null>(null);
    const [selectedIndicatorName, setSelectedIndicatorName] = useState<string | null>(null);
    const [progressValue, setProgressValue] = useState<number>(0);
    const [yearCount, setYearCount] = useState<number>(5);
    const [reportData, setReportData] = useState<DataType[]>();

    const calculateProgressValue = () => {
        if (indicatorReportReducer.data) {
            const actualValue = ChartUtils.getActualValueForCurrentYear(indicatorReportReducer.data.actualValues);
            const progress = ChartUtils.generateGaugeValue({
                baseValue: indicatorReportReducer.data.baseValue,
                targetValue: indicatorReportReducer.data.targetValue,
                actualValue: actualValue === 0 ? indicatorReportReducer.data.baseValue : actualValue
            });

            setProgressValue(progress);
        }
    }

    useEffect(() => {
        if (!selectedOption && indicatorsByProgramReducer.data.length > 0) {
            setSelectedOption(indicatorsByProgramReducer.data[0]);
            setSelectedIndicatorName(indicatorsByProgramReducer.data[0].name);
            dispatch(fetchIndicatorReport({ filters, id: indicatorsByProgramReducer.data[0].id, yearCount, settings }));
            calculateProgressValue();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (level1Child) dispatch(fetchIndicatorsByProgram(level1Child));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [level1Child]);


    useEffect(() => {
        calculateProgressValue();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [indicatorReportReducer.data]);

    const handleIndicatorChange = (selectedOption: any, indicatorName: string) => {
        setSelectedOption(selectedOption);
        setSelectedIndicatorName(indicatorName);
        dispatch(fetchIndicatorReport({ filters, id: selectedOption, yearCount, settings }));
        calculateProgressValue();
    }

    useEffect(() => {
        const generatedReport = ChartUtils.generateValuesDataset({
            data: indicatorReportReducer.data,
            translations
        });
        setReportData(generatedReport);
    }, [indicatorReportReducer.data]);


    return (
        <div>
            <Col md={12} id="program-grouped-by-indicators" style={{
                borderBottom: '1px solid #ddd',
            }}>
                <Row md={12} style={{
                    width: '100%',
                    marginLeft: 0,
                    paddingBottom: 10,
                    paddingTop: 20,
                    alignItems: 'center',
                    justifyContent: 'center',
                }}>
                    <Col md={11} style={{
                        paddingRight: 10
                    }}>
                        {!indicatorsByProgramReducer.loading && indicatorsByProgramReducer.data.length > 0 ? (
                            <select
                                defaultValue={indicatorsByProgramReducer.data[0].id}
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                onChange={(e) => handleIndicatorChange(e.target.value, e.target.options[e.target.selectedIndex].text)}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                {indicatorsByProgramReducer.data.map((item: any, index: number) => (<option key={index} value={item.id}>{item.name}</option>))}
                            </select>
                        ) : (
                            <select
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}
                                >
                                <option>{translations['amp.ndd.dashboard:me-no-indicator']}</option>
                            </select>
                        )
                        }
                        <span className="cheat-lineheight" />
                    </Col>
                    <Col md={1} style={{
                        padding: 0,
                        cursor: 'pointer'
                    }}>
                        <div className={styles.download_btn_wrapper} onClick={() => ChartUtils.downloadChartImage(
                            `${translations['amp.ndd.dashboard:me-dashboard']}-indicators`, 'program-grouped-by-indicators')}>
                            <span className="glyphicon glyphicon-cloud-download" />
                        </div>
                    </Col>
                </Row>

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
                        }}>{selectedIndicatorName || ' '}</div>
                    </Col>
                </Row>
                { !indicatorReportReducer.loading && (
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
                            {reportData && (
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
                                        if  (option && selectedOption) {
                                            setYearCount(option.value as any);
                                            dispatch(fetchIndicatorReport({
                                                filters,
                                                id: selectedOption as any,
                                                yearCount : option.value as number,
                                                settings }));
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
                        {!indicatorReportReducer.loading && (
                            <LineChart data={indicatorReportReducer.data}/>
                        )}
                    </Col>
                </Row>
            </Col>
        </div>
    )
}

export default React.memo(ProgramGroupedByIndicator);
