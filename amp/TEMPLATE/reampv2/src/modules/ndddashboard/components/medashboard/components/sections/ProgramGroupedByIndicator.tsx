import React from 'react'
import { Row, Col } from 'react-bootstrap';
import styles from './css/Styles.module.css';
import Gauge from '../charts/GaugesChart';
import BarChart from '../charts/BarChart';

const options = [
    { value: 'Indicator 1', label: 'Indicator 1' },
    { value: 'Indicator 2', label: 'Indicator 2' },
    { value: 'Indicator 3', label: 'Indicator 3' },
    { value: 'Indicator 4', label: 'Indicator 4' },
]

const ProgramGroupedByIndicator = () => {
    return (
        <div>
            <Col md={12} style={{
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
                        <select
                            defaultValue={options[0].value}
                            style={{
                                backgroundColor: '#f3f5f8',
                                boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                            }}
                            className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                            {options.map((item, index) => (<option key={index} value={item.value}>{item.label}</option>))}
                        </select>
                        <span className="cheat-lineheight" />
                    </Col>
                    <Col md={1} style={{
                        padding: 0
                    }}>
                        <div className={styles.download_btn_wrapper}>
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
                        }}>Indicator Name</div>
                    </Col>
                </Row>
                <Row style={{
                    paddingLeft: -10
                }}>
                    <Col md={6} style={{
                    }}>
                        <Gauge innerValue={90} suffix={'%'} height={300} />
                    </Col>
                    <Col md={6}>

                        <div style={{
                            height: 300
                        }}>
                            <BarChart
                                title={'Program Progress'} />
                        </div>

                    </Col>
                </Row>
            </Col>
        </div>
    )
}

export default ProgramGroupedByIndicator;
