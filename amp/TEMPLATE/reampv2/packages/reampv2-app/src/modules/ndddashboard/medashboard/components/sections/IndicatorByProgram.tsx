import React, { useEffect } from 'react';
import { Row, Col } from 'react-bootstrap';
import styles from './css/Styles.module.css';
import Gauge from '../charts/GaugesChart';
import BarChart, {DataType} from '../charts/BarChart';
import {ComponentProps, ProgramConfig, ProgramConfigChild, YearValues} from '../../types';
import { useSelector, useDispatch } from 'react-redux';
import { fetchProgramReport } from '../../reducers/fetchProgramReportReducer';
import ChartUtils from '../../utils/chart';
import {extractLv1Children, findProgramConfig} from "../../utils/data";

interface IndicatorByProgramProps extends ComponentProps {
    level1Child: number | null;
    setLevel1Child: React.Dispatch<React.SetStateAction<number | null>>;
    filters: any;
    settings: any;
}

const IndicatorByProgram: React.FC<IndicatorByProgramProps> = (props) => {
    const {
        translations,
        level1Child,
        setLevel1Child,
        filters,
        settings
    } = props;
    const dispatch = useDispatch();

    const programReportReducer = useSelector((state: any) => state.programReportReducer);
    const programConfigurationReducer = useSelector((state: any) => state.programConfigurationReducer);
    const programConfiguration: ProgramConfig[] = programConfigurationReducer.data;

    const[progressValue, setProgressValue] = React.useState<number>(0);
    const [progressValueLoading, setProgressValueLoading] = React.useState<boolean>(false)
    const [chartData, setChartData] = React.useState<DataType[] | null>(null);
    const [selectedConfiguration, setSelectedConfiguration] = React.useState<number | null>(null);
    const [level1Children, setLevel1Children] = React.useState<ProgramConfigChild[]>([]);


    useEffect(() => {
        if (programConfiguration.length > 0) {
            const defaultConfig = programConfiguration[0].ampProgramSettingsId;
            setSelectedConfiguration(defaultConfig);
        }
    }, []);

    const handleConfigurationChange = () => {
        if (selectedConfiguration) {
            const foundProgram = findProgramConfig(selectedConfiguration, programConfigurationReducer.data);

            if (foundProgram) {
                const children = extractLv1Children(foundProgram);
                setLevel1Children(children);
                setLevel1Child(children[0].id);
            }
        }
    }

    useEffect(() => {
        handleConfigurationChange();
    }, [selectedConfiguration]);

    useEffect(() => {
        if (level1Child) {
            dispatch(fetchProgramReport({ filters, id: level1Child, settings }));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [level1Child]);


    useEffect(() => {
        setChartData(null);
        if (programReportReducer?.data?.length > 0) {
            setProgressValueLoading(true);
            const generateReport = ChartUtils.generateValuesDataset({
                data: programReportReducer.data,
                translations
            });

            setChartData(generateReport);

            const aggregates = ChartUtils.computeAggregateValues(programReportReducer.data);

            const calculatedProgressValue = ChartUtils.generateGaugeValue({
                baseValue: aggregates.baseValue,
                targetValue: aggregates.targetValue,
                actualValue: aggregates.actualValue
            });

            setProgressValue(calculatedProgressValue);
            setProgressValueLoading(false);
        }
    }, [level1Child, programReportReducer.data]);

    return (
        <div>
            <Col md={12} style={{
                borderBottom: '1px solid #ddd',
                minHeight: 500
            }}>
                <div id="indicators-by-program">
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
                            {programConfiguration.length === 0 ? (
                                <select
                                    style={{
                                        backgroundColor: '#f3f5f8',
                                        boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                    }}
                                    className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                    <option>{translations['amp.ndd.dashboard:me-no-mpc']}</option>
                                </select>
                            ) : (
                                <select
                                    defaultValue={programConfiguration[0].ampProgramSettingsId}
                                    onChange={(e) => {
                                        setSelectedConfiguration(Number(e.target.value));
                                        handleConfigurationChange();
                                    }}
                                    style={{
                                        backgroundColor: '#f3f5f8',
                                        boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                    }}
                                    className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                    {programConfiguration.map((item, index: number) => (<option key={index} value={item.ampProgramSettingsId}>{item.name}</option>))}
                                </select>
                            )}
                            <span className="cheat-lineheight" />
                        </Col>
                        <Col md={1} style={{
                            padding: 0,
                            cursor: 'pointer'
                        }}>
                            <div className={styles.download_btn_wrapper} onClick={() => ChartUtils.downloadChartImage(
                                `${translations['amp.ndd.dashboard:me-dashboard']}-programs`, 'indicators-by-program')}>
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
                        <Col md={11}  style={{
                            paddingRight: 10
                        }}>
                            {(level1Children.length === 0 && level1Child) ? <option>Loading...</option> : (
                                <select
                                    defaultValue={level1Child?.toString()}
                                    onChange={(e) => setLevel1Child(Number(e.target.value))}
                                    className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                    {level1Children.map((item, index) => (<option key={index} value={item.id}>{item.name}</option>))}
                                </select>
                            )
                            }
                            <span className="cheat-lineheight" />
                        </Col>
                    </Row>

                    { (!programReportReducer.loading && programReportReducer.data) ?
                      <Row style={{
                          paddingLeft: -10
                      }}>
                        <Col md={6} style={{
                        }}>
                            {!progressValueLoading && (
                                <Gauge innerValue={progressValue} suffix={'%'} />
                            ) }
                        </Col>
                        <Col md={6}>
                          <div style={{
                              height: 250
                          }}>
                              {chartData && (
                                  <BarChart
                                      translations={translations}
                                      data={chartData}
                                      title={translations['amp.ndd.dashboard:me-program-progress']} />
                              )}
                          </div>
                        </Col>
                      </Row>
                    : (
                            <div className="loading"></div>
                        )}
                </div>
            </Col>
        </div>
    )
}

export default IndicatorByProgram;
