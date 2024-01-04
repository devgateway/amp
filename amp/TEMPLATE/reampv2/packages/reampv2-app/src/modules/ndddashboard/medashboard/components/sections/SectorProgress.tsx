import React, {useEffect} from 'react';
import {Col, Row} from "react-bootstrap";
import ChartUtils from "../../utils/chart";
import {useSelector} from "react-redux";
import styles from './css/Styles.module.css';
import {SectorScheme} from "../../types";
import {SectorObjectType} from "../../../../admin/indicator_manager/types";
import Gauge from "../charts/GaugesChart";
import BarChart from "../charts/BarChart";

interface SectorProgressProps {
    translations?: any,
    filters: any,
    settings: any
}

const SectorProgress: React.FC<SectorProgressProps> = (props) => {
    const { translations, filters, settings } = props;

    const [selectedSectorScheme, setSelectedSectorScheme] = React.useState<any>(null);
    const [selectedSector, setSelectedSector] = React.useState<number | null>(null);

    const sectorSchemes: SectorScheme [] = useSelector((state: any) => state.fetchSectorSchemesReducer.data);
    const [sectors, setSectors] = React.useState<SectorObjectType[]>([]);

    const handleSectorSchemeChange = (e: any) => {

    }

    useEffect(() => {

    }, []);

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
                        {sectorSchemes.length === 0 ? (
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
                                defaultValue={sectorSchemes[0].ampSecSchemeId}
                                onChange={(e) => setSelectedSectorScheme(parseInt(e.target.value))}
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                {sectorSchemes.map((item, index: number) => (<option key={index} value={item.ampSecSchemeId}>{item.secSchemeName}</option>))}
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
                        {sectors.length === 0 ? (
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
                                defaultValue={sectors[0].id}
                                onChange={(e) => setSelectedSector(parseInt(e.target.value))}
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                {sectors.map((item, index: number) => (<option key={index} value={item.id}>{item.name}</option>))}
                            </select>
                        )}

                        <span className="cheat-lineheight"/>
                    </Col>
                </Row>

                <Row style={{
                    paddingLeft: -10
                }}>

                    <Col md={6} style={{
                    }}>
                        <Gauge innerValue={90} suffix={'%'} />
                    </Col>
                    <Col md={6}>

                        <div style={{
                            height: 250
                        }}>
                            <BarChart
                                translations={translations}
                                data={[]}
                                title={translations['amp.ndd.dashboard:me-program-progress']} />
                        </div>
                    </Col>
                </Row>
            </div>
            </Col>

            <div>

            </div>
        </div>
    );
};

export default  SectorProgress;
