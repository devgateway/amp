import React from 'react';
import {Col, Row} from "react-bootstrap";
import ChartUtils from "../../utils/chart";
import {useSelector} from "react-redux";
import styles from './css/Styles.module.css';

interface SectorProgressProps {
    translations?: any,
    filters: any,
    settings: any
}

const SectorProgress: React.FC<SectorProgressProps> = (props) => {
    const { translations, filters, settings } = props;

    const [selectedSector, setSelectedSector] = React.useState<number | null>(null);

    const sectors = useSelector((state: any) => state.fetchSectorsReducer.data);

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
                                defaultValue={sectors[0].ampProgramSettingsId}
                                onChange={(e) => setSelectedSector(parseInt(e.target.value))}
                                style={{
                                    backgroundColor: '#f3f5f8',
                                    boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                }}
                                className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                {sectors.map((item: any, index: number) => (<option key={index} value={item.ampProgramSettingsId}>{item.name}</option>))}
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
            </div>
            </Col>
        </div>
    );
};

export default  SectorProgress;
