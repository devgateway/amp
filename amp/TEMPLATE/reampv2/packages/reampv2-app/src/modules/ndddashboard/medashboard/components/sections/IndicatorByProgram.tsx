import React, { useState, useEffect } from 'react';
import { Row, Col } from 'react-bootstrap';
import styles from './css/Styles.module.css';
import { ComponentProps } from '../../types';
import { IndicatorObjectType } from '../../../../admin/indicator_manager/types';
import ChartUtils from '../../utils/chart';
import IndicatorProgressChart from "../IndicatorProgressChart";
import {useSelector} from "react-redux";

interface ProgramGroupedByIndicatorProps extends ComponentProps {
    level1Child: number | null;
    filters: any;
    settings: any;
    index: number;
    indicators: IndicatorObjectType[];
}

const IndicatorByProgram: React.FC<ProgramGroupedByIndicatorProps> = (props) => {
    const { translations, level1Child, filters, settings, index, indicators } = props;

    // @ts-ignore
    const globalSettings = useSelector(state => state.fetchSettingsReducer.settings);

    const [selectedIndicator, setSelectedIndicator] = useState<IndicatorObjectType | null>(null);
    const [selectedIndicatorId, setSelectedIndicatorId] = useState<number | null>(null);


    const handleIndicatorChange = (value: number) => {
        if (indicators && indicators.length > 0) {
            setSelectedIndicator(null);
            const findIndicator = indicators.find((item: any) => item.id ===  value);
            if (findIndicator) {
                setSelectedIndicator(findIndicator);
            }
        }
    }

    useEffect(() => {
            setSelectedIndicator(null);
            if (indicators.length > 0) {
                setSelectedIndicatorId(indicators[0].id);
                setSelectedIndicator(indicators[0]);
            }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [indicators]);

    useEffect(() => {
        handleIndicatorChange(selectedIndicatorId as number)
    }, [selectedIndicatorId]);

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
                        {indicators.length > 0 ? (
                            <select
                                defaultValue={indicators[0].id}
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                onChange={(e) => {
                                    setSelectedIndicatorId(e.target.value as unknown as number);
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                {indicators.map((item: any, index: number) => (<option key={index} value={item.id}>{item.name}</option>))}
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
                {selectedIndicator && (
                    <IndicatorProgressChart
                        title={translations['amp.ndd.dashboard:me-program-progress']}
                        section="left"
                        translations={translations}
                        filters={filters}
                        settings={settings}
                        index={index}
                        indicator={selectedIndicator}
                        globalSettings={globalSettings}
                    />
                )}

            </Col>
        </div>
    )
}

export default React.memo(IndicatorByProgram);
