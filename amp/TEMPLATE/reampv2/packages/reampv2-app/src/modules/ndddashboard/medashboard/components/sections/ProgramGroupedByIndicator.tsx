import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col } from 'react-bootstrap';
import styles from './css/Styles.module.css';
import { ComponentProps } from '../../types';
import { IndicatorObjectType } from '../../../../admin/indicator_manager/types';
import { fetchIndicatorsByProgram } from '../../reducers/fetchIndicatorsByProgramReducer';
import ChartUtils from '../../utils/chart';
import IndicatorProgressChart from "../IndicatorProgressChart";

interface ProgramGroupedByIndicatorProps extends ComponentProps {
    level1Child: number | null;
    filters: any;
    settings: any;
    index: number;
}

const ProgramGroupedByIndicator: React.FC<ProgramGroupedByIndicatorProps> = (props) => {
    const { translations, level1Child, filters, settings, index } = props;
    const dispatch = useDispatch();
    const indicatorsByProgramReducer = useSelector((state: any) => state.indicatorsByProgramReducer);

    const [selectedIndicator, setSelectedIndicator] = useState<IndicatorObjectType | null>(null);
    const [selectedIndicatorId, setSelectedIndicatorId] = useState<number | null>(null);

    useEffect(() => {
        if (level1Child) dispatch(fetchIndicatorsByProgram(level1Child));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [level1Child]);

    const handleIndicatorChange = (value: number) => {
        const findIndicator = indicatorsByProgramReducer.data.find((item: any) => item.id ===  value);
        if (findIndicator) {
            setSelectedIndicator(findIndicator);
        }
    }

    useEffect(() => {
        if (!indicatorsByProgramReducer.loading && !indicatorsByProgramReducer.error && indicatorsByProgramReducer.data) {
            if (indicatorsByProgramReducer.data.length > 0) {
                setSelectedIndicatorId(indicatorsByProgramReducer.data[0].id);
                setSelectedIndicator(indicatorsByProgramReducer.data[0]);
            }
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [indicatorsByProgramReducer]);

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
                                onChange={(e) => {
                                    setSelectedIndicatorId(e.target.value as unknown as number);
                                    handleIndicatorChange(parseInt(e.target.value));
                                }}
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
                { console.log('selectedIndicator', selectedIndicator) }
                {
                    (selectedIndicator) ? (
                        <IndicatorProgressChart
                            section="left"
                            translations={translations}
                            filters={filters}
                            settings={settings}
                            index={index}
                            indicator={selectedIndicator}/>
                    ) : (
                        <div className="loading"></div>
                    )
                }
            </Col>
        </div>
    )
}

export default React.memo(ProgramGroupedByIndicator);
