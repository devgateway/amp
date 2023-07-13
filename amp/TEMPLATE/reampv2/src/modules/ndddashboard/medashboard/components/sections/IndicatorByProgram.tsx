import React, { useEffect } from 'react';
import { Row, Col } from 'react-bootstrap';
import styles from './css/Styles.module.css';
import Gauge from '../charts/GaugesChart';
import BarChart from '../charts/BarChart';
import { ComponentProps, ProgramConfig, ProgramConfigChild, ReportData } from '../../types';
import { useSelector, useDispatch } from 'react-redux';
import { fetchProgramProgressReport } from '../../reducers/fetchProgramProgressReport';

interface IndicatorByProgramProps extends ComponentProps {
    programConfiguration: ProgramConfig[];
    level1Children: ProgramConfigChild[];
    setLevel1Children?: React.Dispatch<React.SetStateAction<ProgramConfigChild[]>>;
    level1Child: number | null;
    setLevel1Child: React.Dispatch<React.SetStateAction<number | null>>;
    selectedConfiguration: number | null;
    setSelectedConfiguration: React.Dispatch<React.SetStateAction<number | null>>;
    filters: any;
}

const IndicatorByProgram: React.FC<IndicatorByProgramProps> = (props) => {
    const {
        programConfiguration,
        translations,
        level1Child,
        setLevel1Child,
        level1Children,
        selectedConfiguration,
        setSelectedConfiguration,
        filters
    } = props;
    const dispatch = useDispatch();

    const programProgressReportReducer = useSelector((state: any) => state.programProgressReportReducer);
    const report: ReportData | null = programProgressReportReducer.data || null;

    if (!selectedConfiguration && programConfiguration) {
        setSelectedConfiguration(programConfiguration[0].ampProgramSettingsId);
    }

    useEffect(() => {
        if (level1Child) {
            dispatch(fetchProgramProgressReport({ filters, id: level1Child }));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [level1Child]);

    return (
        <div>
            <Col md={12} style={{
                borderBottom: '1px solid #ddd',
                minHeight: 500
            }}>
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
                            onChange={(e) => setSelectedConfiguration(parseInt(e.target.value))}
                            style={{
                                backgroundColor: '#f3f5f8',
                                boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                            }}
                            className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                            {programConfiguration.map((item: any, index: number) => (<option key={index} value={item.ampProgramSettingsId}>{item.name}</option>))}
                        </select>
                        )}
                        <span className="cheat-lineheight" />
                    </Col>
                    <Col md={1} style={{
                        padding: 0,
                        cursor: 'pointer'
                    }}>
                        <div className={styles.download_btn_wrapper}>
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
                        {level1Children.length === 0 ? <option>Loading...</option> : (
                            <select
                            defaultValue={level1Child ? level1Child : level1Children[0].id}
                            onChange={(e) => setLevel1Child(Number(e.target.value))}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                {level1Children.map((item, index) => (<option key={index} value={item.id}>{item.name}</option>))}
                            </select>
                        )
                        }
                        <span className="cheat-lineheight" />
                    </Col>
                </Row>
                { (!programProgressReportReducer.loading && report) &&
                <Row style={{
                    paddingLeft: -10
                }}>

                    <Col md={6} style={{
                    }}>
                        <Gauge innerValue={report.progress || 0} suffix={'%'} />
                    </Col>
                    <Col md={6}>

                        <div style={{
                            height: 250
                        }}>
                            <BarChart
                                translations={translations}
                                title={translations['amp.ndd.dashboard:me-program-progress']} />
                        </div>
                    </Col>
                </Row>
                }
            </Col>
        </div>
    )
}

export default IndicatorByProgram;
