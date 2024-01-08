import React, {useEffect} from "react";
import styles from './css/Styles.module.css';
import {Col, Row} from "react-bootstrap";
import ChartUtils from "../../utils/chart";
import Gauge from "../charts/GaugesChart";
import BarChart, {DataType} from "../charts/BarChart";
import {IndicatorObjectType, SectorObjectType} from "../../../../admin/indicator_manager/types";
import {DefaultTranslations} from "../../types";
import { useDispatch, useSelector } from "react-redux";
import { fetchIndicatorsByClassification } from "../../reducers/fetchIndicatorsByClassificationReducer";

interface IndicatorBySectorProps {
    translations: DefaultTranslations;
    filters: any;
    settings: any;
    selectedClassification?: number | null;
}

const IndicatorBySector: React.FC<IndicatorBySectorProps> = (props) => {
    const { translations, selectedClassification } = props;

    const dispatch = useDispatch();
    const indicatorsReducer = useSelector((state: any) => state.fetchIndicatorsByClassificationReducer);

    const [sectors, setSectors] = React.useState<SectorObjectType[]>([]);
    const [indicators, setIndicators] = React.useState<IndicatorObjectType[]>([]);
    const [selectedIndicator, setSelectedIndicator] = React.useState<number | null>(null);
    const [selectedSector, setSelectedSector] = React.useState<number | null>(null);
    const [chartData, setChartData] = React.useState<DataType[]>([]);

    useEffect(() => {
        if (selectedClassification) {
            dispatch(fetchIndicatorsByClassification(selectedClassification));
        }
    }, [selectedClassification]);

    useEffect(() => {
        if (!indicatorsReducer.loading && indicatorsReducer.data && !indicatorsReducer.error) {
            setIndicators(indicatorsReducer.data);
            setSelectedIndicator(indicatorsReducer.data[0].id);
        }

    }, [indicatorsReducer]);

    return (
        <div>
            <Col md={12} style={{
                borderBottom: '1px solid #ddd',
            }}>
                <div id="indicator-by-sector">
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
                            {indicators.length === 0 ? (
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
                                    defaultValue={indicators[0].id}
                                    onChange={(e) => setSelectedIndicator(parseInt(e.target.value))}
                                    style={{
                                        backgroundColor: '#f3f5f8',
                                        boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                    }}
                                    className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                    {indicators.map((item: any, index: number) => (<option key={index} value={item.id}>{item.name}</option>))}
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


                    { ( 2 < 4) &&
                      <Row style={{
                          paddingLeft: -10
                      }}>

                        <Col md={6} style={{
                        }}>
                          <Gauge innerValue={80} suffix={'%'} />
                        </Col>
                        <Col md={6}>

                          <div style={{
                              height: 250
                          }}>
                              {chartData && (
                                  <BarChart
                                      translations={translations}
                                      data={[]}
                                      title={translations['amp.ndd.dashboard:me-program-progress']} />
                              )}
                          </div>
                        </Col>
                      </Row>
                    }
                </div>
            </Col>
        </div>
    )
};

export default IndicatorBySector;
