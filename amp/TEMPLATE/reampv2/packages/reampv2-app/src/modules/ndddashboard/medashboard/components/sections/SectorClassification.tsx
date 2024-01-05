import React, {useEffect} from 'react';
import {Col, Row} from "react-bootstrap";
import ChartUtils from "../../utils/chart";
import {useSelector} from "react-redux";
import styles from './css/Styles.module.css';
import {SectorClassifcation, SectorScheme} from "../../types";
import {SectorObjectType} from "../../../../admin/indicator_manager/types";
import Gauge from "../charts/GaugesChart";
import BarChart from "../charts/BarChart";

interface SectorProgressProps {
    translations?: any,
    filters: any,
    settings: any
}

const SectorClassification: React.FC<SectorProgressProps> = (props) => {
    const { translations, filters, settings } = props;

    const [selectedSectorClassification, setSelectedSectorClassification] = React.useState<number | null>(null);
    const [selectedSectorScheme, setSelectedSectorScheme] = React.useState<any>(null);
    const [selectedSector, setSelectedSector] = React.useState<number | null>(null);

    const [sectorScheme, setSectorScheme] = React.useState<SectorScheme>();

    const sectorClassification: SectorClassifcation [] = useSelector((state: any) => state.fetchSectorClassificationReducer.data);
    const [sectors, setSectors] = React.useState<SectorObjectType[]>([]);

    const handleSectorClassificationChange = () => {
        const classification = sectorClassification.find(item => item.id === selectedSectorClassification);

        if (classification) {
            setSectorScheme(classification.sectorScheme);
            setSectors(classification.sectorScheme.children);
        }
    }

    useEffect(() => {
        if (sectorClassification.length > 0) {
            setSelectedSectorClassification(sectorClassification[0].id);
        }
    }, []);

    useEffect(() => {
        handleSectorClassificationChange();
    }, [selectedSectorClassification]);

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
                                onChange={(e) => setSelectedSectorClassification(parseInt(e.target.value))}
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
                    paddingLeft: -10
                }}>
                    <Col md={12}>
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

export default  SectorClassification;
